package br.akd.svc.akadia.modules.web.plano.proxy.operations.criacao.models.request;

import br.akd.svc.akadia.modules.web.pagamento.models.enums.FormaPagamentoSistemaEnum;
import br.akd.svc.akadia.modules.web.plano.proxy.global.request.discount.DiscountRequest;
import br.akd.svc.akadia.modules.web.plano.proxy.global.request.enums.CycleEnum;
import br.akd.svc.akadia.modules.web.plano.proxy.global.request.fine.FineRequest;
import br.akd.svc.akadia.modules.web.plano.proxy.global.request.interest.InterestRequest;
import br.akd.svc.akadia.modules.web.plano.proxy.operations.criacao.models.request.split.SplitRequest;
import lombok.*;

@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class CriaPlanoAsaasRequest {
    private String customer;
    private FormaPagamentoSistemaEnum billingType;
    private Double value;
    private String nextDueDate;
    private DiscountRequest discount;
    private InterestRequest interest;
    private FineRequest fine;
    private CycleEnum cycle;
    private String description;
    private String endDate;
    private String maxPayments;
    private String externalReference;
    private SplitRequest split;
}
