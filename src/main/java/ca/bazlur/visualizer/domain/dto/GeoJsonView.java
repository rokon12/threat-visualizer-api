package ca.bazlur.visualizer.domain.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

/**
 * Sample GeoJson
 * Reference:  https://geojson.io/
 * {
 * "type": "FeatureCollection",
 * "features": [
 * {
 * "type": "Feature",
 * "properties": {},
 * "geometry": {
 * "type": "Point",
 * "coordinates": [
 * 96.6796875,
 * 62.062733258846514
 * ]
 * }
 * },
 * {
 * "type": "Feature",
 * "properties": {},
 * "geometry": {
 * "type": "Point",
 * "coordinates": [
 * 103.88671875,
 * 47.39834920035926
 * ]
 * }
 * }
 * ]
 * }
 */
@Data
@Builder
public class GeoJsonView {
    @Builder.Default
    private String type = "FeatureCollection";
    private List<Feature> features;
}
