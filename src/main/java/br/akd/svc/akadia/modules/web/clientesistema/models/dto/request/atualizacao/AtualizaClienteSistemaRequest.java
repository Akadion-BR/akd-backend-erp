package br.akd.svc.akadia.modules.web.clientesistema.models.dto.request.atualizacao;

import br.akd.svc.akadia.modules.global.endereco.dto.request.EnderecoRequest;
import br.akd.svc.akadia.modules.global.telefone.request.TelefoneRequest;
import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class AtualizaClienteSistemaRequest {
    //TODO JAVAX VALIDATOR
    private String dataNascimento;
    private String email;
    private String nome;
    private String senha;
    private String cpf;
    private TelefoneRequest telefone;
    private EnderecoRequest endereco;
}
