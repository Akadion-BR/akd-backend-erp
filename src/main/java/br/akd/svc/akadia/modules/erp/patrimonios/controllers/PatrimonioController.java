package br.akd.svc.akadia.modules.erp.patrimonios.controllers;

import br.akd.svc.akadia.config.security.UserSS;
import br.akd.svc.akadia.exceptions.InvalidRequestException;
import br.akd.svc.akadia.exceptions.ObjectNotFoundException;
import br.akd.svc.akadia.modules.erp.clientes.models.dto.response.ClienteResponse;
import br.akd.svc.akadia.modules.erp.patrimonios.models.dto.request.PatrimonioRequest;
import br.akd.svc.akadia.modules.erp.patrimonios.models.dto.response.PatrimonioResponse;
import br.akd.svc.akadia.modules.erp.patrimonios.models.dto.response.page.PatrimonioPageResponse;
import br.akd.svc.akadia.modules.erp.patrimonios.services.crud.PatrimonioService;
import br.akd.svc.akadia.modules.erp.patrimonios.services.report.PatrimonioRelatorioService;
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
import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

/**
 * PatrimonioController
 * Esta classe fornece os endpoints para acessar as regras lógicas de negócio referentes à entidade PatrimonioEntity
 *
 * @author Gabriel Lagrota
 */
@Slf4j
@CrossOrigin
@RestController
@RequestMapping("/api/sistema/v1/patrimonios")
@Produces({MediaType.APPLICATION_JSON, "application/json"})
@Consumes({MediaType.APPLICATION_JSON, "application/json"})
public class PatrimonioController {

    @Autowired
    PatrimonioService patrimonioService;

    @Autowired
    PatrimonioRelatorioService relatorioService;

    @Autowired
    RelatorioUtil relatorioUtil;

