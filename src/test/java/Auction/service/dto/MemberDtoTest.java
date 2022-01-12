package Auction.service.dto;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

class MemberDtoTest {

    Validator validator;

    @BeforeEach
    void before() {
        validator = Validation.buildDefaultValidatorFactory().getValidator();
    }

    @Test
    @DisplayName("memberDto 유효성 테스트 : empty확인")
    void validMember() {

        MemberDto memberDto = new MemberDto("", "", "");
        assertThat(checkValidation(memberDto)).isEqualTo(5);
    }

    @Test
    @DisplayName("memberDto 유효성 테스트 : regexp 확인")
    void validMember2() {

        MemberDto memberDto = new MemberDto("0109999999.", "1234", "");
        assertThat(checkValidation(memberDto)).isEqualTo(3);
    }

    int checkValidation(MemberDto memberDto) {
        Set<ConstraintViolation<MemberDto>> violations = validator.validate(memberDto);
        return violations.size();
    }
}