package br.akd.svc.akadia.modules.web.pagamento.hook.services.categorias.criacao;

import br.akd.svc.akadia.modules.web.pagamento.hook.models.PagamentoWebHookRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
public class CriacaoPagamentoWebhookService {

    public void realizaCriacaoDeNovoPagamento(PagamentoWebHookRequest pagamentoWebHookRequest) {
        //TODO IMPLEMENTAR LÓGICA DE CRIAÇÃO DE NOVO PAGAMENTO COM SERVIÇO DE E-MAILS
    }

}
