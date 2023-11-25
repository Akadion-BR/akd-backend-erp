package br.akd.svc.akadia.modules.web.plano.proxy.global.request.discount;

import lombok.*;

@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class DiscountRequest {
    private Double value;
    private Integer dueDateLimitDays;
}
