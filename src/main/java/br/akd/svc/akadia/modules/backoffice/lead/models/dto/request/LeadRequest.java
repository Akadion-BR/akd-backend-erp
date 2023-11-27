package br.akd.svc.akadia.modules.backoffice.lead.models.dto.request;

import br.akd.svc.akadia.modules.global.telefone.request.TelefoneRequest;
import lombok.*;

import javax.validation.Valid;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class LeadRequest {
    @NotEmpty(message = "O campo nome não pode estar vazio")
    @Size(max = 70, message = "O campo nome deve conter no máximo {max} caracteres")
    private String nome;

    @Email(message = "O e-mail informado é inválido")
    @Size(max = 70, message = "O campo e-mail deve conter no máximo {max} caracteres")
    private String email;

    @Valid
    @NotNull(message = "O telefone deve ser preenchido")
    private TelefoneRequest telefone;
}
