package br.akd.svc.akadia.modules.web.clientesistema.models.dto.response;

import br.akd.svc.akadia.modules.global.endereco.dto.response.EnderecoResponse;
import br.akd.svc.akadia.modules.global.telefone.response.TelefoneResponse;
import br.akd.svc.akadia.modules.web.plano.models.dto.response.PlanoResponse;
import lombok.*;

import java.util.UUID;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ClienteSistemaResponse {
    private UUID id;
    private String codigoClienteAsaas;
    private String dataCadastro;
    private String horaCadastro;
    private String dataNascimento;
    private String email;
    private String nome;
    private String senha;
    private String cpf;
    private Double saldo;
    private PlanoResponse plano;
    private TelefoneResponse telefone;
    private EnderecoResponse endereco;
}
