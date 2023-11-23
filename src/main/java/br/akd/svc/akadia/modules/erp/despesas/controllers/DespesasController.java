package br.akd.svc.akadia.modules.erp.despesas.controllers;

import br.akd.svc.akadia.config.security.UserSS;
import br.akd.svc.akadia.exceptions.InvalidRequestException;
import br.akd.svc.akadia.exceptions.ObjectNotFoundException;
import br.akd.svc.akadia.modules.erp.despesas.models.dto.request.DespesaRequest;
import br.akd.svc.akadia.modules.erp.despesas.models.dto.response.DespesaResponse;
import br.akd.svc.akadia.modules.erp.despesas.models.dto.response.page.DespesaPageResponse;
import br.akd.svc.akadia.modules.erp.despesas.services.crud.DespesaService;
import br.akd.svc.akadia.modules.erp.despesas.services.report.DespesaRelatorioService;
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
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

@Slf4j
@CrossOrigin
@RestController
@RequestMapping("/api/sistema/v1/despesas")
@Produces({MediaType.APPLICATION_JSON, "application/json"})
@Consumes({MediaType.APPLICATION_JSON, "application/json"})
public class DespesasController {

    @Autowired
    DespesaService despesaService;

    @Autowired
    RelatorioUtil relatorioUtil;

    @Autowired
    DespesaRelatorioService despesaRelatorioService;

