package br.akd.svc.akadia.modules.web.clientesistema.models.dto.response;

import br.akd.svc.akadia.modules.global.endereco.dto.response.EnderecoResponse;
import br.akd.svc.akadia.modules.global.telefone.response.TelefoneResponse;
import br.akd.svc.akadia.modules.web.clientesistema.models.entity.ClienteSistemaEntity;
import br.akd.svc.akadia.modules.web.plano.models.dto.response.PlanoResponse;
import lombok.*;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Slf4j
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

    public ClienteSistemaResponse buildFromEntity(ClienteSistemaEntity clientesSistemaEntity) {
        log.info("Método de conversão de objeto do tipo ClienteEntity para objeto do tipo ClienteResponse acessado");

        log.info("Iniciando construção do objeto ClienteResponse...");
        ClienteSistemaResponse clienteSistemaResponse = ClienteSistemaResponse.builder()
                .id(clientesSistemaEntity.getId())
                .dataCadastro(clientesSistemaEntity.getDataCadastro())
                .horaCadastro(clientesSistemaEntity.getHoraCadastro())
                .dataNascimento(clientesSistemaEntity.getDataNascimento())
                .email(clientesSistemaEntity.getEmail())
                .nome(clientesSistemaEntity.getNome())
                .cpf(clientesSistemaEntity.getCpf())
                .saldo(clientesSistemaEntity.getSaldo())
                .plano(new PlanoResponse().buildFromEntity(clientesSistemaEntity.getPlano()))
                .telefone(new TelefoneResponse().buildFromEntity(clientesSistemaEntity.getTelefone()))
                .endereco(new EnderecoResponse().buildFromEntity(clientesSistemaEntity.getEndereco()))
                .build();
        log.info("Objeto clienteSistemaResponse buildado com sucesso. Retornando...");
        return clienteSistemaResponse;
    }

    public List<ClienteSistemaResponse> buildFromList(List<ClienteSistemaEntity> clienteSistemaEntities) {
        List<ClienteSistemaResponse> clienteSistemaResponses = new ArrayList<>();
        clienteSistemaEntities.forEach(cliente -> clienteSistemaResponses.add(buildFromEntity(cliente)));
        return clienteSistemaResponses;
    }
}
