package ca.bazlur.visualizer.domain;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.Hibernate;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.time.OffsetDateTime;
import java.util.Objects;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@ToString
@Getter
@Setter
public class AbuseConfidenceScore {
    @Id
    private String ipAddress;
    private String countryCode;
    private Integer score;
    private String country;
    private String city;
    private double longitude;
    private double latitude;
    private OffsetDateTime lastReportedAt;

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o))
            return false;
        final var that = (AbuseConfidenceScore) o;
        return ipAddress != null && Objects.equals(ipAddress, that.ipAddress);
    }

    @Override
    public int hashCode() {
        return Objects.hash(getIpAddress());
    }
}
