package Auction.service.repository;

import Auction.service.dto.ProductSearchCond;
import Auction.service.dto.ProductSearchDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ProductSearchCustom {
    public Page<ProductSearchDto> findProductSearchList(ProductSearchCond condition, Pageable pageable);
}
