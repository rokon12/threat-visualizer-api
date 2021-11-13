package ca.bazlur.visualizer.job;


import ca.bazlur.visualizer.domain.AbuseConfidenceScore;
import ca.bazlur.visualizer.feed.dto.AbuseConfidenceScoreDTO;
import ca.bazlur.visualizer.feed.service.AbuseConfidenceScoreFeedService;
import ca.bazlur.visualizer.feed.service.RawDBDemoGeoIPLocationServiceImpl;
import ca.bazlur.visualizer.repo.AbuseConfidenceScoreRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;

@Slf4j
@AllArgsConstructor
public class AbuseScoreConfidenceFetchingJob {
    private final AbuseConfidenceScoreFeedService confidenceScoreService;
    private final AbuseConfidenceScoreRepository abuseConfidenceScoreRepository;
    private final RawDBDemoGeoIPLocationServiceImpl rawDBDemoGeoIPLocationService;

    @Scheduled(fixedRate = 100000)
    public void fetchAbuseConfidenceScore() {
        log.info("Starting to fetch black listed ip address");
        var blackListedIps = confidenceScoreService.getBlackListedIps();
        log.info("Total black listed ip fetched: {}", blackListedIps.getData().size());

        var abuseConfidenceScores = blackListedIps.getData()
                                                  .parallelStream()
                                                  .map(this::toAbuseConfidenceScore)
                                                  .toList();

        abuseConfidenceScoreRepository.saveAllAndFlush(abuseConfidenceScores);
        log.info("Finished fetching black listed ip");
    }

    private AbuseConfidenceScore toAbuseConfidenceScore(final AbuseConfidenceScoreDTO dto) {
        var geoIpLocation = rawDBDemoGeoIPLocationService.getLocation(dto.getIpAddress());
        var abuseConfidenceScore = AbuseConfidenceScore.builder()
                                                       .ipAddress(dto.getIpAddress())
                                                       .score(dto.getScore())
                                                       .countryCode(dto.getCountryCode())
                                                       .lastReportedAt(dto.getLastReportedAt())
                                                       .build();
        if (geoIpLocation.isPresent()) {
            var geoIPLocation = geoIpLocation.get();
            abuseConfidenceScore.setCity(geoIPLocation.getCity());
            abuseConfidenceScore.setCountry(geoIPLocation.getCountry());
            abuseConfidenceScore.setLongitude(geoIPLocation.getLongitude());
            abuseConfidenceScore.setLatitude(geoIPLocation.getLatitude());
        }

        return abuseConfidenceScore;
    }
}
