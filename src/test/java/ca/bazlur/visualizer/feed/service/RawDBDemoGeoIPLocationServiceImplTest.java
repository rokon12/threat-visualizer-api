package ca.bazlur.visualizer.feed.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class RawDBDemoGeoIPLocationServiceImplTest {

    private RawDBDemoGeoIPLocationService rawDBDemoGeoIPLocationService;

    @BeforeEach
    void setUp() {
        rawDBDemoGeoIPLocationService = new RawDBDemoGeoIPLocationServiceImpl();
    }

    @Test
    void getLocation() {
        var location = rawDBDemoGeoIPLocationService.getLocation("174.95.183.30");
        System.out.println("location = " + location);
        assertTrue(location.isPresent());

        var geoIPLocationDTO = location.get();
        assertEquals("Canada", geoIPLocationDTO.getCountry());
        assertEquals("Toronto", geoIPLocationDTO.getCity());
        assertEquals(43.6752, geoIPLocationDTO.getLatitude());
        assertEquals(-79.3472, geoIPLocationDTO.getLongitude());
    }
}
