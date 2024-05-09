package org.control_parental.hijo.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.control_parental.padre.domain.Padre;
import org.control_parental.publicacion.domain.Publicacion;
import org.control_parental.salon.domain.Salon;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
public class Hijo {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    Long id;

    @Column(name = "nombre")
    String nombre;

    @Column(name = "apellido")
    String apellido;

    @ManyToMany
    List<Padre> padres;

    @ManyToMany
    List<Publicacion> publicaciones;

    @OneToOne
    Salon salon;
}
