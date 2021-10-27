package Auction.service.service;

import Auction.service.domain.Member;
import Auction.service.dto.MemberDto;
import Auction.service.repository.MemberRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.transaction.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class MemberServiceTest {

    @Autowired
    MemberService memberService;

    @Autowired
    MemberRepository memberRepository;

    @Test
    @DisplayName("회원가입 성공")
    void registerMember(){
        MemberDto memberDto = new MemberDto("0100000000", "pass", "ss");

        memberService.registerMember(memberDto);

        Member findMember = memberRepository.findByPhone(memberDto.getPhone());
        assertThat(findMember.getPhone()).isEqualTo(memberDto.getPhone());
    }

    @Test
    @DisplayName("휴대폰 번호 중복 검사")
    void checkMemberByPhone() {
        MemberDto memberDto1 = new MemberDto("01099999999", "test1234", "name");
        memberService.registerMember(memberDto1);

        assertTrue(memberService.checkMemberByPhone("01099999999"));
        assertFalse(memberService.checkMemberByPhone("test2"));
    }

}