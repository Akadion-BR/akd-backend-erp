package br.akd.svc.akadia.modules.web.plano.proxy;

import br.akd.svc.akadia.modules.web.plano.proxy.operations.atualizacao.models.request.AtualizaAssinaturaAsaasRequest;
import br.akd.svc.akadia.modules.web.plano.proxy.operations.atualizacao.models.response.AtualizaAssinaturaAsaasResponse;
import br.akd.svc.akadia.modules.web.plano.proxy.operations.consulta.response.ConsultaAssinaturaResponse;
import br.akd.svc.akadia.modules.web.plano.proxy.operations.criacao.models.request.CriaPlanoAsaasRequest;
import br.akd.svc.akadia.modules.web.plano.proxy.operations.criacao.models.response.CriaPlanoAsaasResponse;
import br.akd.svc.akadia.modules.web.plano.proxy.operations.remocao.response.CancelamentoAssinaturaResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "ASAAS-PLANO", url = "${URL_ASAAS}")
public interface PlanoAsaasProxy {
    @PostMapping(value = "/subscriptions")
    ResponseEntity<CriaPlanoAsaasResponse> cadastraNovaAssinatura(@RequestBody CriaPlanoAsaasRequest criaPlanoAsaasRequest,
                                                                  @RequestHeader(value = "access_token") String accessToken);

    @GetMapping(value = "/subscriptions/{idAssinatura}")
    ResponseEntity<ConsultaAssinaturaResponse> consultaAssinatura(@PathVariable String idAssinatura,
                                                                  @RequestHeader(value = "access_token") String accessToken);

    @PostMapping(value = "/customers/{id}")
    ResponseEntity<AtualizaAssinaturaAsaasResponse> atualizaAssinatura(@PathVariable String id,
                                                                       @RequestBody AtualizaAssinaturaAsaasRequest atualizaAssinaturaAsaasRequest,
                                                                       @RequestHeader(value = "access_token") String accessToken);

    @DeleteMapping(value = "/subscriptions/{id}")
    ResponseEntity<CancelamentoAssinaturaResponse> cancelarAssinatura(@PathVariable(value = "id") String id,
                                                                      @RequestHeader(value = "access_token") String accessToken);
}
