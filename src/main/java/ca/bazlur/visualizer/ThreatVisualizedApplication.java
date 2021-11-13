package ca.bazlur.visualizer;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@Slf4j
@SpringBootApplication
public class ThreatVisualizedApplication implements CommandLineRunner {

    public static void main(String[] args) {
        SpringApplication.run(ThreatVisualizedApplication.class, args);
    }

    @Override
    public void run(final String... args) throws Exception {
    }
}
