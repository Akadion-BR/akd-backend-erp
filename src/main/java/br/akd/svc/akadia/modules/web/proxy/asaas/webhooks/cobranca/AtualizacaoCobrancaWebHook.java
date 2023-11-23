package br.akd.svc.akadia.modules.web.proxy.asaas.webhooks.cobranca;

import br.akd.svc.akadia.modules.web.pagamento.hook.models.PagamentoWebHookRequest;
import br.akd.svc.akadia.modules.web.proxy.asaas.webhooks.cobranca.enums.EventoCobrancaEnum;
import lombok.*;

@ToString
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AtualizacaoCobrancaWebHook {
    private EventoCobrancaEnum event;
    private PagamentoWebHookRequest payment;
}
