package Auction.service.dto;

import Auction.service.domain.Member;
import lombok.*;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MemberDto {

    @NotEmpty
    @Pattern(regexp="^01(?:0|1|[6-9])(?:\\d{3}|\\d{4})\\d{4}$")
    private String phone;

    @NotEmpty
    @Pattern(regexp="(?=.*[0-9])(?=.*[a-zA-Z])(?=\\S+$).{8,20}")
    private String password;

    @NotEmpty
    private String nickname;

    public static Member toEntity(MemberDto memberDto, PasswordEncoder passwordEncoder) {
        return Member.builder()
                .phone(memberDto.getPhone())
                .password(passwordEncoder.encode(memberDto.getPassword()))
                .name(memberDto.getNickname())
                .build();
    }

}
