package geen.tech.greenlight.controller;

import geen.tech.greenlight.domain.MockUser;
import geen.tech.greenlight.domain.RefreshTokenResponse;
import geen.tech.greenlight.domain.UserRegistrationDto;
import geen.tech.greenlight.model.user.User;
import geen.tech.greenlight.domain.UserTokenState;
import geen.tech.greenlight.security.TokenHelper;
import geen.tech.greenlight.security.auth.JwtAuthenticationRequest;
import geen.tech.greenlight.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping(value = "/api/auth", produces = MediaType.APPLICATION_JSON_VALUE)
public class AuthenticationController {
    private final TokenHelper tokenHelper;
    private final AuthenticationManager authenticationManager;
    private final UserService userService;

    public AuthenticationController(TokenHelper tokenHelper, AuthenticationManager authenticationManager, UserService userService) {
        this.tokenHelper = tokenHelper;
        this.authenticationManager = authenticationManager;
        this.userService = userService;
    }

    @PostMapping(value = "/sign-in")
    public ResponseEntity<?> createAuthenticationToken(
            @RequestBody JwtAuthenticationRequest authenticationRequest) throws AuthenticationException {
        try {
            final Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            authenticationRequest.getUsername(),
                            authenticationRequest.getPassword()));

            // Inject into security context
            SecurityContextHolder.getContext().setAuthentication(authentication);

            // token creation
            User user = (User) authentication.getPrincipal();
            String jws = tokenHelper.generateToken(user.getUsername());
            Long expiresIn = tokenHelper.getEXPIRES_IN();

            // Return the token
            return ResponseEntity.ok(new UserTokenState(jws, expiresIn));
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Authentication failed: " + e.getMessage());
            return ResponseEntity.badRequest().body("Authentication failed: " + e.getMessage());
        }
    }

    @GetMapping(value = "/access-token")
    public ResponseEntity<?> accessToken(
            HttpServletRequest request,
            Principal userPrincipal
    ) {
        System.out.println("=====================================");
        System.out.println("call accessToken [userPrincipal]: " + userPrincipal);

        String authToken = tokenHelper.getToken(request);
        System.out.println("call accessToken [authToken]: " + authToken);

        if (authToken != null && userPrincipal != null) {

            User user = userService.findByUsername(userPrincipal.getName());
            System.out.println("call accessToken [findByUsername]: " + user);
            String refreshedToken = tokenHelper.refreshToken(authToken);
            System.out.println("call accessToken [refreshedToken]: " + refreshedToken);
            System.out.println("=====================================");

//            return ResponseEntity.ok(new RefreshTokenResponse(
//                    refreshedToken, tokenHelper.getEXPIRES_IN(), user));
            return ResponseEntity.ok(new RefreshTokenResponse(
                    refreshedToken, tokenHelper.getEXPIRES_IN(), new MockUser()));
        } else {
            System.out.println("Authentication failed");
            System.out.println("=====================================");
            return ResponseEntity.badRequest().body("Authentication failed");
        }
    }

    @PostMapping("/sign-up")
    public ResponseEntity<?> registerUser(@Valid @RequestBody UserRegistrationDto userRegistration) {
        try {
            if (userService.findByUsername(userRegistration.getPhone()) != null) {
                return new ResponseEntity<>("Username already exists", HttpStatus.BAD_REQUEST);
            }

            userService.addUser(userRegistration);
            return new ResponseEntity<>("User registered successfully", HttpStatus.CREATED);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Authentication failed: " + e.getMessage());
            return ResponseEntity.badRequest().body("Something went wrong");
        }
    }
}
