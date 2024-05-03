package org.control_parental.profesor;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.control_parental.post.domain.Post;
import org.control_parental.salon.domain.Salon;
import org.control_parental.usuario.domain.Usuario;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class Profesor extends Usuario {

    @OneToMany
    List<Post> posts;

    @ManyToMany
    List<Salon> salones;

}
