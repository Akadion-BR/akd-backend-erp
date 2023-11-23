package br.akd.svc.akadia.modules.web.pagamento.hook.services.categorias.removido;

import br.akd.svc.akadia.modules.web.pagamento.hook.models.PagamentoWebHookRequest;
import br.akd.svc.akadia.modules.web.pagamento.models.entity.PagamentoSistemaEntity;
import br.akd.svc.akadia.modules.web.pagamento.models.enums.StatusPagamentoSistemaEnum;
import br.akd.svc.akadia.modules.web.pagamento.repository.impl.PagamentoSistemaRepositoryImpl;
import br.akd.svc.akadia.modules.web.pagamento.utils.ConstantesPagamento;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class RemocaoPagamentoWebhookService {

    @Autowired
    PagamentoSistemaRepositoryImpl pagamentoRepositoryImpl;

    public void realizaAtualizacaoDePlanoParaPagamentoRemovido(PagamentoWebHookRequest pagamentoWebHookRequest) {
        log.info(ConstantesPagamento.INICIANDO_IMPLEMENTACAO_BUSCA_PAGAMENTO_ASAAS);
        PagamentoSistemaEntity pagamentoEntity = pagamentoRepositoryImpl
                .implementaBuscaPorCodigoPagamentoAsaas(pagamentoWebHookRequest.getId());

        log.info(ConstantesPagamento.ATUALIZANDO_VARIAVEIS_PAGAMENTO);
        pagamentoEntity.setStatusPagamentoSistemaEnum(StatusPagamentoSistemaEnum.CANCELADO);
        log.info(ConstantesPagamento.OBJETO_PAGAMENTO_CRIADO, pagamentoEntity);

        pagamentoRepositoryImpl.implementaPersistencia(pagamentoEntity);
        log.info("Atualização do status do pagamento para cancelado realizada com sucesso");
    }
}
