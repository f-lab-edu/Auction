package Auction.service.utils;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.*;

@Getter
@AllArgsConstructor
public enum ResultCode {

    // 200 OK : 성공
    SUCCESS(OK,""),

    // 400 BAD_REQUEST : 잘못된 요청
    INVALID_PHONE(BAD_REQUEST,"휴대폰 번호를 정확히 입력해주세요."),
    INVALID_PASSWORD(BAD_REQUEST,"비밀번호는 영문 대,소문자와 숫자가 적어도 1개 이상씩 포함된 8자 ~ 20자의 비밀번호여야 합니다."),
    INVALID_NICKNAME(BAD_REQUEST,"닉네임을 입력해주세요."),
    INVALID_PARAMS(BAD_REQUEST, "잘못된 정보입니다."),
    INVALID_TOKEN(BAD_REQUEST, "토큰이 유효하지 않습니다"),
    INVALID_TIME(BAD_REQUEST, "경매 마감 시간은 현재 시간보다 이후여야 합니다"),
    INVALID_CATEGORY(BAD_REQUEST, "잘못된 카테고리 입니다"),
    INVALID_SALETYPE(BAD_REQUEST, "잘못된 판매방법 입니다"),
    INVALID_MEMBER(BAD_REQUEST, "잘못된 사용자 입니다"),
    INVALID_PRODUCT(BAD_REQUEST, "잘못된 상품 정보 입니다"),
    INVALID_PRODUCT_ID(BAD_REQUEST, "잘못된 상품 ID 입니다"),
    INVALID_IMAGE_INFROM(BAD_REQUEST, "잘못된 이미지 정보입니다"),
    INVALID_IMAGE_STATUS(BAD_REQUEST, "잘못된 이미지 status입니다"),
    MAX_FILE_SIZE(BAD_REQUEST, "파일 용량이 최대치를 초과했습니다"),
    INVALID_PRICE(BAD_REQUEST, "현재 입찰가보다 낮게 입찰할 수 없습니다"),
    INVALID_FIX_PRICE(BAD_REQUEST, "상품 고정 가격을 입력해주세요"),
    INVALID_BIDDING_PRICE(BAD_REQUEST, "상품 시작 가격을 입력해주세요"),

    // 401 UNAUTHORIZED : 인증되지 않은 사용자
    INVALID_AUTH_TOKEN(UNAUTHORIZED, "권한 정보가 없는 토큰입니다"),

    // 403 Forbidden : 자원에 대한 권한 없음
    INVALID_AUTH(FORBIDDEN, "권한이 없습니다"),

    // 404 Not Found : 요청한 URI에 대한 리소스 없음
    INVALID_RESOURCE(NOT_FOUND, "요청한 리소스가 없습니다"),

    // 405 Method Not Allowed : 사용 불가능한 Method 이용
    INVALID_METHOD(METHOD_NOT_ALLOWED, "지원하지 않는 Method 입니다."),

    // 409 CONFLICT : 중복된 데이터 존재
    DUPLICATE_RESOURCE(CONFLICT, ""),

    // 500 INTERNAL_SERVER_ERROR : 서버 에러
    SERVER_ERROR(INTERNAL_SERVER_ERROR,"");

    private final HttpStatus httpStatus;
    private final String message;

}
