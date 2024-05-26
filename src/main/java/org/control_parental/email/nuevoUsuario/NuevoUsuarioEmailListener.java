package org.control_parental.email.nuevoUsuario;

import org.control_parental.email.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
public class NuevoUsuarioEmailListener {

    @Autowired
    private EmailService emailService;

    @EventListener
    @Async
    public void sendNuevoUsuarioEmail(NuevoUsuarioEmailEvent event) {
        emailService.sendEmail(
                event.getEmail(),
                "Nuevo Usuario en Control Parental!",
                event.getNombre() + ",\nUsted ha sido agregado como "
                + event.role + " en Control Parental. \n\nSus credenciales son:\n\n" +
                        "Usuario: " + event.email
                + "\nContraseña: " + event.password + "\n\nRecuerde que puede cambiar su " +
                        "contraseña en cualquier momento dentro de la aplicacion!\n\n" +
                        "Un saludo, \n" +
                        "equipo de Control Parental"
        );
    }

}
