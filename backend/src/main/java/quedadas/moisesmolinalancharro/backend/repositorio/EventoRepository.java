package quedadas.moisesmolinalancharro.backend.repositorio;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import quedadas.moisesmolinalancharro.backend.entidad.Evento;

@Repository
public interface EventoRepository extends JpaRepository<Evento, Integer> {

}
