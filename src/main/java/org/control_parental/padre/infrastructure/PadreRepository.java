package org.control_parental.padre.infrastructure;

import org.control_parental.padre.domain.Padre;
import org.control_parental.usuario.infrastructure.UsuarioRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PadreRepository extends UsuarioRepository<Padre> {
}
