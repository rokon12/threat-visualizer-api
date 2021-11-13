package ca.bazlur.visualizer.domain.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserView {
    private Integer id;

    private String username;
    private String fullName;
}
