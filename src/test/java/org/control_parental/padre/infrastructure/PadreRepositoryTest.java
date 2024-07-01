package org.control_parental.padre.infrastructure;

import org.control_parental.AbstractContainerBaseTest;
import org.control_parental.configuration.TestConfig;
import org.control_parental.hijo.domain.Hijo;
import org.control_parental.padre.domain.Padre;
import org.control_parental.usuario.domain.Role;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;

import java.util.ArrayList;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import(TestConfig.class)
public class PadreRepositoryTest extends AbstractContainerBaseTest {

    @Autowired
    PadreRepository padreRepository;

    @Autowired
    TestEntityManager entityManager;

    Padre padre1;
    Padre padre2;

    @BeforeEach
    public void setUp() {
        padre1 = new Padre();
        padre1.setNombre("Jorge");
        padre1.setApellido("Rios");
        padre1.setEmail("jrios@utec.edu.pe");
        padre1.setPassword("password123");
        padre1.setPhoneNumber("123456789");
        padre1.setRole(Role.PADRE);
        padre1.setHijos(new ArrayList<>());
        padre1.setPosts_likeados(new ArrayList<>());
        entityManager.persist(padre1);

        padre2 = new Padre();
        padre2.setNombre("Jesus");
        padre2.setApellido("Bellido");
        padre2.setEmail("jbellido@utec.edu.pe");
        padre2.setPassword("password123");
        padre2.setPhoneNumber("987654321");
        padre2.setRole(Role.PADRE);
        padre2.setHijos(new ArrayList<>());
        padre2.setPosts_likeados(new ArrayList<>());
        entityManager.persist(padre2);

        entityManager.flush();
    }

    @Test
    public void createPadre() {
        Padre padre = new Padre();
        padre.setNombre("Maria");
        padre.setApellido("Hilda");
        padre.setEmail("mhilda@utec.edu.pe");
        padre.setPassword("password123");
        padre.setPhoneNumber("123123123");
        padre.setRole(Role.PADRE);

        Padre savedPadre = padreRepository.save(padre);
        Optional<Padre> retrievedPadre = padreRepository.findById(savedPadre.getId());
        assertTrue(retrievedPadre.isPresent());
        assertEquals("Maria", retrievedPadre.get().getNombre());
        assertEquals("Hilda", retrievedPadre.get().getApellido());
    }

    @Test
    public void testFindByEmail() {
        Optional<Padre> optionalPadre = padreRepository.findByEmail("jrios@utec.edu.pe");
        assertTrue(optionalPadre.isPresent());
        assertEquals("Jorge", optionalPadre.get().getNombre());
        assertEquals("Rios", optionalPadre.get().getApellido());
    }

    @Test
    public void shouldDeletePadre() {
        padreRepository.deleteById(padre1.getId());
        assertTrue(padreRepository.findById(padre1.getId()).isEmpty());
    }

    @Test
    public void addHijoToPadre() {
        Hijo hijo = new Hijo();
        hijo.setNombre("Chapulin");
        hijo.setApellido("Colorado");
        hijo.setPadre(padre1);

        padre1.getHijos().add(hijo);

        entityManager.persist(hijo);
        entityManager.persist(padre1);
        entityManager.flush();

        Padre retrievedPadre = padreRepository.findById(padre1.getId()).orElseThrow();
        assertEquals(1, retrievedPadre.getHijos().size());
        assertEquals("Chapulin", retrievedPadre.getHijos().get(0).getNombre());
        assertEquals("Colorado", retrievedPadre.getHijos().get(0).getApellido());
    }
}
