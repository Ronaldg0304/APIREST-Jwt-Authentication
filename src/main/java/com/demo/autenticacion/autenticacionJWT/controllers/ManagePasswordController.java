package com.demo.autenticacion.autenticacionJWT.controllers;

import com.demo.autenticacion.autenticacionJWT.dtos.PasswordChangeRequest;
import com.demo.autenticacion.autenticacionJWT.dtos.PasswordChangeResponse;
import com.demo.autenticacion.autenticacionJWT.dtos.PasswordRecoveryRequest;
import com.demo.autenticacion.autenticacionJWT.dtos.PasswordResetRequest;
import com.demo.autenticacion.autenticacionJWT.services.AuthenticationService;
import com.demo.autenticacion.autenticacionJWT.services.ManagePasswordService;
import jakarta.mail.MessagingException;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/auth")
public class ManagePasswordController {

    private final ManagePasswordService managePasswordService;

    public ManagePasswordController(ManagePasswordService managePasswordService) {
        this.managePasswordService = managePasswordService;
    }

    @PostMapping("/change-password")
    public ResponseEntity<PasswordChangeResponse> changePassword(@Valid @RequestBody PasswordChangeRequest request,
                                                                 @RequestHeader("Authorization") String authHeader){

        String jwt = authHeader.substring(7);
        managePasswordService.changePassword(jwt, request);

        return ResponseEntity.ok(new PasswordChangeResponse(
                true,
                "Password changed successfully",
                Instant.now()
        ));
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<?> forgotPassword(@Valid @RequestBody PasswordRecoveryRequest request) throws MessagingException {
        managePasswordService.initiatePasswordRecovery(request.getEmail());
        return ResponseEntity.ok(
                Map.of("message", "If the email exists, a recovery link has been sent")
        );
    }

    @PostMapping("/reset-password")
    public ResponseEntity<?> resetPassword(@Valid @RequestBody PasswordResetRequest request, @RequestParam String token) {
        managePasswordService.resetPassword(token, request.getNewPassword());
        return ResponseEntity.ok(
                Map.of("message", "Password reset successfully")
        );
    }
}
