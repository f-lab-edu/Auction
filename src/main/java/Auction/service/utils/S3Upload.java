package Auction.service.utils;

import Auction.service.domain.product.ProductImg;
import Auction.service.exception.CustomMessageException;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.util.IOUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

@Service
@RequiredArgsConstructor
public class S3Upload {

    private final AmazonS3Client amazonS3Client;
    public static String PRODUCT_FOLDER = "product/";

    public String upload(MultipartFile file) {

        String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();

        try {
            ObjectMetadata objMeta = new ObjectMetadata();
            byte[] bytes = IOUtils.toByteArray(file.getInputStream());
            objMeta.setContentLength(bytes.length);

            amazonS3Client.putObject(new PutObjectRequest(System.getenv("AWS_S3_BUCKET_KEY"),
                    PRODUCT_FOLDER + fileName, file.getInputStream(), objMeta)
                    .withCannedAcl(CannedAccessControlList.PublicRead));
        } catch (IOException e) {
            throw new CustomMessageException(INTERNAL_SERVER_ERROR, e.getMessage());
        }

        return fileName;
    }

    public void delete(String fileName) {
        amazonS3Client.deleteObject(System.getenv("AWS_S3_BUCKET_KEY"), PRODUCT_FOLDER + fileName);
    }
}
