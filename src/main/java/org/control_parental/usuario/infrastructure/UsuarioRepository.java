package org.control_parental.usuario.infrastructure;

import org.control_parental.usuario.domain.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UsuarioRepository<T extends Usuario> extends JpaRepository<T, Long> {

    Optional<T> findByEmail(String email);
}
