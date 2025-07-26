package by.egorivanov.cloudstorage.config.properties;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "minio")
@Getter
@Setter
public class MinioProperties {
    @NotBlank
    private String url;
    @NotBlank
    private String accessKey;
    @NotBlank
    private String secretKey;
    @NotBlank
    private String bucket;
}
