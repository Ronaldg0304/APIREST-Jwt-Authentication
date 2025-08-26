package com.demo.autenticacion.autenticacionJWT.dtos;

import com.demo.autenticacion.autenticacionJWT.models.Role;
import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequest {


    @NotBlank(message = "First name is required")
    @Size(min = 2, max = 50, message = "First name must be between 2 and 50 characters")
    @Pattern(regexp = "^[a-zA-Z\\sáéíóúÁÉÍÓÚñÑ'-]+$",
            message = "First name can only contain letters, spaces, and valid special characters")
    private String firstName;

    @Size(max = 50, message = "Second name cannot exceed 50 characters")
    @Pattern(regexp = "^[a-zA-Z\\sáéíóúÁÉÍÓÚñÑ'-]*$",
            message = "Second name can only contain letters, spaces, and valid special characters")
    private String secondName;

    @NotBlank(message = "First last name is required")
    @Size(min = 2, max = 50, message = "First last name must be between 2 and 50 characters")
    @Pattern(regexp = "^[a-zA-Z\\sáéíóúÁÉÍÓÚñÑ'-]+$",
            message = "First last name can only contain letters, spaces, and valid special characters")
    private String firstLastName;

    @Size(max = 50, message = "Second last name cannot exceed 50 characters")
    @Pattern(regexp = "^[a-zA-Z\\sáéíóúÁÉÍÓÚñÑ'-]*$",
            message = "Second last name can only contain letters, spaces, and valid special characters")
    private String secondLastName;

    @NotBlank(message = "Username is required")
    @Size(min = 3, max = 20, message = "Username must be between 3 and 20 characters")
    @Pattern(regexp = "^[a-zA-Z0-9_]+$",
            message = "Username can only contain letters, numbers, and underscores")
    private String username;

    @NotBlank(message = "Email is required")
    @Email(message = "Must be a valid email address")
    @Size(max = 100, message = "Email cannot exceed 100 characters")
    private String email;

    @NotBlank(message = "Password is required")
    @Size(min = 8, max = 100, message = "Password must be between 8 and 100 characters")
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]+$",
            message = "Password must contain at least one uppercase letter, one lowercase letter, one number, and one special character")
    private String password;

    @Enumerated(EnumType.STRING)
    private String role;
}