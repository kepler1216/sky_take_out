package com.sky.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "minio.config")
@Data
public class MinioProperties {
    private String url;
    private String accessKey;
    private String secretKey;
    private String secure;
    private String bucketName;
}
