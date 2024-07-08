//package org.control_parental.publicacion.infrastructure;
//
//import org.control_parental.AbstractContainerBaseTest;
//import org.control_parental.configuration.TestConfig;
//import org.control_parental.publicacion.domain.Publicacion;
//import org.control_parental.salon.domain.Salon;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
//import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
//import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
//import org.springframework.context.annotation.Import;
//
//import java.time.LocalDateTime;
//import java.util.List;
//import java.util.Optional;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//@DataJpaTest
//@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
//@Import(TestConfig.class)
//public class PublicacionRepositoryTest extends AbstractContainerBaseTest {
//
//    @Autowired
//    PublicacionRepository publicacionRepository;
//
//    @Autowired
//    TestEntityManager entityManager;
//
//    @Test
//    public void createPublicacion() {
//        Publicacion publicacion = new Publicacion();
//        publicacion.setTitulo("Mi primera publicacion");
//        publicacion.setDescripcion("Contenido de la publicacion");
//        publicacion.setFecha(LocalDateTime.now());
//        publicacion.setLikes(0);
//
//        Salon salon = new Salon();
//        salon.setNombre("Salon 1");
//        entityManager.persist(salon);
//
//        publicacion.setSalon(salon);
//
//        Publicacion savedPublicacion = publicacionRepository.save(publicacion);
//        Optional<Publicacion> retrievedPublicacion = publicacionRepository.findById(savedPublicacion.getId());
//        assertTrue(retrievedPublicacion.isPresent());
//        assertEquals("Mi primera publicacion", retrievedPublicacion.get().getTitulo());
//    }
//
//    @Test
//    public void testFindAllBySalon() {
//        Salon salon = new Salon();
//        salon.setNombre("Salon 1");
//        entityManager.persist(salon);
//
//        Publicacion publicacion1 = new Publicacion();
//        publicacion1.setTitulo("Publicacion 1");
//        publicacion1.setDescripcion("Contenido de la publicacion 1");
//        publicacion1.setFecha(LocalDateTime.now());
//        publicacion1.setLikes(0);
//        publicacion1.setSalon(salon);
//        entityManager.persist(publicacion1);
//
//        Publicacion publicacion2 = new Publicacion();
//        publicacion2.setTitulo("Publicacion 2");
//        publicacion2.setDescripcion("Contenido de la publicacion 2");
//        publicacion2.setFecha(LocalDateTime.now());
//        publicacion2.setLikes(0);
//        publicacion2.setSalon(salon);
//        entityManager.persist(publicacion2);
//
//        entityManager.flush();
//
//        List<Publicacion> publicaciones = publicacionRepository.findAllBySalon(salon);
//        assertEquals(2, publicaciones.size());
//    }
//
//    @Test
//    public void deletePublicacion() {
//        Publicacion publicacion = new Publicacion();
//        publicacion.setTitulo("Publicacion a borrar");
//        publicacion.setDescripcion("Contenido de la publicacion a borrar");
//        publicacion.setFecha(LocalDateTime.now());
//        publicacion.setLikes(0);
//
//        entityManager.persist(publicacion);
//        entityManager.flush();
//
//        Long publicacionId = publicacion.getId();
//
//        Optional<Publicacion> publicacionOptional = publicacionRepository.findById(publicacionId);
//        assertTrue(publicacionOptional.isPresent());
//
//        publicacionRepository.deleteById(publicacionId);
//
//        Optional<Publicacion> deletedPublicacionOptional = publicacionRepository.findById(publicacionId);
//        assertTrue(deletedPublicacionOptional.isEmpty());
//    }
//}
