package br.akd.svc.akadia.modules.erp.despesas.models.dto.response;

import br.akd.svc.akadia.modules.erp.despesas.models.entity.DespesaEntity;
import lombok.*;

import java.util.UUID;

@Setter
@Getter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class DespesaResponse {
    private UUID id;
    private String dataCadastro;
    private String horaCadastro;
    private String dataPagamento;
    private String dataAgendamento;
    private String descricao;
    private Double valor;
    private String observacao;
    private Integer qtdRecorrencias;
    private String statusDespesa;
    private String tipoDespesa;
    private String tipoRecorrencia;

    public DespesaResponse buildFromEntity(DespesaEntity despesaEntity) {
        return despesaEntity != null
                ? DespesaResponse.builder()
                .id(despesaEntity.getId())
                .dataCadastro(despesaEntity.getDataCadastro())
                .horaCadastro(despesaEntity.getHoraCadastro())
                .dataPagamento(despesaEntity.getDataPagamento())
                .dataAgendamento(despesaEntity.getDataAgendamento())
                .descricao(despesaEntity.getDescricao())
                .valor(despesaEntity.getValor())
                .observacao(despesaEntity.getObservacao())
                .qtdRecorrencias(despesaEntity.getRecorrencias().size())
                .statusDespesa(despesaEntity.getStatusDespesa().toString())
                .tipoDespesa(despesaEntity.getTipoDespesa().toString())
                .tipoRecorrencia(despesaEntity.getTipoRecorrencia().toString())
                .build()
                : null;
    }
}
