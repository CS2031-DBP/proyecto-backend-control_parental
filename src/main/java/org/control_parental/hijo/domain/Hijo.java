package org.control_parental.hijo.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.control_parental.nido.Domain.Nido;
import org.control_parental.padre.domain.Padre;
import org.control_parental.publicacion.domain.Publicacion;
import org.control_parental.salon.domain.Salon;

import java.util.ArrayList;
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

    @Column(name = "nombre", nullable = false)
    @Size(min = 2, max = 50)
    String nombre;

    @Column(name = "apellido", nullable = false)
    @Size(min = 2, max = 50)
    String apellido;

    @ManyToOne
    @JsonBackReference
    @JoinColumn(name = "padre")
    Padre padre;

    @ManyToMany
    List<Publicacion> publicaciones = new ArrayList<>();

    @ManyToOne
    Salon salon;

    @ManyToOne
    @JsonBackReference
    @JoinColumn(name = "nido")
    Nido nido;
}
