package ca.bazlur.visualizer.domain.dto;

import lombok.Builder;
import lombok.Data;

import java.time.OffsetDateTime;

@Data
@Builder
public class Properties {
    private String country;
    private double score;
    private String city;
    private String ip;
    private OffsetDateTime lastReportedAt;
}
