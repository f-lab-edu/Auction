package Auction.service.aop.product;

import Auction.service.dto.ProductDto;
import Auction.service.domain.product.SaleType;
import Auction.service.exception.CustomException;
import Auction.service.exception.CustomMessageException;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;

import java.time.LocalDateTime;

import static Auction.service.utils.ResultCode.*;
import static org.springframework.http.HttpStatus.BAD_REQUEST;

@Component
@RequiredArgsConstructor
@Aspect
public class ProductAspect {

    @Before("@annotation(Auction.service.aop.product.ProductCheck)")
    private void productCheck(JoinPoint joinPoint) {

        ProductDto productDto = (ProductDto) joinPoint.getArgs()[1];
        BindingResult bindingResult = (BindingResult) joinPoint.getArgs()[2];

        // ProductDto 유효성 확인
        bindingResultCheck(bindingResult);

        // 판매방법이 경매 or 경매&즉시구매일때 경매마감시간 유효성 확인
        if (productDto.getSaleType() == SaleType.BIDDING || productDto.getSaleType() == SaleType.FIX_AND_BIDDING) {

            if (productDto.getDeadline().isBefore(LocalDateTime.now())) {
                throw new CustomException(INVALID_TIME);
            }

            if (productDto.getStartPrice() == null) {
                throw new CustomException(INVALID_BIDDING_PRICE);
            }

            if(productDto.getSaleType() == SaleType.FIX_AND_BIDDING && productDto.getFixPrice()==null) {
                throw new CustomException(INVALID_FIX_PRICE);
            }
        } else if (productDto.getFixPrice() == null) {
            throw new CustomException(INVALID_FIX_PRICE);
        }
    }

    @Before("@annotation(Auction.service.aop.product.ProductDeleteCheck)")
    private void productDeleteCheck(JoinPoint joinPoint) {

        BindingResult bindingResult = (BindingResult) joinPoint.getArgs()[1];

        bindingResultCheck(bindingResult);

    }

    @Before("@annotation(Auction.service.aop.product.OrderCheck)")
    private void OrderCheck(JoinPoint joinPoint) {

        BindingResult bindingResult = (BindingResult) joinPoint.getArgs()[1];

        bindingResultCheck(bindingResult);
    }

    private void bindingResultCheck(BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            throw new CustomMessageException(BAD_REQUEST, bindingResult.getAllErrors().get(0).getDefaultMessage());
        }
    }

}
