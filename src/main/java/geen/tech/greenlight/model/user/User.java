package geen.tech.greenlight.model.user;

import com.fasterxml.jackson.annotation.JsonIgnore;
import geen.tech.greenlight.domain.UserRegistrationDto;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.Hibernate;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.sql.Date;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "users")
@NoArgsConstructor
@RequiredArgsConstructor
public @Getter
@Setter
@ToString class User implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false, unique = true)
    @NonNull private String username;
    @JsonIgnore @NonNull private String password;

    // ----------------- User Details -----------------
    private String phone;
    private String name;
    private String email;
    private String location;
    private String personalAccount;

    // ----------------- User Role -----------------
    private String role;

    // ----------------- User Tech info -----------------
    private Date createdDate;

    private Date updatedDate;
    @JsonIgnore private boolean enabled;

    @JsonIgnore
    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinTable(name = "user_authority", joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"), inverseJoinColumns = @JoinColumn(name = "authority_id", referencedColumnName = "id"))
    private List<Authority> authorities;
    // ---------------------------------------------------

    public User(UserRegistrationDto userDetails) {
        this.username = userDetails.getPhone();
        // ----------------- User Details -----------------

        this.phone = userDetails.getPhone();
        this.name = userDetails.getName();
        this.email = userDetails.getEmail();
        this.location = userDetails.getLocation();
        this.personalAccount = userDetails.getAccountNumber();
        // ------------------------------------------------

        // ----------------- User Role -----------------
        this.role = "user";
        // ---------------------------------------------

        // ----------------- User Tech info -----------------
        this.createdDate = new Date(System.currentTimeMillis());
        this.updatedDate = new Date(System.currentTimeMillis());
        this.enabled = true;
        // ---------------------------------------------------
    }

    @JsonIgnore
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.authorities;
    }

    @JsonIgnore
    @Override
    public boolean isEnabled() {
        return enabled;
    }

    @JsonIgnore
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @JsonIgnore
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @JsonIgnore
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        User user = (User) o;
        return id != null && Objects.equals(id, user.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}

