package ca.bazlur.visualizer.domain.dto;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Feature {
    @Builder.Default

    private Feature.Type type = Type.FEATURE;
    private Geometry geometry;
    private Properties properties;

    public enum Type {
        FEATURE("Feature");

        private final String name;

        Type(final String name) {
            this.name = name;
        }

        @JsonValue
        public String getName() {
            return name;
        }
    }
}
