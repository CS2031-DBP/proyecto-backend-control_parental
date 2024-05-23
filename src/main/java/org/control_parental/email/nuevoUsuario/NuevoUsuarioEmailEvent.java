package org.control_parental.email.nuevoUsuario;

public class NuevoUsuarioEmailEvent {

    String email;

    String password;

    String nombre;

    String role;

    public NuevoUsuarioEmailEvent(String email, String password, String nombre, String role) {
        this.email = email;
        this.password = password;
        this.nombre = nombre;
        this.role = role;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getRole() {
        return role;
    }
}
