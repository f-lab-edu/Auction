package Auction.service.controller;

import Auction.service.dto.JwtRequest;
import Auction.service.dto.JwtResponse;
import Auction.service.service.LoginService;
import Auction.service.utils.JwtUtility;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
public class LoginController {

    private final LoginService loginService;
    private final JwtUtility jwtUtility;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder bcryptEncoder;

    @GetMapping("/")
    public String home(){
        return "Welcome to Auction Service";
    }

    @PostMapping("/authenticate")
    public JwtResponse authenticate(@RequestBody JwtRequest request) throws Exception{
        try{
           authenticationManager.authenticate(
                   new UsernamePasswordAuthenticationToken(
                           request.getUsername(),
                           request.getPassword()
                   )
           );
        }catch(BadCredentialsException e){
            throw new Exception("Invalid credentials", e);
        }
        final UserDetails userDetails=
            loginService.loadUserByUsername(request.getUsername());

        if(userDetails == null) {

            return new JwtResponse("Can not find User");
        }

        final String token =
                jwtUtility.generateToken(userDetails);

        return new JwtResponse(token);
    }

}
