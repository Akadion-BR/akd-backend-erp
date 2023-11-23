package br.akd.svc.akadia.modules.web.pagamento.hook.services.categorias.confirmado;

import br.akd.svc.akadia.modules.web.pagamento.hook.models.PagamentoWebHookRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
public class ConfirmacaoPagamentoWebhookService {

    public void realizaAtualizacaoDePagamentoRealizado(PagamentoWebHookRequest pagamentoWebHookRequest) {
        //TODO CRIAR LÓGICA PARA PAGAMENTO CONFIRMADO

        //TODO CRIAR LÓGICA DE ENVIO DE E-MAILS
    }

}
