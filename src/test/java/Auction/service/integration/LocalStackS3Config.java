package Auction.service.integration;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.testcontainers.containers.localstack.LocalStackContainer;
import org.testcontainers.utility.DockerImageName;

@TestConfiguration
public class LocalStackS3Config {
    DockerImageName localstackImage = DockerImageName.parse("localstack/localstack");

    @Bean(initMethod = "start", destroyMethod = "stop")
    public LocalStackContainer localStackContainer() {
        return new LocalStackContainer(localstackImage)
                .withServices(LocalStackContainer.Service.S3);
    }

    @Bean
    public AmazonS3Client amazonS3Client(LocalStackContainer localStackContainer) {
        return (AmazonS3Client)AmazonS3ClientBuilder.standard()
                .withEndpointConfiguration(localStackContainer.getEndpointConfiguration(LocalStackContainer.Service.S3))
                .withCredentials(localStackContainer.getDefaultCredentialsProvider())
                .build();
    }
}
