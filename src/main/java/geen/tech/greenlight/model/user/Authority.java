package geen.tech.greenlight.model.user;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import jakarta.persistence.*;

@Entity
@Table(name = "authority")
public @Data class Authority implements GrantedAuthority {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @JsonIgnore
    Long id;

    @Enumerated(EnumType.STRING)
    @JsonIgnore
    UserRoleName name;

    @Override
    public String getAuthority() {
        return name.name();
    }
}
