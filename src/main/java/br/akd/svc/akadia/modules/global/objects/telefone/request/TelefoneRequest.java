package br.akd.svc.akadia.modules.global.objects.telefone.request;

import br.akd.svc.akadia.modules.global.objects.telefone.enums.TipoTelefoneEnum;
import lombok.*;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

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
    @Pattern(regexp = "^(((?:9\\d|[2-9])\\d{3})(\\d{4}))$", message = "O número de telefone é inválido")
    private String numero;

    @NotNull(message = "O tipo do telefone deverá ser informado")
    private TipoTelefoneEnum tipoTelefone;

    public String obtemPrefixoComNumeroJuntos() {
        return this.getPrefixo().toString() + this.getNumero();
    }
}
