package com.demo.autenticacion.autenticacionJWT.repositories;

import com.demo.autenticacion.autenticacionJWT.models.PasswordResetToken;
import com.demo.autenticacion.autenticacionJWT.models.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface PasswordResetTokenRepository extends JpaRepository<PasswordResetToken, Long> {
    @Query(value = "SELECT t FROM PasswordResetToken t WHERE t.token = :token")
    Optional<PasswordResetToken> findByToken(String token);
}
