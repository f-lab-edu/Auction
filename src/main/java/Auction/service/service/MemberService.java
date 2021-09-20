package Auction.service.service;

import Auction.service.domain.Member;
import Auction.service.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;

    public void createMember(Member member){
        memberRepository.save(member);
    }
}
