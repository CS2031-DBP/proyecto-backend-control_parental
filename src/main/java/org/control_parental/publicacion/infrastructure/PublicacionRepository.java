package org.control_parental.publicacion.infrastructure;

import org.control_parental.publicacion.domain.Publicacion;
import org.control_parental.salon.domain.Salon;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PublicacionRepository extends JpaRepository<Publicacion, Long> {
    List<Publicacion> findAllBySalon(Salon salon);

}
