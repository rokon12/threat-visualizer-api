package ca.bazlur.visualizer.web;

import ca.bazlur.visualizer.domain.AbuseConfidenceScore;
import ca.bazlur.visualizer.service.AbuseConfidenceScoreService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/v1/")
@AllArgsConstructor
public class AbuseConfidenceScoreResource {
    private final AbuseConfidenceScoreService abuseConfidenceScoreService;

    @GetMapping("/abuse-confidence-score")
    public List<AbuseConfidenceScore> getScore() {
        var data = abuseConfidenceScoreService.findAll();
        log.info("Total data found: {}", data.size());
        return data;
    }
}
