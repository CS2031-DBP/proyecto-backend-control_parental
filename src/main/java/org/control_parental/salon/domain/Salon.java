package org.control_parental.salon.domain;

import jakarta.persistence.*;
import lombok.Data;
import org.control_parental.hijo.domain.Hijo;
import org.control_parental.post.domain.Post;
import org.control_parental.profesor.Profesor;

import java.util.List;

@Data
@Entity
public class Salon {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    Long id;

    String nombre;

    @ManyToMany
    List<Profesor> profesores;

    @OneToMany
    List<Post> posts;

    @ManyToMany
    List<Hijo> hijos;

}
