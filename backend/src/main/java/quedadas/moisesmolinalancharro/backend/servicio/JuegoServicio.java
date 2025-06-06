package quedadas.moisesmolinalancharro.backend.servicio;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import quedadas.moisesmolinalancharro.backend.entidad.Juego;
import quedadas.moisesmolinalancharro.backend.repositorio.JuegoRepository;

import java.util.List;
import java.util.Optional;

@Service
public class JuegoServicio {

    @Autowired
    private JuegoRepository juegoRepository;

    public List<Juego> obtenerTodosLosJuegos() {
        List<Juego> juegos = this.juegoRepository.findAll();
        return juegos;
    }

    public Optional<Juego> obtenerPorId(int id) {
        return this.juegoRepository.findById(id);
    }

}
