package org.control_parental.salon.infrastructure;

import org.control_parental.salon.domain.Salon;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.swing.text.html.Option;
import java.util.Optional;

public interface SalonRepository extends JpaRepository<Salon, Long> {
    Optional<Salon> findByNombre(String nombre);
}
