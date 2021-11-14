package ca.bazlur.visualizer.domain.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Feature {
    @Builder.Default
    private Feature.Type type = Type.Feature;
    public Geometry geometry;
    public Properties properties;

    public enum Type {
        Feature;
    }
}
