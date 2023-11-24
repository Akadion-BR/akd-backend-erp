package br.akd.svc.akadia.modules.web.clientesistema.models.dto.request.criacao;

import br.akd.svc.akadia.modules.global.endereco.dto.request.EnderecoRequest;
import br.akd.svc.akadia.modules.global.telefone.request.TelefoneRequest;
import br.akd.svc.akadia.modules.web.plano.models.dto.request.PlanoRequest;
import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ClienteSistemaRequest {
    //TODO JAVAX VALIDATOR
    private String dataNascimento;
    private String email;
    private String nome;
    private String senha;
    private String cpf;
    private PlanoRequest plano;
    private TelefoneRequest telefone;
    private EnderecoRequest endereco;
}
