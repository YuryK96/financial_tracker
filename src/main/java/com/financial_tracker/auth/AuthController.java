package com.financial_tracker.auth;

import com.financial_tracker.auth.dto.request.JwtRequest;
import com.financial_tracker.auth.dto.request.RegistrationRequest;
import com.financial_tracker.auth.dto.response.JWTResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("auth")
public class AuthController {
    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("login")
    public ResponseEntity<JWTResponse> login(@RequestBody JwtRequest jwtRequest) {
        return ResponseEntity.status(HttpStatus.OK).body(authService.login(jwtRequest));
    }

    @PostMapping("registration")
    public ResponseEntity<JWTResponse> registration(@RequestBody RegistrationRequest registrationRequest) {
        return ResponseEntity.status(HttpStatus.OK).body(authService.registration(registrationRequest));
    }

}
