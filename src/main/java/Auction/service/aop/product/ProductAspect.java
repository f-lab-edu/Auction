package Auction.service.aop.product;

import Auction.service.dto.ProductDto;
import Auction.service.domain.product.SaleType;
import Auction.service.exception.CustomException;
import Auction.service.exception.CustomMessageException;
import Auction.service.repository.CategoryRepository;
import Auction.service.repository.MemberRepository;
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

    private final CategoryRepository categoryRepository;
    private final MemberRepository memberRepository;

    @Before("@annotation(Auction.service.aop.product.ProductCheck)")
    private void productCheck(JoinPoint joinPoint) {

        ProductDto productDto = (ProductDto) joinPoint.getArgs()[1];
        BindingResult bindingResult = (BindingResult) joinPoint.getArgs()[2];

        // ProductDto 유효성 확인
        if(bindingResult.hasErrors()) {
            throw new CustomMessageException(BAD_REQUEST, bindingResult.getAllErrors().get(0).getDefaultMessage());
        }

        // member 유효성 확인
        if(!memberRepository.existsById(productDto.getMember_id())){
            throw new CustomException(INVALID_MEMBER);
        }

        // 카테고리 유효성 확인
        if(!categoryRepository.existsById(productDto.getCategory_id())) {
            throw new CustomException(INVALID_CATEGORY);
        }

        // 판매방법 유효성 확인
        if(SaleType.valueOf(productDto.getSaleType())==null) {
            throw new CustomException(INVALID_SALETYPE);
        }

        // 판매방법이 경매 or 경매&즉시구매일때 경매마감시간 유효성 확인
        if(productDto.getSaleType() == SaleType.BIDDING.name() || productDto.getSaleType() == SaleType.FIX_AND_BIDDING.name()) {
            if(productDto.getDeadline().isBefore(LocalDateTime.now())) {
                throw new CustomException(INVALID_TIME);
            }
        }
    }
}
