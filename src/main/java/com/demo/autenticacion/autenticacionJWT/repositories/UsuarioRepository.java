package com.demo.autenticacion.autenticacionJWT.repositories;

import com.demo.autenticacion.autenticacionJWT.models.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Repository;

import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    @Query(value = "SELECT u FROM Usuario u WHERE u.username = :username")
    Optional<Usuario> findByUsername(String username);

    @Query(value = "SELECT u FROM Usuario u WHERE u.email = :email")
    Optional<Usuario> findByEmail(String email);
}
