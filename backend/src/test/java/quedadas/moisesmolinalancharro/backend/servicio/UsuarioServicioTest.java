package quedadas.moisesmolinalancharro.backend.servicio;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import quedadas.moisesmolinalancharro.backend.entidad.Usuario;
import quedadas.moisesmolinalancharro.backend.repositorio.UsuarioRepository;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UsuarioServicioTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @InjectMocks
    private UsuarioServicio usuarioServicio;

    private Usuario usuario1;
    private Usuario usuario2;

    @BeforeEach
    void init() {
        usuario1 = new Usuario();
        usuario1.setId(1);
        usuario1.setNickname("usuario1");
        usuario1.setEmail("user1@example.com");
        usuario1.setSiguiendo(new HashSet<>());

        usuario2 = new Usuario();
        usuario2.setId(2);
        usuario2.setNickname("usuario2");
        usuario2.setEmail("user2@example.com");
        usuario2.setSiguiendo(new HashSet<>());
    }

    @Test
    void testObtenerUsuarioPorId_Existe() {
        when(usuarioRepository.findById(1)).thenReturn(Optional.of(usuario1));

        Optional<Usuario> result = usuarioServicio.obtenerUsuarioPorId(1);

        assertTrue(result.isPresent());
        assertEquals(1, result.get().getId());
    }

    @Test
    void testObtenerUsuarioPorId_NoExiste() {
        when(usuarioRepository.findById(3)).thenReturn(Optional.empty());

        Optional<Usuario> result = usuarioServicio.obtenerUsuarioPorId(3);

        assertFalse(result.isPresent());
    }

    @Test
    void testCrearUsuario() {
        when(usuarioRepository.save(usuario1)).thenReturn(usuario1);

        Usuario result = usuarioServicio.crearUsuario(usuario1);

        assertEquals(usuario1, result);
    }

    @Test
    void testObtenerUsuarioPorUsername_Existe() {
        when(usuarioRepository.findByNickname("usuario1")).thenReturn(Optional.of(usuario1));

        Optional<Usuario> result = usuarioServicio.obtenerUsuarioPorUsername("usuario1");

        assertTrue(result.isPresent());
        assertEquals("usuario1", result.get().getNickname());
    }

    @Test
    void testObtenerUsuarioPorUsername_NoExiste() {
        when(usuarioRepository.findByNickname("noexiste")).thenReturn(Optional.empty());

        Optional<Usuario> result = usuarioServicio.obtenerUsuarioPorUsername("noexiste");

        assertFalse(result.isPresent());
    }

    @Test
    void testObtenerUsuarioPorEmail_Existe() {
        when(usuarioRepository.findByEmail("user1@example.com")).thenReturn(Optional.of(usuario1));

        Optional<Usuario> result = usuarioServicio.obtenerUsuarioPorEmail("user1@example.com");

        assertTrue(result.isPresent());
        assertEquals("user1@example.com", result.get().getEmail());
    }

    @Test
    void testObtenerUsuarioPorEmail_NoExiste() {
        when(usuarioRepository.findByEmail("no@no.com")).thenReturn(Optional.empty());

        Optional<Usuario> result = usuarioServicio.obtenerUsuarioPorEmail("no@no.com");

        assertFalse(result.isPresent());
    }

    @Test
    void testObtenerTodos() {
        List<Usuario> usuarios = Arrays.asList(usuario1, usuario2);
        when(usuarioRepository.findAll()).thenReturn(usuarios);

        List<Usuario> result = usuarioServicio.obtenerTodos();

        assertEquals(2, result.size());
        assertTrue(result.contains(usuario1));
        assertTrue(result.contains(usuario2));
    }

    @Test
    void testSeguirUsuario_Success() {
        when(usuarioRepository.findById(1)).thenReturn(Optional.of(usuario1));
        when(usuarioRepository.findById(2)).thenReturn(Optional.of(usuario2));
        when(usuarioRepository.save(usuario1)).thenReturn(usuario1);

        boolean result = usuarioServicio.seguirUsuario(1, 2);

        assertTrue(result);
        assertTrue(usuario1.getSiguiendo().contains(usuario2));
    }

    @Test
    void testSeguirUsuario_YaSigue() {
        usuario1.getSiguiendo().add(usuario2);

        when(usuarioRepository.findById(1)).thenReturn(Optional.of(usuario1));
        when(usuarioRepository.findById(2)).thenReturn(Optional.of(usuario2));

        boolean result = usuarioServicio.seguirUsuario(1, 2);

        assertFalse(result);
    }

    @Test
    void testSeguirUsuario_A_SiMismo() {
        when(usuarioRepository.findById(1)).thenReturn(Optional.of(usuario1));

        boolean result = usuarioServicio.seguirUsuario(1, 1);

        assertFalse(result);
    }

    @Test
    void testSeguirUsuario_UsuarioNoExiste() {
        when(usuarioRepository.findById(1)).thenReturn(Optional.of(usuario1));
        when(usuarioRepository.findById(99)).thenReturn(Optional.empty());

        boolean result = usuarioServicio.seguirUsuario(1, 99);

        assertFalse(result);
    }

    @Test
    void testDejarDeSeguirUsuario_Success() {
        usuario1.getSiguiendo().add(usuario2);

        when(usuarioRepository.findById(1)).thenReturn(Optional.of(usuario1));
        when(usuarioRepository.findById(2)).thenReturn(Optional.of(usuario2));

        boolean result = usuarioServicio.dejarDeSeguirUsuario(1, 2);

        assertTrue(result);
        assertFalse(usuario1.getSiguiendo().contains(usuario2));
    }

    @Test
    void testDejarDeSeguirUsuario_NoLoSigue() {
        when(usuarioRepository.findById(1)).thenReturn(Optional.of(usuario1));
        when(usuarioRepository.findById(2)).thenReturn(Optional.of(usuario2));

        boolean result = usuarioServicio.dejarDeSeguirUsuario(1, 2);

        assertFalse(result);
    }

    @Test
    void testGuardarUsuario() {
        when(usuarioRepository.save(usuario1)).thenReturn(usuario1);

        Usuario result = usuarioServicio.guardarUsuario(usuario1);

        assertEquals(usuario1, result);
    }
}
