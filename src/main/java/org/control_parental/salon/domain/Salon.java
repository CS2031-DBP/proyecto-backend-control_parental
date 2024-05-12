package org.control_parental.salon.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.control_parental.hijo.domain.Hijo;
import org.control_parental.publicacion.domain.Publicacion;
import org.control_parental.profesor.domain.Profesor;

import java.util.List;

@Data
@Entity
public class Salon {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    Long id;

    @Column(nullable = false)
    @Size(min = 2, max = 50)
    String nombre;

    @ManyToMany
    List<Profesor> profesores;

    @OneToMany
    List<Publicacion> publicaciones;

    @JsonIgnore
    @OneToMany(mappedBy = "salon")
    List<Hijo> hijos;

    void addStudent(Hijo hijo) {
        hijos.add(hijo);
    }

}
