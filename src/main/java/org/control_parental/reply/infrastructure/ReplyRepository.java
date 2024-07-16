package org.control_parental.reply.infrastructure;

import org.control_parental.reply.domain.Reply;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReplyRepository extends JpaRepository<Reply, Long> {
    Page<Reply> findAllByComentario_IdOrderByFechaDesc(Long id, Pageable pageable);
}
