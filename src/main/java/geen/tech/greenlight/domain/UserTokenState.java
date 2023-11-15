package geen.tech.greenlight.domain;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
public @Data class UserTokenState {
    private String accessToken;
    private Long expiresIn;
}