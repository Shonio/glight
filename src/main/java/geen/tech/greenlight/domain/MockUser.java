package geen.tech.greenlight.domain;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public @Data class MockUser {
    private Long id = 1L;
    private String username = "user";
    private String password = "password";
    private String firstName = "John";
    private String lastName = "Doe";
    private String email = "ahram1992@gmail.com";
    private String role = "admin";
}
