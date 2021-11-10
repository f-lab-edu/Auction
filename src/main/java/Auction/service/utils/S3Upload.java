package Auction.service.utils;

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
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class S3Upload {

    private final AmazonS3Client amazonS3Client;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    public String upload(MultipartFile file) throws IOException {
        String fileName = UUID.randomUUID() + file.getOriginalFilename();

        ObjectMetadata objMeta = new ObjectMetadata();
        byte[] bytes = IOUtils.toByteArray(file.getInputStream());
        objMeta.setContentLength(bytes.length);

        amazonS3Client.putObject(new PutObjectRequest(bucket, fileName, file.getInputStream(), objMeta)
                .withCannedAcl(CannedAccessControlList.PublicRead));
        return amazonS3Client.getUrl(bucket, fileName).toString();
    }
}