package org.control_parental.profesor.infrastructure;

import org.control_parental.profesor.domain.Padre;
import org.control_parental.usuario.infrastructure.UsuarioRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PadreRepository extends UsuarioRepository<Padre> {
}
