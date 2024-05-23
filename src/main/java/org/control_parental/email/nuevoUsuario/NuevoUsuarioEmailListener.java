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
                + event.role + " en Control Parental \n sus credenciales son: \n" +
                        "Usuario: " + event.email
                + "Contraseña" + event.password + ".\n Recuerde que puede cambiar su " +
                        "contraseña en cualquier momento dentro de la aplicacion! \n \n" +
                        "Un saludo, \n" +
                        "El equipo de Control Parental"
        );
    }

}
