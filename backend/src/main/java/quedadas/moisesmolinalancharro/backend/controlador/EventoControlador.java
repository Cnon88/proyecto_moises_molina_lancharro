package quedadas.moisesmolinalancharro.backend.controlador;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticatedPrincipal;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import quedadas.moisesmolinalancharro.backend.dto.CrearEventoDto;
import quedadas.moisesmolinalancharro.backend.dto.EventoDto;
import quedadas.moisesmolinalancharro.backend.entidad.Evento;
import quedadas.moisesmolinalancharro.backend.entidad.Juego;
import quedadas.moisesmolinalancharro.backend.servicio.EventoServicio;
import quedadas.moisesmolinalancharro.backend.servicio.JuegoServicio;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("eventos")
public class EventoControlador {

    @Autowired
    private EventoServicio eventoServicio;

    @Autowired
    private JuegoServicio juegoServicio;

    @GetMapping
    public ResponseEntity<List<EventoDto>> obtenerTodosLosEventos() {
        List<Evento> eventos = this.eventoServicio.obtenerTodos();
        List<EventoDto> resultados = eventos.stream()
                .map(EventoDto::buildFromEntity)
                .toList();

        return new ResponseEntity<>(resultados, HttpStatus.OK);
    }

    @GetMapping("{id}")
    public ResponseEntity<EventoDto> obtenerEventoPorId(@PathVariable(name = "id") Integer id) {

        Optional<Evento> optionalEvento = this.eventoServicio.obtenerPorId(id);
        if (optionalEvento.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        Evento evento = optionalEvento.get();
        EventoDto resultado = EventoDto.buildFromEntity(evento);
        return new ResponseEntity<>(resultado, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<EventoDto> crearEvento(@AuthenticationPrincipal UserDetails principal,
                                                 @RequestBody CrearEventoDto dto) {

        Optional<Juego> optJuego = this.juegoServicio.obtenerPorId(dto.getJuegoId());
        if (optJuego.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }

        Evento eventoACrear = dto.toEntity(optJuego.get());
        String email = principal.getUsername();
        Evento eventoCreado = this.eventoServicio.crearEvento(eventoACrear, email);

        EventoDto resultado = EventoDto.buildFromEntity(eventoCreado);
        return new ResponseEntity<>(resultado, HttpStatus.CREATED);
    }

}
