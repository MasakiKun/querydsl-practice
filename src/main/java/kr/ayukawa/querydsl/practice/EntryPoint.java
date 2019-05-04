package kr.ayukawa.querydsl.practice;

import com.querydsl.jpa.impl.JPAQuery;
import kr.ayukawa.querydsl.practice.entity.Member;
import kr.ayukawa.querydsl.practice.entity.QMember;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

public class EntryPoint {
	public static void main(String[] args) throws Exception {
		EntityManagerFactory emf = Persistence.createEntityManagerFactory("main");
		EntityManager em = emf.createEntityManager();
		EntityTransaction etx = em.getTransaction();

		try {
			etx.begin();

			em.persist(new Member("을지문덕", "문덕", 47));
			em.persist(new Member("감강찬", "감찬", 50));
			em.persist(new Member("잔다르크", "잔", 18));
			em.persist(new Member("마리 앙투아네트", "마리", 18));

			em.flush();
			etx.commit();

			QMember member = QMember.member;

			Member someMember = new JPAQuery<Member>(em).from(member)
					.where(member.name.eq("잔다르크"))
					.fetchOne();

			System.out.println(someMember);
		} catch(Exception e) {
			e.printStackTrace();
			etx.rollback();
		} finally {
			em.close();
		}

		emf.close();
	}
}
