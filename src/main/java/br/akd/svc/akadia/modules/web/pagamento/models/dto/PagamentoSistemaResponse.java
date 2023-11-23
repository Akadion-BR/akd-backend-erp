package br.akd.svc.akadia.modules.web.pagamento.models.dto;

import br.akd.svc.akadia.modules.web.pagamento.models.enums.FormaPagamentoSistemaEnum;
import br.akd.svc.akadia.modules.web.pagamento.models.enums.StatusPagamentoSistemaEnum;
import lombok.*;

import java.util.UUID;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class PagamentoSistemaResponse {
    private UUID id;
    private String dataCadastro;
    private String horaCadastro;
    private String dataPagamento;
    private String horaPagamento;
    private String dataVencimento;
    private Double valor;
    private Double valorLiquido;
    private String descricao;
    private FormaPagamentoSistemaEnum formaPagamentoSistemaEnum;
    private StatusPagamentoSistemaEnum statusPagamentoSistemaEnum;
}
