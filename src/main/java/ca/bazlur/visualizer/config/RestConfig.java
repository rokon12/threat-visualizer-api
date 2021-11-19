package ca.bazlur.visualizer.config;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;

@Configuration
public class RestConfig {
    @Bean
    public RestTemplate abuseIpDbRestTemplate(RestTemplateBuilder builder) {
        builder.setReadTimeout(Duration.ofSeconds(30));
        builder.setConnectTimeout(Duration.ofSeconds(30));
        return builder.build();
    }
}
