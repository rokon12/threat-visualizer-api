package ca.bazlur.visualizer.domain.mapper;

import ca.bazlur.visualizer.domain.AbuseConfidenceScore;
import ca.bazlur.visualizer.domain.User;
import ca.bazlur.visualizer.domain.dto.AbuseConfidenceScoreDTO;
import ca.bazlur.visualizer.domain.dto.CreateUserRequest;
import ca.bazlur.visualizer.domain.dto.GeoJsonView;
import ca.bazlur.visualizer.domain.dto.UserView;

import java.util.stream.Stream;


public interface DataMapper {
    AbuseConfidenceScore toAbuseConfidenceScore(AbuseConfidenceScoreDTO abuseConfidenceScoreDTO);

    User toUser(CreateUserRequest createUserRequest);

    UserView toUserView(User user);

    GeoJsonView toGeJsonView(Stream<AbuseConfidenceScore> stream);
}
