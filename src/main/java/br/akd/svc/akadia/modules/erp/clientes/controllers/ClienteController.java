package br.akd.svc.akadia.modules.erp.clientes.controllers;

import br.akd.svc.akadia.config.security.UserSS;
import br.akd.svc.akadia.exceptions.InvalidRequestException;
import br.akd.svc.akadia.exceptions.ObjectNotFoundException;
import br.akd.svc.akadia.modules.erp.clientes.models.dto.request.ClienteRequest;
import br.akd.svc.akadia.modules.erp.clientes.models.dto.response.ClienteResponse;
import br.akd.svc.akadia.modules.erp.clientes.models.dto.response.page.ClientePageResponse;
import br.akd.svc.akadia.modules.erp.clientes.services.crud.ClienteService;
import br.akd.svc.akadia.modules.erp.clientes.services.report.ClienteRelatorioService;
import br.akd.svc.akadia.modules.erp.clientes.services.validator.ClienteValidationService;
import br.akd.svc.akadia.utils.RelatorioUtil;
import com.lowagie.text.DocumentException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

/**
 * ClienteController
 * Esta classe fornece os endpoints para acessar as regras lógicas de negócio referentes à entidade ClienteEntity
 *
 * @author Gabriel Lagrota
 */
@Slf4j
@CrossOrigin
@RestController
@RequestMapping("api/sistema/v1/cliente")
@Produces({MediaType.APPLICATION_JSON, "application/json"})
@Consumes({MediaType.APPLICATION_JSON, "application/json"})
public class ClienteController {

    @Autowired
    ClienteService clienteService;

    @Autowired
    ClienteValidationService clienteValidationService;

    @Autowired
    ClienteRelatorioService relatorioService;

    @Autowired
    RelatorioUtil relatorioUtil;

