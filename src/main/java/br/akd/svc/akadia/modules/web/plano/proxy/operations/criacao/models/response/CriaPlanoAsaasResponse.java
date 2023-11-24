package br.akd.svc.akadia.modules.web.plano.proxy.operations.criacao.models.response;

import br.akd.svc.akadia.modules.web.pagamento.models.enums.FormaPagamentoSistemaEnum;
import br.akd.svc.akadia.modules.web.plano.proxy.global.request.enums.CycleEnum;
import br.akd.svc.akadia.modules.web.plano.proxy.global.response.discount.DiscountResponse;
import br.akd.svc.akadia.modules.web.plano.proxy.global.response.fine.FineResponse;
import br.akd.svc.akadia.modules.web.plano.proxy.global.response.interest.InterestResponse;
import br.akd.svc.akadia.modules.web.plano.proxy.global.response.split.SplitResponse;
import br.akd.svc.akadia.modules.web.plano.proxy.operations.criacao.models.response.enums.StatusEnum;
import lombok.*;

@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class CriaPlanoAsaasResponse {
    private String id;
    private String dateCreated;
    private String customer;
    private String paymentLink;
    private FormaPagamentoSistemaEnum billingType;
    private Double value;
    private String nextDueDate;
    private DiscountResponse discount;
    private InterestResponse interest;
    private FineResponse fine;
    private CycleEnum cycle;
    private String description;
    private String endDate;
    private String maxPayments;
    private StatusEnum status;
    private String externalReference;
    private SplitResponse split;
}
