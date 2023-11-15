package geen.tech.greenlight.domain;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
public @Data class RefreshTokenResponse {
    private String refreshedToken;
    private Long expiresIn;
    private MockUser user;
}
