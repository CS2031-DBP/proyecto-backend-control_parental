package org.control_parental.nido.Infrastructure;

import org.control_parental.nido.Domain.Nido;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NidoRepository extends JpaRepository<Nido, Long> {



}
