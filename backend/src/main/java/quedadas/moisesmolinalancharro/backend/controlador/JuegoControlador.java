package quedadas.moisesmolinalancharro.backend.controlador;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import quedadas.moisesmolinalancharro.backend.dto.JuegoDto;
import quedadas.moisesmolinalancharro.backend.entidad.Juego;
import quedadas.moisesmolinalancharro.backend.servicio.JuegoServicio;

import java.util.List;

@RestController
@RequestMapping("juegos")
public class JuegoControlador {

    @Autowired
    private JuegoServicio juegoServicio;

    @GetMapping
    public ResponseEntity<List<JuegoDto>> getAllJuegos() {
        List<Juego> juegos = this.juegoServicio.obtenerTodosLosJuegos();

        List<JuegoDto> resultados = juegos.stream()
                .map(JuegoDto::buildFromEntity)
                .toList();

        return new ResponseEntity<>(resultados, HttpStatus.OK);
    }

}
