package org.control_parental.events.uploadImage;

import jakarta.transaction.Transactional;
import org.control_parental.configuration.RandomCode;
import org.control_parental.exceptions.ResourceNotFoundException;
import org.control_parental.publicacion.domain.Publicacion;
import org.control_parental.publicacion.infrastructure.PublicacionRepository;
import org.control_parental.s3.S3;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;

import java.io.File;
import java.io.IOException;

import static org.springframework.transaction.event.TransactionPhase.AFTER_COMMIT;

@Transactional
@Component
public class UploadImageEventLIstener {
    @Autowired
    private PublicacionRepository publicacionRepository;

    @Autowired
    private S3 cliente;

    @Value("${application.bucket.name}")
    private String bucketName;

    @TransactionalEventListener(phase=AFTER_COMMIT)
    @Async
    public void uploadImageToS3(UploadImageEvent uploadImageEvent) {
        Publicacion newPublicacion = publicacionRepository.findById(uploadImageEvent.getPublicacionId())
                .orElseThrow(()-> new ResourceNotFoundException("La publicacion no fue encontrada"));

        RandomCode rc = new RandomCode();
        uploadImageEvent.fotos.forEach(foto -> {

            String code = rc.generatePassword();
            String fileName = newPublicacion.getTitulo().replace(" ", "-")
                    + "-" + newPublicacion.getFecha().toString().replace(":", "-") + "-" + code;
            cliente.uploadFileToS3Bucket(bucketName, foto, fileName);
            foto.deleteOnExit();
            fileName = fileName.replace(" ", "-");
            String URI = String.format("https://%s.s3.amazonaws.com/%s",bucketName,fileName);
            newPublicacion.addFoto(URI);
        });
        System.out.println("Fotos subidas de forma exitosa");
        publicacionRepository.save(newPublicacion);
    }

}
