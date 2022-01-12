package Auction.service.service;

import Auction.service.cache.ProductSearchCacheRepository;
import Auction.service.domain.member.Member;
import Auction.service.domain.product.*;
import Auction.service.dto.ProductDeleteDto;
import Auction.service.dto.ProductDto;
import Auction.service.dto.UpdateImgDto;
import Auction.service.exception.CustomException;
import Auction.service.redis.RedisSubscriber;
import Auction.service.repository.*;
import Auction.service.utils.S3Upload;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock
    ProductRepository productRepository;

    @Mock
    CategoryRepository categoryRepository;

    @Mock
    MemberRepository memberRepository;

    @Mock
    ProductSearchCacheRepository productSearchCacheRepository;

    @Mock
    RedisSubscriber redisSubscriber;

    @Mock
    SseEmitterRepository sseEmitterRepository;

    @InjectMocks
    ProductService productService;

    @Mock
    S3Upload s3Upload;

    Product product;

    ProductDto productDto;

    List<MultipartFile> images;

    List<UpdateImgDto> updateImgDtos;

    Member member;

    Category category;

    @BeforeEach
    void before() {
        productDto = ProductDto.builder()
                .memberId(1l)
                .name("상품명")
                .description("상품 설명")
                .categoryId(1L)
                .saleType(SaleType.BIDDING)
                .deadline(LocalDateTime.of(2022,2, 6, 13, 10, 0))
                .build();
        product = ProductDto.toEntity(productDto);

        member = Member.builder().id(1L).build();
        category = new Category(1L, "카테고리명");

    }

    @DisplayName("상품 업로드 - O")
    @Test
    void upload() {

        beforeUpload();

        when(memberRepository.findById(any())).thenReturn(Optional.of(member));
        when(categoryRepository.findById(any())).thenReturn(Optional.of(category));

        product.setCategory(category);
        when(productRepository.save(any())).thenReturn(product);
        when(s3Upload.upload(any())).thenReturn("image");
        when(productSearchCacheRepository.existsById(any())).thenReturn(false);

        productService.upload(productDto, images);

        verify(memberRepository,times(1)).findById(any());
        verify(categoryRepository,times(1)).findById(any());
        verify(productRepository,times(2)).save(any());
        verify(s3Upload,times(3)).upload(any());
        verify(productSearchCacheRepository,times(1)).existsById(any());

    }

    @DisplayName("상품 업로드 - X [잘못된 MemberId]")
    @Test
    void upload_invalidMemberId() {

        doThrow(CustomException.class).when(memberRepository).findById(any());

        assertThrows(CustomException.class,
                ()-> productService.upload(productDto, images));

        verify(memberRepository,times(1)).findById(any());

    }

    @DisplayName("상품 업로드 - X [잘못된 CategoryId]")
    @Test
    void upload_invalidCategoryId() {

        when(memberRepository.findById(any())).thenReturn(Optional.of(member));
        doThrow(CustomException.class).when(categoryRepository).findById(any());

        assertThrows(CustomException.class,
                ()-> productService.upload(productDto, images));

        verify(memberRepository,times(1)).findById(any());
        verify(categoryRepository,times(1)).findById(any());

    }

    @Test
    @DisplayName("상품 삭제 - O")
    public void delete() throws Exception {

        beforeDelete();

        when(productRepository.findById(any())).thenReturn(Optional.of(product));
        doNothing().when(productRepository).delete(product);
        when(productSearchCacheRepository.existsById(any())).thenReturn(false);
        doNothing().when(s3Upload).delete(any());
        doNothing().when(redisSubscriber).removeChannel(any());
        doNothing().when(sseEmitterRepository).deleteAll(any());

        productService.delete(new ProductDeleteDto(1L,1l));

        verify(productRepository,times(1)).findById(any());
        verify(productRepository,times(1)).delete(any());
        verify(productSearchCacheRepository,times(1)).existsById(any());
        verify(s3Upload,times(3)).delete(any());
        verify(redisSubscriber,times(1)).removeChannel(any());
        verify(sseEmitterRepository,times(1)).deleteAll(any());

    }

    @DisplayName("상품 삭제 - X [잘못된 ProductId]")
    @Test
    void delete_invalid_ProductId() {

        doThrow(CustomException.class).when(productRepository).findById(any());

        assertThrows(CustomException.class,
                ()-> productService.delete(new ProductDeleteDto(1L,1l)));

        verify(productRepository,times(1)).findById(any());

    }

    @DisplayName("상품 삭제 - X [상품 memberId와 삭제 요청한 memberId 불일치]")
    @Test
    void delete_invalid_memberId() {

        beforeDelete();

        when(productRepository.findById(any())).thenReturn(Optional.of(product));

        assertThrows(CustomException.class,
                ()-> productService.delete(new ProductDeleteDto(2L,1l)));

        verify(productRepository,times(1)).findById(any());

    }

    @DisplayName("상품 수정 - X [잘못된 ProductId]")
    @Test
    void update_invalid_ProductId() {

        doThrow(CustomException.class).when(productRepository).findById(any());

        assertThrows(CustomException.class,
                ()-> productService.update(productDto, images, updateImgDtos));

        verify(productRepository,times(1)).findById(any());

    }

    @DisplayName("상품 수정 - X [잘못된 CategoryId]")
    @Test
    void update_invalid_CategoryId() {

        when(productRepository.findById(any())).thenReturn(Optional.of(product));
        doThrow(CustomException.class).when(categoryRepository).findById(any());

        assertThrows(CustomException.class,
                ()-> productService.update(productDto, images, updateImgDtos));

        verify(productRepository,times(1)).findById(any());
        verify(categoryRepository,times(1)).findById(any());

    }

    @DisplayName("상품 수정 - X [MultipartFile과 MultipartFile 사이즈 불일치]")
    @Test
    void update_invalid_Img() {

        beforeUpdate();

        images.add(mock(MultipartFile.class));

        // images size = 4, updateImgDtos size = 3
        assertThrows(CustomException.class,
                ()-> productService.update(productDto, images, updateImgDtos));

        verify(productRepository,times(1)).findById(any());

    }

    void beforeUpload() {
        images = new ArrayList<>();
        images.add(mock(MultipartFile.class));
        images.add(mock(MultipartFile.class));
        images.add(mock(MultipartFile.class));
    }

    void beforeUpdate() {

        images = new ArrayList<>();
        images.add(mock(MultipartFile.class));
        images.add(mock(MultipartFile.class));
        images.add(mock(MultipartFile.class));

        updateImgDtos = new ArrayList<>();
        updateImgDtos.add(mock(UpdateImgDto.class));
        updateImgDtos.add(mock(UpdateImgDto.class));
        updateImgDtos.add(mock(UpdateImgDto.class));
    }

    void beforeDelete() {
        product.setMember(member);
        product.setCategory(category);
        product.getImages().add(new ProductImg(product, "fileName", ProductThumbnailState.THUMBNAIL));
        product.getImages().add(new ProductImg(product, "fileName", ProductThumbnailState.NORMAL));
        product.getImages().add(new ProductImg(product, "fileName", ProductThumbnailState.NORMAL));
    }

}