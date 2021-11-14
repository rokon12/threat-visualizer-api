package ca.bazlur.visualizer.domain.dto;

import lombok.Data;

import java.util.List;


@Data
public class AbuseConfidenceScoreData {
    private MetaDTO meta;
    private List<AbuseConfidenceScoreDTO> data;
}
