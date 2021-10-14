package Auction.service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.springframework.http.HttpStatus;

@Data
@AllArgsConstructor
@Builder
public class Result<T> {

    private int statusCode;
    private String message;
    private T data;

    public Result(final int statusCode, final String message) {
        this.statusCode = statusCode;
        this.message = message;
        this.data = null;
    }

    public static<T> Result<T> response(final HttpStatus statusCode) {
        return response(statusCode, null, null);
    }

    public static<T> Result<T> response(final HttpStatus statusCode, final String responseMessage) {
        return response(statusCode, responseMessage, null);
    }

    public static<T> Result<T> response(final HttpStatus statusCode, final String message, final T t) {
        return Result.<T>builder()
                .statusCode(statusCode.value())
                .message(message)
                .data(t)
                .build();
    }
}