package org.control_parental.email.nuevaPublicacion;

import lombok.Data;
import org.springframework.context.ApplicationEvent;

import java.time.Clock;

public class PublicacionEmailEvent extends ApplicationEvent {

    private String nombre;
    private String email;

    private String titulo;

    private String padreNombre;

    public PublicacionEmailEvent(Object source, String nombre, String email, String titulo, String padreNombre) {
        super(source);
        this.nombre = nombre;
        this.email = email;
        this.titulo = titulo;
        this.padreNombre = padreNombre;
    }

    public PublicacionEmailEvent(Object source, Clock clock, String nombre, String email, String titulo, String padreNombre) {
        super(source, clock);
        this.nombre = nombre;
        this.email = email;
        this.titulo = titulo;
        this.padreNombre = padreNombre;
    }

    public String getNombre() {
        return nombre;
    }

    public String getEmail() {
        return email;
    }

    public String getTitulo() {
        return titulo;
    }

    public String getPadreNombre() {
        return padreNombre;
    }
}
