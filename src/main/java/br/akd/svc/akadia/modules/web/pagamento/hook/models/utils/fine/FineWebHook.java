package br.akd.svc.akadia.modules.web.pagamento.hook.models.utils.fine;

import lombok.*;

@ToString
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class FineWebHook {
    private Double value;
    private String type;
}
