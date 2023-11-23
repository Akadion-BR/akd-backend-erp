package br.akd.svc.akadia.modules.erp.despesas.models.dto.request;

import br.akd.svc.akadia.modules.erp.despesas.models.enums.StatusDespesaEnum;
import br.akd.svc.akadia.modules.erp.despesas.models.enums.TipoDespesaEnum;
import lombok.*;

@Setter
@Getter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class DespesaRequest {
    //TODO JAVAX VALIDATOR
    private String dataPagamento;
    private String dataAgendamento;
    private String descricao;
    private Double valor;
    private Integer qtdRecorrencias;
    private StatusDespesaEnum statusDespesa;
    private TipoDespesaEnum tipoDespesa;
}
