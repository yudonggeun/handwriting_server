package com.promotion.handwriting;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
public class AwsConfig {

    private final Regions regions = Regions.AP_NORTHEAST_2;//한국
    @Value("${aws.s3.accessKey}")
    private String accessKey;
    @Value("${aws.s3.secretKey}")
    private String secretKey;

    @Bean
    public AmazonS3 createS3Client(){
        if(accessKey == null || secretKey == null) return null;

        BasicAWSCredentials credentials = new BasicAWSCredentials(accessKey, secretKey);
        return AmazonS3ClientBuilder
                .standard()
                .enableUseArnRegion()
                .withCredentials(new AWSStaticCredentialsProvider(credentials))
                .withRegion(regions)
                .build();
    }
}
