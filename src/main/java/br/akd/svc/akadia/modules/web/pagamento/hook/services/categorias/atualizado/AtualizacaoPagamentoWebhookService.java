package br.akd.svc.akadia.modules.web.pagamento.hook.services.categorias.atualizado;

import br.akd.svc.akadia.modules.web.clientesistema.models.entity.ClienteSistemaEntity;
import br.akd.svc.akadia.modules.web.pagamento.hook.models.PagamentoWebHookRequest;
import br.akd.svc.akadia.modules.web.pagamento.hook.models.enums.BillingTypeEnum;
import br.akd.svc.akadia.modules.web.pagamento.models.entity.PagamentoSistemaEntity;
import br.akd.svc.akadia.modules.web.pagamento.models.enums.FormaPagamentoSistemaEnum;
import br.akd.svc.akadia.modules.web.pagamento.repository.impl.PagamentoSistemaRepositoryImpl;
import br.akd.svc.akadia.modules.web.pagamento.utils.ConstantesPagamento;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class AtualizacaoPagamentoWebhookService {

    @Autowired
    PagamentoSistemaRepositoryImpl pagamentoSistemaRepository;

    public void realizaAtualizacaoDePagamentoAlterado(PagamentoWebHookRequest pagamentoWebHookRequest,
                                                      ClienteSistemaEntity clienteSistema) {
        log.info(ConstantesPagamento.INICIANDO_IMPLEMENTACAO_BUSCA_PAGAMENTO_ASAAS);
        PagamentoSistemaEntity pagamentoEntity = pagamentoSistemaRepository
                .implementaBuscaPorCodigoPagamentoAsaas(pagamentoWebHookRequest.getId());

        log.info(ConstantesPagamento.ATUALIZANDO_VARIAVEIS_PAGAMENTO);
        pagamentoEntity.setDescricao(pagamentoWebHookRequest.getDescription());
        pagamentoEntity.setFormaPagamentoSistemaEnum(FormaPagamentoSistemaEnum.valueOf(
                pagamentoWebHookRequest.getBillingType().getFormaPagamentoResumida()));
        pagamentoEntity.setCartao(pagamentoWebHookRequest.getBillingType()
                .equals(BillingTypeEnum.CREDIT_CARD) && clienteSistema.getCartao() != null
                ? clienteSistema.getCartao()
                : null);
        log.info(ConstantesPagamento.OBJETO_PAGAMENTO_CRIADO, pagamentoEntity);

        log.info("Iniciando persistência do pagamento atualizado...");
        pagamentoSistemaRepository.implementaPersistencia(pagamentoEntity);
        log.info("Pagamento atualizado com sucesso");
    }
}
