package br.akd.svc.akadia.modules.web.plano.proxy.operations.criacao.models.request;

import br.akd.svc.akadia.modules.web.pagamento.models.enums.FormaPagamentoSistemaEnum;
import br.akd.svc.akadia.modules.web.plano.models.dto.request.PlanoRequest;
import br.akd.svc.akadia.modules.web.plano.models.entity.PlanoEntity;
import br.akd.svc.akadia.modules.web.plano.proxy.global.request.discount.DiscountRequest;
import br.akd.svc.akadia.modules.web.plano.proxy.global.request.enums.CycleEnum;
import br.akd.svc.akadia.modules.web.plano.proxy.global.request.fine.FineRequest;
import br.akd.svc.akadia.modules.web.plano.proxy.global.request.interest.InterestRequest;
import br.akd.svc.akadia.modules.web.plano.proxy.operations.atualizacao.models.request.AtualizaAssinaturaAsaasRequest;
import br.akd.svc.akadia.modules.web.plano.proxy.operations.criacao.models.request.split.SplitRequest;
import lombok.*;

import java.time.LocalDate;

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

    public CriaPlanoAsaasRequest buildFromRequest(String asaasIdCliente,
                                                  PlanoRequest planoRequest) {
        return (planoRequest != null)
                ? CriaPlanoAsaasRequest.builder()
                .customer(asaasIdCliente)
                .billingType(planoRequest.getFormaPagamentoSistema())
                .value(planoRequest.getTipoPlano().getValor())
                .nextDueDate(String.valueOf(LocalDate.now().plusDays(7L)))
                .discount(null)
                .interest(null)
                .fine(null)
                .cycle(CycleEnum.MONTHLY)
                .description(planoRequest.getTipoPlano().getDesc())
                .endDate(null)
                .maxPayments(null)
                .externalReference(null)
                .split(null)
                .build()
                : null;
    }
}
