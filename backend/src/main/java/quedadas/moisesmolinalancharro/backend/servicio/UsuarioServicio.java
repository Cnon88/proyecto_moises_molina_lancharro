package quedadas.moisesmolinalancharro.backend.servicio;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import quedadas.moisesmolinalancharro.backend.entidad.Evento;
import quedadas.moisesmolinalancharro.backend.entidad.Usuario;
import quedadas.moisesmolinalancharro.backend.repositorio.UsuarioRepository;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class UsuarioServicio {

    @Autowired
    private UsuarioRepository usuarioRepository;

    public Optional<Usuario> obtenerUsuarioPorId(int id) {
        return this.usuarioRepository.findById(id);
    }

    public Usuario crearUsuario(Usuario u) {
         return usuarioRepository.save(u);
    }

    public Optional<Usuario> obtenerUsuarioPorUsername(String username) {
        return usuarioRepository.findByNickname(username);
    }

    public Optional<Usuario> obtenerUsuarioPorEmail(String email) {
        return usuarioRepository.findByEmail(email);
    }

    public List<Usuario> obtenerTodos() {
        return this.usuarioRepository.findAll();
    }

    public boolean seguirUsuario(int idSeguidor, int idSeguido) {
        Optional<Usuario> optSeguidor = usuarioRepository.findById(idSeguidor);
        Optional<Usuario> optSeguido = usuarioRepository.findById(idSeguido);

        if (optSeguidor.isPresent() && optSeguido.isPresent()) {
            Usuario seguidor = optSeguidor.get();
            Usuario seguido = optSeguido.get();

            // Evitar seguirse a sí mismo o duplicados
            if (!seguidor.equals(seguido) && !seguidor.getSiguiendo().contains(seguido)) {
                seguidor.getSiguiendo().add(seguido);
                usuarioRepository.save(seguidor);
                return true;
            }
        }
        return false;
    }

    public boolean dejarDeSeguirUsuario(int idSeguidor, int idSeguido) {
        Optional<Usuario> optSeguidor = usuarioRepository.findById(idSeguidor);
        Optional<Usuario> optSeguido = usuarioRepository.findById(idSeguido);

        if (optSeguidor.isPresent() && optSeguido.isPresent()) {
            Usuario seguidor = optSeguidor.get();
            Usuario seguido = optSeguido.get();

            if (seguidor.getSiguiendo().contains(seguido)) {
                seguidor.getSiguiendo().remove(seguido);
                usuarioRepository.save(seguidor);
                return true;
            }
        }
        return false;
    }

    public Usuario guardarUsuario(Usuario usuario) {
        return usuarioRepository.save(usuario);
    }

}
