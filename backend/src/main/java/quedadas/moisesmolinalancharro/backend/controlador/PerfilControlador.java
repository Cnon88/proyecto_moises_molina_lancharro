package quedadas.moisesmolinalancharro.backend.controlador;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import quedadas.moisesmolinalancharro.backend.dto.EventoDto;
import quedadas.moisesmolinalancharro.backend.dto.UsuarioDto;
import quedadas.moisesmolinalancharro.backend.entidad.Usuario;
import quedadas.moisesmolinalancharro.backend.servicio.EventoServicio;
import quedadas.moisesmolinalancharro.backend.servicio.UsuarioServicio;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("perfil")
public class PerfilControlador {

    @Autowired
    private UsuarioServicio usuarioServicio;

    @Autowired
    private EventoServicio eventoServicio;

    @GetMapping
    public ResponseEntity<UsuarioDto> obtenerPerfil(@AuthenticationPrincipal UserDetails principal) {
        String email = principal.getUsername();
        Optional<Usuario> optionalUsuario = usuarioServicio.obtenerUsuarioPorEmail(email);
        if (optionalUsuario.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        Usuario usuario = optionalUsuario.get();
        UsuarioDto resultado = UsuarioDto.buildFromEntity(usuario);
        return new ResponseEntity<>(resultado, HttpStatus.OK);
    }

    @PostMapping("seguir/{idSeguido}")
    public ResponseEntity<Void> seguirUsuario(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable int idSeguido) {

        String email = userDetails.getUsername(); // UserDetails usa el email
        Optional<Usuario> optSeguidor = usuarioServicio.obtenerUsuarioPorEmail(email);

        if (optSeguidor.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        boolean exito = usuarioServicio.seguirUsuario(optSeguidor.get().getId(), idSeguido);
        return exito
                ? ResponseEntity.ok().build()
                : ResponseEntity.badRequest().build();
    }

    @DeleteMapping("dejar-de-seguir/{idSeguido}")
    public ResponseEntity<String> dejarDeSeguirUsuario(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable int idSeguido) {

        String email = userDetails.getUsername();
        Optional<Usuario> optSeguidor = usuarioServicio.obtenerUsuarioPorEmail(email);

        if (optSeguidor.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        boolean exito = usuarioServicio.dejarDeSeguirUsuario(optSeguidor.get().getId(), idSeguido);
        return exito
                ? ResponseEntity.ok().build()
                : ResponseEntity.badRequest().build();
    }

    @GetMapping("eventos/participando")
    public ResponseEntity<Set<EventoDto>> obtenerEventosComoParticipante(
            @AuthenticationPrincipal UserDetails userDetails) {

        String email = userDetails.getUsername();
        Optional<Usuario> optUsuario = usuarioServicio.obtenerUsuarioPorEmail(email);

        if (optUsuario.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        Usuario usuario = optUsuario.get();
        Set<EventoDto> eventos = usuario.getEventosComoParticipante()
                .stream()
                .map(EventoDto::buildFromEntity)
                .collect(Collectors.toSet());

        return ResponseEntity.ok(eventos);
    }

    @GetMapping("eventos/creados")
    public ResponseEntity<Set<EventoDto>> obtenerEventosComoCreador(
            @AuthenticationPrincipal UserDetails userDetails) {

        String email = userDetails.getUsername();
        Optional<Usuario> optUsuario = usuarioServicio.obtenerUsuarioPorEmail(email);

        if (optUsuario.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        Usuario usuario = optUsuario.get();
        Set<EventoDto> eventos = usuario.getEventosComoCreador()
                .stream()
                .map(EventoDto::buildFromEntity)
                .collect(Collectors.toSet());

        return ResponseEntity.ok(eventos);
    }

    @PostMapping("eventos/inscribirse/{idEvento}")
    public ResponseEntity<Void> inscribirseEnEvento(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable int idEvento) {

        String email = userDetails.getUsername();
        Optional<Usuario> optUsuario = usuarioServicio.obtenerUsuarioPorEmail(email);

        if (optUsuario.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        boolean exito = eventoServicio.inscribirseEnEvento(optUsuario.get().getId(), idEvento);
        return exito
                ? ResponseEntity.ok().build()
                : ResponseEntity.badRequest().build();
    }

    @DeleteMapping("eventos/desinscribirse/{idEvento}")
    public ResponseEntity<Void> desinscribirseDelEvento(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable int idEvento) {

        String email = userDetails.getUsername();
        Optional<Usuario> optUsuario = usuarioServicio.obtenerUsuarioPorEmail(email);

        if (optUsuario.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        boolean exito = eventoServicio.desinscribirseDelEvento(optUsuario.get().getId(), idEvento);
        return exito
                ? ResponseEntity.noContent().build()
                : ResponseEntity.badRequest().build();
    }

    @DeleteMapping("eventos/eliminar/{idEvento}")
    public ResponseEntity<Void> eliminarEvento(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable int idEvento) {

        String email = userDetails.getUsername();
        Optional<Usuario> optUsuario = usuarioServicio.obtenerUsuarioPorEmail(email);

        if (optUsuario.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        boolean exito = eventoServicio.eliminarEventoSiEsCreador(optUsuario.get().getId(), idEvento);
        return exito
                ? ResponseEntity.noContent().build()
                : ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }


}
