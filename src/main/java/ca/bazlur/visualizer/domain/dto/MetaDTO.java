package ca.bazlur.visualizer.domain.dto;

import lombok.Data;

import java.time.OffsetDateTime;

@Data
public class MetaDTO {
    private OffsetDateTime generatedAt;
}
