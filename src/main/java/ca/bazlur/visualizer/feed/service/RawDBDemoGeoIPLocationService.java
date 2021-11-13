package ca.bazlur.visualizer.feed.service;

import ca.bazlur.visualizer.feed.dto.GeoIPLocationDTO;

import java.util.Optional;

public interface RawDBDemoGeoIPLocationService {
    Optional<GeoIPLocationDTO> getLocation(String ip);
}