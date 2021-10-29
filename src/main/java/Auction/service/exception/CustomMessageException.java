package Auction.service.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

/**
 * httpStatus, message 직접 입력
 */
@Getter
@AllArgsConstructor
public class CustomMessageException extends RuntimeException{
    private final HttpStatus httpStatus;
    private final String message;
}
