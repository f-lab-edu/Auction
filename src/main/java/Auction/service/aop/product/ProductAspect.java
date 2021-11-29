package Auction.service.aop.product;

import Auction.service.dto.ProductDeleteDto;
import Auction.service.dto.ProductDto;
import Auction.service.domain.product.SaleType;
import Auction.service.exception.CustomException;
import Auction.service.exception.CustomMessageException;
import Auction.service.repository.CategoryRepository;
import Auction.service.repository.MemberRepository;
import Auction.service.repository.ProductRepository;
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

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final MemberRepository memberRepository;

    @Before("@annotation(Auction.service.aop.product.ProductCheck)")
    private void productCheck(JoinPoint joinPoint) {

        ProductDto productDto = (ProductDto) joinPoint.getArgs()[1];
        BindingResult bindingResult = (BindingResult) joinPoint.getArgs()[2];

        // ProductDto 유효성 확인
        bindingResultCheck(bindingResult);

        // member 유효성 확인
        memberCheck(productDto.getMemberId());

        // 카테고리 유효성 확인
        if(!categoryRepository.existsById(productDto.getCategoryId())) {
            throw new CustomException(INVALID_CATEGORY);
        }

        // 판매방법이 경매 or 경매&즉시구매일때 경매마감시간 유효성 확인
        if(productDto.getSaleType() == SaleType.BIDDING || productDto.getSaleType() == SaleType.FIX_AND_BIDDING) {
            if(productDto.getDeadline().isBefore(LocalDateTime.now())) {
                throw new CustomException(INVALID_TIME);
            }
        }
    }

    @Before("@annotation(Auction.service.aop.product.ProductDeleteCheck)")
    private void productDeleteCheck(JoinPoint joinPoint) {

        ProductDeleteDto productDeleteDto = (ProductDeleteDto) joinPoint.getArgs()[0];
        BindingResult bindingResult = (BindingResult) joinPoint.getArgs()[1];

        // ProductDeleteDto 유효성 확인
        bindingResultCheck(bindingResult);

        // member 유효성 확인
        memberCheck(productDeleteDto.getMember_id());

        // product 유효성 확인
        if(!productRepository.existsById(productDeleteDto.getProduct_id())){
            throw new CustomException(INVALID_PRODUCT);
        }

    }

    private void memberCheck(Long member_id) {

      if(!memberRepository.existsById(member_id)){
          throw new CustomException(INVALID_MEMBER);
        }
    }

    private void bindingResultCheck(BindingResult bindingResult) {

        if(bindingResult.hasErrors()) {
            throw new CustomMessageException(BAD_REQUEST, bindingResult.getAllErrors().get(0).getDefaultMessage());
        }
    }

}
