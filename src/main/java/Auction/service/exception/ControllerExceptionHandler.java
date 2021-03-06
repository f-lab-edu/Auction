package Auction.service.exception;

import Auction.service.dto.Result;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.HttpClientErrorException.Forbidden;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.servlet.NoHandlerFoundException;

import static Auction.service.utils.ResultCode.*;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

@RestControllerAdvice
public class ControllerExceptionHandler {

    /**
     * 커스텀 런타임 에러 : ResultCode enum class 이용
     */
    @ExceptionHandler(CustomException.class)
    private ResponseEntity<Result> customException(CustomException e) {
        return Result.toResponseEntity(e.getResultCode());
    }

    /**
     * 커스텀 런타임 에러 : httpStatus, message 직접 입력
     */
    @ExceptionHandler(CustomMessageException.class)
    private ResponseEntity<Result> customMessageException(CustomMessageException e) {
        return Result.toResponseEntity(e.getHttpStatus(), e.getMessage());
    }

    /**
     * 자원에 대한 권한 없음
     */
    @ExceptionHandler(Forbidden.class)
    private ResponseEntity<Result> forbiddenException() {
        return Result.toResponseEntity(INVALID_AUTH);
    }

    /**
     * 요청한 URI에 대한 리소스 없음
     */
    @ExceptionHandler(NoHandlerFoundException.class)
    private ResponseEntity<Result> resourceException() {
        return Result.toResponseEntity(INVALID_RESOURCE);
    }

    /**
     * 사용 불가능한 Method 이용
     */
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    private ResponseEntity<Result> methodException() {
        return Result.toResponseEntity(INVALID_METHOD);
    }

  /**
   * 파일 용량 제한 초과
   */
  @ExceptionHandler(MaxUploadSizeExceededException.class)
    private ResponseEntity<Result> maxUploadSizeException() {
      return Result.toResponseEntity(MAX_FILE_SIZE);
    }

    /**
     * 그 외 에러
     */
    @ExceptionHandler(Exception.class)
    private ResponseEntity<Result> Exception(Exception e) {
        return Result.toResponseEntity(INTERNAL_SERVER_ERROR, e.getMessage());
    }

}
