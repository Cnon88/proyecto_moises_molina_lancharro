package quedadas.moisesmolinalancharro.backend.entidad;


import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "eventos")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
@EqualsAndHashCode(of = "id")
public class Evento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "titulo", nullable = false, length = 100)
    private String titulo;

    @Column(name = "descripcion", columnDefinition = "TEXT")
    private String descripcion;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "juego_id", nullable = false)
    private Juego juego;

    @Column(name = "latitud", precision = 9, scale = 6)
    private BigDecimal latitud;

    @Column(name = "longitud", precision = 9, scale = 6)
    private BigDecimal longitud;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "creador", referencedColumnName = "id")
    private Usuario creador;

    @Column(name = "min_personas", nullable = false)
    private Integer minPersonas;

    @Column(name = "max_personas", nullable = false)
    private Integer maxPersonas;

    @Column(name = "fecha_inicio_inscripciones", nullable = false)
    private LocalDateTime fechaInicioInscripciones;

    @Column(name = "fecha_fin_inscripciones")
    private LocalDateTime fechaFinInscripciones;

    @Column(name = "fecha_evento", nullable = false)
    private LocalDateTime fechaEvento;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado", nullable = false)
    private EstadoEvento estado;

    // Participantes del evento
    @ManyToMany(mappedBy = "eventosComoParticipante")
    @Builder.Default
    private Set<Usuario> participantes = new HashSet<>();

    public enum EstadoEvento {
        PENDIENTE,
        LLENO,
        FINALIZADO
    }
}
