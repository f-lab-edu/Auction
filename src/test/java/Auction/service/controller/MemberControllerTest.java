package Auction.service.controller;

import Auction.service.service.MemberService;
import org.hamcrest.core.IsNull;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class MemberControllerTest {

    static {
        System.setProperty("spring.config.location", "classpath:/application.yaml, classpath:/aws.yaml");
    }

    @Autowired
    MockMvc mockMvc;

    @MockBean
    MemberService memberService;

    @Test
    @DisplayName("핸드폰 번호 중복 체크 - 중복 X")
    public void phone_check_x() throws Exception {

        when(memberService.checkMemberByPhone(anyString())).thenReturn(Boolean.FALSE);

        mockMvc
                .perform(get("/api/member/check/01099999999")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.statusCode").value(HttpStatus.OK.value()))
                .andExpect(jsonPath("$.message").isEmpty())
                .andExpect(jsonPath("$.data").value(IsNull.nullValue()));

        verify(memberService, times(1)).checkMemberByPhone(anyString());

    }

    @Test
    @DisplayName("핸드폰 번호 체크 - 중복 O")
    public void phone_check_o() throws Exception {

        when(memberService.checkMemberByPhone(anyString())).thenReturn(Boolean.TRUE);

        mockMvc
                .perform(get("/api/member/check/01099999999")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.statusCode").value(HttpStatus.CONFLICT.value()))
                .andExpect(jsonPath("$.message").isEmpty())
                .andExpect(jsonPath("$.data").value(IsNull.nullValue()));

        verify(memberService, times(1)).checkMemberByPhone(anyString());

    }
}