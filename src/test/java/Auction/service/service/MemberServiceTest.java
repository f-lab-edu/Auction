package Auction.service.service;

import Auction.service.domain.Member;
import Auction.service.dto.MemberDto;
import Auction.service.repository.MemberRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.transaction.Transactional;

import static org.assertj.core.api.Assertions.*;
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
        MemberDto memberDto = new MemberDto("ttttt", "pass", "ss", "0100000000");

        memberService.registerMember(memberDto);

        Member findMember = memberRepository.findByMemberId(memberDto.getId());
        assertThat(findMember.getMemberId()).isEqualTo(memberDto.getId());
    }

    @Test
    @DisplayName("id 중복 검사")
    void checkMemberByMemberId() {
        MemberDto memberDto1 = new MemberDto("test", "test1234", "name", "0100000000");
        memberService.registerMember(memberDto1);

        assertTrue(memberService.checkMemberByMemberId("test"));
        assertFalse(memberService.checkMemberByMemberId("test2"));
    }

}