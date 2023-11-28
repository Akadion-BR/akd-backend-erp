package br.akd.svc.akadia.modules.web.plano.proxy.operations.atualizacao.models.request;

import br.akd.svc.akadia.modules.web.pagamento.models.enums.FormaPagamentoSistemaEnum;
import br.akd.svc.akadia.modules.web.plano.models.entity.PlanoEntity;
import br.akd.svc.akadia.modules.web.plano.proxy.global.request.discount.DiscountRequest;
import br.akd.svc.akadia.modules.web.plano.proxy.global.request.enums.CycleEnum;
import br.akd.svc.akadia.modules.web.plano.proxy.global.request.fine.FineRequest;
import br.akd.svc.akadia.modules.web.plano.proxy.global.request.interest.InterestRequest;
import lombok.*;

import javax.validation.constraints.NotNull;

@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class AtualizaAssinaturaAsaasRequest {

    @NotNull
    private FormaPagamentoSistemaEnum billingType;

    @NotNull
    private Double value;

    @NotNull
    private String nextDueDate;

    private DiscountRequest discount;

    private InterestRequest interest;

    private FineRequest fine;

    @NotNull
    private CycleEnum cycle;

    private String description;

    private Boolean updatePendingPayments;

    private String externalReference;

    public AtualizaAssinaturaAsaasRequest buildFromRequest(PlanoEntity plano) {
        return (plano != null)
                ? AtualizaAssinaturaAsaasRequest.builder()
                .billingType(plano.getFormaPagamentoSistemaEnum())
                .value(plano.getTipoPlanoEnum().getValor())
                .nextDueDate(plano.getDataVencimento())
                .discount(null)
                .interest(null)
                .fine(null)
                .cycle(CycleEnum.MONTHLY)
                .description(plano.getTipoPlanoEnum().getDesc())
                .updatePendingPayments(true)
                .externalReference(null)
                .build()
                : null;
    }
}
