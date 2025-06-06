package quedadas.moisesmolinalancharro.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import quedadas.moisesmolinalancharro.backend.entidad.Evento;
import quedadas.moisesmolinalancharro.backend.entidad.Juego;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CrearEventoDto {

    private String titulo;
    private String descripcion;
    private Integer juegoId;
    private BigDecimal latitud;
    private BigDecimal longitud;
    private Integer minPersonas;
    private Integer maxPersonas;
    private LocalDateTime fechaInicioInscripciones;
    private LocalDateTime fechaFinInscripciones;
    private LocalDateTime fechaEvento;

    public Evento toEntity(Juego juego) {
        Evento e = Evento.builder()
                .titulo(titulo)
                .descripcion(descripcion)
                .juego(juego)
                .latitud(latitud)
                .longitud(longitud)
                .minPersonas(minPersonas)
                .maxPersonas(maxPersonas)
                .fechaInicioInscripciones(fechaInicioInscripciones)
                .fechaFinInscripciones(fechaFinInscripciones)
                .fechaEvento(fechaEvento)
                .build();

        return e;
    }
}
