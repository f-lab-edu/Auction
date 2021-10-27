package Auction.service.controller;

import Auction.service.aop.JoinCheck;
import Auction.service.aop.PhoneCheck;
import Auction.service.aop.LoginCheck;
import Auction.service.dto.LoginDto;
import Auction.service.dto.MemberDto;
import Auction.service.dto.Result;
import Auction.service.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static Auction.service.utils.ResultCode.SUCCESS;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/member")
public class MemberController {

    private final MemberService memberService;

    @PhoneCheck
    @GetMapping("/check/{phone}")
    public ResponseEntity<Result> checkMemberByPhone(@PathVariable String phone) {
        return Result.toResponseEntity(SUCCESS);
    }

    @JoinCheck
    @PostMapping("/register")
    public ResponseEntity register(@RequestBody @Valid MemberDto memberDto, BindingResult bindingResult) {
        Long id = memberService.registerMember(memberDto);
        return Result.toResponseEntity(SUCCESS, id);
    }

    @LoginCheck
    @PostMapping("/login")
    public ResponseEntity<Result> login(@RequestBody LoginDto loginDto) {
        return Result.toResponseEntity(SUCCESS);
    }

}
