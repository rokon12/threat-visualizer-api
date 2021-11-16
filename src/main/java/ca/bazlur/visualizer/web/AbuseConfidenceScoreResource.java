package ca.bazlur.visualizer.web;

import ca.bazlur.visualizer.domain.dto.GeoJsonView;
import ca.bazlur.visualizer.service.AbuseConfidenceScoreService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping(value = "/api/v1/")
@AllArgsConstructor
@PreAuthorize(value = "hasRole('ROLE_USER')")
@Tag(name = "Abuse Confidence Score")
public class AbuseConfidenceScoreResource {
    private final AbuseConfidenceScoreService abuseConfidenceScoreService;

    @GetMapping(value = "/abuse-confidence-score", produces = MediaType.APPLICATION_JSON_VALUE)
    public GeoJsonView getScore(@RequestParam(value = "page", required = false, defaultValue = "0") Integer page,
                                @RequestParam(value = "size", required = false, defaultValue = "50") Integer size,
                                @RequestParam(value = "sortBy", required = false, defaultValue = "country") String sortBy) {
        var data = abuseConfidenceScoreService.getAbuseConfidenceScore(page, size, sortBy);
        log.info("Total data found: {}", data.getFeatures().size());

        return data;
    }
}
