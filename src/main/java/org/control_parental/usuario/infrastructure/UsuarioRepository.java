package org.control_parental.usuario.infrastructure;

import org.control_parental.usuario.domain.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UsuarioRepository<T extends Usuario> extends JpaRepository<T, Long> {

}
