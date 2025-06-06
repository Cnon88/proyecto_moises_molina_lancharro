package quedadas.moisesmolinalancharro.backend.servicio;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import quedadas.moisesmolinalancharro.backend.entidad.Evento;
import quedadas.moisesmolinalancharro.backend.entidad.Usuario;
import quedadas.moisesmolinalancharro.backend.repositorio.EventoRepository;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static quedadas.moisesmolinalancharro.backend.entidad.Evento.EstadoEvento.PENDIENTE;

@ExtendWith(MockitoExtension.class)
class EventoServicioTest {

    @Mock
    private EventoRepository eventoRepository;

    @Mock
    private UsuarioServicio usuarioServicio;

    @InjectMocks
    private EventoServicio eventoServicio;

    private Usuario usuario;
    private Evento evento;

    @BeforeEach
    void setUp() {
        usuario = new Usuario();
        usuario.setId(1);
        usuario.setEmail("user@example.com");
        usuario.setEventosComoParticipante(new HashSet<>());

        evento = new Evento();
        evento.setId(10);
        evento.setEstado(PENDIENTE);
        evento.setParticipantes(new HashSet<>());
    }

    @Test
    void testObtenerPorId() {
        when(eventoRepository.findById(10)).thenReturn(Optional.of(evento));

        Optional<Evento> result = eventoServicio.obtenerPorId(10);

        assertTrue(result.isPresent());
        assertEquals(10, result.get().getId());
    }

    @Test
    void testObtenerTodos() {
        List<Evento> eventos = List.of(evento);
        when(eventoRepository.findAll()).thenReturn(eventos);

        List<Evento> result = eventoServicio.obtenerTodos();

        assertEquals(1, result.size());
        assertEquals(evento, result.get(0));
    }

    @Test
    void testCrearEvento_ConUsuarioValido() {
        when(usuarioServicio.obtenerUsuarioPorEmail("user@example.com")).thenReturn(Optional.of(usuario));
        when(eventoRepository.save(any(Evento.class))).thenAnswer(i -> i.getArgument(0));

        Evento nuevoEvento = new Evento();
        Evento creado = eventoServicio.crearEvento(nuevoEvento, "user@example.com");

        assertEquals(PENDIENTE, creado.getEstado());
        assertEquals(usuario, creado.getCreador());
    }

    @Test
    void testCrearEvento_SinUsuario() {
        when(usuarioServicio.obtenerUsuarioPorEmail("noexiste@example.com")).thenReturn(Optional.empty());

        Evento nuevoEvento = new Evento();

        assertThrows(IllegalArgumentException.class,
                () -> eventoServicio.crearEvento(nuevoEvento, "noexiste@example.com"));
    }

    @Test
    void testInscribirseEnEvento_Success() {
        when(usuarioServicio.obtenerUsuarioPorId(1)).thenReturn(Optional.of(usuario));
        when(eventoRepository.findById(10)).thenReturn(Optional.of(evento));
        when(usuarioServicio.guardarUsuario(usuario)).thenReturn(usuario);

        boolean result = eventoServicio.inscribirseEnEvento(1, 10);

        assertTrue(result);
        assertTrue(usuario.getEventosComoParticipante().contains(evento));
    }

    @Test
    void testInscribirseEnEvento_EventoNoPendiente() {
        evento.setEstado(Evento.EstadoEvento.LLENO);
        when(usuarioServicio.obtenerUsuarioPorId(1)).thenReturn(Optional.of(usuario));
        when(eventoRepository.findById(10)).thenReturn(Optional.of(evento));

        boolean result = eventoServicio.inscribirseEnEvento(1, 10);

        assertFalse(result);
    }

    @Test
    void testInscribirseEnEvento_UsuarioYaInscrito() {
        usuario.getEventosComoParticipante().add(evento);
        when(usuarioServicio.obtenerUsuarioPorId(1)).thenReturn(Optional.of(usuario));
        when(eventoRepository.findById(10)).thenReturn(Optional.of(evento));

        boolean result = eventoServicio.inscribirseEnEvento(1, 10);

        assertFalse(result);
    }

    @Test
    void testInscribirseEnEvento_UsuarioOEventoNoExiste() {
        when(usuarioServicio.obtenerUsuarioPorId(1)).thenReturn(Optional.empty());

        boolean result = eventoServicio.inscribirseEnEvento(1, 10);

        assertFalse(result);
    }

    @Test
    void testDesinscribirseDelEvento_Success() {
        usuario.getEventosComoParticipante().add(evento);
        evento.getParticipantes().add(usuario);

        when(usuarioServicio.obtenerUsuarioPorId(1)).thenReturn(Optional.of(usuario));
        when(eventoRepository.findById(10)).thenReturn(Optional.of(evento));
        when(usuarioServicio.guardarUsuario(usuario)).thenReturn(usuario);

        boolean result = eventoServicio.desinscribirseDelEvento(1, 10);

        assertTrue(result);
        assertFalse(usuario.getEventosComoParticipante().contains(evento));
        assertFalse(evento.getParticipantes().contains(usuario));
    }

    @Test
    void testDesinscribirseDelEvento_EventoNoPendiente() {
        evento.setEstado(Evento.EstadoEvento.LLENO);
        usuario.getEventosComoParticipante().add(evento);

        when(usuarioServicio.obtenerUsuarioPorId(1)).thenReturn(Optional.of(usuario));
        when(eventoRepository.findById(10)).thenReturn(Optional.of(evento));

        boolean result = eventoServicio.desinscribirseDelEvento(1, 10);

        assertFalse(result);
    }

    @Test
    void testDesinscribirseDelEvento_NoEstabaInscrito() {
        when(usuarioServicio.obtenerUsuarioPorId(1)).thenReturn(Optional.of(usuario));
        when(eventoRepository.findById(10)).thenReturn(Optional.of(evento));

        boolean result = eventoServicio.desinscribirseDelEvento(1, 10);

        assertFalse(result);
    }

    @Test
    void testEliminarEventoSiEsCreador_Success() {
        evento.setCreador(usuario);
        Usuario otro = new Usuario();
        otro.setId(2);
        otro.setEventosComoParticipante(new HashSet<>(Set.of(evento)));
        evento.setParticipantes(new HashSet<>(Set.of(otro)));

        when(eventoRepository.findById(10)).thenReturn(Optional.of(evento));

        boolean result = eventoServicio.eliminarEventoSiEsCreador(1, 10);

        assertTrue(result);
        assertTrue(evento.getParticipantes().isEmpty());
        verify(eventoRepository).delete(evento);
    }

    @Test
    void testEliminarEventoSiEsCreador_NoEsCreador() {
        Usuario otro = new Usuario();
        otro.setId(2);
        evento.setCreador(otro);

        when(eventoRepository.findById(10)).thenReturn(Optional.of(evento));

        boolean result = eventoServicio.eliminarEventoSiEsCreador(1, 10);

        assertFalse(result);
        verify(eventoRepository, never()).delete(any());
    }

    @Test
    void testEliminarEventoSiEsCreador_EventoNoExiste() {
        when(eventoRepository.findById(10)).thenReturn(Optional.empty());

        boolean result = eventoServicio.eliminarEventoSiEsCreador(1, 10);

        assertFalse(result);
    }
}
