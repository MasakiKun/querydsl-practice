# QueryDSL with Gradle and IntelliJ IDEA without Spring Framework

QueryDSL을 한번 써보려고 했는데, 인터넷에서 돌고있는 문서들은 내 입장에서는 아래와 같은 문제점이 있었다.

  * Spring Framework 없이 일단 Plain하게 QueryDSL을 써보려고 했는데, 대부분의 문서가 Spring JPA나 SpringBoot와 함께 진행하는 문서였다.
  * QueryDSL 쿼리 클래스를 자동 생성하는데에도 아래와 같은 문제가 있었다.
    * 공식 문서에는 Maven의 JPAAnnotationProcessor 플러그인을 이용해서 쿼리 타입 클래스를 자동 생성하는 방법이 설명되어 있었는데,
      나는 빌드툴을 Gradle을 사용하기 때문에 적용할 수 없었다.
    * 몇몇 Gradle용 QueryDSL 플러그인을 사용하는 문서를 보았는데, 나는 요즘 빌드 스크립트를 짤 때 KotlinDSL을 사용하고 있고,
      이 KotlinDSL이 익숙하지 못하기 때문에(...) 플러그인의 환경설정을 할 수 없었다.
    * 또 다른 몇몇 문서는 쿼리 타입 클래스를 생성해주는 Gradle용 전용 Task를 만들고 compile Task가 이 Task에 의존성을 갖도록 설정하는 것도 있었는데,
      솔직히 그냥 실습차 쓰는 마당에 딱히 Task를 만들고 싶지 않았다 (......)

그런 연유로 검색을 계속하다가 아래와 같이 진행해서 성공했기에 기록차 남겨둔다.

기본적으로는 아래 웹문서를 참고했다

* [How to use QueryDSL with JPA, Gradle and IDEA](http://bsideup.blogspot.com/2015/04/querydsl-with-gradle-and-idea.html)

```gradle.build``` 파일에 아래 의존성을 추가한다.

	compileOnly("com.querydsl:querydsl-apt:4.2.1:jpa")

여기 ```querydsl-apt```의 APT란 ```Annotation Processor Tool```의 약자로써, 어노테이션을 이용해서 특정한 처리를 해주는 툴을 이야기한다.
많이들 사용하는 [Lombok](https://projectlombok.org/) 같은 것들이 APT를 이용해서 자동으로 코드를 생성한다.

위 의존성을 추가하면, 프로잭트 내 전체 클래스 중 ```@Entity``` 어노테이션이 붙은 클래스들을 모두 읽어들여, 클래스 이름 앞에 ```Q```가 붙은
QueryDSL 쿼리 타입의 클래스를 생성해준다.

Lombok 라이브러리가```@Getter```나 ```@Setter```, ```@ToString``` 어노테이션이 붙은 시그니처를 찾아내,
자동으로 코드를 생성해주는 것과 같은 역할을 한다고 보면 되겠다.

그리고 QueryDSL의 APT가 자동으로 생성해주는 쿼리타입 클래스를 사용할 수 있도록, ```build.gradle``` 파일에 아래 설정을 추가한다.

	plugins {
		id("idea")			// 인텔리제이 플러그인 추가
	}
	
	// ...생략...
	
	idea {
		module {
			// 자동 생성된 쿼리타입 클래스들을 프로젝트 소스셋에 추가해준다
			sourceDirs += file("generated/")
		}
	}

그런데 위 의존성만 추가해서는 인텔리제이에서 엔티티의 쿼리 타입 클래스가 인식되지 않는다.

이 문제에 대해서는 아래 웹문서를 참고했다.

* [QueryDSL + JPA + Gradle in IntelliJ](http://izeye.blogspot.com/2015/09/querydsl-jpa-gradle-in-intellij.html)

인텔리제이의 환경설정(맥OS 기준으로 Cmd + ,)에서 아래 설정을 변경해준다.

```Build, Execution, Deployment``` > ```Compiler``` > ```Annotation Processors``` 설정에서
```Enable annotation processing``` 항목을 찾아서 활성화 시켜주고,
해당 설정 페이지에서 ```Store generated sources relative to:``` 항목을 찾아서 해당 항목의 설정을  ```Module content root```로 변경해준다.

위 설정까지 끝나면 프로젝트에서 쿼리타입 클래스를 사용할 수 있다.

	@Entity
	public class Member {
		/// blahblah
	}
	
	......
	
	QMember member = QMember.member;

~~아오, 인텔리제이에서 JavaFX 쓸때도 프로젝트 환경설정 따로 해줘야 하더니, QueryDSL 쓸때 또...!~~

## 문제점

* 순전히 Gradle + IntelliJ 환경에서만 테스트해본거라서, 이클립스에서는 이 설정대로 안될지도 모르겠다.
  이때는, APT에 의해서 자동 생성된 generated 폴더를 프로젝트 소스 폴더로 직접 수동으로 추가해주면 될 것 같다.
* 자바9 이후부터 APT 쓸때마다 계속 겪는 문제지만, 9 이후부터는 모듈 시스템 때문에 APT가 어노테이션을 읽어들이지 못하고 컴파일 타임에 오류가 난다.
  그냥 자바8을 이용해서 개발하거나, ```module-info.java``` 파일을 생성해서 QueryDSL-APT가 패키지를 읽어들일 수 있도록 설정해주면 해결될 것 같긴 하다.