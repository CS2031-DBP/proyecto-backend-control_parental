package org.control_parental.profesor.infraestructure;

import org.control_parental.profesor.domain.Profesor;
import org.control_parental.profesor.infrastructure.ProfesorRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class ProfesorRepositoryTest {

    @Autowired
    private ProfesorRepository profesorRepository;

    @Autowired
    private TestEntityManager entityManager;

    private Profesor profesor;

    @BeforeEach
    public void setUp() {
        profesor = new Profesor();
        profesor.setNombre("Nombre");
        profesor.setApellido("Apellido");

        entityManager.persist(profesor);
        entityManager.flush();
    }

    @Test
    public void testFindById() {
        Optional<Profesor> optionalProfesor = profesorRepository.findById(profesor.getId());
        assertTrue(optionalProfesor.isPresent());
        assertEquals("Nombre", optionalProfesor.get().getNombre());
        assertEquals("Apellido", optionalProfesor.get().getApellido());
    }

    @Test
    public void testCreateProfesor() {
        Profesor profesor = new Profesor();
        profesor.setNombre("John");
        profesor.setApellido("Doe");

        Profesor savedProfesor = profesorRepository.save(profesor);
        assertNotNull(savedProfesor.getId());

        Profesor retrievedProfesor = entityManager.find(Profesor.class, savedProfesor.getId());
        assertNotNull(retrievedProfesor);
        assertEquals("John", retrievedProfesor.getNombre());
        assertEquals("Doe", retrievedProfesor.getApellido());
    }

    @Test
    public void testDeleteProfesor() {
        Profesor profesor = new Profesor();
        profesor.setNombre("Jane");
        profesor.setApellido("Doe");

        Profesor savedProfesor = entityManager.persistAndFlush(profesor);
        Long profesorId = savedProfesor.getId();
        profesorRepository.deleteById(profesorId);

        //assertFalse(entityManager.contains(profesorRepository.findById(profesorId).orElse(null)));

    }
}
