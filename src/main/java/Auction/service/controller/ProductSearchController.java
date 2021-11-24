package Auction.service.controller;

import Auction.service.dto.Result;
import Auction.service.service.ProductSearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/products")
public class ProductSearchController {

    private final ProductSearchService productSearchService;

    @GetMapping
    public ResponseEntity<Result> getProductList(Pageable pageable) {
        return productSearchService.getList(pageable);
    }

}
