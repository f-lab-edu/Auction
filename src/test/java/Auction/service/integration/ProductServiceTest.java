package Auction.service.integration;

import Auction.service.domain.member.Member;
import Auction.service.domain.product.*;
import Auction.service.dto.MemberDto;
import Auction.service.dto.ProductDto;
import Auction.service.dto.UpdateImgDto;
import Auction.service.exception.CustomException;
import Auction.service.repository.CategoryRepository;
import Auction.service.repository.MemberRepository;
import Auction.service.repository.ProductRepository;
import Auction.service.utils.S3Upload;
import com.amazonaws.services.s3.AmazonS3Client;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItem;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import org.testcontainers.shaded.org.apache.commons.io.IOUtils;

import javax.transaction.Transactional;
import java.io.*;
import java.nio.file.Files;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static Auction.service.utils.ResultCode.*;
import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("test")
@SpringBootTest(properties = "spring.config.location = classpath:/application.yaml, classpath:/aws.yaml", classes = LocalStackS3Config.class)
public class ProductServiceTest {

    private final static String IMAGE_NAME = "test.jpg";
    private final static String IMAGE_DIR = "/src/test/resources/static/";

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private AmazonS3Client amazonS3Client;

    @Autowired
    private S3Upload s3Upload;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    Product originalProduct;

    @BeforeEach
    void beforeEach() {
        ProductDto productDto = ProductDto.builder()
                .memberId(1l)
                .name("name")
                .description("description")
                .categoryId(1L)
                .saleType(SaleType.BIDDING)
                .deadline(LocalDateTime.of(2022,2, 6, 13, 10, 0))
                .build();
        Product product = ProductDto.toEntity(productDto);

        MemberDto memberDto = new MemberDto("01099999999", "pwd12345", "sohee");
        Member member = memberRepository.save(MemberDto.toEntity(memberDto, passwordEncoder));
        Category category = categoryRepository.save(new Category(productDto.getCategoryId(), "category"));

        product.setCategory(category);
        product.setMember(member);

        originalProduct = productRepository.save(product);

        amazonS3Client.createBucket(bucket);
    }

    @Transactional
    @DisplayName("상품 업로드 - O")
    @Test
    void upload() throws IOException {

        ProductDto productDto = ProductDto.builder()
                .memberId(1l)
                .name("name")
                .description("description")
                .categoryId(1L)
                .saleType(SaleType.BIDDING)
                .deadline(LocalDateTime.of(2022,2, 6, 13, 10, 0))
                .build();

        Product product = ProductDto.toEntity(productDto);

        Member findMember = memberRepository.findById(1L).orElseThrow(()->new CustomException(INVALID_MEMBER));
        product.setMember(findMember);

        Category findCategory = categoryRepository.findById(1L).orElseThrow(() -> new CustomException(INVALID_CATEGORY));
        product.setCategory(findCategory);

        Product newProduct = productRepository.save(product);

        boolean isThumbnail = true;
        ProductThumbnailState state = ProductThumbnailState.THUMBNAIL;

        File file = new File(new File("").getAbsolutePath() + IMAGE_DIR + IMAGE_NAME);
        MultipartFile multipartFile = generateMultiPartFile(file);

        List<MultipartFile> images = new ArrayList<>();
        images.add(multipartFile);
        images.add(multipartFile);
        images.add(multipartFile);

        for (MultipartFile image : images) {
            String fileName = s3Upload.upload(image);

            newProduct.addImage(new ProductImg(newProduct, fileName, state));

            if(isThumbnail == true){
                state = ProductThumbnailState.NORMAL;
                isThumbnail = false;
            }
        }
        productRepository.save(newProduct);

        assertThat(productRepository.findProductById(newProduct.getId()).getCategory().getId()).isEqualTo(1L);
        assertThat(productRepository.findProductById(newProduct.getId()).getImages().size()).isEqualTo(3);

    }

