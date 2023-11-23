package br.akd.svc.akadia.modules.web.pagamento.hook.controller;

import br.akd.svc.akadia.exceptions.InvalidRequestException;
import br.akd.svc.akadia.exceptions.ObjectNotFoundException;
import br.akd.svc.akadia.modules.web.pagamento.hook.services.PagamentoWebhookService;
import br.akd.svc.akadia.modules.web.proxy.asaas.webhooks.cobranca.AtualizacaoCobrancaWebHook;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Slf4j
@CrossOrigin
@RestController
@RequestMapping("/hook/v1/pagamentos")
@Produces({MediaType.APPLICATION_JSON, "application/json"})
@Consumes({MediaType.APPLICATION_JSON, "application/json"})
public class PagamentoWebhookController {

    @Autowired
    PagamentoWebhookService pagamentoWebhookService;

    //TODO IMPLEMENTAR FILTRO DO SPRING BOOT
    @PostMapping
    @Tag(name = "Recebimento de atualização de status de pagamento")
    @Operation(summary = "Esse endpoint tem como objetivo receber atualizações no status dos pagamentos das " +
            "assinaturas por parte da integradora ASAAS", method = "POST")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Requisição finalizada com sucesso",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = HttpStatus.class))}),
            @ApiResponse(responseCode = "404",
                    description = "Nenhum cliente foi encontrado com o id informado",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ObjectNotFoundException.class))}),
            @ApiResponse(responseCode = "400",
                    description = "Erro de requisição inválida",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = InvalidRequestException.class))}),
            @ApiResponse(responseCode = "500",
                    description = "Ocorreu uma falha na comunicação com a integradora de pagamentos",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = InvalidRequestException.class))})
    })
    public ResponseEntity<HttpStatus> recebeStatusPagamento(@RequestBody AtualizacaoCobrancaWebHook atualizacaoCobrancaWebHook,
                                                            @RequestHeader(value = "asaas-access-token") String token) {
        log.info("Webhook ASAAS de atualização do status de cobrança recebido: {}", atualizacaoCobrancaWebHook);
        pagamentoWebhookService.realizaRedirecionamentoParaMetodoCorreto(atualizacaoCobrancaWebHook);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

}
