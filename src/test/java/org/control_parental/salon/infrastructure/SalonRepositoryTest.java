//package org.control_parental.salon.infrastructure;
//
//import org.control_parental.AbstractContainerBaseTest;
//import org.control_parental.configuration.TestConfig;
//import org.control_parental.salon.domain.Salon;
//import org.control_parental.hijo.domain.Hijo;
//import org.control_parental.profesor.domain.Profesor;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
//import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
//import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
//import org.springframework.context.annotation.Import;
//
//import java.util.ArrayList;
//import java.util.Optional;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//@DataJpaTest
//@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
//@Import(TestConfig.class)
//public class SalonRepositoryTest extends AbstractContainerBaseTest {
//
//    @Autowired
//    SalonRepository salonRepository;
//
//    @Autowired
//    TestEntityManager entityManager;
//
//    Salon salon1;
//    Salon salon2;
//
//    @BeforeEach
//    public void setUp() {
//        salon1 = new Salon();
//        salon1.setNombre("Salon 1");
//        salon1.setHijos(new ArrayList<>()); // inicializa con lista de hijos
//        salon1.setProfesores(new ArrayList<>()); // inicializa con lista de profesores
//        salon1.setPublicaciones(new ArrayList<>()); // inicializa con lista de publicaciones
//        entityManager.persist(salon1);
//
//        salon2 = new Salon();
//        salon2.setNombre("Salon 2");
//        salon2.setHijos(new ArrayList<>());
//        salon2.setProfesores(new ArrayList<>());
//        salon2.setPublicaciones(new ArrayList<>());
//        entityManager.persist(salon2);
//
//        entityManager.flush();
//    }
//
//    @Test
//    public void createSalon() {
//        Salon salon = new Salon();
//        salon.setNombre("Salon 3");
//        Salon savedSalon = salonRepository.save(salon);
//        Optional<Salon> retrievedSalon = salonRepository.findById(savedSalon.getId());
//        assertTrue(retrievedSalon.isPresent());
//        assertEquals("Salon 3", retrievedSalon.get().getNombre());
//    }
//
//    @Test
//    public void testFindByNombre() {
//        Optional<Salon> optionalSalon = salonRepository.findByNombre("Salon 1");
//        assertTrue(optionalSalon.isPresent());
//        assertEquals("Salon 1", optionalSalon.get().getNombre());
//    }
//
//    @Test
//    public void shouldDelete() {
//        salonRepository.deleteById(salon1.getId());
//        assertTrue(salonRepository.findById(salon1.getId()).isEmpty());
//    }
//
//    @Test
//    public void addHijoToSalon() {
//        Hijo hijo = new Hijo();
//        hijo.setNombre("Pepito");
//        hijo.setApellido("Lopez");
//        hijo.setSalon(salon1);
//
//        salon1.getHijos().add(hijo);
//
//        entityManager.persist(hijo);
//        entityManager.persist(salon1);
//        entityManager.flush();
//
//        Salon retrievedSalon = salonRepository.findById(salon1.getId()).orElseThrow();
//        assertEquals(1, retrievedSalon.getHijos().size());
//        assertEquals("Pepito", retrievedSalon.getHijos().get(0).getNombre());
//        assertEquals("Lopez", retrievedSalon.getHijos().get(0).getApellido());
//    }
//
//    @Test
//    public void addProfesorToSalon() {
//        Profesor profesor = new Profesor();
//        profesor.setNombre("Jorge");
//        profesor.setApellido("Rios");
//        profesor.setEmail("jrios@utec.edu.pe");
//        profesor.setPassword("password123");
//
//        salon1.getProfesores().add(profesor);  // no usar el método addProfesor manrea directa
//        profesor.getSalones().add(salon1);
//
//        entityManager.persist(profesor);
//        entityManager.persist(salon1);
//        entityManager.flush();
//
//        Salon retrievedSalon = salonRepository.findById(salon1.getId()).orElseThrow();
//        assertEquals(1, retrievedSalon.getProfesores().size());
//        assertEquals("Jorge", retrievedSalon.getProfesores().get(0).getNombre());
//    }
//
//}
