package org.control_parental.email.nuevaContraseña;

import org.control_parental.email.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
public class NuevaContraseñaEmailListener {

    @Autowired
    EmailService emailService;

    @EventListener
    @Async
    public void sendNuevaContraseñaEmail(NuevaContaseñaEmailEvent event) {
        emailService.sendEmail(event.getEmail(), "Cambio de contraseña!", "Querid@ " + event.getNombre() +
        ", \npor su seguridad le informamos que el dia " + event.getHora() + " se cambió su contraseña. Si usted no fue, " +
                "comuníquese de inmediato con el administrador de la aplicación." );

    }
}
