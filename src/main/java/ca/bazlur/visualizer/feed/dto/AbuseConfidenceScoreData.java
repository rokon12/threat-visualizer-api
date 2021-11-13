package ca.bazlur.visualizer.feed.dto;

import lombok.Data;

import java.util.List;


@Data
public class AbuseConfidenceScoreData {
    private MetaDTO meta;
    private List<AbuseConfidenceScoreDTO> data;
}
