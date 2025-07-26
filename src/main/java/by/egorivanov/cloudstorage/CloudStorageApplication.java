package by.egorivanov.cloudstorage;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;

@SpringBootApplication
@ConfigurationPropertiesScan
@EnableRedisHttpSession
public class CloudStorageApplication {

    public static void main(String[] args) {
        SpringApplication.run(CloudStorageApplication.class, args);
    }

}