    /**
     * Criação de nova despesa
     * Esse endpoint tem como objetivo realizar a criação de uma nova despesa na base dados da empresa
     *
     * @param userDetails    Dados do usuário logado na sessão atual
     * @param despesaRequest Objeto contendo todos os atributos necessários para a criação de uma nova despesa
     * @return Retorna objeto Despesa criado convertido para o tipo DespesaResponse
     * @throws InvalidRequestException Exception lançada caso ocorra alguma falha interna na criação da despesa
     */
    @PostMapping
    @PreAuthorize("hasAnyRole('DESPESAS')")
    @Tag(name = "Criação de nova despesa")
    @Operation(summary = "Esse endpoint tem como objetivo realizar a criação de uma nova despesa na base dados " +
            "da empresa", method = "POST")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201",
                    description = "Despesa persistida com sucesso",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = DespesaResponse.class))}),
            @ApiResponse(responseCode = "400",
                    description = "Requisição inválida",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = InvalidRequestException.class))}),
    })
    public ResponseEntity<DespesaResponse> criaNovaDespesa(@AuthenticationPrincipal UserDetails userDetails,
                                                           @RequestBody DespesaRequest despesaRequest) {
        log.info("Método controlador de criação de nova despesa acessada");
        return ResponseEntity.status(HttpStatus.CREATED).body(
                despesaService.criaNovaDespesa(
                        ((UserSS) userDetails).getColaboradorId(),
                        despesaRequest));
    }

    /**
     * Busca paginada por despesas cadastradas
     * Esse endpoint tem como objetivo realizar a busca paginada de despesas cadastradas na empresa do usuário que
     * acionou a requisição com os filtros de busca enviados pelo usuário
     *
     * @param userDetails Dados do usuário logado na sessão atual
     * @param busca       Parâmetro opcional. Recebe uma string para busca de despesas por atributos específicos
     * @param pageable    Contém especificações da paginação, como tamanho da página, página atual, etc
     * @return Retorna objeto do tipo DespesaPageResponse, que possui informações da paginação e a lista de despesas
     * encontradas inserida em seu body
     */
    @GetMapping
    @PreAuthorize("hasAnyRole('DESPESAS')")
    @Tag(name = "Busca paginada por despesas cadastradas")
    @Operation(summary = "Esse endpoint tem como objetivo realizar a busca paginada de despesas cadastradas na " +
            "empresa do usuário que acionou a requisição com os filtros de busca enviados pelo usuário", method = "GET")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "A busca paginada de despesas foi realizada com sucesso",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = DespesaPageResponse.class))}),
    })
    public ResponseEntity<DespesaPageResponse> obtemDespesasPaginadas(@AuthenticationPrincipal UserDetails userDetails,
                                                                      @PageableDefault(sort = {"dataAgendamento", "dataPagamento"}, direction = Sort.Direction.ASC) Pageable pageable,
                                                                      @RequestParam(value = "busca", required = false) String busca,
                                                                      @RequestParam(value = "mesAno") String mesAno) {
        log.info("Endpoint de busca paginada por despesas acessado. Filtros de busca: {}",
                busca == null ? "Nulo" : busca);
        return ResponseEntity.ok().body(
                despesaService.realizaBuscaPaginadaPorDespesas(
                        ((UserSS) userDetails).getColaboradorId(),
                        pageable,
                        mesAno,
                        busca));
    }

    /**
     * Busca de despesa por id
     * Esse endpoint tem como objetivo realizar a busca de uma despesa pelo id recebido pelo parâmetro
     *
     * @param userDetails Dados do usuário logado na sessão atual
     * @param idDespesa   Id da despesa a ser buscada
     * @return Retorna objeto Despesa encontrada convertida para o tipo DespesaResponse
     */
    @GetMapping("/{idDespesa}")
    @Tag(name = "Busca de despesa por id")
    @PreAuthorize("hasAnyRole('DESPESAS')")
    @Operation(summary = "Esse endpoint tem como objetivo realizar a busca de uma despesa pelo id recebido " +
            "pelo parâmetro", method = "GET")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "A busca de despesa por id foi realizada com sucesso",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = DespesaResponse.class))}),
            @ApiResponse(responseCode = "404",
                    description = "Nenhuma despesa foi encontrado com o id informado",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ObjectNotFoundException.class))})
    })
    public ResponseEntity<DespesaResponse> obtemDespesaPorId(@AuthenticationPrincipal UserDetails userDetails,
                                                             @PathVariable("idDespesa") UUID idDespesa) {
        log.info("Endpoint de busca de despesa por id acessado. ID recebido: {}", idDespesa);
        return ResponseEntity.ok().body(
                despesaService.realizaBuscaDeDespesaPorId(
                        ((UserSS) userDetails).getColaboradorId(),
                        idDespesa));
    }

    /**
     * Atualização de despesa
     * Esse endpoint tem como objetivo realizar a atualização de uma despesa na base de dados da empresa
     *
     * @param userDetails    Dados do usuário logado na sessão atual
     * @param despesaRequest objeto que deve conter todos os dados necessários para atualização da despesa
     * @param idDespesa      Id da despesa a ser atualizada
     * @return Retorna objeto Despesa encontrada convertida para o tipo DespesaResponse
     */
    @PutMapping("/{idDespesa}")
    @Tag(name = "Atualização de despesa")
    @PreAuthorize("hasAnyRole('DESPESAS')")
    @Operation(summary = "Esse endpoint tem como objetivo realizar a atualização de uma despesa na base de dados " +
            "da empresa", method = "PUT")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Despesa atualizada com sucesso",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = DespesaResponse.class))}),
            @ApiResponse(responseCode = "404", description = "Nenhuma despesa foi encontrada com o id informado",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ObjectNotFoundException.class))})
    })
    public ResponseEntity<DespesaResponse> atualizaDespesa(@AuthenticationPrincipal UserDetails userDetails,
                                                           @RequestBody DespesaRequest despesaRequest,
                                                           @PathVariable UUID idDespesa) {
        log.info("Método controlador de atualização de despesa acessado");
        return ResponseEntity.status(HttpStatus.OK).body(
                despesaService.atualizaDespesa(
                        ((UserSS) userDetails).getColaboradorId(),
                        idDespesa,
                        despesaRequest));
    }

    /**
     * Remoção de despesa
     * Esse endpoint tem como objetivo realizar a exclusão de uma despesa
     *
     * @param userDetails Dados do usuário logado na sessão atual
     * @param idDespesa   Id da despesa a ser removida
     * @return Retorna objeto Despesa removida convertida para o tipo DespesaResponse
     */
    @DeleteMapping("/{idDespesa}")
    @Tag(name = "Remoção de despesa")
    @PreAuthorize("hasAnyRole('DESPESAS')")
    @Operation(summary = "Esse endpoint tem como objetivo realizar a exclusão de uma despesa", method = "DELETE")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Despesa excluída com sucesso"),
            @ApiResponse(responseCode = "404",
                    description = "Nenhuma despesa foi encontrada com o id informado",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ObjectNotFoundException.class))}),
            @ApiResponse(responseCode = "400",
                    description = "A despesa selecionada já foi excluída",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = InvalidRequestException.class))}),
    })
    public ResponseEntity<DespesaResponse> removeDespesa(@AuthenticationPrincipal UserDetails userDetails,
                                                         @RequestParam(value = "removeRecorrencia") Boolean removeRecorrencia,
                                                         @PathVariable UUID idDespesa) {
        log.info("Método controlador de remoção de despesa acessado");
        return ResponseEntity.status(HttpStatus.OK).body(
                despesaService.removeDespesa(
                        ((UserSS) userDetails).getColaboradorId(),
                        idDespesa,
                        removeRecorrencia));
    }

    /**
     * Remoção de despesa em massa
     * Esse endpoint tem como objetivo realizar a exclusão de despesas em massa
     *
     * @param userDetails Dados do usuário logado na sessão atual
     * @param idsDespesa  Ids das despesas a serem removidas
     * @return Retorna ResponseEntity contendo status da requisição
     */
    @DeleteMapping
    @Tag(name = "Remoção de despesa em massa")
    @PreAuthorize("hasAnyRole('DESPESAS')")
    @Operation(summary = "Esse endpoint tem como objetivo realizar a exclusão de despesas em massa", method = "DELETE")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Despesas excluídas com sucesso"),
            @ApiResponse(responseCode = "404",
                    description = "Nenhuma despesa foi encontrada com o id informado",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ObjectNotFoundException.class))}),
            @ApiResponse(responseCode = "400",
                    description = "A despesa selecionada já foi excluída",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = InvalidRequestException.class))}),
    })
    public ResponseEntity<?> removeDespesasEmMassa(@AuthenticationPrincipal UserDetails userDetails,
                                                   @RequestBody List<UUID> idsDespesa) {
        log.info("Método controlador de remoção de despesas em massa acessado");
        despesaService.removeDespesasEmMassa(
                ((UserSS) userDetails).getColaboradorId(),
                idsDespesa);
        return ResponseEntity.ok().build();
    }

    /**
     * Gerar relatório em PDF
     * Esse endpoint tem como objetivo realizar a criação de um relatório em PDF com a listagem de
     * despesas solicitada
     *
     * @param userDetails Dados do usuário logado na sessão atual
     * @param res         Atributo do tipo HttpServletResponse que possui as informações da resposta e deve retornar o arquivo
     *                    em PDF com o relatório de despesas cadastradas
     * @param ids         Ids dos despesas que deverão ser exibidas no relatório
     * @throws DocumentException Exception lançada caso ocorra um erro na criação do relatório em PDF
     * @throws IOException       Exception lançada caso ocorra um erro na criação do relatório em PDF
     */
    @PostMapping("/relatorio")
    @Tag(name = "Gerar relatório em PDF")
    @PreAuthorize("hasAnyRole('DESPESAS')")
    @Operation(summary = "Esse endpoint tem como objetivo realizar a criação de um relatório em PDF com a " +
            "listagem de despesas solicitada", method = "POST")
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
        log.info("Método controlador de obtenção de relatório de despesas em PDF acessado. IDs: {}", ids);
        relatorioUtil.setaAtributosHttpServletResponseRelatorio(res, "despesas");
        despesaRelatorioService.exportarPdf(
                ((UserSS) userDetails).getColaboradorId(),
                res,
                ids);
    }

}
