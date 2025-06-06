package quedadas.moisesmolinalancharro.backend.entidad;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "usuarios")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString(exclude = {"siguiendo", "seguidores", "eventosComoParticipante", "eventosComoCreador" })
@EqualsAndHashCode(of = "id")
public class Usuario implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "nombre_usuario", length = 100, nullable = false, unique = true)
    private String nickname;

    @Column(name = "correo_electronico", length = 100, nullable = false, unique = true)
    private String email;

    @Column(name = "contrasena", length = 255, nullable = false)
    private String password;

    @Column(name = "latitud_hogar", precision = 9, scale = 6)
    private BigDecimal latitudHogar;

    @Column(name = "longitud_hogar", precision = 9, scale = 6)
    private BigDecimal longitudHogar;

    // Relación de seguimiento: usuarios que sigo
    @ManyToMany
    @JoinTable(
            name = "usuarios_siguiendo",
            joinColumns = @JoinColumn(name = "id_usuario"),
            inverseJoinColumns = @JoinColumn(name = "id_usuario_siguiendo")
    )
    @Builder.Default
    private Set<Usuario> siguiendo = new HashSet<>();

    // Relación inversa: usuarios que me siguen
    @ManyToMany(mappedBy = "siguiendo")
    @Builder.Default
    private Set<Usuario> seguidores = new HashSet<>();

    // Eventos en los que participa el usuario
    @ManyToMany
    @JoinTable(
            name = "eventos_usuarios",
            joinColumns = @JoinColumn(name = "id_usuario"),
            inverseJoinColumns = @JoinColumn(name = "id_evento")
    )
    @Builder.Default
    private Set<Evento> eventosComoParticipante = new HashSet<>();

    // Eventos que ha creado el usuario
    @OneToMany(mappedBy = "creador")
    @Builder.Default
    private Set<Evento> eventosComoCreador = new HashSet<>();

    /* Autenticacion */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of();
    }

    // OJO, Este metodo es de spring del UserDetails y debe
    // devolver el campo por el que autenticamos, en mi caso el email
    @Override
    public String getUsername() {
        return this.email;
    }
    /* FIN Autenticacion */
}
