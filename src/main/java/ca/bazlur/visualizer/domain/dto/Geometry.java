package ca.bazlur.visualizer.domain.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class Geometry {
    private List<Double> coordinates;
    private GeometryType type;

    public enum GeometryType {
        Point, LineString, Polygon, MultiPoint, MultiLineString, MultiPolygon;
    }
}
