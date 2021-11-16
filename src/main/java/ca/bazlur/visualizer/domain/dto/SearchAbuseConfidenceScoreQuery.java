package ca.bazlur.visualizer.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SearchAbuseConfidenceScoreQuery {
    private String ipAddress;
    private String countryCode;
    private String country;
    private String city;
}
