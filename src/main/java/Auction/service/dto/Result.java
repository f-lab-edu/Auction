package Auction.service.dto;

import Auction.service.utils.ResultCode;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.springframework.http.ResponseEntity;

@Data
@AllArgsConstructor
@Builder
public class Result<T> {

    private int statusCode;
    private String message;
    private T data;

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
}