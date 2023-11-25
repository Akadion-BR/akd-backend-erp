package br.akd.svc.akadia.modules.web.clientesistema.models.dto.response;

import br.akd.svc.akadia.modules.global.endereco.dto.response.EnderecoResponse;
import br.akd.svc.akadia.modules.global.telefone.response.TelefoneResponse;
import br.akd.svc.akadia.modules.web.clientesistema.models.entity.ClienteSistemaEntity;
import br.akd.svc.akadia.modules.web.plano.models.dto.response.PlanoResponse;
import lombok.*;

import java.util.UUID;

@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ClienteSistemaResponse {
    private UUID id;
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

    public ClienteSistemaResponse buildFromEntity(ClienteSistemaEntity clienteSistemaEntity) {
        return clienteSistemaEntity != null
                ? ClienteSistemaResponse.builder()
                .id(clienteSistemaEntity.getId())
                .dataCadastro(clienteSistemaEntity.getDataCadastro())
                .horaCadastro(clienteSistemaEntity.getHoraCadastro())
                .dataNascimento(clienteSistemaEntity.getDataNascimento())
                .email(clienteSistemaEntity.getEmail())
                .nome(clienteSistemaEntity.getNome())
                .senha(clienteSistemaEntity.getSenha())
                .cpf(clienteSistemaEntity.getCpf())
                .saldo(clienteSistemaEntity.getSaldo())
                .plano(new PlanoResponse().buildFromEntity(clienteSistemaEntity.getPlano()))
                .telefone(new TelefoneResponse().buildFromEntity(clienteSistemaEntity.getTelefone()))
                .endereco(new EnderecoResponse().buildFromEntity(clienteSistemaEntity.getEndereco()))
                .build()
                : null;
    }
}
