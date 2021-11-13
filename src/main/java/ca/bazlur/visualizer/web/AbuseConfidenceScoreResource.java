package ca.bazlur.visualizer.web;

import ca.bazlur.visualizer.domain.AbuseConfidenceScore;
import ca.bazlur.visualizer.service.AbuseConfidenceScoreService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequestMapping(value = "/api/v1/", produces = MediaType.APPLICATION_JSON_VALUE,
    consumes = MediaType.APPLICATION_JSON_VALUE)
@AllArgsConstructor
@Tag(name = "Abuse Confidence Score")
public class AbuseConfidenceScoreResource {
    private final AbuseConfidenceScoreService abuseConfidenceScoreService;

    @GetMapping("/abuse-confidence-score")
    public List<AbuseConfidenceScore> getScore() {
        var data = abuseConfidenceScoreService.findAll();
        log.info("Total data found: {}", data.size());
        return data;
    }
}
