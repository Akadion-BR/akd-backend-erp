package br.akd.svc.akadia.modules.erp.colaboradores.advertencia.controllers;

import br.akd.svc.akadia.config.security.UserSS;
import br.akd.svc.akadia.exceptions.InvalidRequestException;
import br.akd.svc.akadia.modules.erp.colaboradores.advertencia.models.dto.response.AdvertenciaPageResponse;
import br.akd.svc.akadia.modules.erp.colaboradores.advertencia.models.enums.StatusAdvertenciaEnum;
import br.akd.svc.akadia.modules.erp.colaboradores.advertencia.services.AdvertenciaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.io.IOException;
import java.util.UUID;

@Slf4j
@CrossOrigin
@RestController
@RequestMapping("/api/sistema/v1/advertencia")
@Produces({MediaType.APPLICATION_JSON, "application/json"})
@Consumes({MediaType.APPLICATION_JSON, "application/json"})
public class AdvertenciaController {

    @Autowired
    AdvertenciaService advertenciaService;

    /**
     * Cadastro de nova advertência
     * Este método tem como objetivo disponibilizar o endpoint de acionamento da lógica de criação de nova advertência
     *
     * @param userDetails   Dados do usuário logado na sessão atual
     * @param res           Objeto que visa retornar o arquivo da advertência em PDF
     * @param idColaborador Id do colaborador na qual a advertência será aplicada
     * @param advertencia   Json em String contendo dados da advertência que deverá ser gerada
     * @return Retorna ResponseEntity com status da requisição
     */
    @PostMapping("/{idColaborador}")
    @PreAuthorize("hasAnyRole('COLABORADORES')")
    @Tag(name = "Criação de nova advertência")
    @Operation(summary = "Esse endpoint tem como objetivo realizar a criação de uma advertência e atribuí-la a um " +
            "colaborador específico",
            method = "POST")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201",
                    description = "Advertência persistida com sucesso",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = String.class))}),
            @ApiResponse(responseCode = "400",
                    description = "Erro de requisição inválida",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = InvalidRequestException.class))}),
    })
    public ResponseEntity<?> criaNovaAdvertencia(@AuthenticationPrincipal UserDetails userDetails,
                                                 HttpServletResponse res,
                                                 @PathVariable(value = "idColaborador") UUID idColaborador,
                                                 @RequestParam(value = "arquivoAdvertencia", required = false) MultipartFile arquivoAdvertencia,
                                                 @RequestParam("advertencia") String advertencia) throws IOException {
        log.info("Método controlador de criação de nova advertência acessado");
        advertenciaService.geraAdvertenciaColaborador(
                ((UserSS) userDetails).getColaboradorId(),
                res,
                idColaborador,
                arquivoAdvertencia,
                advertencia);
        return ResponseEntity.ok().build();
    }

    /**
     * Busca paginada de advertências
     * Este método tem como objetivo disponibilizar o endpoint de acionamento da lógica de busca paginada
     * de advertências
     *
     * @param pageable      Contém especificações da paginação, como tamanho da página, página atual, etc
     * @param userDetails   Dados do usuário logado na sessão atual
     * @param idColaborador Id do colaborador no qual as advertências deverão ser buscadas
     * @return Retorna objeto do tipo AdvertenciaPageResponse, que possui informações da paginação e a lista
     * de advertências encontrados inserida em seu body
     */
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('COLABORADORES')")
    @Tag(name = "Busca paginada por advertências do colaborador")
    @Operation(summary = "Esse endpoint tem como objetivo realizar a busca paginada de advertências do colaborador",
            method = "GET")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "A busca paginada de advertências do colaborador foi realizada com sucesso",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = AdvertenciaPageResponse.class))}),
    })
    public ResponseEntity<AdvertenciaPageResponse> obtemAdvertenciasColaboradorPaginada(Pageable pageable,
                                                                                        @AuthenticationPrincipal UserDetails userDetails,
                                                                                        @PathVariable("idColaborador") UUID idColaborador) {
        log.info("Endpoint de busca paginada de advertências do colaborador acessada");
        return ResponseEntity.ok().body(
                advertenciaService.obtemAdvertenciasPaginadasColaborador(
                        pageable,
                        ((UserSS) userDetails).getColaboradorId(),
                        idColaborador));
    }

    /**
     * Busca paginada de advertências
     * Este método tem como objetivo disponibilizar o endpoint de acionamento da lógica de busca paginada
     * de advertências
     *
     * @param userDetails   Dados do usuário logado na sessão atual
     * @param res           Objeto que visa retornar o arquivo da advertência em PDF
     * @param idColaborador Id do colaborador no qual a advertência pertence
     * @param idAdvertencia Id da advertência
     * @return Retorna ResponseEntity com status da requisição
     */
    @GetMapping("/pdf-padrao/{idColaborador}/{idAdvertencia}")
    @PreAuthorize("hasAnyRole('COLABORADORES')")
    @Tag(name = "Obtenção de PDF padrão")
    @Operation(summary = "Esse endpoint tem como objetivo realizar a obtenção do PDF padrão da advertência recebida",
            method = "GET")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "PDF obtido com sucesso",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = String.class))}),
            @ApiResponse(responseCode = "400", description = "Erro de requisição inválida",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = InvalidRequestException.class))}),
    })
    public ResponseEntity<?> obtemPdfPadraoAdvertencia(@AuthenticationPrincipal UserDetails userDetails,
                                                       HttpServletResponse res,
                                                       @PathVariable(value = "idColaborador") UUID idColaborador,
                                                       @PathVariable(value = "idAdvertencia") UUID idAdvertencia) throws IOException {
        log.info("Método controlador de obtenção de PDF padrão da advertência acessado");
        advertenciaService.geraPdfPadraoAdvertencia(
                ((UserSS) userDetails).getColaboradorId(),
                res,
                idColaborador,
                idAdvertencia);
        return ResponseEntity.ok().build();
    }

    /**
     * Obtenção de anexo de advertência
     * Esse endpoint tem como objetivo realizar a obtenção de anexo de uma advertência
     *
     * @param userDetails   Dados do usuário logado na sessão atual
     * @param idColaborador Id do colaborador no qual a advertência pertence
     * @param idAdvertencia Id da advertência
     * @return Retorna arquivo da advertência
     */
    @GetMapping("/obtem-anexo/{idColaborador}/{idAdvertencia}")
    @PreAuthorize("hasAnyRole('COLABORADORES')")
    @Tag(name = "Obtem anexo de advertência")
    @Operation(summary = "Esse endpoint tem como objetivo realizar a obtenção de anexo de uma advertência",
            method = "GET")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Anexo obtido com sucesso",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = String.class))}),
            @ApiResponse(responseCode = "400", description = "Erro de requisição inválida",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = InvalidRequestException.class))}),
    })
    public ResponseEntity<byte[]> obtemAnexoAdvertencia(@AuthenticationPrincipal UserDetails userDetails,
                                                        @PathVariable(value = "idColaborador") UUID idColaborador,
                                                        @PathVariable(value = "idAdvertencia") UUID idAdvertencia) {
        log.info("Método controlador de obtenção de anexo de advertência acessado");
        return ResponseEntity.ok().body(advertenciaService.obtemAnexoAdvertencia(
                ((UserSS) userDetails).getColaboradorId(),
                idColaborador,
                idAdvertencia).getArquivo());
    }

    /**
     * Alteração de status da advertência
     * Esse endpoint tem como objetivo realizar a alteração do status da advertência
     *
     * @param userDetails           Dados do usuário logado na sessão atual
     * @param statusAdvertenciaEnum Status da advertência a ser atualizado
     * @param idColaborador         Id do colaborador no qual a advertência pertence
     * @param idAdvertencia         Id da advertência
     * @return Retorna ResponseEntity com status da requisição
     */
    @GetMapping("/altera-status/{idColaborador}/{idAdvertencia}")
    @PreAuthorize("hasAnyRole('COLABORADORES')")
    @Tag(name = "Alteração de status da advertência")
    @Operation(summary = "Esse endpoint tem como objetivo realizar a alteração do status da advertência",
            method = "GET")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Status da advertência alterado com sucesso",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = String.class))}),
            @ApiResponse(responseCode = "400", description = "Erro de requisição inválida",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = InvalidRequestException.class))}),
    })
    public ResponseEntity<?> alteraStatusAdvertencia(@AuthenticationPrincipal UserDetails userDetails,
                                                     @RequestBody String statusAdvertenciaEnum,
                                                     @PathVariable(value = "idColaborador") UUID idColaborador,
                                                     @PathVariable(value = "idAdvertencia") UUID idAdvertencia) {
        log.info("Método controlador de alteração de status da advertência acessado");
        advertenciaService.alteraStatusAdvertencia(
                ((UserSS) userDetails).getColaboradorId(),
                StatusAdvertenciaEnum.valueOf(statusAdvertenciaEnum),
                idColaborador,
                idAdvertencia);
        return ResponseEntity.ok().build();
    }

    /**
     * Anexação de arquivo na advertência
     * Esse endpoint tem como objetivo realizar a chamada à lógica de anexação de arquivo em uma advertência
     *
     * @param userDetails   Dados do usuário logado na sessão atual
     * @param anexo         Arquivo a ser anexado no objeto da advertência
     * @param idColaborador Id do colaborador no qual a advertência pertence
     * @param idAdvertencia Id da advertência
     * @return Retorna ResponseEntity com status da requisição
     */
    @GetMapping("/anexa-documento/{idColaborador}/{idAdvertencia}")
    @PreAuthorize("hasAnyRole('COLABORADORES')")
    @Tag(name = "Anexação de arquivo na advertência")
    @Operation(summary = "Esse endpoint tem como objetivo realizar a chamada à lógica de anexação de arquivo em " +
            "uma advertência", method = "GET")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Arquivo anexado com sucesso",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = String.class))}),
            @ApiResponse(responseCode = "400", description = "Erro de requisição inválida",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = InvalidRequestException.class))}),
    })
    public ResponseEntity<?> anexaArquivoAdvertencia(@AuthenticationPrincipal UserDetails userDetails,
                                                     @RequestParam(value = "anexo", required = false) MultipartFile anexo,
                                                     @PathVariable(value = "idColaborador") UUID idColaborador,
                                                     @PathVariable(value = "idAdvertencia") UUID idAdvertencia) throws IOException {
        log.info("Método controlador de anexação de arquivo em advertência acessado");
        advertenciaService.anexaArquivoAdvertencia(
                ((UserSS) userDetails).getColaboradorId(),
                anexo,
                idColaborador,
                idAdvertencia);
        return ResponseEntity.ok().build();
    }

    /**
     * Alteração de status da advertência
     * Esse endpoint tem como objetivo realizar a alteração do status da advertência
     *
     * @param userDetails   Dados do usuário logado na sessão atual
     * @param idColaborador Id do colaborador no qual a advertência pertence
     * @param idAdvertencia Id da advertência
     * @return Retorna ResponseEntity com status da requisição
     */
    @DeleteMapping("/{idColaborador}/{idAdvertencia}")
    @PreAuthorize("hasAnyRole('COLABORADORES')")
    @Tag(name = "Remove uma advertência")
    @Operation(summary = "Esse endpoint tem como objetivo realizar a remoção de uma advertência",
            method = "DELETE")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Advertência removida com sucesso",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = String.class))}),
            @ApiResponse(responseCode = "400", description = "Erro de requisição inválida",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = InvalidRequestException.class))}),
    })
    public ResponseEntity<?> removeAdvertencia(@AuthenticationPrincipal UserDetails userDetails,
                                               @PathVariable(value = "idColaborador") UUID idColaborador,
                                               @PathVariable(value = "idAdvertencia") UUID idAdvertencia) {
        log.info("Método controlador de exclusão da advertência acessado");
        advertenciaService.removerAdvertencia(
                ((UserSS) userDetails).getColaboradorId(),
                idColaborador,
                idAdvertencia);
        return ResponseEntity.ok().build();
    }

}
