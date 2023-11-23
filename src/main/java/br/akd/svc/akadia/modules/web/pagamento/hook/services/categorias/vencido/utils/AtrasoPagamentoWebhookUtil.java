package br.akd.svc.akadia.modules.web.pagamento.hook.services.categorias.vencido.utils;

import br.akd.svc.akadia.modules.web.pagamento.models.entity.PagamentoSistemaEntity;
import br.akd.svc.akadia.modules.web.pagamento.models.enums.StatusPagamentoSistemaEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class AtrasoPagamentoWebhookUtil {

    //TODO ELABORAR SERVIÇO DE ENVIO DE E-MAILS
//    @Autowired
//    EmailService emailService;
//
//    @Async
//    public void realizaAcionamentoDoServicoDeEnvioDeEmails(PagamentoEntity pagamentoAtualizado) {
//        log.info("Método utilitário de validação de acionamento do serviço de e-mails acessado");
//
//        log.info("Obtendo plano do pagamento...");
//        PlanoEntity planoPaiPagamento = pagamentoAtualizado.getPlano();
//        log.info("Plano obtido com sucesso");
//
//        log.info("Obtendo cliente do plano...");
//        ClienteEntity clientePaiPlano = planoPaiPagamento.getCliente();
//        log.info("Cliente obtido com sucesso");
//
//        log.info("Validando se plano referenciado está autorizado a enviar notificações por e-mails e se o cliente " +
//                "pai do plano possui algum e-mail cadastrado...");
//        if (planoPaiPagamento.getNotificacoes().contains(NotificacaoEnum.EMAIL) && clientePaiPlano.getEmail() != null) {
//            log.info(ConstantesEmail.INICIA_SERVICO_ENVIO_EMAILS);
//            emailService.enviarEmailAtrasoPagamento(
//                    pagamentoAtualizado,
//                    planoPaiPagamento,
//                    clientePaiPlano);
//        } else log.warn("O serviço de e-mails não pode ser acionado para o plano referenciado");
//    }

    public void realizaAtualizacaoNoObjetoPagamentoParaAtraso(PagamentoSistemaEntity pagamentoEntity) {
        log.info("Setando status do pagamento como atrasado...");
        pagamentoEntity.setStatusPagamentoSistemaEnum(StatusPagamentoSistemaEnum.ATRASADO);

        //TODO VERIFICAR SE NECESSÁRIO SETAR STATUS DO PLANO COMO INATIVO/ATRASADO TAMBÉM
    }

}
