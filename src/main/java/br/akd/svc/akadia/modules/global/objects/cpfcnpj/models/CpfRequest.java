package br.akd.svc.akadia.modules.global.objects.cpfcnpj.models;

import lombok.*;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Setter
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class CpfRequest {
    @NotEmpty(message = "O CPF não pode ser vazio")
    @Size(message = "O campo CPF deve ter no máximo {max} caracteres", max = 14)
    @Pattern(regexp = "\\d{3}\\.\\d{3}\\.\\d{3}-\\d{2}", message = "O CPF informado é inválido")
    private String cpf;
}
