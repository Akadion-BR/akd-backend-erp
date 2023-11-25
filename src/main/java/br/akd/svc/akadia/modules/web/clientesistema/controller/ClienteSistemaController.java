package br.akd.svc.akadia.modules.web.clientesistema.controller;

import br.akd.svc.akadia.exceptions.FeignConnectionException;
import br.akd.svc.akadia.exceptions.InvalidRequestException;
import br.akd.svc.akadia.exceptions.ObjectNotFoundException;
import br.akd.svc.akadia.modules.backoffice.lead.models.entity.LeadEntity;
import br.akd.svc.akadia.modules.backoffice.lead.service.LeadService;
import br.akd.svc.akadia.modules.web.clientesistema.models.dto.request.atualizacao.AtualizaClienteSistemaRequest;
import br.akd.svc.akadia.modules.web.clientesistema.models.dto.request.criacao.ClienteSistemaRequest;
import br.akd.svc.akadia.modules.web.clientesistema.models.dto.response.ClienteSistemaResponse;
import br.akd.svc.akadia.modules.web.clientesistema.models.entity.ClienteSistemaEntity;
import br.akd.svc.akadia.modules.web.clientesistema.repository.impl.ClienteSistemaRepositoryImpl;
import br.akd.svc.akadia.modules.web.clientesistema.services.crud.ClienteSistemaService;
import br.akd.svc.akadia.modules.web.clientesistema.services.validator.ClienteSistemaValidationService;
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

import javax.validation.Valid;
import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.List;
import java.util.UUID;

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
@RequestMapping("/api/site/v1/cliente-sistema")
@Produces({MediaType.APPLICATION_JSON, "application/json"})
@Consumes({MediaType.APPLICATION_JSON, "application/json"})
public class ClienteSistemaController {

    @Autowired
    ClienteSistemaService clienteSistemaService;

    @Autowired
    ClienteSistemaValidationService clienteSistemaValidationService;

    @Autowired
    ClienteSistemaRepositoryImpl clienteSistemaRepositoryImpl;

    @Autowired
    LeadService leadService;

