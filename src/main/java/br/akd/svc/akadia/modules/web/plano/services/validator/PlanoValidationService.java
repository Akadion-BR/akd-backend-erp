package br.akd.svc.akadia.modules.web.plano.services.validator;

import br.akd.svc.akadia.exceptions.InvalidRequestException;
import br.akd.svc.akadia.modules.web.cartao.models.dto.request.CartaoRequest;
import br.akd.svc.akadia.modules.web.pagamento.models.enums.FormaPagamentoSistemaEnum;
import br.akd.svc.akadia.modules.web.plano.models.dto.request.PlanoRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class PlanoValidationService {

    public void realizaValidacoesParaAtualizacaoDePlano(PlanoRequest planoRequest,
                                                        CartaoRequest cartaoRequest) {
        log.info("Método responsável por acionar as validações de atualização do plano acessado");

        log.info("Iniciando acesso ao método que valida se cartão foi recebido da forma correta caso a forma " +
                "de pagamento seja cartão...");
        verificaSeFormaDePagamentoCondizComParametrosEnviados(
                planoRequest.getFormaPagamentoSistemaEnum(), cartaoRequest);
    }

    public void verificaSeFormaDePagamentoCondizComParametrosEnviados(FormaPagamentoSistemaEnum formaPagamentoSistemaEnum,
                                                                      CartaoRequest cartaoRequest) {
        log.info("Iniciando validação de forma de pagamento...");
        if (FormaPagamentoSistemaEnum.CREDIT_CARD.equals(formaPagamentoSistemaEnum) && cartaoRequest == null) {
            log.warn("A validação da forma de pagamento falhou. Nenhum cartão foi detectado através do body da requisição");
            throw new InvalidRequestException("É necessário cadastrar um cartão de crédito para a forma de pagamento " +
                    "da assinatura cartão de crédito");
        }
    }
}
