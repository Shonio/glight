package geen.tech.greenlight.security.auth;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
public @Data class JwtAuthenticationRequest {
    private String username;
    private String password;
}
