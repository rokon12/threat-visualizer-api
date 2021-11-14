package ca.bazlur.visualizer.feed.service;

import ca.bazlur.visualizer.domain.dto.GeoIPLocationDTO;
import com.maxmind.geoip2.DatabaseReader;
import com.maxmind.geoip2.exception.GeoIp2Exception;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.net.InetAddress;
import java.util.Optional;

@Slf4j
@Service
public class RawDBDemoGeoIPLocationServiceImpl implements RawDBDemoGeoIPLocationService {
    private final DatabaseReader dbReader;

    public RawDBDemoGeoIPLocationServiceImpl() {
        try {
            var file = ResourceUtils.getFile("classpath:db/GeoLite2-City.mmdb");
            dbReader = new DatabaseReader.Builder(file).build();
        } catch (IOException e) {
            log.error("Failed to initialize the databaseReader for GeoLite2 geo location database", e);
            throw new UncheckedIOException(e);
        }
    }

    public Optional<GeoIPLocationDTO> getLocation(String ip) {
        try {
            var response = dbReader.city(InetAddress.getByName(ip));
            var country = response.getCountry().getName();
            var cityName = response.getCity().getName();
            var latitude =
                response.getLocation().getLatitude().toString();
            var longitude =
                response.getLocation().getLongitude().toString();
            var geoIPLocation = new GeoIPLocationDTO(ip, cityName, country,
                Double.parseDouble(latitude), Double.parseDouble(longitude));

            return Optional.of(geoIPLocation);
        } catch (IOException | GeoIp2Exception e) {
            log.error("Failed to fetch Ip to geo location from GeoLite2 db", e);
            return Optional.empty();
        }
    }
}
