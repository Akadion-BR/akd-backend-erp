package br.akd.svc.akadia.modules.backoffice.lead.controller;

import br.akd.svc.akadia.exceptions.InvalidRequestException;
import br.akd.svc.akadia.modules.backoffice.lead.models.dto.request.LeadRequest;
import br.akd.svc.akadia.modules.backoffice.lead.models.entity.LeadEntity;
import br.akd.svc.akadia.modules.backoffice.lead.service.LeadService;
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

import javax.validation.Valid;
import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

/**
 * ClienteSistemaController
 * Esta classe fornece os endpoints para acessar as regras lógicas de negócio referentes à
 * entidade ClienteSistemaEntity
 *
 * @author Gabriel Lagrota
 */
@Slf4j
@CrossOrigin
@RestController
@RequestMapping("/api/backoffice/v1/lead")
@Produces({MediaType.APPLICATION_JSON, "application/json"})
@Consumes({MediaType.APPLICATION_JSON, "application/json"})
public class LeadController {

    @Autowired
    LeadService leadService;

    /**
     * Captação de lead da criação de novo cliente
     * Esse endpoint tem como objetivo realizar o cadastro das informações básicas do cliente sistêmico no banco de dados para
     * formação de leads, além de verificar se o e-mail informado já existe
     *
     * @param leadRequest Objeto contendo todos os atributos necessários para a criação de um novo lead
     * @return Retorna Lead persistido
     */
    @PostMapping("/capta-leads")
    @Tag(name = "Captação de lead da criação de novo cliente")
    @Operation(summary = "Esse endpoint tem como objetivo realizar o cadastro das informações básicas do cliente " +
            "no banco de dados para formação de leads, além de verificar se o e-mail informado já existe",
            method = "POST")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201",
                    description = "Dados persistidos com sucesso",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = LeadEntity.class))}),
            @ApiResponse(responseCode = "400",
                    description = "O e-mail informado já existe",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = InvalidRequestException.class))})
    })
    public ResponseEntity<LeadEntity> captaLeadsPreCadastro(@Valid @RequestBody LeadRequest leadRequest) {
        log.info("Método controlador de captação de leads pré-cadastro acessado");
        return ResponseEntity.status(HttpStatus.CREATED).body(
                leadService.criaNovoLead(
                        leadRequest));
    }

}