    /**
     * Criação de novo patrimônio
     * Esse endpoint tem como objetivo realizar a criação de um novo patrimônio na base dados da empresa
     *
     * @param userDetails       Dados do usuário logado na sessão atual
     * @param patrimonioRequest Objeto contendo todos os atributos necessários para a criação de um novo patrimônio
     * @return Retorna objeto Patrimonio criado convertido para o tipo PatrimonioResponse
     * @throws InvalidRequestException Exception lançada caso ocorra alguma falha interna na criação do patrimônio
     */
    @PostMapping
    @PreAuthorize("hasAnyRole('PATRIMONIOS')")
    @Tag(name = "Criação de novo patrimônio")
    @Operation(summary = "Esse endpoint tem como objetivo realizar a criação de um novo patrimônio na base " +
            "de dados da empresa", method = "POST")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201",
                    description = "Patrimônio persistido com sucesso",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = PatrimonioResponse.class))}),
            @ApiResponse(responseCode = "400",
                    description = "Erro de requisição inválida",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = InvalidRequestException.class))}),
    })
    public ResponseEntity<PatrimonioResponse> criaNovoPatrimonio(@AuthenticationPrincipal UserDetails userDetails,
                                                                 @RequestBody PatrimonioRequest patrimonioRequest) {
        log.info("Método controlador de criação de novo patrimônio acessado");
        return ResponseEntity.status(HttpStatus.CREATED).body(
                patrimonioService.criaNovoPatrimonio(
                        ((UserSS) userDetails).getColaboradorId(),
                        patrimonioRequest));
    }

    /**
     * Busca paginada por patrimônios cadastrados
     * Esse endpoint tem como objetivo realizar a busca paginada de patrimônios cadastrados na empresa do usuário
     * que acionou a requisição com os filtros de busca enviados pelo usuário
     *
     * @param userDetails Dados do usuário logado na sessão atual
     * @param busca       Parâmetro opcional. Recebe uma string para busca de patrimônios por atributos específicos
     * @param pageable    Contém especificações da paginação, como tamanho da página, página atual, etc
     * @return Retorna objeto do tipo PatrimonioPageResponse, que possui informações da paginação e a lista de
     * patrimônios encontrados inserida em seu body
     */
    @GetMapping
    @PreAuthorize("hasAnyRole('PATRIMONIOS')")
    @Tag(name = "Busca paginada por patrimônios cadastrados")
    @Operation(summary = "Esse endpoint tem como objetivo realizar a busca paginada de patrimônios cadastrados " +
            "na empresa do usuário que acionou a requisição com os filtros de busca enviados pelo usuário", method = "GET")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "A busca paginada de patrimônios foi realizada com sucesso",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = PatrimonioPageResponse.class))}),
    })
    public ResponseEntity<PatrimonioPageResponse> obtemPatrimoniosPaginados(@AuthenticationPrincipal UserDetails userDetails,
                                                                            @RequestParam(value = "busca", required = false) String busca,
                                                                            Pageable pageable) {
        log.info("Endpoint de busca paginada por patrimônioss acessado");
        return ResponseEntity.ok().body(
                patrimonioService.realizaBuscaPaginada(
                        ((UserSS) userDetails).getColaboradorId(),
                        pageable,
                        busca));
    }

    /**
     * Busca de patrimônio por id
     * Esse endpoint tem como objetivo realizar a busca de um patrimônio pelo id recebido pelo parâmetro
     *
     * @param userDetails  Dados do usuário logado na sessão atual
     * @param idPatrimonio Id do patrimônio a ser buscado
     * @return Retorna objeto Patrimonio encontrado convertido para o tipo PatrimonioResponse
     */
    @GetMapping("/{idPatrimonio}")
    @Tag(name = "Busca de patrimônio por id")
    @PreAuthorize("hasAnyRole('PATRIMONIOS')")
    @Operation(summary = "Esse endpoint tem como objetivo realizar a busca de um patrimônio pelo id recebido " +
            "pelo parâmetro", method = "GET")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "A busca de patrimônio por id foi realizada com sucesso",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = PatrimonioResponse.class))}),
            @ApiResponse(responseCode = "404",
                    description = "Nenhum patrimônio foi encontrado com o id informado",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ObjectNotFoundException.class))})
    })
    public ResponseEntity<PatrimonioResponse> obtemObjetoPorId(@AuthenticationPrincipal UserDetails userDetails,
                                                               @PathVariable("idPatrimonio") UUID idPatrimonio) {
        log.info("Endpoint de busca de patrimônio por id acessado. ID recebido: {}", idPatrimonio);
        return ResponseEntity.ok().body(
                patrimonioService.realizaBuscaPorId(
                        ((UserSS) userDetails).getColaboradorId(),
                        idPatrimonio));
    }

    /**
     * Atualização de patrimônio
     * Esse endpoint tem como objetivo realizar a atualização de um patrimônio na base de dados da empresa
     *
     * @param userDetails       Dados do usuário logado na sessão atual
     * @param idPatrimonio      Id do patrimônio a ser atualizado
     * @param patrimonioRequest objeto que deve conter todos os dados necessários para atualização do patrimônio
     * @return Retorna objeto Patrimonio encontrado convertido para o tipo PatrimonioResponse
     */
    @PutMapping("/{idPatrimonio}")
    @Tag(name = "Atualização de patrimônio")
    @PreAuthorize("hasAnyRole('CLIENTES')")
    @Operation(summary = "Esse endpoint tem como objetivo realizar a atualização de um patrimônio na base de dados " +
            "da empresa", method = "PUT")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Patrimônio atualizado com sucesso",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ClienteResponse.class))}),
            @ApiResponse(responseCode = "404", description = "Nenhum patrimônio foi encontrado com o id informado",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ObjectNotFoundException.class))})
    })
    public ResponseEntity<PatrimonioResponse> atualizaPatrimonio(@AuthenticationPrincipal UserDetails userDetails,
                                                                 @PathVariable UUID idPatrimonio,
                                                                 @RequestBody PatrimonioRequest patrimonioRequest) {
        log.info("Método controlador de atualização de patrimônio acessado");
        return ResponseEntity.status(HttpStatus.OK).body(
                patrimonioService.atualizaObjeto(
                        ((UserSS) userDetails).getColaboradorId(),
                        idPatrimonio,
                        patrimonioRequest));
    }

    /**
     * Remoção de patrimônio
     * Esse endpoint tem como objetivo realizar a exclusão de um patrimônio
     *
     * @param userDetails  Dados do usuário logado na sessão atual
     * @param idPatrimonio Id do patrimonio a ser removido
     * @return Retorna objeto Patrimonio removido convertido para o tipo PatrimonioResponse
     */
    @DeleteMapping("/{idPatrimonio}")
    @Tag(name = "Remoção de patrimônio")
    @PreAuthorize("hasAnyRole('PATRIMONIOS')")
    @Operation(summary = "Esse endpoint tem como objetivo realizar a exclusão de um patrimônio", method = "DELETE")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Patrimonios excluídos com sucesso"),
            @ApiResponse(responseCode = "404",
                    description = "Nenhum patrimônio foi encontrado com o id informado",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ObjectNotFoundException.class))}),
            @ApiResponse(responseCode = "400",
                    description = "O patrimônio selecionado já foi excluído",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = InvalidRequestException.class))}),
    })
    public ResponseEntity<PatrimonioResponse> removePatrimonio(@AuthenticationPrincipal UserDetails userDetails,
                                                               @PathVariable UUID idPatrimonio) {
        log.info("Método controlador de remoção de patrimônio acessado");
        return ResponseEntity.status(HttpStatus.OK).body(
                patrimonioService.removeObjeto(
                        ((UserSS) userDetails).getColaboradorId(),
                        idPatrimonio));
    }

    /**
     * Remoção de patrimônio em massa
     * Esse endpoint tem como objetivo realizar a exclusão de patrimônios em massa
     *
     * @param userDetails   Dados do usuário logado na sessão atual
     * @param idsPatrimonio Ids dos patrimônios a serem removidos
     * @return Retorna ResponseEntity contendo status da requisição
     */
    @DeleteMapping
    @Tag(name = "Remoção de patrimônio em massa")
    @PreAuthorize("hasAnyRole('PATRIMONIOS')")
    @Operation(summary = "Esse endpoint tem como objetivo realizar a exclusão de patrimônios em massa",
            method = "DELETE")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Patrimonios excluídos com sucesso"),
            @ApiResponse(responseCode = "404",
                    description = "Nenhum patrimônio foi encontrado com o id informado",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ObjectNotFoundException.class))}),
            @ApiResponse(responseCode = "400",
                    description = "O Patrimônio selecionado já foi excluído",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = InvalidRequestException.class))}),
    })
    public ResponseEntity<?> removePatrimoniosEmMassa(@AuthenticationPrincipal UserDetails userDetails,
                                                      @RequestBody List<UUID> idsPatrimonio) {
        log.info("Método controlador de remoção de patrimônioss em massa acessado");
        patrimonioService.removeEmMassa(
                ((UserSS) userDetails).getColaboradorId(),
                idsPatrimonio);

        return ResponseEntity.ok().build();
    }

    @PostMapping("/relatorio")
    @Tag(name = "Gerar relatório em PDF")
    @PreAuthorize("hasAnyRole('PATRIMONIOS')")
    @Operation(summary = "Esse endpoint tem como objetivo realizar a criação de um relatório em PDF com a listagem de " +
            "patrimônios solicitada", method = "POST")
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
        relatorioUtil.setaAtributosHttpServletResponseRelatorio(res, "patrimonios");
        relatorioService.exportarPdf(
                ((UserSS) userDetails).getColaboradorId(),
                res,
                ids);
    }

}
