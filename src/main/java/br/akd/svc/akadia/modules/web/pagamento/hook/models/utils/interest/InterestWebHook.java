package br.akd.svc.akadia.modules.web.pagamento.hook.models.utils.interest;

import lombok.*;

@ToString
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class InterestWebHook {
    private Double value;
    private String type;
}
