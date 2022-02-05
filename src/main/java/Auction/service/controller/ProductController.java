package Auction.service.controller;

import Auction.service.aop.product.OrderCheck;
import Auction.service.aop.product.ProductCheck;
import Auction.service.aop.product.ProductDeleteCheck;
import Auction.service.dto.*;
import Auction.service.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.util.List;

import static Auction.service.utils.ResultCode.SUCCESS;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/product")
public class ProductController {

    private final ProductService productService;

    @ProductCheck
    @PostMapping
    public ResponseEntity<Result> uploadProduct(@RequestPart(value = "images", required = false) List<MultipartFile> images,
                                                @RequestPart(value = "productDto") @Valid ProductDto productDto,
                                                BindingResult bindingResult) {
        productService.upload(productDto, images);
        return Result.toResponseEntity(SUCCESS);
    }

    @ProductDeleteCheck
    @DeleteMapping
    public ResponseEntity<Result> deleteProduct(@RequestBody @Valid ProductDeleteDto productDeleteDto,
                                                BindingResult bindingResult) {
        productService.delete(productDeleteDto);
        return Result.toResponseEntity(SUCCESS);
    }

    @ProductCheck
    @PatchMapping
    public ResponseEntity<Result> updateProduct(@RequestPart(value = "images", required = false) List<MultipartFile> images,
                                                @RequestPart(value = "updateImgDtos", required = false) List<UpdateImgDto> updateImgDtos,
                                                @RequestPart(value = "productDto") @Valid ProductDto productDto,
                                                BindingResult bindingResult) {
        productService.update(productDto, images, updateImgDtos);
        return Result.toResponseEntity(SUCCESS);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Result> detailsProduct(@PathVariable Long id) {
        return Result.toResponseEntity(SUCCESS, productService.detail(id));
    }

    @OrderCheck
    @PostMapping("/order")
    public ResponseEntity<Result> order(@RequestBody @Valid OrderInfoDto orderInfoDto, BindingResult bindingResult) {
        productService.order(orderInfoDto);
        return Result.toResponseEntity(SUCCESS);
    }

    @GetMapping("/sendSMSList")
    public ResponseEntity<Result> sendSMSList(String time, @PageableDefault(page = 0, size = 1000) Pageable pageable) {
        return Result.toResponseEntity(SUCCESS, productService.getSendSMSList(time, pageable));
    }

    @PatchMapping("/sendSMS")
    public ResponseEntity<Result> updateSendSMS(@RequestBody SendSMSDto sendSMSDto) {
        return Result.toResponseEntity(SUCCESS, productService.updateSendSMS(sendSMSDto.getTime()));
    }

}
