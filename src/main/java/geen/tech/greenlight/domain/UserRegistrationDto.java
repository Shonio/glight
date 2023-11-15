package geen.tech.greenlight.domain;

import lombok.Data;

public @Data class UserRegistrationDto {
    private String accountNumber;
    private String email;
    private String location;
    private String name;
    private String password;
    private String phone;
    private Boolean news = false;
}
