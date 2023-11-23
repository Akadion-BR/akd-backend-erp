package br.akd.svc.akadia.modules.web.pagamento.hook.models;

import br.akd.svc.akadia.modules.web.pagamento.hook.models.enums.EventoCobrancaEnum;
import lombok.*;

@ToString
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AtualizacaoPagamentoWebHook {
    private EventoCobrancaEnum event;
    private PagamentoWebHookRequest payment;
}
