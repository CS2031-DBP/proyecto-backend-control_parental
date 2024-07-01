package org.control_parental.profesor.infrastructure;

import jakarta.transaction.Transactional;
import org.control_parental.profesor.domain.Profesor;
import org.control_parental.salon.domain.Salon;
import org.control_parental.usuario.infrastructure.UsuarioRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Transactional
@Repository
public interface ProfesorRepository extends UsuarioRepository<Profesor> {

    List<Profesor> findAllBySalones(Salon salon);

    Page<Profesor> findAll(Pageable pageable);


}
