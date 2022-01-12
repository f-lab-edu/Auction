package Auction.service.service;

import Auction.service.domain.member.Member;
import Auction.service.dto.MemberDto;
import Auction.service.repository.MemberRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class MemberServiceTest {

    @Mock
    MemberRepository memberRepository;

    @Mock
    PasswordEncoder passwordEncoder;

    @InjectMocks
    MemberService memberService;

    MemberDto memberDto;

    Member member;

    @BeforeEach
    void before() {
        memberDto = new MemberDto("01099999999", "test1234", "name");
        member = MemberDto.toEntity(memberDto, passwordEncoder);
    }

    @Test
    @DisplayName("회원가입 - O")
    void registerMember_success(){
        Member joinMember = Member.builder().id(1L).build();
        given(memberRepository.save(any())).willReturn(joinMember);

        assertThat(memberService.registerMember(memberDto)).isEqualTo(1l);
        verify(memberRepository, times(1)).save(any());
    }

}