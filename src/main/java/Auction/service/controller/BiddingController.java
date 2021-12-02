package Auction.service.controller;

import Auction.service.dto.*;
import Auction.service.service.BiddingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static Auction.service.utils.ResultCode.SUCCESS;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/bidding")
public class BiddingController {

    private final BiddingService biddingService;

    @PostMapping
    public ResponseEntity<Result> biddingProduct(@RequestBody BiddingDto biddingDto) {
        biddingService.bidding(biddingDto);
        return Result.toResponseEntity(SUCCESS);
    }

}
