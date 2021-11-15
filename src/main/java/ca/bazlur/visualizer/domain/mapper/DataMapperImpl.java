package ca.bazlur.visualizer.domain.mapper;

import ca.bazlur.visualizer.domain.AbuseConfidenceScore;
import ca.bazlur.visualizer.domain.User;
import ca.bazlur.visualizer.domain.dto.AbuseConfidenceScoreDTO;
import ca.bazlur.visualizer.domain.dto.CreateUserRequest;
import ca.bazlur.visualizer.domain.dto.UserView;
import org.springframework.stereotype.Component;

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
}
