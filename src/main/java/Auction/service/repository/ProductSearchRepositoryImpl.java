package Auction.service.repository;


import Auction.service.domain.product.*;

import Auction.service.condition.ProductSearchCond;
import Auction.service.dto.ProductSearchDto;
import Auction.service.dto.QProductSearchDto;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;

import java.util.List;

@RequiredArgsConstructor
public class ProductSearchRepositoryImpl implements ProductSearchCustom{

    private final JPAQueryFactory queryFactory;

    private QProduct product = QProduct.product;
    private QCategory category = QCategory.category;
    private QProductImg productImg = QProductImg.productImg;

    private BooleanExpression productSearchByName(String productName){
        return productName != null ? product.name.contains(productName) : null;
    }

    private BooleanExpression productSearchByCategory(Long categoryId){
        return categoryId != null ? category.id.eq(categoryId) : null;
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
                        productImg.fileName
                )).from(product)
                .leftJoin(product.category, category)
                .leftJoin(product.images, productImg)
                .where(productSearchByName(condition.getProductName()),
                        productSearchByCategory(condition.getCategoryId()),
                        productImg.thumbState.eq(ProductThumbnailState.THUMBNAIL))
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
                        productImg.fileName
                )).from(product)
                .leftJoin(product.category, category)
                .where(productSearchByName(condition.getProductName()),
                        productSearchByCategory(condition.getCategoryId()),
                        productImg.thumbState.eq(ProductThumbnailState.THUMBNAIL));

        return PageableExecutionUtils.getPage(results, pageable, countQuery::fetchCount);
    }

}