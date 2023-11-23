package br.akd.svc.akadia.modules.web.pagamento.hook.services.categorias.vencido.services;

import br.akd.svc.akadia.modules.web.pagamento.hook.models.PagamentoWebHookRequest;
import br.akd.svc.akadia.modules.web.pagamento.hook.services.categorias.vencido.utils.AtrasoPagamentoWebhookUtil;
import br.akd.svc.akadia.modules.web.pagamento.models.entity.PagamentoSistemaEntity;
import br.akd.svc.akadia.modules.web.pagamento.repository.impl.PagamentoSistemaRepositoryImpl;
import br.akd.svc.akadia.modules.web.pagamento.utils.ConstantesPagamento;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
public class AtrasoPagamentoWebhookService {

    @Autowired
    PagamentoSistemaRepositoryImpl pagamentoRepositoryImpl;

    @Autowired
    AtrasoPagamentoWebhookUtil atrasoPagamentoWebhookUtil;

    public void realizaAtualizacaoDePlanoParaPagamentoVencido(PagamentoWebHookRequest pagamentoWebHookRequest) {

        log.info("Método de atualização de pagamento e plano como vencidos acessado");

        log.info(ConstantesPagamento.INICIANDO_IMPLEMENTACAO_BUSCA_PAGAMENTO_ASAAS);
        PagamentoSistemaEntity pagamentoEntity = pagamentoRepositoryImpl
                .implementaBuscaPorCodigoPagamentoAsaas(pagamentoWebHookRequest.getId());

        log.info("Iniciando acesso ao método responsável pela setagem dos dados de atualização do pagamento...");
        atrasoPagamentoWebhookUtil.realizaAtualizacaoNoObjetoPagamentoParaAtraso(pagamentoEntity);

        log.info("Iniciando persistência do pagamento atualizado...");
        PagamentoSistemaEntity pagamentoAtualizado = pagamentoRepositoryImpl.implementaPersistencia(pagamentoEntity);

        //TODO IMPLEMENTAR LÓGICA PARA ATRASO E CANCELAMENTO NOS PAGAMENTOS DO SISTEMA
        // QUANTOS DIAS PARA CANCELAR? ETC...

        //TODO IMPLEMENTAR SERVIÇO DE ENVIO DE E-MAILS
//        log.info("Iniciando acesso ao método assíncrono de envio de e-mails...");
//        atrasoPagamentoWebhookUtil.realizaAcionamentoDoServicoDeEnvioDeEmails(pagamentoAtualizado);

        log.info("Lógica de atualização de status do pagamento para ATRASADO finalizada com sucesso");
    }
}
