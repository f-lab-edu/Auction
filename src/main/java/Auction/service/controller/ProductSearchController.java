package Auction.service.controller;

import Auction.service.dto.ProductSearchDto;
import Auction.service.dto.Result;
import Auction.service.service.ProductSearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static Auction.service.utils.ResultCode.SUCCESS;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/products")
public class ProductSearchController {

    private final ProductSearchService productSearchService;

    @GetMapping("/search")
    public ResponseEntity<Result> getProductListByName(@RequestParam(value = "name", required = false)String name,
                                                       @RequestParam(value = "categoryId", required = false)Long categoryId,
                                                       @PageableDefault(page = 0, size = 20) Pageable pageable){

        Page<ProductSearchDto> results = productSearchService.getListByNameAndCategory(name, categoryId, pageable);
        return Result.toResponseEntity(SUCCESS,results);
    }
}
