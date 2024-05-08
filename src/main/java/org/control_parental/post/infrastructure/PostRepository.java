package org.control_parental.post.infrastructure;

import org.control_parental.post.domain.Post;
import org.control_parental.salon.domain.Salon;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
    List<Post> findAllBySalon(Salon salon);
}
