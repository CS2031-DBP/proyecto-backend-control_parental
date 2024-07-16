package org.control_parental.email.nuevoSalon;

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
public class AgregacionHijoSalonEmailListener {

    @Autowired
    private EmailService emailService;

    @Autowired
    private EmailThymeleafService thymeleafService;

    //    @EventListener
//    @Async
//    public void sendAgregacionHijoSalonEmail(AgregacionHijoSalonEmailEvent event) {
//        emailService.sendEmail(event.getEmail(), "Nuevo Salon!",
//                event.getPadreNombre() + ",\n" + event.getName() + " ha sido agregad@ al salon " + event.getSalon()
//        + ".\nIngrese a la aplicacion para poder ver más detalles!" +
//                        "\n\nUn saludo,\nequipo de Control Parental");
//    }
    @Value("${spring.mail.username}")
    private String email;
    @EventListener
    @Async
    public void sendAgregacionHijoSalonEmail(AgregacionHijoSalonEmailEvent event) throws MessagingException, IOException {
        Map<String, Object> properties = new HashMap<>();
        properties.put("padreNombre", event.getPadreNombre());
        properties.put("name", event.getName());
        properties.put("salon", event.getSalon());

        Mail mail = Mail.builder()
                .from(email)
                .to(event.getEmail())
                .htmlTemplate(new Mail.HtmlTemplate("agregacionHijoSalon", properties))
                .subject("¡Nuevo Salon!")
                .build();

        thymeleafService.sendEmail(mail);
    }
}
