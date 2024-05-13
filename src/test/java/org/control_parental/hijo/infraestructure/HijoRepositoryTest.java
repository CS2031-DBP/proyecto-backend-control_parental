package org.control_parental.hijo.infraestructure;

import org.control_parental.hijo.domain.Hijo;
import org.control_parental.hijo.infrastructure.HijoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@Testcontainers
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)

public class HijoRepositoryTest {
    @Container
    private static final PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>("postgres:latest")
            .withDatabaseName("test")
            .withUsername("test")
            .withPassword("test");

    @Autowired
    private HijoRepository hijoRepository;

    @Test
    public void createHijo() {
        Hijo hijo = new Hijo();
        hijo.setNombre("Pepito");
        hijo.setApellido("Perez");

        Hijo savedHijo = hijoRepository.save(hijo);

        Optional<Hijo> retrievedHijo = hijoRepository.findById(savedHijo.getId());
        assertTrue(retrievedHijo.isPresent());
        assertEquals("Pepito", retrievedHijo.get().getNombre());
        assertEquals("Perez", retrievedHijo.get().getApellido());
    }

    @Test
    public void testFindByNombreAndApellido() {
        Hijo hijo = new Hijo();
        hijo.setNombre("Peppa");
        hijo.setApellido("Pig");
        hijoRepository.save(hijo);

        Optional<Hijo> optionalHijo = hijoRepository.findByNombreAndApellido("Peppa", "Pig");

        assertTrue(optionalHijo.isPresent());
        assertEquals("Peppa", optionalHijo.get().getNombre());
        assertEquals("Pig", optionalHijo.get().getApellido());
    }
}
