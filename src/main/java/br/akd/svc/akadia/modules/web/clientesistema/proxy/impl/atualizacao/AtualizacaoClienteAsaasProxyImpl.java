package br.akd.svc.akadia.modules.web.clientesistema.proxy.impl.atualizacao;

import br.akd.svc.akadia.exceptions.InternalErrorException;
import br.akd.svc.akadia.modules.web.clientesistema.models.dto.request.atualizacao.AtualizaClienteSistemaRequest;
import br.akd.svc.akadia.modules.web.clientesistema.proxy.ClienteSistemaAsaasProxy;
import br.akd.svc.akadia.modules.web.clientesistema.proxy.impl.atualizacao.utils.AtualizacaoClienteAsaasProxyUtils;
import br.akd.svc.akadia.modules.web.clientesistema.proxy.models.request.ClienteAsaasRequest;
import br.akd.svc.akadia.modules.web.clientesistema.proxy.models.response.ClienteAsaasResponse;
import br.akd.svc.akadia.utils.Constantes;
import com.fasterxml.jackson.core.JsonProcessingException;
import feign.FeignException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class AtualizacaoClienteAsaasProxyImpl {

    @Autowired
    ClienteSistemaAsaasProxy clienteAsaasProxy;

    @Autowired
    AtualizacaoClienteAsaasProxyUtils atualizacaoClienteAsaasProxyUtils;

    public void realizaAtualizacaoClienteAsaas(String asaasId,
                                               AtualizaClienteSistemaRequest atualizaClienteSistemaRequest) throws JsonProcessingException {

        log.info("Método de serviço responsável pela atualização de cliente na integradora ASAAS acessado");

        log.info("Iniciando construção do objeto atualizaClienteAsaasRequest...");
        ClienteAsaasRequest clienteAsaasRequest = new ClienteAsaasRequest()
                .constroiObjetoCriaClienteAsaasRequestParaAtualizacao(atualizaClienteSistemaRequest);
        log.info("Objeto AtualizaClienteAsaasRequest construído com sucesso");

        try {
            log.info("Realizando envio de requisição de atualização de cliente para a integradora ASAAS...");
            ResponseEntity<ClienteAsaasResponse> responseAsaas = clienteAsaasProxy.atualizaDadosCliente(
                    asaasId, clienteAsaasRequest, System.getenv(Constantes.TOKEN_ASAAS));
            log.info("Atualização de cliente sistêmico na integradora ASAAS realizado com sucesso");

            log.info("Realizando validações referente à resposta da integradora...");
            atualizacaoClienteAsaasProxyUtils.realizaValidacaoResponseAsaas(responseAsaas);
            log.info("Validações realizadas com sucesso. Cliente atualizado na ASAAS com sucesso.");
        } catch (FeignException feignException) {
            log.error("Ocorreu um erro durante a integração com o ASAAS para atualização de cliente sistêmico: {}",
                    feignException.getMessage());

            log.info("Iniciando acesso ao método responsável por realizar o tratamento do erro retornado pelo " +
                    "feign client...");
            throw atualizacaoClienteAsaasProxyUtils.realizaTratamentoRetornoErroFeignException(feignException);
        } catch (Exception e) {
            log.error("Ocorreu um erro interno durante a atualização do cliente sistêmico na integradora de " +
                    "pagamentos ASAAS: {}", e.getMessage());
            throw new InternalErrorException(Constantes.ERRO_INTERNO);
        }
    }


}
