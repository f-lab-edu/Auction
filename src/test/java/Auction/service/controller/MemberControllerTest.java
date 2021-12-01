package Auction.service.controller;

import Auction.EnableMockMvc;
import Auction.service.dto.LoginDto;
import Auction.service.dto.MemberDto;
import Auction.service.repository.MemberRepository;
import Auction.service.service.LoginService;
import Auction.service.service.MemberService;
import Auction.service.utils.JwtUtility;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.core.IsNull;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.User;
import org.springframework.test.web.servlet.MockMvc;
import java.util.ArrayList;

import static Auction.service.utils.ResultCode.INVALID_PARAMS;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(properties = "spring.config.location = classpath:/application.yaml, classpath:/aws.yaml")
@EnableMockMvc
class MemberControllerTest {

    String URL = "/api/member" ;

    @Autowired
    MockMvc mockMvc;

    @MockBean
    MemberService memberService;

    @MockBean
    LoginService loginService;

    @MockBean
    MemberRepository memberRepository;

    @MockBean
    AuthenticationManager authenticationManager;

    @MockBean
    JwtUtility jwtUtility;

    @Autowired
    ObjectMapper mapper;

    @Test
    @DisplayName("핸드폰 번호 체크 - 중복 X")
    public void checkMemberByPhone_check_x() throws Exception {

        when(memberService.checkMemberByPhone(anyString())).thenReturn(Boolean.FALSE);

        mockMvc
                .perform(get(URL +"/check/01099999999")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.statusCode").value(HttpStatus.OK.value()))
                .andExpect(jsonPath("$.message").isEmpty())
                .andExpect(jsonPath("$.data").value(IsNull.nullValue()));

        verify(memberService, times(1)).checkMemberByPhone(anyString());

    }

    @Test
    @DisplayName("핸드폰 번호 체크 - 중복 O")
    public void checkMemberByPhone_check_o() throws Exception {

        when(memberService.checkMemberByPhone(anyString())).thenReturn(Boolean.TRUE);

        mockMvc
                .perform(get(URL +"/check/01099999999")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.statusCode").value(HttpStatus.CONFLICT.value()))
                .andExpect(jsonPath("$.message").isEmpty())
                .andExpect(jsonPath("$.data").value(IsNull.nullValue()));

        verify(memberService, times(1)).checkMemberByPhone(anyString());

    }

    @Test
    @DisplayName("회원가입[휴대폰 번호 유효성] - X ")
    public void register_phoneX() throws Exception {

        when(memberService.checkMemberByPhone(anyString())).thenReturn(Boolean.FALSE);

        MemberDto memberDto = new MemberDto("010-", "password123", "sohee");

        mockMvc
                .perform(post(URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(memberDto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.statusCode").value(HttpStatus.BAD_REQUEST.value()))
                .andExpect(jsonPath("$.message").value("휴대폰 번호는 숫자로만 구성되어야 합니다."))
                .andExpect(jsonPath("$.data").value(IsNull.nullValue()))
                .andDo(print());

        verify(memberService).checkMemberByPhone(anyString());

    }

    @Test
    @DisplayName("회원가입[닉네임 유효성] - X ")
    public void register_nicknameX() throws Exception {

        when(memberService.checkMemberByPhone(anyString())).thenReturn(Boolean.FALSE);

        MemberDto memberDto = new MemberDto("0102332903", "password123", "");

        mockMvc
                .perform(post(URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(memberDto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.statusCode").value(HttpStatus.BAD_REQUEST.value()))
                .andExpect(jsonPath("$.message").value("닉네임을 입력해주세요."))
                .andExpect(jsonPath("$.data").value(IsNull.nullValue()))
                .andDo(print());

        verify(memberService).checkMemberByPhone(anyString());

    }

    @Test
    @DisplayName("회원가입[비밀번호 유효성] - X ")
    public void register_pwsX() throws Exception {

        when(memberService.checkMemberByPhone(anyString())).thenReturn(Boolean.FALSE);

        MemberDto memberDto = new MemberDto("01099999999", "123", "sohee");

        mockMvc
                .perform(post(URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(memberDto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.statusCode").value(HttpStatus.BAD_REQUEST.value()))
                .andExpect(jsonPath("$.message").value("비밀번호는 영문 대,소문자와 숫자가 적어도 1개 이상씩 포함된 8자 ~ 20자의 비밀번호여야 합니다."))
                .andExpect(jsonPath("$.data").value(IsNull.nullValue()))
                .andDo(print());

        verify(memberService).checkMemberByPhone(anyString());

    }

    @Test
    @DisplayName("회원가입 - O ")
    public void register_O() throws Exception {

        when(memberService.checkMemberByPhone(anyString())).thenReturn(Boolean.FALSE);
        when(memberService.registerMember(any(MemberDto.class))).thenReturn(1L);

        MemberDto memberDto = new MemberDto("0109999202", "password123", "sohee");

        mockMvc
                .perform(post(URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(memberDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.statusCode").value(HttpStatus.OK.value()))
                .andExpect(jsonPath("$.message").isEmpty())
                .andExpect(jsonPath("$.data").value(1L));

        verify(memberService).checkMemberByPhone(anyString());
        verify(memberService).registerMember(any(MemberDto.class));

    }

    @Test
    @DisplayName("로그인 - O")
    public void login_O() throws Exception {

        LoginDto loginDto = new LoginDto("01099999999", "password1234");
        User user = new User(loginDto.getPhone(), loginDto.getPassword(), new ArrayList<>());

        when(loginService.loadUserByUsername(anyString())).thenReturn(user);
        when(jwtUtility.generateToken(any(User.class))).thenReturn("token");

        mockMvc
                .perform(post(URL +"/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(loginDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.statusCode").value(HttpStatus.OK.value()))
                .andExpect(jsonPath("$.message").isEmpty())
                .andExpect(jsonPath("$.data").value("token"));

        verify(loginService).loadUserByUsername(anyString());
        verify(jwtUtility).generateToken(any(User.class));

    }

    @Test
    @DisplayName("로그인 - X")
    public void login_X() throws Exception {

        when(authenticationManager.authenticate(any())).thenThrow(new BadCredentialsException(""));

        LoginDto loginDto = new LoginDto("01099999999", "password12345");

        mockMvc
                .perform(post(URL +"/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(loginDto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.statusCode").value(HttpStatus.BAD_REQUEST.value()))
                .andExpect(jsonPath("$.message").value(INVALID_PARAMS.getMessage()))
                .andExpect(jsonPath("$.data").isEmpty());

        verify(authenticationManager).authenticate(any());

    }

}