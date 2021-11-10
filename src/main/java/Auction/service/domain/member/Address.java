package Auction.service.domain.member;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
public class Address {

    private String city;
    private String street;
    private String zipcode;

}
