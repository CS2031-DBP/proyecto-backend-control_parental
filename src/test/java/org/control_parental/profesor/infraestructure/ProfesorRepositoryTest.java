package org.control_parental.profesor.infraestructure;

import org.control_parental.configuration.TestConfig;
import org.control_parental.profesor.domain.Profesor;
import org.control_parental.profesor.infrastructure.ProfesorRepository;
import org.control_parental.usuario.domain.Role;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import(TestConfig.class)
public class ProfesorRepositoryTest {

    @Autowired
    private ProfesorRepository profesorRepository;

    @Autowired
    private TestEntityManager entityManager;

    private Profesor profesor;

    @BeforeEach
    public void setUp() {
        profesor = new Profesor();
        profesor.setNombre("Jorge");
        profesor.setApellido("Rios");
        profesor.setEmail("jorge.rios@utec.edu.pe");
        profesor.setPassword("el_mejor_profe");
        profesor.setRole(Role.PROFESOR);

        entityManager.persist(profesor);
        entityManager.flush();
    }

    @Test
    public void testFindById() {
        Optional<Profesor> optionalProfesor = profesorRepository.findById(profesor.getId());
        assertTrue(optionalProfesor.isPresent());
        assertEquals("Jorge", optionalProfesor.get().getNombre());
        assertEquals("Rios", optionalProfesor.get().getApellido());
        assertEquals("jorge.rios@utec.edu.pe", optionalProfesor.get().getEmail());
        assertEquals("el_mejor_profe", optionalProfesor.get().getPassword());
    }

    @Test
    public void testCreateProfesor() {
        Profesor profesor = new Profesor();
        profesor.setNombre("Mateo");
        profesor.setApellido("Noel");
        profesor.setEmail("mateo.noel@utec.edu.pe");
        profesor.setPassword("contraseña");
        profesor.setRole(Role.PROFESOR);

        Profesor savedProfesor = profesorRepository.save(profesor);
        entityManager.flush();

        Optional<Profesor> retrievedProfesor = profesorRepository.findById(savedProfesor.getId());
        assertTrue(retrievedProfesor.isPresent());
        assertEquals("Mateo", retrievedProfesor.get().getNombre());
        assertEquals("Noel", retrievedProfesor.get().getApellido());
        assertEquals("mateo.noel@utec.edu.pe", retrievedProfesor.get().getEmail());
        assertEquals("contraseña", retrievedProfesor.get().getPassword());
    }

    @Test
    public void testDeleteProfesor() {
        Profesor profesor = new Profesor();
        profesor.setNombre("Jesus");
        profesor.setApellido("Bellido");
        profesor.setEmail("jesus.bellido@utec.edu.pe");
        profesor.setPassword("director");
        profesor.setRole(Role.PROFESOR);

        Profesor savedProfesor = entityManager.persistAndFlush(profesor);
        Long profesorId = savedProfesor.getId();
        profesorRepository.deleteById(profesorId);
        entityManager.flush();

        Optional<Profesor> deletedProfesor = profesorRepository.findById(profesorId);
        assertFalse(deletedProfesor.isPresent());
    }
}
