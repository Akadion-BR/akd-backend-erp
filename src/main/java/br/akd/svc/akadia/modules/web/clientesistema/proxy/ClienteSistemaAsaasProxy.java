package br.akd.svc.akadia.modules.web.clientesistema.proxy;

import br.akd.svc.akadia.modules.web.clientesistema.proxy.models.request.ClienteAsaasRequest;
import br.akd.svc.akadia.modules.web.clientesistema.proxy.models.response.ClienteAsaasResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "ASAAS-CLIENTES", url = "${URL_ASAAS}")
public interface ClienteSistemaAsaasProxy {
    @PostMapping(value = "/customers")
    ResponseEntity<ClienteAsaasResponse> cadastraNovoCliente(@RequestBody ClienteAsaasRequest clienteAsaasRequest,
                                                             @RequestHeader(value = "access_token") String accessToken);

    @PostMapping(value = "/customers/{idCliente}")
    ResponseEntity<ClienteAsaasResponse> atualizaDadosCliente(@PathVariable String idCliente,
                                                              @RequestBody ClienteAsaasRequest clienteAsaasRequest,
                                                              @RequestHeader(value = "access_token") String accessToken);
}
