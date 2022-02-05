package Auction.service.dto;

public interface SendSMSProjection {

    String getMemberPhone();
    Long  getProductId();
    String getProductName();
    String getProductPrice();

}
