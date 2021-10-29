package Auction.service.dto;

import Auction.service.utils.ResultCode;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@Data
@AllArgsConstructor
@Builder
public class Result<T> {

    private int statusCode;
    private String message;
    private T data;

    public static<T> ResponseEntity<Result> toResponseEntity(HttpStatus statusCode, String responseMessage) {
        return toResponseEntity(statusCode, responseMessage, null);
    }

    public static<T> ResponseEntity<Result> toResponseEntity(ResultCode resultCode) {
        return toResponseEntity(resultCode, null);
    }

    public static<T> ResponseEntity<Result> toResponseEntity(ResultCode resultCode, T t) {
        return ResponseEntity
                .status(resultCode.getHttpStatus())
                .body(Result.builder()
                        .statusCode(resultCode.getHttpStatus().value())
                        .message(resultCode.getMessage())
                        .data(t)
                        .build()
                );
    }

    public static<T> ResponseEntity<Result> toResponseEntity(HttpStatus statusCode, String responseMessage, T t) {
        return ResponseEntity
                .status(statusCode)
                .body(Result.builder()
                        .statusCode(statusCode.value())
                        .message(responseMessage)
                        .data(t)
                        .build()
                );
    }

}