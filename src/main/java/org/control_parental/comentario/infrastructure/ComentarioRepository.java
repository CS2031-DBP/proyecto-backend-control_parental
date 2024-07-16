package org.control_parental.comentario.infrastructure;

import org.control_parental.comentario.domain.Comentario;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ComentarioRepository extends JpaRepository<Comentario, Long> {

    Page<Comentario> findAllByPublicacion_IdOrderByFechaDesc(Long id, Pageable pageable);
}
