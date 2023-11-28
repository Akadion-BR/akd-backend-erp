package br.akd.svc.akadia.modules.web.plano.controller;

import br.akd.svc.akadia.exceptions.FeignConnectionException;
import br.akd.svc.akadia.exceptions.InvalidRequestException;
import br.akd.svc.akadia.exceptions.ObjectNotFoundException;
import br.akd.svc.akadia.modules.web.plano.models.dto.request.PlanoRequest;
import br.akd.svc.akadia.modules.web.plano.models.dto.response.PlanoResponse;
import br.akd.svc.akadia.modules.web.plano.services.crud.PlanoService;
import com.fasterxml.jackson.core.JsonProcessingException;
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
import java.util.UUID;

/**
 * PlanoController
 * Esta classe fornece os endpoints para acessar as regras lógicas de negócio referentes à
 * entidade PlanoEntity
 *
 * @author Gabriel Lagrota
 */
@Slf4j
@CrossOrigin
@RestController
@RequestMapping("/api/site/v1/plano")
@Produces({MediaType.APPLICATION_JSON, "application/json"})
@Consumes({MediaType.APPLICATION_JSON, "application/json"})
public class PlanoController {

    @Autowired
    PlanoService planoService;

    /**
     * Atualização de dados de assinatura
     * Esse endpoint tem como objetivo realizar a atualização dos dados da assinatura do cliente sistêmico no banco
     * de dados do projeto e na integradora de pagamentos ASAAS
     *
     * @param idClienteSistema ID do cliente sistêmico que deverá ser atualizado
     * @param planoRequest     Objeto contendo todos os atributos necessários para a atualização de um
     *                         cliente sistêmico
     * @return Retorna objeto Plano criado convertido para o tipo Response
     */
    @PutMapping("/{idClienteSistema}")
    @Tag(name = "Atualização de dados da assinatura do cliente")
    @Operation(summary = "Esse endpoint tem como objetivo realizar a atualização dos dados da assinatura do cliente " +
            "no banco de dados do projeto e na integradora de pagamentos ASAAS",
            method = "PUT")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Assinatura atualizada com sucesso",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = PlanoResponse.class))}),
            @ApiResponse(responseCode = "400",
                    description = "Ocorreu um erro no processo de atualização da assinatura",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = InvalidRequestException.class))}),
            @ApiResponse(responseCode = "404",
                    description = "Nenhum cliente sistêmico foi encontrado com o id informado",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ObjectNotFoundException.class))}),
            @ApiResponse(responseCode = "500",
                    description = "Ocorreu uma falha na conexão com o feign client",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = FeignConnectionException.class))})
    })
    public ResponseEntity<PlanoResponse> atualizaDadosAssinaturaCliente(@PathVariable UUID idClienteSistema,
                                                                        @RequestBody PlanoRequest planoRequest) throws JsonProcessingException {
        log.info("Método controlador de atualização de dados da assinatura do cliente de id {} acessado", idClienteSistema);
        return ResponseEntity.status(HttpStatus.OK).body(
                planoService.atualizaPlanoDoClienteSistemico(
                        idClienteSistema,
                        planoRequest));
    }

    /**
     * Cancelamento de assinatura
     * Esse endpoint tem como objetivo realizar o cancelammento da assinatura de um cliente sistêmico no banco de
     * dados do projeto e na integradora de pagamentos ASAAS
     *
     * @param idClienteSistema ID do cliente sistêmico que deverá ser atualizado
     * @return Retorna objeto Cliente removido convertido para o tipo Response
     */
    @DeleteMapping("/{idClienteSistema}")
    @Tag(name = "Cancelamento da assinatura do cliente sistêmico")
    @Operation(summary = "Esse endpoint tem como objetivo realizar o cancelamento de uma assinatura de um " +
            "cliente sistêmico no banco de dados do projeto e na integradora de pagamentos ASAAS",
            method = "DELETE")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Assinatura cancelada com sucesso",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = PlanoResponse.class))}),
            @ApiResponse(responseCode = "400",
                    description = "Ocorreu um erro no processo de cancelamento da assinatura",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = InvalidRequestException.class))}),
            @ApiResponse(responseCode = "404",
                    description = "Nenhum cliente sistêmico foi encontrado com o id informado",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ObjectNotFoundException.class))}),
            @ApiResponse(responseCode = "500",
                    description = "Ocorreu uma falha na conexão com o feign client",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = FeignConnectionException.class))})
    })
    public ResponseEntity<PlanoResponse> cancelaAssinaturaCliente(@PathVariable UUID idClienteSistema) throws JsonProcessingException {
        log.info("Método controlador de cancelamento de assinatura do cliente acessado");
        return ResponseEntity.status(HttpStatus.OK).body(
                planoService.cancelaPlanoDoClienteSistemico(
                        idClienteSistema));
    }
}