    /**
     * Cadastro de novo cliente
     * Este método tem como objetivo disponibilizar o endpoint de acionamento da lógica de criação de novo cliente
     *
     * @param userDetails    Dados do usuário logado na sessão atual
     * @param clienteRequest Objeto contendo todos os atributos necessários para a criação de um novo cliente
     * @return Retorna objeto Cliente criado convertido para o tipo ClienteResponse
     * @throws InvalidRequestException Exception lançada caso ocorra alguma falha interna na criação do cliente
     */
    @PostMapping
    @PreAuthorize("hasAnyRole('CLIENTES')")
    @Tag(name = "Cadastro de novo cliente")
    @Operation(summary = "Esse endpoint tem como objetivo realizar a criação de um novo cliente",
            method = "POST")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201",
                    description = "Cliente persistido com sucesso",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ClienteResponse.class))}),
            @ApiResponse(responseCode = "400",
                    description = "A inscrição estadual informada já existe",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = InvalidRequestException.class))}),
            @ApiResponse(responseCode = "400",
                    description = "Erro de requisição inválida",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = InvalidRequestException.class))}),
            @ApiResponse(responseCode = "400",
                    description = "O CPF/CNPJ informado já existe",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = InvalidRequestException.class))})
    })
    public ResponseEntity<ClienteResponse> criaNovoCliente(@AuthenticationPrincipal UserDetails userDetails,
                                                           @RequestBody ClienteRequest clienteRequest) {
        log.info("Método controlador de criação de novo cliente acessado");
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(clienteService.criaNovoCliente(
                        ((UserSS) userDetails).getColaboradorId(),
                        clienteRequest));
    }

    /**
     * Busca paginada de clientes
     * Este método tem como objetivo disponibilizar o endpoint de acionamento da lógica de busca paginada de clientes
     *
     * @param userDetails Dados do usuário logado na sessão atual
     * @param busca       Parâmetro opcional. Recebe uma string para busca de clientes por atributos específicos
     * @param pageable    Contém especificações da paginação, como tamanho da página, página atual, etc
     * @return Retorna objeto do tipo ClientePageResponse, que possui informações da paginação e a lista de clientes
     * encontrados inserida em seu body
     */
    @GetMapping
    @PreAuthorize("hasAnyRole('CLIENTES')")
    @Tag(name = "Busca paginada por clientes cadastrados")
    @Operation(summary = "Esse endpoint tem como objetivo realizar a busca paginada de clientes cadastrados na empresa " +
            "logada que acionou a requisição com os filtros de busca enviados", method = "GET")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "A busca paginada de clientes foi realizada com sucesso",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ClientePageResponse.class))}),
    })
    public ResponseEntity<ClientePageResponse> obtemClientesPaginados(@AuthenticationPrincipal UserDetails userDetails,
                                                                      @RequestParam(value = "busca", required = false) String busca,
                                                                      Pageable pageable) {
        log.info("Endpoint de busca paginada por clientes acessado. Filtros de busca: {}", busca);
        return ResponseEntity.ok().body(
                clienteService.realizaBuscaPaginadaPorClientes(
                        pageable,
                        ((UserSS) userDetails).getColaboradorId(),
                        busca));
    }

    /**
     * Busca de cliente por id
     * Este método tem como objetivo disponibilizar o endpoint de acionamento da lógica de busca de cliente por id
     *
     * @param userDetails Dados do usuário logado na sessão atual
     * @param idCliente   Id do cliente a ser buscado
     * @return Retorna objeto Cliente encontrado convertido para o tipo ClienteResponse
     */
    @GetMapping("/{id}")
    @Tag(name = "Busca de cliente por id")
    @PreAuthorize("hasAnyRole('CLIENTES')")
    @Operation(summary = "Esse endpoint tem como objetivo realizar a busca de um cliente pelo id recebido pelo " +
            "parâmetro", method = "GET")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "A busca de cliente por id foi realizada com sucesso",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ClienteResponse.class))}),
            @ApiResponse(responseCode = "404",
                    description = "Nenhum cliente foi encontrado com o id informado",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ObjectNotFoundException.class))})
    })
    public ResponseEntity<ClienteResponse> obtemClientePorId(@AuthenticationPrincipal UserDetails userDetails,
                                                             @PathVariable("id") UUID idCliente) {
        log.info("Endpoint de busca de cliente por id acessado. ID recebido: {}", idCliente);
        return ResponseEntity.ok()
                .body(clienteService.realizaBuscaDeClientePorId(
                        ((UserSS) userDetails).getColaboradorId(),
                        idCliente));
    }

    /**
     * Atualiza cliente
     * Este método tem como objetivo disponibilizar o endpoint de acionamento da lógica de atualização de cliente por id
     *
     * @param userDetails    Dados do usuário logado na sessão atual
     * @param clienteRequest objeto que deve conter todos os dados necessários para atualização do cliente
     * @param id             Id do cliente a ser atualizado
     * @return Retorna objeto Cliente encontrado convertido para o tipo ClienteResponse
     */
    @PutMapping("/{id}")
    @Tag(name = "Atualização de cliente")
    @PreAuthorize("hasAnyRole('CLIENTES')")
    @Operation(summary = "Esse endpoint tem como objetivo realizar a atualização de um cliente na base de dados da " +
            "empresa", method = "PUT")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Cliente atualizado com sucesso",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ClienteResponse.class))}),
            @ApiResponse(responseCode = "400", description = "A inscrição estadual informada já existe",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = InvalidRequestException.class))}),
            @ApiResponse(responseCode = "400", description = "O CPF/CNPJ informado já existe",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = InvalidRequestException.class))}),
            @ApiResponse(responseCode = "404", description = "Nenhum cliente foi encontrado com o id informado",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ObjectNotFoundException.class))})
    })
    public ResponseEntity<ClienteResponse> atualizaCliente(@AuthenticationPrincipal UserDetails userDetails,
                                                           @PathVariable UUID id,
                                                           @Valid @RequestBody ClienteRequest clienteRequest) {
        log.info("Método controlador de atualização de cliente acessado");
        return ResponseEntity.status(HttpStatus.OK).body(
                clienteService.atualizaCliente(
                        ((UserSS) userDetails).getColaboradorId(),
                        id,
                        clienteRequest));
    }

    /**
     * Exclusão de cliente
     * Este método tem como objetivo disponibilizar o endpoint de acionamento da lógica de exclusão de cliente por id
     *
     * @param userDetails Dados do usuário logado na sessão atual
     * @param id          Id do cliente a ser removido
     * @return Retorna objeto Cliente removido convertido para o tipo ClienteResponse
     */
    @DeleteMapping("/{id}")
    @Tag(name = "Remoção de cliente")
    @PreAuthorize("hasAnyRole('CLIENTES')")
    @Operation(summary = "Esse endpoint tem como objetivo realizar a exclusão de um cliente", method = "DELETE")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Cliente excluído com sucesso"),
            @ApiResponse(responseCode = "404",
                    description = "Nenhum cliente foi encontrado com o uuid informado",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ObjectNotFoundException.class))}),
            @ApiResponse(responseCode = "400",
                    description = "O cliente selecionado já foi excluído",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = InvalidRequestException.class))}),
            @ApiResponse(responseCode = "400",
                    description = "Nenhum cliente foi encontrado para remoção",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = InvalidRequestException.class))}),
            @ApiResponse(responseCode = "400",
                    description = "Não é possível remover um cliente que já foi excluído",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = InvalidRequestException.class))}),
    })
    public ResponseEntity<ClienteResponse> removeCliente(@AuthenticationPrincipal UserDetails userDetails,
                                                         @PathVariable UUID id) {
        log.info("Método controlador de remoção de cliente acessado");
        return ResponseEntity.status(HttpStatus.OK).body(
                clienteService.removeCliente(
                        ((UserSS) userDetails).getColaboradorId(),
                        id));
    }

    /**
     * Exclusão de clientes em massa
     * Este método tem como objetivo disponibilizar o endpoint de acionamento da lógica de exclusão de clientes em
     * massa por id
     *
     * @param userDetails Dados do usuário logado na sessão atual
     * @param ids         Ids dos clientes a serem removidos
     * @return Retorna ResponseEntity contendo status da requisição
     */
    @DeleteMapping
    @Tag(name = "Remoção de cliente")
    @PreAuthorize("hasAnyRole('CLIENTES')")
    @Operation(summary = "Esse endpoint tem como objetivo realizar a exclusão de um cliente", method = "DELETE")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Cliente excluído com sucesso"),
            @ApiResponse(responseCode = "404",
                    description = "Nenhum cliente foi encontrado com o uuid informado",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ObjectNotFoundException.class))}),
            @ApiResponse(responseCode = "400",
                    description = "O cliente selecionado já foi excluído",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = InvalidRequestException.class))}),
            @ApiResponse(responseCode = "400",
                    description = "Nenhum cliente foi encontrado para remoção",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = InvalidRequestException.class))}),
            @ApiResponse(responseCode = "400",
                    description = "Não é possível remover um cliente que já foi excluído",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = InvalidRequestException.class))}),
    })
    public ResponseEntity<?> removeClientesEmMassa(@AuthenticationPrincipal UserDetails userDetails,
                                                   @RequestBody List<UUID> ids) {
        log.info("Método controlador de remoção de clientes em massa acessado");
        clienteService.removeClientesEmMassa(((UserSS) userDetails).getColaboradorId(), ids);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    /**
     * Validação de duplicidade de inscrição estadual
     * Este método tem como objetivo disponibilizar o endpoint de acionamento da lógica de validação se inscrição
     * estadual já existe na empresa atual
     *
     * @param userDetails       Dados do usuário logado na sessão atual
     * @param inscricaoEstadual Inscrição estadual a ser validada
     * @return Retorna ResponseEntity contendo status da requisição
     */
    @PostMapping("/verifica-ie")
    @Tag(name = "Validação de duplicidade na entrada da inscrição estadual")
    @PreAuthorize("hasAnyRole('CLIENTES')")
    @Operation(summary = "Esse endpoint tem como objetivo realizar a validação se a inscrição estadual digitada pelo " +
            "cliente já existe na base de dados da empresa.", method = "GET")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Requisição finalizada com sucesso",
                    content = {@Content(mediaType = "application/json")}),
            @ApiResponse(responseCode = "400",
                    description = "A inscrição estadual informada já existe",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = InvalidRequestException.class))})
    })
    public ResponseEntity<?> verificaDuplicidadeInscricaoEstadual(@AuthenticationPrincipal UserDetails userDetails,
                                                                  @RequestBody String inscricaoEstadual) {
        log.info("Endpoint de validação de duplicidade de inscrição estadual acessado. IE: " + inscricaoEstadual);
        clienteValidationService.validaSeInscricaoEstadualJaExiste(
                ((UserSS) userDetails).getColaboradorId().getIdEmpresa(),
                inscricaoEstadual);
        return ResponseEntity.ok().build();
    }

    /**
     * Validação de duplicidade de cpf ou cnpj
     * Este método tem como objetivo disponibilizar o endpoint de acionamento da lógica de validação se cpf ou cnpj
     * já existe na empresa atual
     *
     * @param userDetails Dados do usuário logado na sessão atual
     * @param cpfCnpj     cpf ou cnpj a ser validado
     * @return Retorna ResponseEntity contendo status da requisição
     */
    @PostMapping("/verifica-cpfCnpj")
    @Tag(name = "Validação de duplicidade na entrada do cpf ou cnpj")
    @PreAuthorize("hasAnyRole('CLIENTES')")
    @Operation(summary = "Esse endpoint tem como objetivo realizar a validação se o cpf ou cnpj digitado pelo " +
            "cliente já existe na base de dados da empresa.", method = "GET")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Requisição finalizada com sucesso",
                    content = {@Content(mediaType = "application/json")}),
            @ApiResponse(responseCode = "400",
                    description = "O cpf ou cnpj digitado já existe",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = InvalidRequestException.class))})
    })
    public ResponseEntity<?> verificaDuplicidadeCpfCnpj(@AuthenticationPrincipal UserDetails userDetails,
                                                        @RequestBody String cpfCnpj) {
        log.info("Endpoint de validação de duplicidade de CPF/CNPJ acessado. CPF/CNPJ: " + cpfCnpj);
        clienteValidationService.validaSeCpfCnpjJaExiste(
                ((UserSS) userDetails).getColaboradorId().getIdEmpresa(),
                cpfCnpj);
        return ResponseEntity.ok().build();
    }

    /**
     * Criação de relatório de clientes em PDF
     * Este método tem como objetivo disponibilizar o endpoint de acionamento da lógica de criação de relatório de
     * clientes registrados em PDF
     *
     * @param userDetails Dados do usuário logado na sessão atual
     * @param res         Atributo do tipo HttpServletResponse que possui as informações da resposta e deve retornar o arquivo
     *                    em PDF com o relatório de clientes cadastrados
     * @param ids         Ids dos clientes que deverão ser exibidos no relatório
     * @throws DocumentException Exception lançada caso ocorra um erro na criação do relatório em PDF
     * @throws IOException       Exception lançada caso ocorra um erro na criação do relatório em PDF
     */
    @PostMapping("/relatorio")
    @Tag(name = "Gerar relatório em PDF")
    @PreAuthorize("hasAnyRole('CLIENTES')")
    @Operation(summary = "Esse endpoint tem como objetivo realizar a criação de um relatório em PDF com a listagem de " +
            "clientes solicitada", method = "POST")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "PDF gerado com sucesso"),
            @ApiResponse(responseCode = "400",
                    description = "Ocorreu um erro na criação do PDF",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = Exception.class))}),
    })
    public void relatorio(@AuthenticationPrincipal UserDetails userDetails,
                          HttpServletResponse res,
                          @RequestBody List<UUID> ids) throws DocumentException, IOException {
        log.info("Método controlador de obtenção de relatório de clientes em PDF acessado. IDs: {}", ids);
        relatorioUtil.setaAtributosHttpServletResponseRelatorio(res, "clientes");
        relatorioService.exportarPdf(
                ((UserSS) userDetails).getColaboradorId(), res, ids);
    }
}
