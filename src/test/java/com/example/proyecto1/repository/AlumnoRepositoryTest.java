package com.example.proyecto1.repository;

import com.example.proyecto1.model.Alumno;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test") // Configura el perfil de pruebas, en este caso, para usar MySQL
class AlumnoRepositoryTest {

    @Autowired
    private AlumnoRepository alumnoRepository;

    /**
     * Configuración inicial antes de cada prueba:
     * - Limpia la base de datos para evitar interferencias entre pruebas.
     * - Agrega dos alumnos iniciales como datos de prueba.
     */
    @BeforeEach
    void setUp() {
        alumnoRepository.deleteAll(); // Limpia la tabla antes de cada test

        Alumno alumno1 = new Alumno();
        alumno1.setNombre("John");
        alumno1.setApellido("Doe");
        alumno1.setEdad(20);
        alumno1.setCurso("Matematicas");

        Alumno alumno2 = new Alumno();
        alumno2.setNombre("Jane");
        alumno2.setApellido("Smith");
        alumno2.setEdad(22);
        alumno2.setCurso("Ciencias");

        // Guarda los datos iniciales en la base de datos
        alumnoRepository.save(alumno1);
        alumnoRepository.save(alumno2);
    }

    /**
     * Prueba para guardar un nuevo alumno en la base de datos.
     * Verifica que:
     * - El ID generado automáticamente no sea nulo.
     * - Los datos guardados sean los esperados.
     */
    @Test
    void testGuardarAlumno() {
        Alumno alumno = new Alumno(null, "Alice", "Brown", 19, "Historia");
        Alumno alumnoGuardado = alumnoRepository.save(alumno);

        assertNotNull(alumnoGuardado.getId()); // Verifica que el ID no sea nulo
        assertEquals("Alice", alumnoGuardado.getNombre()); // Verifica que el nombre sea correcto
    }

    /**
     * Prueba para buscar un alumno por su ID.
     * Verifica que:
     * - El alumno existe en la base de datos.
     * - Los datos obtenidos son correctos.
     */
    @Test
    void testBuscarPorId() {
        Optional<Alumno> alumno = alumnoRepository.findById(1L);

        assertTrue(alumno.isPresent()); // Verifica que el alumno exista
        assertEquals("John", alumno.get().getNombre()); // Verifica el nombre del alumno
    }

    /**
     * Prueba para obtener todos los alumnos de la base de datos.
     * Verifica que:
     * - La cantidad de registros obtenidos es la esperada.
     */
    @Test
    void testBuscarTodos() {
        // Obtiene todos los registros asociados a la entidad Alumno
        List<Alumno> alumnos = alumnoRepository.findAll();

        // Compara la cantidad esperada de registros con los obtenidos
        assertEquals(2, alumnos.size());

        // Imprime los alumnos para fines de depuración
        for (Alumno alumno : alumnos) {
            System.out.println(alumno);
        }
    }

    /**
     * Prueba para actualizar un registro existente.
     * Verifica que:
     * - El alumno existe antes de actualizarlo.
     * - Los datos actualizados se guardan correctamente.
     */
    @Test
    void testActualizarAlumno() {
        Optional<Alumno> optionalAlumno = alumnoRepository.findById(1L);
        assertTrue(optionalAlumno.isPresent()); // Verifica que el alumno existe

        // Actualiza el nombre del alumno
        Alumno alumno = optionalAlumno.get();
        alumno.setNombre("NombreActualizado");
        Alumno alumnoActualizado = alumnoRepository.save(alumno);

        // Verifica que el cambio se realizó correctamente
        assertEquals("NombreActualizado", alumnoActualizado.getNombre());
    }

    /**
     * Prueba para eliminar un alumno por su ID.
     * Verifica que:
     * - El alumno ya no existe en la base de datos después de eliminarlo.
     */
    @Test
    void testEliminarAlumno() {
        alumnoRepository.deleteById(1L); // Elimina el registro con ID 1

        // Verifica que el registro no esté presente
        Optional<Alumno> alumno = alumnoRepository.findById(1L);
        assertFalse(alumno.isPresent());
    }
}