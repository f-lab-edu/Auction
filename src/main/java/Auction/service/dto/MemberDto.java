package Auction.service.dto;

import Auction.service.domain.member.Member;
import lombok.*;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;

/**
 * message 변경시
 * MemberControllerTest join 관련 메서드 message expect value 수정 필요
 */
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MemberDto {

    @NotEmpty(message = "휴대폰 번호를 입력해주세요.")
    @Pattern(regexp="^01(?:0|1|[6-9])(?:\\d{3}|\\d{4})\\d{4}$", message = "휴대폰 번호는 숫자로만 구성되어야 합니다.")
    private String phone;

    @NotEmpty(message = "비밀번호를 입력해주세요.")
    @Pattern(regexp="(?=.*[0-9])(?=.*[a-zA-Z])(?=\\S+$).{8,20}",
            message = "비밀번호는 영문 대,소문자와 숫자가 적어도 1개 이상씩 포함된 8자 ~ 20자의 비밀번호여야 합니다.")
    private String password;

    @NotEmpty(message = "닉네임을 입력해주세요.")
    private String nickname;

    public static Member toEntity(MemberDto memberDto, PasswordEncoder passwordEncoder) {
        return Member.builder()
                .phone(memberDto.getPhone())
                .password(passwordEncoder.encode(memberDto.getPassword()))
                .name(memberDto.getNickname())
                .build();
    }

}
