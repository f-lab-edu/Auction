package Auction.service.domain;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;

@SpringBootTest
class MemberTest {

    @Autowired
    private EntityManager em;

    @Test
    @DisplayName("멤버 생성 테스트")
    @Transactional
    public void createAccount(){
        Member member = new Member("testId", "testPassword");
        em.persist(member);

        em.flush();
        em.clear();

        Member findMember = em.find(Member.class, member.getId());

        Assertions.assertThat(findMember.getId()).isEqualTo(member.getId());
    }
}