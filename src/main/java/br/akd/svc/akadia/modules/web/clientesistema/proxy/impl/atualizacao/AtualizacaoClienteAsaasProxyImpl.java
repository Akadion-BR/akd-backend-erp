package br.akd.svc.akadia.modules.web.clientesistema.proxy.impl.atualizacao;

import br.akd.svc.akadia.exceptions.InvalidRequestException;
import br.akd.svc.akadia.modules.web.clientesistema.models.dto.request.atualizacao.AtualizaClienteSistemaRequest;
import br.akd.svc.akadia.modules.web.clientesistema.models.dto.request.criacao.ClienteSistemaRequest;
import br.akd.svc.akadia.modules.web.clientesistema.proxy.ClienteSistemaAsaasProxy;
import br.akd.svc.akadia.modules.web.clientesistema.proxy.impl.atualizacao.utils.AtualizacaoClienteAsaasProxyUtils;
import br.akd.svc.akadia.modules.web.clientesistema.proxy.models.request.ClienteAsaasRequest;
import br.akd.svc.akadia.modules.web.clientesistema.proxy.models.response.ClienteAsaasResponse;
import br.akd.svc.akadia.modules.web.clientesistema.utils.ConstantesClienteSistema;
import br.akd.svc.akadia.utils.Constantes;
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
                                               AtualizaClienteSistemaRequest atualizaClienteSistemaRequest) {

        log.info("Método de serviço responsável pela atualização de cliente na integradora ASAAS acessado");

        log.info("Iniciando construção do objeto criaClienteAsaasRequest...");
        ClienteAsaasRequest clienteAsaasRequest = new ClienteAsaasRequest()
                .constroiObjetoCriaClienteAsaasRequestParaAtualizacao(atualizaClienteSistemaRequest);
        log.info("Objeto CriaClienteAsaasRequest construído com sucesso");

        ResponseEntity<ClienteAsaasResponse> responseAsaas;

        try {
            log.info("Realizando envio de requisição de atualização de cliente para a integradora ASAAS...");
            responseAsaas = clienteAsaasProxy.atualizaDadosCliente(
                    asaasId, clienteAsaasRequest, System.getenv(Constantes.TOKEN_ASAAS));
        } catch (Exception e) {
            log.error(ConstantesClienteSistema.ERRO_ATUALIZACAO_CLIENTE_ASAAS
                    + e.getMessage());
            throw new InvalidRequestException(ConstantesClienteSistema.ERRO_ATUALIZACAO_CLIENTE_ASAAS
                    + e.getMessage());
        }

        log.info("Realizando validações referente à resposta da integradora...");
        atualizacaoClienteAsaasProxyUtils.realizaValidacaoResponseAsaas(responseAsaas);
        log.info("Validações realizadas com sucesso. Cliente atualizado na ASAAS com sucesso.");
    }


}
