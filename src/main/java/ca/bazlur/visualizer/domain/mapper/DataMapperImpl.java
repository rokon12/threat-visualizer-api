package ca.bazlur.visualizer.domain.mapper;

import ca.bazlur.visualizer.domain.AbuseConfidenceScore;
import ca.bazlur.visualizer.domain.User;
import ca.bazlur.visualizer.domain.dto.AbuseConfidenceScoreDTO;
import ca.bazlur.visualizer.domain.dto.CreateUserRequest;
import ca.bazlur.visualizer.domain.dto.Feature;
import ca.bazlur.visualizer.domain.dto.GeoJsonView;
import ca.bazlur.visualizer.domain.dto.Geometry;
import ca.bazlur.visualizer.domain.dto.Properties;
import ca.bazlur.visualizer.domain.dto.UserView;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Stream;

@Component
public class DataMapperImpl implements DataMapper {
    public AbuseConfidenceScore toAbuseConfidenceScore(AbuseConfidenceScoreDTO abuseConfidenceScoreDTO) {
        if (abuseConfidenceScoreDTO == null) {
            return null;
        } else {
            return AbuseConfidenceScore
                .builder()
                .score(abuseConfidenceScoreDTO.getScore())
                .ipAddress(abuseConfidenceScoreDTO.getIpAddress())
                .lastReportedAt(abuseConfidenceScoreDTO.getLastReportedAt())
                .countryCode(abuseConfidenceScoreDTO.getCountryCode())
                .build();
        }
    }

    public User toUser(CreateUserRequest createUserRequest) {
        if (createUserRequest == null) {
            return null;
        } else {
            return User
                .builder()
                .username(createUserRequest.getUsername())
                .fullName(createUserRequest.getFullName())
                .build();
        }
    }

    public UserView toUserView(User user) {
        if (user == null) {
            return null;
        } else {
            return UserView
                .builder()
                .id(user.getId())
                .fullName(user.getFullName())
                .username(user.getUsername())
                .build();
        }
    }

    public GeoJsonView toGeJsonView(Stream<AbuseConfidenceScore> stream) {
        return GeoJsonView.builder()
                          .features(stream
                              .map(this::toFeature)
                              .toList())
                          .build();
    }

    private Feature toFeature(final AbuseConfidenceScore item) {

        return Feature.builder()
                      .geometry(Geometry.builder()
                                        .type(Geometry.GeometryType.POINT)
                                        .coordinates(List.of(item.getLongitude(), item.getLatitude()))
                                        .build())
                      .properties(Properties.builder()
                                            .score(item.getScore())
                                            .country(item.getCountry())
                                            .ip(item.getIpAddress())
                                            .city(item.getCity())
                                            .lastReportedAt(item.getLastReportedAt())
                                            .build())
                      .build();
    }
}
