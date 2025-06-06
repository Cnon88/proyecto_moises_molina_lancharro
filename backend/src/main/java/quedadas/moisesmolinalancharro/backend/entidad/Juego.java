package quedadas.moisesmolinalancharro.backend.entidad;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "juegos")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Juego {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "nombre", length = 100, unique = true, nullable = false)
    private String nombre;

    @Column(name = "min_jugadores", nullable = false)
    private int minJugadores;

    @Column(name = "max_jugadores", nullable = false)
    private int maxJugadores;

    @Column(name = "tiempo_estimado", nullable = false)
    private int tiempoEstimadoEnMinutos;


}
