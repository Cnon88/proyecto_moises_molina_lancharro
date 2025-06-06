package quedadas.moisesmolinalancharro.backend.servicio;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import quedadas.moisesmolinalancharro.backend.entidad.Juego;
import quedadas.moisesmolinalancharro.backend.repositorio.JuegoRepository;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class JuegoServicioTest {

    @Mock
    private JuegoRepository juegoRepository;

    @InjectMocks
    private JuegoServicio juegoServicio;

    private Juego juego1;
    private Juego juego2;

    @BeforeEach
    void setUp() {
        juego1 = new Juego();
        juego1.setId(1);
        juego1.setNombre("Ajedrez");

        juego2 = new Juego();
        juego2.setId(2);
        juego2.setNombre("Fútbol");
    }

    @Test
    void testObtenerTodosLosJuegos() {
        List<Juego> juegos = Arrays.asList(juego1, juego2);
        when(juegoRepository.findAll()).thenReturn(juegos);

        List<Juego> resultado = juegoServicio.obtenerTodosLosJuegos();

        assertEquals(2, resultado.size());
        assertTrue(resultado.contains(juego1));
        assertTrue(resultado.contains(juego2));
    }

    @Test
    void testObtenerPorId_Existe() {
        when(juegoRepository.findById(1)).thenReturn(Optional.of(juego1));

        Optional<Juego> resultado = juegoServicio.obtenerPorId(1);

        assertTrue(resultado.isPresent());
        assertEquals("Ajedrez", resultado.get().getNombre());
    }

    @Test
    void testObtenerPorId_NoExiste() {
        when(juegoRepository.findById(99)).thenReturn(Optional.empty());

        Optional<Juego> resultado = juegoServicio.obtenerPorId(99);

        assertFalse(resultado.isPresent());
    }
}
