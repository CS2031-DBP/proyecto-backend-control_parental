package org.control_parental.hijo.domain;

import jakarta.persistence.*;
import lombok.Data;
import org.control_parental.padre.domain.Padre;
import org.control_parental.post.domain.Post;
import org.control_parental.salon.domain.Salon;

import java.util.List;

@Data
@Entity
public class Hijo {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    Long id;

    Integer edad;

    String nombre;

    String apellido;

    @ManyToMany
    List<Padre> padres;

    @ManyToMany
    List<Post> posts;

    @ManyToMany
    List<Salon> salones;
}
