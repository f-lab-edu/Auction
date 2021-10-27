package Auction.service.exception;

import Auction.service.utils.ResultCode;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CustomException extends RuntimeException{
    private final ResultCode resultCode;
}
