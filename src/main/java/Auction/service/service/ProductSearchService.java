package Auction.service.service;

import Auction.service.condition.ProductSearchCond;
import Auction.service.dto.ProductSearchDto;
import Auction.service.cache.RedisCategoryCheck;
import Auction.service.cache.RedisCategoryContainer;
import Auction.service.cache.ProductSearchCacheRepository;
import Auction.service.repository.ProductSearchRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProductSearchService {

    private final ProductSearchRepository productSearchRepository;
    private final ProductSearchCacheRepository productSearchCacheRepository;

    @RedisCategoryCheck
    public Page<ProductSearchDto> getListByNameAndCategory(String productName, Long categoryId, Pageable pageable){
        ProductSearchCond condition = new ProductSearchCond();
        condition.setProductName(productName);
        condition.setCategoryId(categoryId);
        Page<ProductSearchDto> productList = getProductList(condition, pageable);

        // cache write 시작
        RedisCategoryContainer redisCategoryContainer = new RedisCategoryContainer();
        redisCategoryContainer.setId(categoryId);
        for (ProductSearchDto productSearchDto : productList) {
            redisCategoryContainer.setProductItems(productSearchDto);
        }
        productSearchCacheRepository.save(redisCategoryContainer);

        return productList;
    }

    private Page<ProductSearchDto> getProductList(ProductSearchCond condition, Pageable pageable){
        return productSearchRepository.findProductSearchList(condition, pageable);
    }
}
