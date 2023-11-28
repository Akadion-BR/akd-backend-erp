package br.akd.svc.akadia.modules.web.pagamento.hook.services;

import br.akd.svc.akadia.modules.web.pagamento.hook.services.categorias.atualizado.AtualizacaoPagamentoWebhookService;
import br.akd.svc.akadia.modules.web.pagamento.hook.services.categorias.confirmado.ConfirmacaoPagamentoWebhookService;
import br.akd.svc.akadia.modules.web.pagamento.hook.services.categorias.criacao.CriacaoPagamentoWebhookService;
import br.akd.svc.akadia.modules.web.pagamento.hook.services.categorias.removido.RemocaoPagamentoWebhookService;
import br.akd.svc.akadia.modules.web.pagamento.hook.services.categorias.vencido.services.AtrasoPagamentoWebhookService;
import br.akd.svc.akadia.modules.web.proxy.asaas.webhooks.cobranca.AtualizacaoCobrancaWebHook;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
public class PagamentoWebhookService {

    @Autowired
    CriacaoPagamentoWebhookService criacaoPagamentoWebhookService;

    @Autowired
    ConfirmacaoPagamentoWebhookService confirmacaoPagamentoWebhookService;

    @Autowired
    AtrasoPagamentoWebhookService atrasoPagamentoWebhookService;

    @Autowired
    AtualizacaoPagamentoWebhookService atualizacaoPagamentoWebhookService;

    @Autowired
    RemocaoPagamentoWebhookService remocaoPagamentoWebhookService;

    @Transactional
    public void realizaRedirecionamentoParaMetodoCorreto(AtualizacaoCobrancaWebHook atualizacaoCobrancaWebHook) {
        log.info("Método orquestrador de Webhook de pagamentos acessado");

        log.info("Iniciando orquestração por tipo de atualização de pagamento do webhook...");
        switch (atualizacaoCobrancaWebHook.getEvent()) {
            case PAYMENT_CREATED:
                log.debug("Condicional para novo pagamento CRIADO acessada");
                criacaoPagamentoWebhookService.realizaCriacaoDeNovoPagamento(atualizacaoCobrancaWebHook.getPayment());
                log.info("Cobrança CRIADA com sucesso");
                break;
            case PAYMENT_RECEIVED:
            case PAYMENT_CONFIRMED:
                log.debug("Condicional de pagamento CONFIRMADO acessada");
                confirmacaoPagamentoWebhookService.realizaAtualizacaoDePagamentoRealizado(
                        atualizacaoCobrancaWebHook.getPayment());
                log.info("Cobrança CONFIRMADA sucesso");
                break;
            case PAYMENT_UPDATED:
                log.debug("Condicional de pagamento ALTERADO acessada");
                atualizacaoPagamentoWebhookService.realizaAtualizacaoDePagamentoAlterado(
                        atualizacaoCobrancaWebHook.getPayment());
                log.info("Cobrança ALTERADA com sucesso");
                break;
            case PAYMENT_OVERDUE:
                log.debug("Condicional de pagamento VENCIDO acessada");
                atrasoPagamentoWebhookService.realizaAtualizacaoDePlanoParaPagamentoVencido(
                        atualizacaoCobrancaWebHook.getPayment());
                log.info("Atualização de plano para pagamento VENCIDO realizada com sucesso");
                break;
            case PAYMENT_DELETED:
                log.info("Condicional de pagamento DELETADO acessada");
                remocaoPagamentoWebhookService.realizaAtualizacaoDePlanoParaPagamentoRemovido(
                        atualizacaoCobrancaWebHook.getPayment());
                log.info("Atualização de plano para pagamento DELETADO realizada com sucesso");
                break;
            case PAYMENT_ANTICIPATED:
            case PAYMENT_AWAITING_RISK_ANALYSIS:
            case PAYMENT_APPROVED_BY_RISK_ANALYSIS:
            case PAYMENT_REPROVED_BY_RISK_ANALYSIS:
            case PAYMENT_RESTORED:
            case PAYMENT_REFUNDED: //TODO Implantar lógica para estorno
            case PAYMENT_RECEIVED_IN_CASH_UNDONE:
            case PAYMENT_CHARGEBACK_REQUESTED:
            case PAYMENT_CHARGEBACK_DISPUTE:
            case PAYMENT_AWAITING_CHARGEBACK_REVERSAL:
            case PAYMENT_DUNNING_RECEIVED:
            case PAYMENT_DUNNING_REQUESTED:
            case PAYMENT_BANK_SLIP_VIEWED:
            case PAYMENT_CHECKOUT_VIEWED: //TODO Implantar lógica para fatura visualizada
                break;
        }
    }
}
