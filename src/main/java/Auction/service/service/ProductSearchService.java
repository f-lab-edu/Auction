package Auction.service.service;

import Auction.service.Projection.ProductSearchProjection;
import Auction.service.domain.product.ProductThumbnailState;
import Auction.service.dto.ProductSearchCond;
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

    public ResponseEntity<Result> getList(Pageable pageable) {
        ProductSearchCond condition = new ProductSearchCond(
                null, null, ProductThumbnailState.THUMBNAIL);
        return getProductList(condition, pageable);
    }

    public ResponseEntity<Result> getListByName(String productName, Long categoryId, Pageable pageable){
        ProductSearchCond condition = new ProductSearchCond(productName, categoryId, ProductThumbnailState.THUMBNAIL);
        return getProductList(condition, pageable);
    }

    private ResponseEntity<Result> getProductList(ProductSearchCond condition, Pageable pageable){
        Page<ProductSearchDto> productSearchList = productSearchRepository
                .findProductSearchList(condition, pageable);
        return Result.toResponseEntity(SUCCESS, productSearchList);
    }
}
