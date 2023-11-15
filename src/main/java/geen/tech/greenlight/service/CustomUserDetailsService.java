package geen.tech.greenlight.service;

import geen.tech.greenlight.model.user.User;
import geen.tech.greenlight.repository.UserRepository;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements org.springframework.security.core.userdetails.UserDetailsService {
	private final UserRepository repository;

	public CustomUserDetailsService(UserRepository repository) {
		this.repository = repository;
	}

	@Override
	public User loadUserByUsername(String username) throws UsernameNotFoundException {
		System.out.println("UserDetailsService.loadUserByUsername(" + username + ")");
		User user = repository.findByUsername(username);
		System.out.println("UserDetailsService.loadUserByUsername(" + username + ") = " + user);
		if (user == null) {
			throw new UsernameNotFoundException(String.format("No user found with username '%s'.", username));
		} else {
			return user;
		}
	}
}
