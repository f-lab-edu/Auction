package Auction.service.domain.product;

import Auction.service.domain.BaseTime;

import javax.persistence.*;

@Entity
public class Category extends BaseTime {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "category_id")
    private Long id;

    private String name;

}