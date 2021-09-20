package Auction.service.service;

import Auction.service.domain.Member;
import Auction.service.repository.MemberRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.transaction.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class MemberServiceTest {

    @Autowired
    private MemberService memberService;
    @Autowired
    private MemberRepository memberRepository;

    @Test
    @DisplayName("멤버 생성 테스트")
    public void createMember(){
        Member member = new Member("testId", "testPassword");
        memberService.createMember(member);

        Member findMember = memberRepository.getById(member.getId());

        Assertions.assertThat(findMember.getId()).isEqualTo(member.getId());
    }

}