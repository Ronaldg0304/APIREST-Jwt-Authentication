package com.demo.autenticacion.autenticacionJWT.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PasswordChangeResponse {

    private boolean success;
    private String message;
    private Instant lastChange;
}
