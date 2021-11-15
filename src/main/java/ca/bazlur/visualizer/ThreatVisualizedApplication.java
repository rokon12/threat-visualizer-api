package ca.bazlur.visualizer;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.env.Environment;

import java.util.Arrays;

@Slf4j
@SpringBootApplication
@AllArgsConstructor
public class ThreatVisualizedApplication implements CommandLineRunner {


    public static void main(String[] args) {
        SpringApplication.run(ThreatVisualizedApplication.class, args);
    }

    private final Environment environment;

    @Override
    public void run(final String... args) throws Exception {
        log.info("Application started with profile:{}", Arrays.toString(environment.getActiveProfiles()));
    }
}
