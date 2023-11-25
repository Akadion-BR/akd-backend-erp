package br.akd.svc.akadia.modules.global.endereco.dto.request;

import br.akd.svc.akadia.modules.global.endereco.enums.EstadoEnum;
import lombok.*;

import javax.validation.constraints.*;

@Builder
@ToString
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EnderecoRequest {
    @NotEmpty(message = "O campo logradouro deverá ser informado")
    @Size(max = 100, message = "O campo logradouro deve conter no máximo {max} caracteres")
    private String logradouro;

    @NotNull(message = "O campo número deverá ser informado")
    @Min(value = 1, message = "O valor mínimo para o campo número é {value}")
    @Max(value = 99999, message = "O valor máximo para o campo número é {value}")
    private Integer numero;

    @Size(max = 80, message = "O campo bairro deve conter no máximo {max} caracteres")
    private String bairro;

    @Pattern(regexp = "\\d{8}", message = "O campo CEP deve conter 8 caracteres numéricos")
    private String codigoPostal;

    @Size(max = 80, message = "O campo cidade deve conter no máximo {max} caracteres")
    private String cidade;

    @Size(max = 100, message = "O campo complemento deve conter no máximo {max} caracteres")
    private String complemento;

    @NotNull(message = "O estado deverá ser informado")
    private EstadoEnum estado;
}
