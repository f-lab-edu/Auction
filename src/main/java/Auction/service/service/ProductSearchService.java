package Auction.service.service;

import Auction.service.Projection.ProductSearchProjection;
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
        Page<ProductSearchProjection> productSearchList = productSearchRepository.findProductSearchList(pageable);
        return Result.toResponseEntity(SUCCESS, productSearchList);
    }

}
