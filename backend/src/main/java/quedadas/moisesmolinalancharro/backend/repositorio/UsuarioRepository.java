package quedadas.moisesmolinalancharro.backend.repositorio;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import quedadas.moisesmolinalancharro.backend.entidad.Usuario;

import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Integer> {

    Optional<Usuario> findByNickname(String nickname);
    Optional<Usuario> findByEmail(String email);


}
