package Auction.service.controller;

import Auction.service.dto.MemberDto;
import Auction.service.dto.Result;
import Auction.service.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/member")
public class MemberController {

    private final MemberService memberService;

    @GetMapping("/check/{id}")
    public ResponseEntity checkMemberByMemberId(@PathVariable String id) {

        if(memberService.checkMemberByMemberId(id)) {
            return new ResponseEntity(Result.response(HttpStatus.CONFLICT), HttpStatus.CONFLICT);
        } else {
            return new ResponseEntity(Result.response(HttpStatus.OK), HttpStatus.OK);
        }
    }

    @PostMapping("/register")
    public ResponseEntity register(@RequestBody @Valid MemberDto memberDto, BindingResult bindingResult) {

        // 회원가입 유효성 검사
        if (bindingResult.hasErrors()) {
            String msg = bindingResult.getAllErrors().get(0).getDefaultMessage();
            return new ResponseEntity(Result.response(HttpStatus.BAD_REQUEST, msg), HttpStatus.BAD_REQUEST);
        }

        // 중복 아이디 검사
        if(memberService.checkMemberByMemberId(memberDto.getId())) {
            return new ResponseEntity(Result.response(HttpStatus.CONFLICT), HttpStatus.CONFLICT);
        }

        Long id = memberService.registerMember(memberDto);
        return new ResponseEntity(Result.response(HttpStatus.OK,"회원 가입 성공", id), HttpStatus.OK);
    }

}
