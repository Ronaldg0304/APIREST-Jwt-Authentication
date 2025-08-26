package com.demo.autenticacion.autenticacionJWT.services;

import com.demo.autenticacion.autenticacionJWT.dtos.PasswordChangeRequest;
import com.demo.autenticacion.autenticacionJWT.models.PasswordResetToken;
import com.demo.autenticacion.autenticacionJWT.models.Usuario;
import com.demo.autenticacion.autenticacionJWT.repositories.PasswordResetTokenRepository;
import com.demo.autenticacion.autenticacionJWT.repositories.UsuarioRepository;
import jakarta.mail.MessagingException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class ManagePasswordService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordResetTokenRepository passwordResetTokenRepository;
    private final EmailService emailService;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public ManagePasswordService(UsuarioRepository usuarioRepository, PasswordResetTokenRepository passwordResetTokenRepository, EmailService emailService, PasswordEncoder passwordEncoder, JwtService jwtService) {
        this.usuarioRepository = usuarioRepository;
        this.passwordResetTokenRepository = passwordResetTokenRepository;
        this.emailService = emailService;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }


    public void changePassword(String jwt, PasswordChangeRequest request){
        String username = jwtService.extractUserName(jwt);

        Usuario user = usuarioRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("User not found"));

        if (!passwordEncoder.matches(request.getCurrentPassword(), user.getPassword())) {
            throw new BadCredentialsException("Current password is incorrect");
        }

        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        usuarioRepository.save(user);
    }

    public void initiatePasswordRecovery(String email) throws MessagingException {
        Usuario user = usuarioRepository.findByEmail(email)
                .orElse(null); // Don't reveal if user exists

        if (user != null) {
            String token = UUID.randomUUID().toString();
            PasswordResetToken resetToken = PasswordResetToken.builder()
                    .token(token)
                    .user(user)
                    .expiryDateToken(LocalDateTime.now().plusHours(1000))
                    .build();

            passwordResetTokenRepository.save(resetToken);

            // Send email
            emailService.sendPasswordResetEmail(
                    user.getEmail(),
                    "Password Reset",
                    "https://api/v1/auth/reset-password?token=" + token
            );
        }
    }

    public void resetPassword(String token, String newPassword) {
        PasswordResetToken resetToken = passwordResetTokenRepository.findByToken(token)
                .orElseThrow(() -> new IllegalArgumentException("Invalid token"));

        if (resetToken.getExpiryDateToken().isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("Token expired");
        }

        Usuario user = resetToken.getUser();
        user.setPassword(passwordEncoder.encode(newPassword));
        usuarioRepository.save(user);

        // Invalidate token
        passwordResetTokenRepository.delete(resetToken);
    }
}
