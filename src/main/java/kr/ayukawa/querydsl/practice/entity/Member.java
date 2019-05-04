package kr.ayukawa.querydsl.practice.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Member {
	@Id
	@GeneratedValue(strategy= GenerationType.SEQUENCE)
	private long id;
	private String name;
	private String nickName;
	private int age;

	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getNickName() {
		return nickName;
	}
	public void setNickName(String nickName) {
		this.nickName = nickName;
	}
	public int getAge() {
		return age;
	}
	public void setAge(int age) {
		this.age = age;
	}

	public Member(String name, String nickName, int age) {
		this.name = name;
		this.nickName = nickName;
		this.age = age;
	}

	@Override
	public String toString() {
		return String.format("Member [id=%d, name=%s, nickName=%s, age=%d]", this.id, this.name, this.nickName, this.age);
	}
}
