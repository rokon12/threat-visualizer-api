package ca.bazlur.visualizer.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GeoIPLocationDTO {
    private String ipAddress;
    private String city;
    private String country;
    private Double latitude;
    private Double longitude;
}
