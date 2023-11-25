package br.akd.svc.akadia.modules.web.plano.proxy.global.response.split;

import br.akd.svc.akadia.modules.web.plano.proxy.global.response.split.enums.RefusalReasonEnum;
import br.akd.svc.akadia.modules.web.plano.proxy.global.response.split.enums.StatusEnum;
import lombok.*;

@Builder
@ToString
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SplitResponse {
    private String walletId;
    private Double fixedValue;
    private Double percentualValue;
    private StatusEnum statusEnum;
    private RefusalReasonEnum refusalReasonEnum;
}
