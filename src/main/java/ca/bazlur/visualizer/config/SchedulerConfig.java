package ca.bazlur.visualizer.config;

import ca.bazlur.visualizer.feed.service.AbuseConfidenceScoreFeedService;
import ca.bazlur.visualizer.job.AbuseScoreConfidenceFetchingJob;
import ca.bazlur.visualizer.repo.AbuseConfidenceScoreRepository;
import lombok.AllArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

@Configuration
@EnableScheduling
@AllArgsConstructor
public class SchedulerConfig {
    private final AbuseConfidenceScoreFeedService confidenceScoreService;
    private final AbuseConfidenceScoreRepository abuseConfidenceScoreRepository;

    @Bean
    @ConditionalOnProperty(value = "abuse.confidence.score.fetch.jobs.enabled", havingValue = "true")
    public AbuseScoreConfidenceFetchingJob job() {

        return new AbuseScoreConfidenceFetchingJob(confidenceScoreService,
            abuseConfidenceScoreRepository);
    }
}