    @Transactional
    @DisplayName("상품 수정 - O [이미지 수정]")
    @Test
    void imageUpdate() throws IOException {

        File file = new File(new File("").getAbsolutePath() + IMAGE_DIR + IMAGE_NAME);
        MultipartFile multipartFile = generateMultiPartFile(file);

        List<MultipartFile> images = new ArrayList<>();
        images.add(multipartFile);
        images.add(multipartFile);

        List<UpdateImgDto> updateImgDtos = new ArrayList<>();
        updateImgDtos.add(new UpdateImgDto(null, UpdateImgDto.Status.INSERT.toString()));
        updateImgDtos.add(new UpdateImgDto(null, UpdateImgDto.Status.INSERT.toString()));

        List<ProductImg> originalImgs = originalProduct.getImages();

        List<Long> originalImgIds = originalImgs.stream().map(ProductImg::getId).collect(Collectors.toList());

        if (originalImgIds != null) {
            deleteImage(updateImgDtos, originalImgs, originalImgIds);
        }

        for (UpdateImgDto updateImgDto : updateImgDtos) {
            int updateIdx = updateImgDtos.indexOf(updateImgDto);

            if(originalImgs.isEmpty() == true){
                originalImgs.add(new ProductImg(originalProduct, s3Upload.upload(images.get(updateIdx)),
                        ProductThumbnailState.THUMBNAIL));
            }else {
                originalImgs.add(new ProductImg(originalProduct, s3Upload.upload(images.get(updateIdx)),
                        ProductThumbnailState.NORMAL));
            }
        }

        productRepository.save(originalProduct);

        assertThat(productRepository.getById(originalProduct.getId()).getImages().size()).isEqualTo(2);

    }

    @Transactional
    @DisplayName("상품 수정 - O [일반 정보 수정]")
    @Test
    void update() {

        Long productId = originalProduct.getId();

        ProductDto updateProductDto = ProductDto.builder()
                .productId(productId)
                .memberId(1l)
                .name("name1")
                .description("description1")
                .categoryId(2L)
                .saleType(SaleType.FIX)
                .fixPrice(1000)
                .build();

        Category updateCategory = categoryRepository.findById(updateProductDto.getCategoryId()).orElseThrow(() -> new CustomException(INVALID_CATEGORY));
        originalProduct.setCategory(updateCategory);

        originalProduct.update(updateProductDto);

        productRepository.save(originalProduct);

        assertThat(productRepository.getById(productId).getName()).isEqualTo(updateProductDto.getName());
        assertThat(productRepository.getById(productId).getDescription()).isEqualTo(updateProductDto.getDescription());
        assertThat(productRepository.getById(productId).getCategory().getId()).isEqualTo(updateProductDto.getCategoryId());
        assertThat(productRepository.getById(productId).getSaleType()).isEqualTo(updateProductDto.getSaleType());
        assertThat(productRepository.getById(productId).getFixPrice()).isEqualTo(updateProductDto.getFixPrice());

    }

    @Transactional
    @DisplayName("상품 삭제 - O")
    @Test
    void delete() {

        Long productId = originalProduct.getId();
        productRepository.delete(originalProduct);

        List<ProductImg> productImgs = originalProduct.getImages();

        if (productImgs != null) {
            for (ProductImg productImg : productImgs) {
                s3Upload.delete(productImg.getFileName());
            }
        }

        assertThat(productRepository.findById(productId)).isEqualTo(Optional.empty());
    }


    private void deleteImage(List<UpdateImgDto> updateImgDtos, List<ProductImg> originalProductImages, List<Long> originalImgIds) {
        List<Long> updateImgIds = updateImgDtos.stream().map(UpdateImgDto::getProduct_img_id).collect(Collectors.toList());
        for (Long originalImgId : originalImgIds) {
            if (!updateImgIds.contains(originalImgId)) {
                Optional<ProductImg> productImg = originalProductImages.stream().filter(img -> img.getId() == originalImgId).findAny();
                originalProductImages.remove(productImg.get());
                s3Upload.delete(productImg.get().getFileName());
            }
        }
    }

    private MultipartFile generateMultiPartFile(File file) throws IOException {
        FileItem fileItem = new DiskFileItem("test", Files.probeContentType(file.toPath()), false, file.getName(), (int) file.length(), file.getParentFile());

        InputStream input = new FileInputStream(file);
        OutputStream os = fileItem.getOutputStream();
        IOUtils.copy(input, os);
        return new CommonsMultipartFile(fileItem);
    }

}
