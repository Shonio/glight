package geen.tech.greenlight.service;

import geen.tech.greenlight.domain.UserRegistrationDto;
import geen.tech.greenlight.model.user.User;
import geen.tech.greenlight.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository repository, PasswordEncoder passwordEncoder) {
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
    }

    public void addUser(UserRegistrationDto userDetails) {
        User newUser = new User(userDetails);
        newUser.setPassword(passwordEncoder.encode(userDetails.getPassword()));
        repository.save(newUser);
    }

    public User findByUsername(String username) {
        return repository.findByUsername(username);
    }
}
