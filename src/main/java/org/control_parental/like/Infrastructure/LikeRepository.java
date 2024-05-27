package org.control_parental.like.Infrastructure;

import org.control_parental.like.Domain.Padre_Like;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LikeRepository extends JpaRepository<Padre_Like, Long> {
    Optional<Padre_Like> findByPadre_IdAndPublicacion_Id(Long padreId, Long publicacionId);

    Boolean existsByPadre_IdAndPublicacion_Id(Long padreId, Long publicacionId);
}
