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
        emailService.sendEmail(event.getEmail(), "Cambio de contraseña!", event.getNombre() +
        ", \nPor su seguridad le informamos que el dia " + event.getHora() + " se cambió su contraseña.\nSi usted no fue, " +
                "comuníquese de inmediato con el administrador de la aplicación." +
                "\n\nUn saludo,\nequipo de Control Parental" );

    }
}
