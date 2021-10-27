package Auction.service.aop;

import Auction.service.dto.LoginDto;
import Auction.service.dto.MemberDto;
import Auction.service.dto.Result;
import Auction.service.exception.CustomException;
import Auction.service.service.LoginService;
import Auction.service.service.MemberService;
import Auction.service.utils.JwtUtility;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import static Auction.service.utils.ResultCode.*;

@Component
@RequiredArgsConstructor
@Aspect
public class MemberAspect {

    private final MemberService memberService;
    private final LoginService loginService;
    private final JwtUtility jwtUtility;
    private final AuthenticationManager authenticationManager;

    @Before("@annotation(Auction.service.aop.PhoneCheck)")
    private void memberPhoneCheck(JoinPoint joinPoint) {
        String phone = (String) joinPoint.getArgs()[0];

        // 휴대폰 번호 중복 검사
        if(memberService.checkMemberByPhone(phone)) {
            throw new CustomException(DUPLICATE_RESOURCE);
        }
    }

    @Before("@annotation(Auction.service.aop.JoinCheck)")
    private void joinCheck(JoinPoint joinpoint) {

        MemberDto memberDto = (MemberDto) joinpoint.getArgs()[0];
        BindingResult bindingResult = (BindingResult) joinpoint.getArgs()[1];

        // 휴대폰 번호 중복 검사
        if(memberService.checkMemberByPhone(memberDto.getPhone())) {
            throw new CustomException(DUPLICATE_RESOURCE);
        }

        // 회원가입 유효성 검사
        if (bindingResult.hasErrors()) {
            String field = ((FieldError) bindingResult.getAllErrors().get(0)).getField();

            if(field.equals("phone")) {
                throw new CustomException(INVALID_PHONE);
            } else if (field.equals("password")) {
                throw new CustomException(INVALID_PASSWORD);
            } else if (field.equals("nickname")) {
                throw new CustomException(INVALID_NICKNAME);
            }
        }
    }


    @Around("@annotation(Auction.service.aop.LoginCheck))")
    private ResponseEntity<Result> LoginCheck(ProceedingJoinPoint pjp) throws Throwable {

        LoginDto loginDto = (LoginDto) pjp.getArgs()[0];

        try{
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginDto.getPhone(),
                            loginDto.getPassword()
                    )
            );
        }catch(InternalAuthenticationServiceException e){
            throw new CustomException(INVALID_PARAMS);
        }

        UserDetails userDetails= loginService.loadUserByUsername(loginDto.getPhone());

        if(userDetails == null) {
            throw new CustomException(INVALID_PARAMS);
        }

        // 타겟 메서드 실행
        pjp.proceed();
        
        String token = jwtUtility.generateToken(userDetails);

        return Result.toResponseEntity(SUCCESS, token);
    }

}
