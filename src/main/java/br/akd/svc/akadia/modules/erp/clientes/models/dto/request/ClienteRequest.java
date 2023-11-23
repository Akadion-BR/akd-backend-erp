package br.akd.svc.akadia.modules.erp.clientes.models.dto.request;

import br.akd.svc.akadia.modules.erp.clientes.models.enums.StatusClienteEnum;
import br.akd.svc.akadia.modules.erp.clientes.models.enums.TipoPessoaEnum;
import br.akd.svc.akadia.modules.global.endereco.dto.request.EnderecoRequest;
import br.akd.svc.akadia.modules.global.telefone.request.TelefoneRequest;
import lombok.*;

@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ClienteRequest {
    private Long id;
    private String dataNascimento;
    private String nome;
    private String cpfCnpj;
    private String inscricaoEstadual;
    private String email;
    private StatusClienteEnum statusCliente;
    private TipoPessoaEnum tipoPessoa;
    private EnderecoRequest endereco;
    private TelefoneRequest telefone;
}
