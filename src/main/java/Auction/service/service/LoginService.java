package Auction.service.service;

import Auction.service.domain.Member;
import Auction.service.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
@RequiredArgsConstructor
@Slf4j
public class LoginService  implements UserDetailsService {

    private final MemberRepository memberRepository;
    private PasswordEncoder bcryptEncoder;

    @Override
    public UserDetails loadUserByUsername(String userId) throws UsernameNotFoundException {
        Member member = memberRepository.findByUserId(userId);
        if(member != null){
            return new User(member.getUserId(), member.getUserPassword(), new ArrayList<>());
        }
        log.info("member is null");
        return null;
    }
}
