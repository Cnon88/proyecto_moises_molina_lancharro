package quedadas.moisesmolinalancharro.backend.controlador;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import quedadas.moisesmolinalancharro.backend.dto.LoginDto;
import quedadas.moisesmolinalancharro.backend.dto.LoginResponseDto;
import quedadas.moisesmolinalancharro.backend.dto.RegistroUsuarioDto;
import quedadas.moisesmolinalancharro.backend.dto.UsuarioDto;
import quedadas.moisesmolinalancharro.backend.entidad.Usuario;
import quedadas.moisesmolinalancharro.backend.excepciones.*;
import quedadas.moisesmolinalancharro.backend.servicio.AutenticacionServicio;
import quedadas.moisesmolinalancharro.backend.servicio.JWTServicio;

import java.util.Map;

@RestController
@RequestMapping("autenticacion")
public class AutenticacionController {

    @Autowired
    private AutenticacionServicio authServicio;

    @Autowired
    private JWTServicio jwtServicio;

    @PostMapping("login")
    public ResponseEntity<LoginResponseDto> authenticate(@RequestBody LoginDto loginUserDto) {
        Usuario authenticatedUser = authServicio.loginUsuario(loginUserDto.getEmail(), loginUserDto.getPassword());
        String jwtToken = jwtServicio.generateToken(authenticatedUser);

        LoginResponseDto loginResponse = new LoginResponseDto(jwtToken, jwtServicio.getExpirationMillisFromToken(jwtToken));
        return ResponseEntity.ok(loginResponse);
    }

    @PostMapping("registro")
    public ResponseEntity<?> registrarUsuario(@RequestBody RegistroUsuarioDto dto) {

        UsuarioDto resultado = null;

        try {
            Usuario usuarioCreado = authServicio.registrarUsuario(dto.getUsername(), dto.getEmail(), dto.getPassword(), dto.getLat(), dto.getLng());
            resultado = UsuarioDto.buildFromEntity(usuarioCreado);
        } catch (UsuarioExistentePorNombreException | UsuarioExistentePorEmailException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(Map.of("error", e.getMessage()));
        } catch (ContrasenaInvalidaException | CoordenadasInvalidasException | NombreDeUsuarioInvalidoException |
                 EmailInvalidoException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", e.getMessage()));
        }

        return new ResponseEntity<>(resultado, HttpStatus.CREATED);
    }

}
