package Auction.service.controller;

import Auction.service.dto.Result;
import Auction.service.service.ProductSearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
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

    @GetMapping("/search")
    public ResponseEntity<Result> getProductListByName(@RequestParam(value = "name", required = false)String name,
                                                       @RequestParam(value = "categoryId", required = false)Long categoryId,
                                                       @PageableDefault(page = 0, size = 20) Pageable pageable){

        return productSearchService.getListByName(name, categoryId, pageable);
    }


}
