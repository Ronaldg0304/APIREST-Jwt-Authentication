package com.demo.autenticacion.autenticacionJWT.services;

import com.demo.autenticacion.autenticacionJWT.dtos.AuthenticationRequest;
import com.demo.autenticacion.autenticacionJWT.dtos.AuthenticationResponse;
import com.demo.autenticacion.autenticacionJWT.dtos.PasswordChangeRequest;
import com.demo.autenticacion.autenticacionJWT.dtos.RegisterRequest;
import com.demo.autenticacion.autenticacionJWT.models.Role;
import com.demo.autenticacion.autenticacionJWT.models.Usuario;
import com.demo.autenticacion.autenticacionJWT.repositories.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;

@Service
public class AuthenticationService {
    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthenticationService(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder, JwtService jwtService, AuthenticationManager authenticationManager) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
    }


    public AuthenticationResponse register(RegisterRequest registerRequest) {
        var usuario = Usuario.builder()
                .firstName(registerRequest.getFirstName())
                .secondName(registerRequest.getSecondName())
                .firstLastName(registerRequest.getFirstLastName())
                .secondLastName(registerRequest.getSecondLastName())
                .email(registerRequest.getEmail())
                .username(registerRequest.getUsername())
                .password(passwordEncoder.encode(registerRequest.getPassword()))
                .role(Role.valueOf(registerRequest.getRole()))
                .build();
        usuarioRepository.save(usuario);
        var jwtToken = jwtService.generateToken(usuario);
        var refreshToken = jwtService.generateRefresh(new HashMap<>(), usuario);
        return AuthenticationResponse.builder()
                .authenticationToken(jwtToken)
                .refreshToken(refreshToken)
                .build();
    }

    public AuthenticationResponse authenticate(AuthenticationRequest authenticationRequest) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(authenticationRequest.getUsername(), authenticationRequest.getPassword())
        );
        var user = usuarioRepository.findByUsername(authenticationRequest.getUsername()).orElseThrow(() -> new IllegalArgumentException("Invalid username or password"));
        var jwtToken = jwtService.generateToken(user);
        var refreshToken = jwtService.generateRefresh(new HashMap<>(), user);
        return AuthenticationResponse.builder()
                .authenticationToken(jwtToken)
                .refreshToken(refreshToken)
                .build();
    }

    public AuthenticationResponse refreshToken(String refreshToken) {

        var user = usuarioRepository.findByUsername(jwtService.getUsernameFromToken(refreshToken)).orElseThrow(() -> new IllegalArgumentException("Invalid refresh token"));
        var jwtToken = jwtService.generateToken(user);
        var newRefreshToken = jwtService.generateRefresh(new HashMap<>(), user);
        return AuthenticationResponse.builder()
                .authenticationToken(jwtToken)
                .refreshToken(newRefreshToken)
                .build();
    }

    public Boolean validateToken(String token) {
        return jwtService.validateToken(token);
    }


}
