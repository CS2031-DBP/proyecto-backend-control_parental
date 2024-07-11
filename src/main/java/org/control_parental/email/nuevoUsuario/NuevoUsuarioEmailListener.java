package org.control_parental.email.nuevoUsuario;

import jakarta.mail.MessagingException;
import org.control_parental.email.EmailService;
import org.control_parental.email.EmailThymeleafService;
import org.control_parental.email.Mail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Component
public class NuevoUsuarioEmailListener {

    @Autowired
    private EmailService emailService;

    @Autowired
    private EmailThymeleafService thymeleafService;

    @Value("${spring.mail.username}")
    private String email;

// ---------------------- Deprecado porque manda mails sin formato, un string largo con cortes de linea
//    @EventListener
//    @Async
//    public void sendNuevoUsuarioEmail(NuevoUsuarioEmailEvent event) {
//        emailService.sendEmail(
//                event.getEmail(),
//                "Nuevo Usuario en Control Parental!",
//                event.getNombre() + ",\nUsted ha sido agregado como "
//                + event.role + " en Control Parental. \n\nSus credenciales son:\n\n" +
//                        "Usuario: " + event.email
//                + "\nContraseña: " + event.password + "\n\nRecuerde que puede cambiar su " +
//                        "contraseña en cualquier momento dentro de la aplicacion!\n\n" +
//                        "Un saludo, \n" +
//                        "equipo de Control Parental"
//        );
//    }
//
    @EventListener
    @Async
    public void sendNuevoUsuarioEmail(NuevoUsuarioEmailEvent event) throws MessagingException, IOException {
        Map<String ,Object> properties = new HashMap<String, Object>();
        properties.put("nombre", event.getNombre());
        properties.put("email", event.getEmail());
        properties.put("role", event.getRole());
        properties.put("password", event.getPassword());

        Mail mail = Mail.builder()
                .from(email)
                .to(event.getEmail())
                .htmlTemplate(new Mail.HtmlTemplate("nuevoUsuario", properties))
                .subject("Nuevo Usuario en Wawakuna!")
                .build();

        thymeleafService.sendEmail(mail);
    }

}
