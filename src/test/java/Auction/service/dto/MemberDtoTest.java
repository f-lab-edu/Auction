package Auction.service.dto;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;


@SpringBootTest
class MemberDtoTest {

    @Autowired
    ValidatorFactory factory;

    @Autowired
    Validator validator;

    @Test
    @DisplayName("memberDto 유효성 테스트")
    void validMember() {
        MemberDto memberDto = new MemberDto("테스트", "1", "", "테스트");

        Set<ConstraintViolation<MemberDto>> violations = validator.validate(memberDto);
        List<String> message = new ArrayList<>();
        for (ConstraintViolation<MemberDto> violation : violations) {
            message.add(violation.getMessage());
        }

        Assertions.assertThat(message).contains("아이디는 영문 대,소문자, 숫자로만 입력해주세요."
                ,"비밀번호는 영문 대,소문자와 숫자가 적어도 1개 이상씩 포함된 8자 ~ 20자의 비밀번호여야 합니다."
                ,"이름을 입력해주세요"
                ,"휴대폰 번호는 숫자로만 구성되어야 합니다.");
    }
}