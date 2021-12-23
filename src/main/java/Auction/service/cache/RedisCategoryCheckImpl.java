package Auction.service.cache;

import Auction.service.dto.ProductSearchDto;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
@Aspect
public class RedisCategoryCheckImpl {

    private final ProductSearchCacheRepository productSearchCacheRepository;

    @Around("@annotation(Auction.service.cache.RedisCategoryCheck))")
    private Page<ProductSearchDto> RedisCategoryCheck(ProceedingJoinPoint pjp) throws Throwable{

        Long categoryId = (Long)pjp.getArgs()[1];
        Pageable pagable = (Pageable)pjp.getArgs()[2];

        if(categoryId != null){
            RedisCategoryContainer redisCategoryContainer =
                    productSearchCacheRepository.findById(categoryId).orElse(null);

            if(redisCategoryContainer != null){
                for (ProductSearchDto productItem : redisCategoryContainer.getProductItems()) {
                    System.out.println("in redis productItem = " + productItem);
                }
                List<ProductSearchDto> productItems = redisCategoryContainer.getProductItems();
                int start = (int)pagable.getOffset();
                int end = Math.min((start + pagable.getPageSize()), productItems.size());
                return new PageImpl<>(productItems.subList(start, end), pagable, productItems.size());
            }
        }

        Page<ProductSearchDto> proceed = (Page<ProductSearchDto>)pjp.proceed();
        return proceed;
    }
}
