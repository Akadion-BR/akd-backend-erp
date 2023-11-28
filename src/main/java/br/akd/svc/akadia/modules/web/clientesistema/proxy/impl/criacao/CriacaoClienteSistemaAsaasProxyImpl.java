package br.akd.svc.akadia.modules.web.clientesistema.proxy.impl.criacao;

import br.akd.svc.akadia.exceptions.InternalErrorException;
import br.akd.svc.akadia.modules.global.proxy.ProxyUtils;
import br.akd.svc.akadia.modules.global.proxy.enums.ProxyModuleEnum;
import br.akd.svc.akadia.modules.global.proxy.enums.ProxyOperationEnum;
import br.akd.svc.akadia.modules.web.clientesistema.models.dto.request.criacao.ClienteSistemaRequest;
import br.akd.svc.akadia.modules.web.clientesistema.proxy.ClienteSistemaAsaasProxy;
import br.akd.svc.akadia.modules.web.clientesistema.proxy.models.request.ClienteAsaasRequest;
import br.akd.svc.akadia.modules.web.clientesistema.proxy.models.response.ClienteAsaasResponse;
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
public class CriacaoClienteSistemaAsaasProxyImpl {
    @Autowired
    ClienteSistemaAsaasProxy clienteAsaasProxy;

    @Autowired
    ProxyUtils proxyUtils;

    public String realizaCriacaoClienteAsaas(ClienteSistemaRequest clienteRequest)
            throws JsonProcessingException {

        log.info("Método de serviço responsável pela criação de cliente na integradora ASAAS acessado");

        log.info("Iniciando construção do objeto criaClienteAsaasRequest...");
        ClienteAsaasRequest clienteAsaasRequest = new ClienteAsaasRequest()
                .constroiObjetoCriaClienteAsaasRequest(clienteRequest);
        log.info("Objeto CriaClienteAsaasRequest construído com sucesso");

        try {
            log.info("Realizando envio de requisição de criação de cliente para a integradora ASAAS...");
            ResponseEntity<ClienteAsaasResponse> responseAsaas = clienteAsaasProxy.cadastraNovoCliente(
                    clienteAsaasRequest, System.getenv(Constantes.TOKEN_ASAAS));
            log.info("Criação de cliente sistêmico na integradora ASAAS realizado com sucesso");

            log.info("Realizando validações referente à resposta da integradora...");
            proxyUtils.realizaValidacaoResponseAsaas(
                    responseAsaas, ProxyModuleEnum.CLIENTE_SISTEMICO, ProxyOperationEnum.CRIACAO);

            ClienteAsaasResponse clienteAsaasResponse = responseAsaas.getBody();

            log.info("Criação de cliente na integradora ASAAS realizada com sucesso. Retornando id do " +
                    "cliente gerado: {}", Objects.requireNonNull(clienteAsaasResponse).getId());
            return clienteAsaasResponse.getId();
        } catch (FeignException feignException) {
            log.error("Ocorreu um erro durante a integração com o ASAAS para criação de novo cliente sistêmico: {}",
                    feignException.getMessage());

            log.info("Iniciando acesso ao método responsável por realizar o tratamento do erro retornado pelo " +
                    "feign client...");
            throw proxyUtils.realizaTratamentoRetornoErroFeignException(
                    feignException, ProxyModuleEnum.CLIENTE_SISTEMICO, ProxyOperationEnum.CRIACAO);
        } catch (Exception e) {
            log.error("Ocorreu um erro interno durante a criação do cliente sistêmico na integradora de " +
                    "pagamentos ASAAS: {}", e.getMessage());
            throw new InternalErrorException(Constantes.ERRO_INTERNO);
        }
    }

}
