package Auction.service.service;

import Auction.service.domain.product.ProductThumbnailState;
import Auction.service.condition.ProductSearchCond;
import Auction.service.dto.ProductSearchDto;
import Auction.service.dto.Result;
import Auction.service.repository.ProductSearchRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import static Auction.service.utils.ResultCode.SUCCESS;

@Service
@RequiredArgsConstructor
public class ProductSearchService {

    private final ProductSearchRepository productSearchRepository;

    public Page<ProductSearchDto> getListByNameAndCategory(String productName, Long categoryId, Pageable pageable){
        ProductSearchCond condition = new ProductSearchCond();
        condition.setProductName(productName);
        condition.setCategoryId(categoryId);
        return getProductList(condition, pageable);
    }

    private Page<ProductSearchDto> getProductList(ProductSearchCond condition, Pageable pageable){
        return productSearchRepository.findProductSearchList(condition, pageable);
    }
}
