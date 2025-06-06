package quedadas.moisesmolinalancharro.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import quedadas.moisesmolinalancharro.backend.entidad.Evento;
import quedadas.moisesmolinalancharro.backend.entidad.Juego;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Set;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EventoDto {

    private Integer id;
    private String titulo;
    private String descripcion;
    private JuegoDto juego;
    private BigDecimal latitud;
    private BigDecimal longitud;
    private UsuarioDto creador;
    private Integer minPersonas;
    private Integer maxPersonas;
    private LocalDateTime fechaInicioInscripciones;
    private LocalDateTime fechaFinInscripciones;
    private LocalDateTime fechaEvento;
    private Evento.EstadoEvento estado;
    private Set<UsuarioDto> participantes;

    public enum EstadoEvento {
        PENDIENTE,
        LLENO,
        FINALIZADO
    }

    public static EventoDto buildFromEntity(Evento e) {

        EventoDto dto = new EventoDtoBuilder()
                .id(e.getId())
                .titulo(e.getTitulo())
                .descripcion(e.getDescripcion())
                .juego(JuegoDto.buildFromEntity(e.getJuego()))
                .latitud(e.getLatitud())
                .longitud(e.getLongitud())
                .creador(UsuarioDto.buildFromEntity(e.getCreador()))
                .minPersonas(e.getMinPersonas())
                .maxPersonas(e.getMaxPersonas())
                .fechaInicioInscripciones(e.getFechaInicioInscripciones())
                .fechaFinInscripciones(e.getFechaFinInscripciones())
                .fechaEvento(e.getFechaEvento())
                .estado(e.getEstado())
                .participantes(e.getParticipantes().stream().map(UsuarioDto::buildFromEntity).collect(Collectors.toUnmodifiableSet()))
                .build();

        return dto;

    }


}
