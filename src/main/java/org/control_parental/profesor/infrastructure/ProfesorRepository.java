package org.control_parental.profesor.infrastructure;

import jakarta.transaction.Transactional;
import org.control_parental.profesor.domain.Profesor;
import org.control_parental.usuario.infrastructure.UsuarioRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Transactional
@Repository
public interface ProfesorRepository extends UsuarioRepository<Profesor> {
}
