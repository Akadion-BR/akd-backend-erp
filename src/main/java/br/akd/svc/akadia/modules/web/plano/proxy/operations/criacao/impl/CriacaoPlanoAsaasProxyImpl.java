package br.akd.svc.akadia.modules.web.plano.proxy.operations.criacao.impl;

import br.akd.svc.akadia.exceptions.InternalErrorException;
import br.akd.svc.akadia.modules.global.proxy.ProxyUtils;
import br.akd.svc.akadia.modules.global.proxy.enums.ProxyModuleEnum;
import br.akd.svc.akadia.modules.global.proxy.enums.ProxyOperationEnum;
import br.akd.svc.akadia.modules.web.plano.models.dto.request.PlanoRequest;
import br.akd.svc.akadia.modules.web.plano.proxy.PlanoAsaasProxy;
import br.akd.svc.akadia.modules.web.plano.proxy.operations.criacao.models.request.CriaPlanoAsaasRequest;
import br.akd.svc.akadia.modules.web.plano.proxy.operations.criacao.models.response.CriaPlanoAsaasResponse;
import br.akd.svc.akadia.utils.Constantes;
import com.fasterxml.jackson.core.JsonProcessingException;
import feign.FeignException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Slf4j
@Service
public class CriacaoPlanoAsaasProxyImpl {

    @Autowired
    PlanoAsaasProxy planoAsaasProxy;

    @Autowired
    ProxyUtils proxyUtils;

    public String realizaCriacaoDePlanoDeAssinaturaNaIntegradoraAsaas(PlanoRequest planoRequest,
                                                                      String asaasIdCliente) throws JsonProcessingException {

        log.debug("Método de serviço responsável pela criação de assinatura na integradora ASAAS acessado");

        log.debug("Iniciando construção do objeto CriaPlanoAsaasRequest...");
        CriaPlanoAsaasRequest criaPlanoAsaasRequest = new CriaPlanoAsaasRequest()
                .buildFromRequest(asaasIdCliente, planoRequest);

        try {
            log.info("Realizando envio de requisição de criação de assinatura para a integradora ASAAS...");
            ResponseEntity<CriaPlanoAsaasResponse> responseAsaas = planoAsaasProxy.cadastraNovaAssinatura(
                    criaPlanoAsaasRequest, System.getenv(Constantes.TOKEN_ASAAS));

            log.info("Realizando validações referente à resposta da integradora...");
            proxyUtils.realizaValidacaoResponseAsaas(
                    responseAsaas, ProxyModuleEnum.PLANO, ProxyOperationEnum.CRIACAO);
            log.info("Validações realizadas com sucesso. Plano criado no ASAAS com sucesso.");

            log.debug("Retornando id da assinatura gerado");
            return Objects.requireNonNull(responseAsaas.getBody()).getId();
        } catch (FeignException feignException) {
            log.error("Ocorreu um erro durante a integração com o ASAAS para criação de assinatura: {}",
                    feignException.getMessage());

            log.info("Iniciando acesso ao método responsável por realizar o tratamento do erro retornado pelo " +
                    "feign client...");
            throw proxyUtils.realizaTratamentoRetornoErroFeignException(
                    feignException, ProxyModuleEnum.PLANO, ProxyOperationEnum.CRIACAO);
        } catch (Exception e) {
            log.error("Ocorreu um erro interno durante a criação do plano na integradora de " +
                    "pagamentos ASAAS: {}", e.getMessage());
            throw new InternalErrorException(Constantes.ERRO_INTERNO);
        }
    }

}
