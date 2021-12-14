package Auction.service.repository;


import Auction.service.domain.product.*;

import Auction.service.dto.ProductSearchCond;
import Auction.service.dto.ProductSearchDto;
import Auction.service.dto.QProductSearchDto;
import com.querydsl.core.QueryResults;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;

import javax.persistence.EntityManager;
import java.util.List;

public class ProductSearchRepositoryImpl implements ProductSearchCustom{

    private final EntityManager em;
    private final JPAQueryFactory queryFactory;

    private QProduct product = QProduct.product;
    private QCategory category = QCategory.category;
    private QProductImg productImg = QProductImg.productImg;

    public ProductSearchRepositoryImpl(EntityManager em){
        this.em = em;
        queryFactory = new JPAQueryFactory(em);
    }

    private BooleanExpression productSearchByName(String productName){
        return productName != null ? product.name.contains(productName) : null;
    }

    private BooleanExpression productSearchByCategory(Long categoryId){
        return categoryId != null ? category.id.eq(categoryId) : null;
    }

    private BooleanExpression productSearchByThumbState(ProductThumbnailState state){
        return state != null ? productImg.thumbState.eq(state) : null;
    }

    public Page<ProductSearchDto> findProductSearchList(ProductSearchCond condition, Pageable pageable){

        List<ProductSearchDto> results = queryFactory
                .select(new QProductSearchDto(
                        product.id,
                        product.name,
                        category.category,
                        product.saleType,
                        product.fixPrice,
                        product.nowPrice,
                        productImg.file_name
                )).from(product)
                .leftJoin(product.category, category)
                .leftJoin(product.images, productImg)
                .where(productSearchByName(condition.getProductName()),
                        productSearchByCategory(condition.getCategory()),
                        productSearchByThumbState(condition.getState()))
                .orderBy(product.regDate.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        JPAQuery<ProductSearchDto> countQuery = queryFactory
                .select(new QProductSearchDto(
                        product.id,
                        product.name,
                        category.category,
                        product.saleType,
                        product.fixPrice,
                        product.nowPrice,
                        productImg.file_name
                )).from(product)
                .leftJoin(product.category, category)
                .leftJoin(product.images, productImg)
                .where(productSearchByName(condition.getProductName()),
                        productSearchByCategory(condition.getCategory()),
                        productSearchByThumbState(condition.getState()));

        return PageableExecutionUtils.getPage(results, pageable, countQuery::fetchCount);
    }

}