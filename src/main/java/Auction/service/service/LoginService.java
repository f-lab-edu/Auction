package Auction.service.service;

import Auction.service.domain.member.Member;
import Auction.service.exception.CustomException;
import Auction.service.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

import static Auction.service.utils.ResultCode.INVALID_PARAMS;

@Service
@RequiredArgsConstructor
@Slf4j
public class LoginService  implements UserDetailsService {

    private final MemberRepository memberRepository;

    @Override
    public UserDetails loadUserByUsername(String phone) throws UsernameNotFoundException {
        Member member = memberRepository.findByPhone(phone);

        if(member != null){
            return new User(member.getPhone(), member.getPassword(), new ArrayList<>());
        }
        throw new CustomException(INVALID_PARAMS);
    }
}
