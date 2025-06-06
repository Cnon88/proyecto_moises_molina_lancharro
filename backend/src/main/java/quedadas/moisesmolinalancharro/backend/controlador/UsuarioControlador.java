package quedadas.moisesmolinalancharro.backend.controlador;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import quedadas.moisesmolinalancharro.backend.dto.EventoDto;
import quedadas.moisesmolinalancharro.backend.dto.UsuarioDto;
import quedadas.moisesmolinalancharro.backend.entidad.Evento;
import quedadas.moisesmolinalancharro.backend.entidad.Usuario;
import quedadas.moisesmolinalancharro.backend.servicio.UsuarioServicio;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("usuarios")
public class UsuarioControlador {

    @Autowired
    private UsuarioServicio usuarioServicio;

    @GetMapping
    public ResponseEntity<List<UsuarioDto>> obtenerTodosLosUsuarios() {
        List<Usuario> usuarios = this.usuarioServicio.obtenerTodos();
        List<UsuarioDto> resultados = usuarios.stream()
                .map(UsuarioDto::buildFromEntity)
                .toList();

        return new ResponseEntity<>(resultados, HttpStatus.OK);
    }

    @GetMapping("{id}")
    public ResponseEntity<UsuarioDto> obtenerUsuarioPorId(@PathVariable(name = "id") Integer id) {

        Optional<Usuario> optionalUsuario = this.usuarioServicio.obtenerUsuarioPorId(id);
        if (optionalUsuario.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        Usuario usuario = optionalUsuario.get();
        UsuarioDto resultado = UsuarioDto.buildFromEntity(usuario);
        return new ResponseEntity<>(resultado, HttpStatus.OK);
    }

    @GetMapping("{id}/siguiendo")
    public ResponseEntity<Set<UsuarioDto>> obtenerUsuariosQueSigueUnUsuario(@PathVariable(name = "id") Integer id) {
        Optional<Usuario> optionalUsuario = this.usuarioServicio.obtenerUsuarioPorId(id);
        if (optionalUsuario.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        Usuario usuario = optionalUsuario.get();
        Set<Usuario> siguiendo = usuario.getSiguiendo();

        Set<UsuarioDto> resultados = siguiendo.stream()
                .map(UsuarioDto::buildFromEntity)
                .collect(Collectors.toUnmodifiableSet());

        return new ResponseEntity<>(resultados, HttpStatus.OK);
    }

    @GetMapping("{id}/seguidores")
    public ResponseEntity<Set<UsuarioDto>> obtenerSeguidoresDeUnUsuario(@PathVariable(name = "id") Integer id) {
        Optional<Usuario> optionalUsuario = this.usuarioServicio.obtenerUsuarioPorId(id);
        if (optionalUsuario.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        Usuario usuario = optionalUsuario.get();
        Set<Usuario> seguidores = usuario.getSeguidores();

        Set<UsuarioDto> resultados = seguidores.stream()
                .map(UsuarioDto::buildFromEntity)
                .collect(Collectors.toUnmodifiableSet());

        return new ResponseEntity<>(resultados, HttpStatus.OK);
    }

}
