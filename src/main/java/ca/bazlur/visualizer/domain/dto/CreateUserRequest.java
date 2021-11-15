package ca.bazlur.visualizer.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateUserRequest {
    @NotBlank
    @Email
    private String username;
    @NotBlank
    private String fullName;
    @NotBlank
    private String password;
    @NotBlank
    private String rePassword;
    private Set<String> authorities;
}
