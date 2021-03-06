package ca.bazlur.visualizer.domain;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.Id;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@ToString
@Getter
@Setter
@EntityListeners(AuditingEntityListener.class)
public class AbuseConfidenceScore {
    @Id
    private String ipAddress;
    private String countryCode;
    private Integer score;
    private String country;
    private String city;
    private double longitude;
    private double latitude;

    @Column(name = "lastReportedAt", columnDefinition = "TIMESTAMP WITH TIME ZONE")
    private OffsetDateTime lastReportedAt;

    @CreatedDate
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime modifiedAt;

    @CreatedBy
    private String createdBy;

    @LastModifiedBy
    private String lastModifiedBy;
}
