package br.akd.svc.akadia.modules.web.pagamento.hook.models.utils.split;

import lombok.*;

@ToString
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SplitWebHook {
    private String walletId;
    private Double fixedValue;
    private String status;
    private String refusalReason;
}
