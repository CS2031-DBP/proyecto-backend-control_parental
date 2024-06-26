package org.control_parental.email.codigoVerificacion;

public class CodigoVerificacionEmailEvent {

    private String email;

    private String codigo;

    private String nombre;

    public CodigoVerificacionEmailEvent(String email, String codigo, String nombre) {
        this.email = email;
        this.codigo = codigo;
        this.nombre = nombre;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
}
