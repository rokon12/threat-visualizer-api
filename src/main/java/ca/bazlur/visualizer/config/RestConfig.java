package ca.bazlur.visualizer.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class RestConfig {
    @Bean
    public RestTemplate abuseIpDbRestTemplate() {

        return new RestTemplate();
    }
}
