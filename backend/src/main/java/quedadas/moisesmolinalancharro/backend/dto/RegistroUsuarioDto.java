package quedadas.moisesmolinalancharro.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RegistroUsuarioDto {

    private String username;
    private String email;
    private String password;
    private BigDecimal lat;
    private BigDecimal lng;

}
