package ca.bazlur.visualizer.domain.dto;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class Geometry {
    private List<Double> coordinates;
    private GeometryType type;

    public enum GeometryType {
        POINT("Point"),
        LINESTRING("LineString"),
        POLYGON("Polygon"),
        MULTI_POINT("MultiPoint"),
        MULTI_LINE_STRING("MultiLineString"),
        MULTI_POLYGON("MultiPolygon");

        private final String name;

        GeometryType(final String name) {
            this.name = name;
        }

        @JsonValue
        public String getName() {
            return name;
        }

        @Override
        public String toString() {
            return name;
        }
    }
}
