package org.control_parental.hijo.infrastructure;

import org.control_parental.hijo.domain.Hijo;
import org.control_parental.publicacion.domain.Publicacion;
import org.control_parental.salon.domain.Salon;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface HijoRepository extends JpaRepository<Hijo, Long> {
    Optional<Hijo> findByNombreAndApellido(String nombre, String apellido);
    Page<Hijo> findAll(Pageable pageable);

    Page<Hijo> findAllBySalon(Salon salon, Pageable pageable);

}
