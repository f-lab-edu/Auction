package Auction.service.configure;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AmazonS3Configure {

    @Bean
    public AmazonS3Client amazonS3Client() {
        BasicAWSCredentials awsCreds = new BasicAWSCredentials(System.getenv("AWS_S3_ACCESS_KEY"),
                System.getenv("AWS_S3_SECRET_KEY"));
        return (AmazonS3Client) AmazonS3ClientBuilder.standard()
                .withRegion(System.getenv("AWS_S3_REGION_KEY"))
                .withCredentials(new AWSStaticCredentialsProvider(awsCreds))
                .build();
    }
}

