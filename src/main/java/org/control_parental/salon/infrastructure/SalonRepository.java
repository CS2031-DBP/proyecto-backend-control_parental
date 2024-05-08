package org.control_parental.salon.infrastructure;

import org.control_parental.salon.domain.Salon;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SalonRepository extends JpaRepository<Salon, Long> {
}
