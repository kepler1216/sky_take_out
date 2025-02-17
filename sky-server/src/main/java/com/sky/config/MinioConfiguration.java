package com.sky.config;
import com.sky.properties.MinioProperties;
import com.sky.utils.MinIOUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
//配置类，用于创建MinioUtil对象
public class MinioConfiguration {
    @Bean
    @ConditionalOnMissingBean

    public MinIOUtil minioUtil(MinioProperties minioProperties) {
        log.info("开始创建minio工具类对象minioProperties:{}", minioProperties);
        return new MinIOUtil(minioProperties.getUrl(),
                minioProperties.getAccessKey(),
                minioProperties.getSecretKey(),
                minioProperties.getBucketName());
    }

}
