package Auction.service.exception;

import Auction.service.utils.ResultCode;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * enum 클래스 이용
 */
@Getter
@AllArgsConstructor
public class CustomException extends RuntimeException{
    private final ResultCode resultCode;
}
