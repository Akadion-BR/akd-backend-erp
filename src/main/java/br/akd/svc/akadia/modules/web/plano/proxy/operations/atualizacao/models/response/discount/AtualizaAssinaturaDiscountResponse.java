package br.akd.svc.akadia.modules.web.plano.proxy.operations.atualizacao.models.response.discount;

import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class AtualizaAssinaturaDiscountResponse {
    private Double value;
    private Integer dueDateLimitDays;
}
