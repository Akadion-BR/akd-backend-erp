package br.akd.svc.akadia.modules.global.objects.telefone.request;

import br.akd.svc.akadia.modules.global.objects.telefone.enums.TipoTelefoneEnum;
import lombok.*;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Data
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class TelefoneRequest {

    @NotNull(message = "O prefixo do telefone deverá ser informado")
    @Min(value = 10, message = "O prefixo do telefone deve conter 2 caracteres numéricos")
    @Max(value = 99, message = "O prefixo do telefone deve conter 2 caracteres numéricos")
    private Integer prefixo;

    @NotNull(message = "O número do telefone deverá ser informado")
    @Min(value = 8, message = "O número do telefone deve conter no mínimo {value} caracteres numéricos")
    @Min(value = 9, message = "O número do telefone deve conter no máximo {value} caracteres numéricos")
    private Integer numero;

    @NotNull(message = "O tipo do telefone deverá ser informado")
    private TipoTelefoneEnum tipoTelefone;

    public String obtemPrefixoComNumeroJuntos() {
        return this.getPrefixo().toString() + this.getNumero().toString();
    }
}
