package ca.bazlur.visualizer.job;


import ca.bazlur.visualizer.feed.service.AbuseConfidenceScoreFeedService;
import ca.bazlur.visualizer.repo.AbuseConfidenceScoreRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;

@Slf4j
@AllArgsConstructor
public class AbuseScoreConfidenceFetchingJob {
    private final AbuseConfidenceScoreFeedService confidenceScoreService;
    private final AbuseConfidenceScoreRepository abuseConfidenceScoreRepository;

    @Scheduled(fixedRate = 100000)
    public void fetchAbuseConfidenceScore() {
        log.info("Starting to fetch black listed ip address");
        var blackListedIps = confidenceScoreService.getBlackListedIps();
        log.info("Total black listed ip fetched: {}", blackListedIps.getData().size());

        var abuseConfidenceScores = blackListedIps.getData()
                                                  .parallelStream()
                                                  .map(confidenceScoreService::mapIPAddressToGeoLocation)
                                                  .toList();

        abuseConfidenceScoreRepository.saveAllAndFlush(abuseConfidenceScores);
        log.info("Finished fetching black listed ip");
    }
}