    /**
     * Busca de todos os clientes cadastrados
     * Esse endpoint tem como objetivo realizar a busca de todos os clientes cadastrados no banco de dados
     *
     * @return Retorna lista de clientes sistêmicos cadastrados
     */
    @GetMapping
    @Tag(name = "Busca de todos os clientes cadastrados")
    @Operation(summary = "Esse endpoint tem como objetivo realizar a busca de todos os clientes cadastrados no " +
            "banco de dados", method = "GET")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Requisição executada com sucesso",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ClienteSistemaEntity.class))})
    })
    public ResponseEntity<List<ClienteSistemaEntity>> listaTodosClientes() {
        log.info("Método controlador de listagem de todos os clientes do Akadion acessado");
        return ResponseEntity.status(HttpStatus.OK).body(clienteSistemaRepositoryImpl.buscaTodosClientes());
    }

    /**
     * Captação de lead da criação de novo cliente
     * Esse endpoint tem como objetivo realizar o cadastro das informações básicas do cliente no banco de dados para
     * formação de leads, além de verificar se o e-mail informado já existe
     *
     * @param clienteSistemaRequest Objeto contendo todos os atributos necessários para a criação de um novo lead
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
    public ResponseEntity<LeadEntity> captaLeadsPreCadastro(@RequestBody ClienteSistemaRequest clienteSistemaRequest) {
        log.info("Método controlador de captação de leads pré-cadastro acessado");
        //TODO CRIAR OU IMPLEMENTAR UM OBJETO REQUEST SÓ PARA A CAPTAÇÃO DE LEADS, POIS DA FORMA QUE ESTÁ IRÁ
        // ACARRETAR EM CONFLITOS COM OS VALIDATORS DO CLIENTESISTEMAREQUEST
        return ResponseEntity.status(HttpStatus.CREATED).body(
                leadService.encaminhaLeadParaPersistencia(
                        clienteSistemaRequest));
        //TODO IMPLEMENTAR LÓGICA
    }

    /**
     * Validação de CPF para criação de novo cliente
     * Esse endpoint tem como objetivo realizar a verificação de já existência do CPF enviado
     *
     * @param cpf CPF a ser validado
     * @return Retorna status da requisição
     */
    @PostMapping("/verifica-cpf")
    @Tag(name = "Validação de CPF da criação de novo cliente")
    @Operation(summary = "Esse endpoint tem como objetivo realizar a verificação de já existência do CPF enviado",
            method = "POST")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "O CPF informado ainda não existe",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ClienteSistemaEntity.class))}),
            @ApiResponse(responseCode = "400",
                    description = "O CPF informado já existe",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = InvalidRequestException.class))})
    })
    public ResponseEntity<?> verificaSeCpfJaExiste(@RequestBody String cpf) {
        log.info("Método controlador de validação se cpf acessado");
        //TODO ADICIONAR VALIDADOR DE CPF
        clienteSistemaValidationService.validaSeCpfJaExiste(cpf);
        log.info("Validação de cpf realizada com sucesso. O cpf informado está disponível");
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    /**
     * Cadastro de novo cliente
     * Esse endpoint tem como objetivo realizar o cadastro de um novo cliente no banco de dados do projeto e na
     * integradora de pagamentos ASAAS
     *
     * @param clienteSistemaRequest Objeto contendo todos os atributos necessários para a criação de um novo cliente
     * @return Retorna objeto Cliente criado convertido para o tipo ClienteResponse
     */
    @PostMapping
    @Tag(name = "Cadastro de novo cliente")
    @Operation(summary = "Esse endpoint tem como objetivo realizar o cadastro de um novo cliente no banco de dados " +
            "do projeto e na integradora de pagamentos ASAAS",
            method = "POST")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201",
                    description = "Cliente salvo com sucesso",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ClienteSistemaResponse.class))}),
            @ApiResponse(responseCode = "400",
                    description = "Ocorreu um erro no processo de criação do cliente",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = InvalidRequestException.class))}),
            @ApiResponse(responseCode = "400",
                    description = "Ocorreu um erro no processo de criação da assinatura",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = InvalidRequestException.class))}),
            @ApiResponse(responseCode = "500",
                    description = "Ocorreu uma falha na conexão com o feign client",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = FeignConnectionException.class))})
    })
    public ResponseEntity<ClienteSistemaResponse> criaNovoCliente(@Valid @RequestBody ClienteSistemaRequest clienteSistemaRequest) throws JsonProcessingException {
        log.info("Método controlador de criação de novo cliente acessado");
        return ResponseEntity.status(HttpStatus.CREATED).body(
                clienteSistemaService.cadastraNovoCliente(
                        clienteSistemaRequest));
    }

    /**
     * Atualização de dados do cliente
     * Esse endpoint tem como objetivo realizar a atualização dos dados do cliente no banco de dados do projeto
     * e na integradora de pagamentos ASAAS
     *
     * @param idCliente                     ID do cliente que deverá ser atualizado
     * @param atualizaClienteSistemaRequest Objeto contendo todos os atributos necessários para a atualização de um cliente
     * @return Retorna objeto Cliente criado convertido para o tipo ClienteResponse
     */
    @PutMapping("/{idCliente}")
    @Tag(name = "Atualização de dados do cliente")
    @Operation(summary = "Esse endpoint tem como objetivo realizar a atualização dos dados do cliente no banco de " +
            "dados do projeto e na integradora de pagamentos ASAAS",
            method = "POST")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Cliente atualizado com sucesso",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ClienteSistemaResponse.class))}),
            @ApiResponse(responseCode = "400",
                    description = "Ocorreu um erro no processo de atualização do cliente",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = InvalidRequestException.class))}),
            @ApiResponse(responseCode = "404",
                    description = "Nenhum cliente foi encontrado com o id informado",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ObjectNotFoundException.class))}),
            @ApiResponse(responseCode = "500",
                    description = "Ocorreu uma falha na conexão com o feign",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = FeignConnectionException.class))})
    })
    public ResponseEntity<ClienteSistemaResponse> atualizaDadosCliente(@PathVariable UUID idCliente,
                                                                       @Valid @RequestBody AtualizaClienteSistemaRequest atualizaClienteSistemaRequest) throws JsonProcessingException {
        log.info("Método controlador de atualização de dados do cliente de id {} acessado", idCliente);
        return ResponseEntity.status(HttpStatus.OK).body(
                clienteSistemaService.atualizaDadosCliente(
                        idCliente,
                        atualizaClienteSistemaRequest));
    }
}
