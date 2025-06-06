package quedadas.moisesmolinalancharro.backend.repositorio;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import quedadas.moisesmolinalancharro.backend.entidad.Juego;

@Repository
public interface JuegoRepository extends JpaRepository<Juego, Integer> {
}
