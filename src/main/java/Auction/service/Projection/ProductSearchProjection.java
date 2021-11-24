package Auction.service.Projection;

public interface ProductSearchProjection {

    Long getId();
    String getProductName();
    String getCategoryName();
    String getSaleType();
    Integer getFixPrice();
    Integer getNowPrice();
    String getFileName();

}
