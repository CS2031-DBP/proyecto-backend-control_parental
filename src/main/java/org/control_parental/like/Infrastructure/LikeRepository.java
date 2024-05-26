package org.control_parental.like.Infrastructure;

import org.control_parental.like.Domain.Like;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LikeRepository extends JpaRepository<Like, Long> {
    Optional<Like> findByUserIdAndPublicacionId(Long userId, Long PublicacionId);
}
