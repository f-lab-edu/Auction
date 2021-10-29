package Auction.service.service;

import Auction.service.domain.Member;
import Auction.service.dto.MemberDto;
import Auction.service.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    public Long registerMember(MemberDto memberDto){
        Member member = MemberDto.toEntity(memberDto, passwordEncoder);
        return memberRepository.save(member).getId();
    }

    public Boolean checkMemberByPhone(String phone) {
        return memberRepository.existsByPhone(phone);
    }

}

