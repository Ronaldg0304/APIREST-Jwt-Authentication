package com.demo.autenticacion.autenticacionJWT.controllers;

import com.demo.autenticacion.autenticacionJWT.apiResponse.ApiResponse;
import com.demo.autenticacion.autenticacionJWT.dtos.*;
import com.demo.autenticacion.autenticacionJWT.helpers.ApiResponseBuilder;
import com.demo.autenticacion.autenticacionJWT.services.AuthenticationService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthenticationController {

    private final AuthenticationService authenticationService;


    public AuthenticationController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<?>> register(@Valid @RequestBody RegisterRequest registerRequest) {
            AuthenticationResponse res = authenticationService.register(registerRequest);
            return ResponseEntity.ok(ApiResponseBuilder.success(res, HttpStatus.CREATED));
    }

    @PostMapping("/authenticate")
    public ResponseEntity<ApiResponse<?>> authenticate(@RequestBody AuthenticationRequest authenticationRequest) {
            AuthenticationResponse res = authenticationService.authenticate(authenticationRequest);
            return ResponseEntity.ok(ApiResponseBuilder.success(res, HttpStatus.OK));
    }

    @PostMapping("/refresh")
    public ResponseEntity<AuthenticationResponse> refresh(@RequestParam("token") String refreshToken) {
        try {
            AuthenticationResponse res = authenticationService.refreshToken(refreshToken);
            return ResponseEntity.ok(res);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }

    }

    @GetMapping("/validateToken")
    public ResponseEntity<Boolean> validateToken(@RequestParam("token") String token) {
        try {
            Boolean res = authenticationService.validateToken(token);
            return ResponseEntity.ok(res);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }



}