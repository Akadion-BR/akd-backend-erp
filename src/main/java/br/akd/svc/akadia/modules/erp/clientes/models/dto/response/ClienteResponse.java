package br.akd.svc.akadia.modules.erp.clientes.models.dto.response;

import br.akd.svc.akadia.modules.erp.clientes.models.entity.ClienteEntity;
import br.akd.svc.akadia.modules.global.endereco.dto.response.EnderecoResponse;
import br.akd.svc.akadia.modules.global.telefone.response.TelefoneResponse;
import lombok.*;
import lombok.extern.slf4j.Slf4j;

import java.util.UUID;

@Slf4j
@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ClienteResponse {
    private UUID id;
    private String dataCadastro;
    private String horaCadastro;
    private String dataNascimento;
    private String nome;
    private String cpfCnpj;
    private String inscricaoEstadual;
    private String email;
    private String nomeResponsavel;
    private String statusCliente;
    private String tipoPessoa;
    private EnderecoResponse endereco;
    private TelefoneResponse telefone;

    public ClienteResponse buildFromEntity(ClienteEntity clienteEntity) {
        log.info("Método de conversão de objeto do tipo ClienteEntity para objeto do tipo ClienteResponse acessado");

        log.info("Iniciando construção do objeto ClienteResponse...");
        ClienteResponse clienteResponse = ClienteResponse.builder()
                .id(clienteEntity.getId())
                .dataCadastro(clienteEntity.getDataCadastro())
                .horaCadastro(clienteEntity.getHoraCadastro())
                .dataNascimento(clienteEntity.getDataNascimento())
                .nome(clienteEntity.getNome())
                .cpfCnpj(clienteEntity.getCpfCnpj())
                .inscricaoEstadual(clienteEntity.getInscricaoEstadual())
                .email(clienteEntity.getEmail())
                .nomeResponsavel(clienteEntity.getNomeResponsavel())
                .statusCliente(clienteEntity.getStatusCliente().toString())
                .tipoPessoa(clienteEntity.getTipoPessoa().toString())
                .endereco(new EnderecoResponse().buildFromEntity(clienteEntity.getEndereco()))
                .telefone(new TelefoneResponse().buildFromEntity(clienteEntity.getTelefone()))
                .build();
        log.debug("Objeto ClienteResponse buildado com sucesso. Retornando...");
        return clienteResponse;
    }
}
