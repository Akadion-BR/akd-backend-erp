package br.akd.svc.akadia.modules.web.clientesistema.proxy.impl.criacao;

import br.akd.svc.akadia.exceptions.InvalidRequestException;
import br.akd.svc.akadia.modules.web.clientesistema.models.dto.request.ClienteSistemaRequest;
import br.akd.svc.akadia.modules.web.clientesistema.proxy.ClienteSistemaAsaasProxy;
import br.akd.svc.akadia.modules.web.clientesistema.proxy.impl.criacao.utils.CriacaoClienteAsaasProxyUtils;
import br.akd.svc.akadia.modules.web.clientesistema.proxy.models.request.ClienteAsaasRequest;
import br.akd.svc.akadia.modules.web.clientesistema.proxy.models.response.ClienteAsaasResponse;
import br.akd.svc.akadia.modules.web.clientesistema.utils.ConstantesClienteSistema;
import br.akd.svc.akadia.utils.Constantes;
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
    CriacaoClienteAsaasProxyUtils criacaoClienteAsaasProxyUtils;

    public String realizaCriacaoClienteAsaas(ClienteSistemaRequest clienteRequest) {

        log.info("Método de serviço responsável pela criação de cliente na integradora ASAAS acessado");

        log.info("Iniciando construção do objeto criaClienteAsaasRequest...");
        ClienteAsaasRequest clienteAsaasRequest = new ClienteAsaasRequest()
                .constroiObjetoCriaClienteAsaasRequest(clienteRequest);
        log.info("Objeto CriaClienteAsaasRequest construído com sucesso");

        ResponseEntity<ClienteAsaasResponse> responseAsaas;

        try {
            log.info("Realizando envio de requisição de criação de cliente para a integradora ASAAS...");
            responseAsaas =
                    clienteAsaasProxy.cadastraNovoCliente(clienteAsaasRequest, System.getenv(Constantes.TOKEN_ASAAS));
        } catch (Exception e) {
            log.error(ConstantesClienteSistema.ERRO_CRIACAO_CLIENTE_ASAAS
                    + e.getMessage());
            throw new InvalidRequestException(ConstantesClienteSistema.ERRO_CRIACAO_CLIENTE_ASAAS
                    + e.getMessage());
        }

        log.info("Realizando validações referente à resposta da integradora...");
        criacaoClienteAsaasProxyUtils.realizaValidacaoResponseAsaas(responseAsaas);

        ClienteAsaasResponse clienteAsaasResponse = responseAsaas.getBody();

        log.info("Criação de cliente na integradora ASAAS realizada com sucesso. Retornando id do " +
                "cliente gerado: {}", Objects.requireNonNull(clienteAsaasResponse).getId());
        return clienteAsaasResponse.getId();
    }

}
