package org.control_parental.hijo.infraestructure;

import org.control_parental.AbstractContainerBaseTest;
import org.control_parental.hijo.domain.Hijo;
import org.control_parental.hijo.infrastructure.HijoRepository;
import org.control_parental.padre.domain.Padre;
import org.control_parental.salon.domain.Salon;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class HijoRepositoryTest extends AbstractContainerBaseTest {

    @Autowired
    HijoRepository hijoRepository;

    @Autowired
    TestEntityManager entityManager;

    Hijo hijo1;
    Hijo hijo2;
    Hijo hijo3;
    Salon salon;

    @BeforeEach
    public void setUp() {
        salon = new Salon();
        salon.setNombre("Sala 1");
        entityManager.persist(salon);

        hijo1 = new Hijo();
        hijo1.setNombre("Pepito");
        hijo1.setApellido("Perez");
        hijo1.setSalon(salon);

        hijo2 = new Hijo();
        hijo2.setNombre("Peppa");
        hijo2.setApellido("Pig");
        hijo2.setSalon(salon);

        hijo3 = new Hijo();
        hijo3.setNombre("George");
        hijo3.setApellido("Pig");
        hijo3.setSalon(salon);

        entityManager.persist(hijo1);
        entityManager.persist(hijo2);
        entityManager.persist(hijo3);
        entityManager.flush();
    }

    @Test
    public void createHijo() {
        Hijo hijo = new Hijo();
        hijo.setNombre("Carlos");
        hijo.setApellido("Lopez");
        hijo.setSalon(salon);
        Hijo savedHijo = hijoRepository.save(hijo);
        Optional<Hijo> retrievedHijo = hijoRepository.findById(savedHijo.getId());
        assertTrue(retrievedHijo.isPresent());
        assertEquals("Carlos", retrievedHijo.get().getNombre());
        assertEquals("Lopez", retrievedHijo.get().getApellido());
    }

    @Test
    public void testFindByNombreAndApellido() {
        Optional<Hijo> optionalHijo = hijoRepository.findByNombreAndApellido("Peppa", "Pig");
        assertTrue(optionalHijo.isPresent());
        assertEquals("Peppa", optionalHijo.get().getNombre());
        assertEquals("Pig", optionalHijo.get().getApellido());
    }

    @Test
    public void shouldDelete() {
        hijoRepository.deleteById(hijo1.getId());
        assertTrue(hijoRepository.findById(hijo1.getId()).isEmpty());
    }
}
