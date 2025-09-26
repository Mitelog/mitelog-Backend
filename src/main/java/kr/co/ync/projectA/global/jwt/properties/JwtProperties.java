package kr.co.ync.projectA.global.jwt.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@ConfigurationProperties(prefix = "application.jwt")
@Configuration
@Getter
@Setter
public class JwtProperties {
    private String secretKey;
    private long expiration;
    private long refreshExpiration;

}
