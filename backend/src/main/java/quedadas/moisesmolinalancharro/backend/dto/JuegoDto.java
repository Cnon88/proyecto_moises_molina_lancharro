package quedadas.moisesmolinalancharro.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import quedadas.moisesmolinalancharro.backend.entidad.Juego;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class JuegoDto {

    private int id;
    private String nombre;
    private int minJugadores;
    private int maxJugadores;
    private int tiempoEstimadoEnMinutos;

    public static JuegoDto buildFromEntity(Juego j) {

        JuegoDto dto = new JuegoDtoBuilder()
                .id(j.getId())
                .nombre(j.getNombre())
                .minJugadores(j.getMinJugadores())
                .maxJugadores(j.getMaxJugadores())
                .tiempoEstimadoEnMinutos(j.getTiempoEstimadoEnMinutos())
                .build();

        return dto;
    }

}
