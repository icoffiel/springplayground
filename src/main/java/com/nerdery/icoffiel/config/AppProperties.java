package com.nerdery.icoffiel.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration that reads in properties prefix with app.
 */
@Configuration
@ConfigurationProperties(prefix = "app", ignoreUnknownFields = false)
@Data
public class AppProperties {
    private final Security security = new Security();

    @Data
    public static class Security {
        private final Authentication authentication = new Authentication();

        @Data
        public static class Authentication {
            private final Jwt jwt = new Jwt();

            @Data
            public static class Jwt {
                private String secret;
                private String header;
                private String refreshHeader;
                private String bearerHeader;
                private Integer expiration; // In Seconds
                private Integer refreshExpiration; // In Seconds
            }
        }
    }
}
