package org.control_parental.email.codigoVerificacion;

import org.control_parental.email.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.context.event.EventListener;


@Component
public class CodigoVerificacionEmailListener {

    @Autowired
    EmailService emailService;

    @EventListener
    @Async
    public void sendCodigoVerificacion(CodigoVerificacionEmailEvent event) {
        emailService.sendEmail(event.getEmail(), "Código de verificación", event.getNombre()
        + "\nSu código de verificación es: " + event.getCodigo() + ".\n\n Un saludo, \nEquipo de Control Parental.");
    }


}
