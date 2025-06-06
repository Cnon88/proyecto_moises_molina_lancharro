package quedadas.moisesmolinalancharro.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import quedadas.moisesmolinalancharro.backend.entidad.Usuario;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UsuarioDto {

    private int id;
    private String nickname;
    private String email;
    private BigDecimal latitudHogar;
    private BigDecimal longitudHogar;

    public static UsuarioDto buildFromEntity(Usuario u) {
        UsuarioDto dto = new UsuarioDtoBuilder()
                .id(u.getId())
                .nickname(u.getNickname())
                .email(u.getEmail())
                .latitudHogar(u.getLatitudHogar())
                .longitudHogar(u.getLongitudHogar())
                .build();

        return dto;
    }

}
