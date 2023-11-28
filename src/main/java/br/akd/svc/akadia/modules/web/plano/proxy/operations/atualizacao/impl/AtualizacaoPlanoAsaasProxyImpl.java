package br.akd.svc.akadia.modules.web.plano.proxy.operations.atualizacao.impl;

import br.akd.svc.akadia.exceptions.InternalErrorException;
import br.akd.svc.akadia.modules.web.plano.models.entity.PlanoEntity;
import br.akd.svc.akadia.modules.web.plano.proxy.PlanoAsaasProxy;
import br.akd.svc.akadia.modules.web.plano.proxy.operations.atualizacao.impl.utils.AtualizacaoPlanoAsaasProxyUtils;
import br.akd.svc.akadia.modules.web.plano.proxy.operations.atualizacao.models.request.AtualizaAssinaturaAsaasRequest;
import br.akd.svc.akadia.modules.web.plano.proxy.operations.atualizacao.models.response.AtualizaAssinaturaAsaasResponse;
import br.akd.svc.akadia.utils.Constantes;
import com.fasterxml.jackson.core.JsonProcessingException;
import feign.FeignException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class AtualizacaoPlanoAsaasProxyImpl {

    @Autowired
    PlanoAsaasProxy planoAsaasProxy;

    @Autowired
    AtualizacaoPlanoAsaasProxyUtils atualizacaoPlanoAsaasProxyUtils;

    public void realizaAtualizacaoDePlanoDeAssinaturaNaIntegradoraAsaas(PlanoEntity planoAtualizado) throws JsonProcessingException {

        log.debug("Método de serviço responsável pela atualização de assinatura na integradora ASAAS acessado");

        log.info("Iniciando construção do objeto AtualizaAssinaturaAsaasRequest...");
        AtualizaAssinaturaAsaasRequest atualizaAssinaturaAsaasRequest =
                new AtualizaAssinaturaAsaasRequest().buildFromRequest(planoAtualizado);
        log.info("Objeto AtualizaAssinaturaAsaasRequest construído com sucesso");

        try {
            log.debug("Realizando envio de requisição de atualização de assinatura para a integradora ASAAS...");
            ResponseEntity<AtualizaAssinaturaAsaasResponse> responseAsaas =
                    planoAsaasProxy.atualizaAssinatura(
                            planoAtualizado.getCodigoAssinaturaAsaas(),
                            atualizaAssinaturaAsaasRequest,
                            System.getenv(Constantes.TOKEN_ASAAS));

            log.info("Realizando validações referente à resposta da integradora...");
            atualizacaoPlanoAsaasProxyUtils.realizaValidacaoResponseAsaas(responseAsaas);
            log.info("Validações realizadas com sucesso. Plano atualizado na ASAAS com sucesso.");
        } catch (FeignException feignException) {
            log.error("Ocorreu um erro durante a integração com o ASAAS para atualização de assinatura: {}",
                    feignException.getMessage());

            log.info("Iniciando acesso ao método responsável por realizar o tratamento do erro retornado pelo " +
                    "feign client...");
            throw atualizacaoPlanoAsaasProxyUtils.realizaTratamentoRetornoErroFeignException(feignException);
        } catch (Exception e) {
            log.error("Ocorreu um erro interno durante a atualização do plano na integradora de " +
                    "pagamentos ASAAS: {}", e.getMessage());
            throw new InternalErrorException(Constantes.ERRO_INTERNO);
        }
    }
}
