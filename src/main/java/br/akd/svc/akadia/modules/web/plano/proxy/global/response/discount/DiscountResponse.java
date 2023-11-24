package br.akd.svc.akadia.modules.web.plano.proxy.global.response.discount;

import br.akd.svc.akadia.modules.web.plano.proxy.global.response.discount.enums.TypeEnum;
import lombok.*;

@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class DiscountResponse {
    private Double value;
    private Integer dueDateLimitDays;
    private TypeEnum type;
}
