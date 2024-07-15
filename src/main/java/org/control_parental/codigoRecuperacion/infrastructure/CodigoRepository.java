package org.control_parental.codigoRecuperacion.infrastructure;

import org.control_parental.codigoRecuperacion.domain.Codigo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository

public interface CodigoRepository extends JpaRepository<Codigo, Long> {
}
