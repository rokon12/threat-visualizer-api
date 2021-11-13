package ca.bazlur.visualizer.feed.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AbuseConfidenceScoreDTO {
    private String ipAddress;
    private String countryCode;
    @JsonProperty("abuseConfidenceScore")
    private Integer score;
    private OffsetDateTime lastReportedAt;
}
