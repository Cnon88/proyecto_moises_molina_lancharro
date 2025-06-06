package quedadas.moisesmolinalancharro.backend.servicio;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import quedadas.moisesmolinalancharro.backend.entidad.Usuario;
import quedadas.moisesmolinalancharro.backend.excepciones.*;

import java.math.BigDecimal;
import java.util.Optional;

@Service
public class AutenticacionServicio {

    @Autowired
    private UsuarioServicio usuarioServicio;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthenticationManager authenticationManager;

    public Usuario loginUsuario(String email, String password) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(email, password));

        return usuarioServicio.obtenerUsuarioPorEmail(email).orElseThrow();
    }

    public Usuario registrarUsuario(String username, String email, String contrasena, BigDecimal lat, BigDecimal lng) throws UsuarioExistentePorNombreException, UsuarioExistentePorEmailException, ContrasenaInvalidaException, CoordenadasInvalidasException, NombreDeUsuarioInvalidoException, EmailInvalidoException {

        if (username == null || username.isBlank()) {
            throw new NombreDeUsuarioInvalidoException();
        }

        if (email == null || email.isBlank()) {
            throw new EmailInvalidoException();
        }

        Optional<Usuario> usuario = usuarioServicio.obtenerUsuarioPorUsername(username);
        if (usuario.isPresent()) {
            throw new UsuarioExistentePorNombreException(username);
        }

        usuario = usuarioServicio.obtenerUsuarioPorEmail(email);
        if (usuario.isPresent()) {
            throw new UsuarioExistentePorEmailException(email);
        }

        if (contrasena == null || contrasena.isBlank()) {
            throw new ContrasenaInvalidaException();
        }

        if (lat == null || lng == null) {
            throw new CoordenadasInvalidasException();
        }

        Usuario u = new Usuario();
        u.setNickname(username);
        u.setEmail(email);

        // cifrar password
        String contresenaCifrada = passwordEncoder.encode(contrasena);

        u.setPassword(contresenaCifrada);
        u.setLatitudHogar(lat);
        u.setLongitudHogar(lng);

        return usuarioServicio.crearUsuario(u);
    }

}
