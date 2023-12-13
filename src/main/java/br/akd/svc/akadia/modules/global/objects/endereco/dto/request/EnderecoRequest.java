package br.akd.svc.akadia.modules.global.objects.endereco.dto.request;

import br.akd.svc.akadia.modules.global.objects.endereco.enums.EstadoEnum;
import lombok.*;

import javax.validation.constraints.*;

@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class EnderecoRequest {
    @NotEmpty(message = "O campo logradouro deverá ser informado")
    @Size(max = 100, message = "O campo logradouro deve conter no máximo {max} caracteres")
    private String logradouro;

    @NotNull(message = "O número do endereço deverá ser informado")
    @Min(value = 1, message = "O valor mínimo para o campo número é {value}")
    @Max(value = 9999, message = "O valor máximo para o campo número é {value}")
    private Integer numero;

    @NotEmpty(message = "O campo deverá ser informado")
    @Size(max = 80, message = "O campo bairro deve conter no máximo {max} caracteres")
    private String bairro;

    @NotEmpty(message = "O código postal deverá ser informado")
    @Pattern(regexp = "\\d{5}-\\d{3}", message = "O campo CEP deve conter 8 caracteres numéricos")
    private String codigoPostal;

    @NotEmpty(message = "A cidade deverá ser informada")
    @Size(max = 80, message = "O campo cidade deve conter no máximo {max} caracteres")
    private String cidade;

    @Size(max = 100, message = "O campo complemento deve conter no máximo {max} caracteres")
    private String complemento;

    @NotNull(message = "O estado deverá ser informado")
    private EstadoEnum estado;
}
