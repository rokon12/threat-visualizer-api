package ca.bazlur.visualizer.web;

import ca.bazlur.visualizer.domain.AbuseConfidenceScore;
import ca.bazlur.visualizer.service.AbuseConfidenceScoreService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping(value = "/api/v1/")
@AllArgsConstructor
@Tag(name = "Abuse Confidence Score")
public class AbuseConfidenceScoreResource {
    private final AbuseConfidenceScoreService abuseConfidenceScoreService;

    @RequestMapping(value = "/abuse-confidence-score", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public List<AbuseConfidenceScore> getScore(@RequestParam(required = false, defaultValue = "100") Long limit) {
        var data = abuseConfidenceScoreService.findAll();
        log.info("Total data found: {}", data.size());
        return data.stream().limit(limit).toList();
    }
}
