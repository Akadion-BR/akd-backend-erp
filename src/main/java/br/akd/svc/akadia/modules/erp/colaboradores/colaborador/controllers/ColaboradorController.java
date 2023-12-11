package br.akd.svc.akadia.modules.erp.colaboradores.colaborador.controllers;

import br.akd.svc.akadia.config.security.UserSS;
import br.akd.svc.akadia.exceptions.InvalidRequestException;
import br.akd.svc.akadia.exceptions.ObjectNotFoundException;
import br.akd.svc.akadia.exceptions.UnauthorizedAccessException;
import br.akd.svc.akadia.modules.erp.colaboradores.acao.models.dto.response.page.AcaoPageResponse;
import br.akd.svc.akadia.modules.erp.colaboradores.acao.services.AcaoService;
import br.akd.svc.akadia.modules.erp.colaboradores.acesso.models.dto.response.page.AcessoPageResponse;
import br.akd.svc.akadia.modules.erp.colaboradores.acesso.services.AcessoService;
import br.akd.svc.akadia.modules.erp.colaboradores.colaborador.models.dto.colaborador.response.ColaboradorResponse;
import br.akd.svc.akadia.modules.erp.colaboradores.colaborador.models.dto.colaborador.response.CriacaoColaboradorResponse;
import br.akd.svc.akadia.modules.erp.colaboradores.colaborador.models.dto.colaborador.response.page.ColaboradorPageResponse;
import br.akd.svc.akadia.modules.erp.colaboradores.colaborador.services.crud.ColaboradorService;
import br.akd.svc.akadia.modules.erp.colaboradores.colaborador.services.report.ColaboradorRelatorioService;
import br.akd.svc.akadia.modules.external.empresa.entity.EmpresaEntity;
import br.akd.svc.akadia.modules.global.objects.imagem.response.ImagemResponse;
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
import org.springframework.web.multipart.MultipartFile;

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
@RequestMapping("/api/sistema/v1/colaborador")
@Produces({MediaType.APPLICATION_JSON, "application/json"})
@Consumes({MediaType.APPLICATION_JSON, "application/json"})
public class ColaboradorController {

    @Autowired
    ColaboradorService colaboradorService;

    @Autowired
    AcaoService acaoService;

    @Autowired
    AcessoService acessoService;

    @Autowired
    RelatorioUtil relatorioUtil;

    @Autowired
    ColaboradorRelatorioService relatorioService;

    /**
     * Cadastro de novo colaborador
     * Este método tem como objetivo disponibilizar o endpoint de acionamento da lógica de criação de novo colaborador
     *
     * @param userDetails         Dados do usuário logado na sessão atual
     * @param contratoColaborador Contrato de admissão do colaborador na firma
     * @param colaborador         Objeto contendo todos os atributos necessários para a criação de um novo colaborador
     * @return Retorna objeto Colaborador criado convertido para o tipo ColaboradorResponse
     * @throws InvalidRequestException Exception lançada caso ocorra alguma falha interna na criação do colaborador
     */
    @PostMapping
    @PreAuthorize("hasAnyRole('COLABORADORES')")
    @Tag(name = "Cadastro de novo colaborador")
    @Operation(summary = "Esse endpoint tem como objetivo realizar a criação de um novo colaborador",
            method = "POST")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201",
                    description = "Colaborador persistido com sucesso",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ColaboradorResponse.class))}),
            @ApiResponse(responseCode = "400",
                    description = "Erro de requisição inválida",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = InvalidRequestException.class))}),
            @ApiResponse(responseCode = "401",
                    description = "O usuário logado não possui permissão suficiente para executar a operação",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = UnauthorizedAccessException.class))}),
    })
    public ResponseEntity<String> criaNovoColaborador(@AuthenticationPrincipal UserDetails userDetails,
                                                      @RequestParam(value = "contratoColaborador", required = false) MultipartFile contratoColaborador,
                                                      @RequestParam("colaborador") String colaborador) throws IOException {
        log.info("Método controlador de criação de novo colaborador acessado");
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(colaboradorService.criaNovoColaborador(
                        ((UserSS) userDetails).getColaboradorId(),
                        contratoColaborador,
                        colaborador));
    }

    /**
     * Cadastro de colaborador raiz
     * Este método tem como objetivo disponibilizar o endpoint de acionamento da lógica de criação de novo colaborador
     *
     * @param userDetails Dados do usuário logado na sessão atual
     * @param empresa     Empresa na qual o colaborador que será persistido pertence
     * @return Retorna objeto Colaborador criado convertido para o tipo ColaboradorResponse
     * @throws InvalidRequestException Exception lançada caso ocorra alguma falha interna na criação do colaborador
     */
    @PostMapping("/cria-colaborador-raiz")
    @PreAuthorize("hasAnyRole('COLABORADORES')")
    @Tag(name = "Cadastro de colaborador raiz da aplicação")
    @Operation(summary = "Esse endpoint tem como objetivo realizar a criação do primeiro colaborador da aplicação",
            method = "POST")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201",
                    description = "Colaborador persistido com sucesso",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ColaboradorResponse.class))}),
            @ApiResponse(responseCode = "400",
                    description = "Erro de requisição inválida",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = InvalidRequestException.class))}),
    })
    public ResponseEntity<CriacaoColaboradorResponse> criaColaboradorRaiz(@AuthenticationPrincipal UserDetails userDetails,
                                                                          @RequestBody EmpresaEntity empresa) {
        log.info("Método controlador de criação de colaborador raiz acessado");
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(colaboradorService.criaColaboradorAdminParaNovaEmpresa(empresa));
    }

    /**
     * Busca paginada de colaboradores
     * Este método tem como objetivo disponibilizar o endpoint de acionamento da lógica de busca paginada de colaboradores
     *
     * @param userDetails Dados do usuário logado na sessão atual
     * @param busca       Parâmetro opcional. Recebe uma string para busca de colaboradores por atributos específicos
     * @param pageable    Contém especificações da paginação, como tamanho da página, página atual, etc
     * @return Retorna objeto do tipo ColaboradorPageResponse, que possui informações da paginação e a lista
     * de colaboradores encontrados inserida em seu body
     */
    @GetMapping
    @PreAuthorize("hasAnyRole('COLABORADORES')")
    @Tag(name = "Busca paginada por colaboradores cadastrados")
    @Operation(summary = "Esse endpoint tem como objetivo realizar a busca paginada de colaboradores cadastrados na " +
            "empresa do usuário que acionou a requisição com os filtros de busca enviados pelo usuário", method = "GET")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "A busca paginada de colaboradores foi realizada com sucesso",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ColaboradorPageResponse.class))}),
    })
    public ResponseEntity<ColaboradorPageResponse> obtemColaboradoresPaginados(@AuthenticationPrincipal UserDetails userDetails,
                                                                               Pageable pageable,
                                                                               @RequestParam(value = "busca", required = false) String busca) {
        log.info("Endpoint de busca paginada por colaboradores acessado. Filtros de busca: {}", busca);
        return ResponseEntity.ok().body(
                colaboradorService.realizaBuscaPaginadaPorColaboradores(
                        ((UserSS) userDetails).getColaboradorId(),
                        pageable,
                        busca));
    }

    /**
     * Busca paginada de acessos do colaborador
     * Este método tem como objetivo disponibilizar o endpoint de acionamento da lógica de obtenção paginada de acessos
     * de um colaborador buscado pelo ID
     *
     * @param pageable      Contém especificações da paginação, como tamanho da página, página atual, etc
     * @param userDetails   Dados do usuário logado na sessão atual
     * @param idColaborador Id do colaborador na qual os acessos devem ser buscados
     * @return Retorna objeto do tipo AcessoPageResponse, que possui informações da paginação e a lista
     * de acessos encontrados inserida em seu body
     */
    @GetMapping("/{idColaborador}/acessos")
    @PreAuthorize("hasAnyRole('COLABORADORES')")
    @Tag(name = "Busca paginada por acessos do colaborador")
    @Operation(summary = "Esse endpoint tem como objetivo realizar a busca paginada de acessos do colaborador",
            method = "GET")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "A busca paginada de advertências do colaborador foi realizada com sucesso",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = AcessoPageResponse.class))}),
    })
    public ResponseEntity<AcessoPageResponse> obtemAcessosColaboradorPaginada(Pageable pageable,
                                                                              @AuthenticationPrincipal UserDetails userDetails,
                                                                              @PathVariable("idColaborador") UUID idColaborador) {
        log.info("Endpoint de busca paginada de acessos do colaborador acessada");
        return ResponseEntity.ok().body(
                acessoService.obtemAcessosColaborador(
                        pageable,
                        ((UserSS) userDetails).getColaboradorId(),
                        idColaborador));
    }

    /**
     * Busca paginada de acoes do colaborador
     * Este método tem como objetivo disponibilizar o endpoint de acionamento da lógica de obtenção paginada de acoes
     * de um colaborador buscado pelo ID
     *
     * @param pageable      Contém especificações da paginação, como tamanho da página, página atual, etc
     * @param userDetails   Dados do usuário logado na sessão atual
     * @param idColaborador Id do colaborador na qual os acoes devem ser buscados
     * @return Retorna objeto do tipo AcaoPageResponse, que possui informações da paginação e a lista
     * de acoes encontrados inserida em seu body
     */
    @GetMapping("/{idColaborador}/acoes")
    @PreAuthorize("hasAnyRole('COLABORADORES')")
    @Tag(name = "Busca paginada por acoes do colaborador")
    @Operation(summary = "Esse endpoint tem como objetivo realizar a busca paginada de acoes do colaborador",
            method = "GET")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "A busca paginada de advertências do colaborador foi realizada com sucesso",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = AcaoPageResponse.class))}),
    })
    public ResponseEntity<AcaoPageResponse> obtemAcoesColaboradorPaginada(Pageable pageable,
                                                                          @AuthenticationPrincipal UserDetails userDetails,
                                                                          @PathVariable("idColaborador") UUID idColaborador) {
        log.info("Endpoint de busca paginada de ações do colaborador acessada");
        return ResponseEntity.ok().body(
                acaoService.obtemAcoesColaborador(
                        pageable,
                        ((UserSS) userDetails).getColaboradorId(),
                        idColaborador));
    }

    /**
     * Busca de imagem de perfil de colaborador
     * Este método tem como objetivo disponibilizar o endpoint de acionamento da lógica de busca de imagem de perfil de
     * um colaborador através do id informado via parâmetro
     *
     * @param userDetails Dados do usuário logado na sessão atual
     * @return Retorna objeto do tipo ImagemResponse, que possui todas as informações relativas à imagem obtida
     */
    @GetMapping("imagem-perfil/{idColaborador}")
    @PreAuthorize("hasAnyRole('COLABORADORES')")
    @Tag(name = "Obtenção de imagem de perfil do colaborador")
    @Operation(summary = "Esse endpoint tem como objetivo realizar a obtenção da imagem de perfil de um " +
            "colaborador", method = "GET")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Imagem de perfil do colaborador obtida com sucesso",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ImagemResponse.class))}),
            @ApiResponse(responseCode = "400",
                    description = "Nenhum colaborador foi encontrado com o id informado",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ObjectNotFoundException.class))}),
    })
    public ResponseEntity<ImagemResponse> obtemImagemDePerfilDoColaborador(@AuthenticationPrincipal UserDetails userDetails,
                                                                           @PathVariable("idColaborador") UUID idColaborador) {
        log.info("Método controlador de obtenção de imagem de perfil de colaborador acessado");
        return ResponseEntity.ok().body(
                colaboradorService.obtemImagemPerfilColaborador(
                        ((UserSS) userDetails).getColaboradorId(),
                        idColaborador));
    }

    /**
     * Busca de imagem de perfil de colaborador
     * Este método tem como objetivo disponibilizar o endpoint de acionamento da lógica de busca de imagem de perfil de
     * um colaborador através do id informado via parâmetro
     *
     * @param userDetails Dados do usuário logado na sessão atual
     * @return Retorna objeto do tipo ImagemResponse, que possui todas as informações relativas à imagem obtida
     */
    @GetMapping("/ocupacoes")
    @PreAuthorize("hasAnyRole('COLABORADORES')")
    @Tag(name = "Obtém todas as ocupações da empresa")
    @Operation(summary = "Esse endpoint tem como objetivo retornar uma lista com todas as ocupações já cadastradas " +
            "da empresa atual", method = "GET")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Ocupações obtidas com sucesso",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = String.class))})
    })
    public ResponseEntity<List<String>> obtemOcupacoes(@AuthenticationPrincipal UserDetails userDetails) {
        log.info("Método controlador de obtenção de ocupações da empresa logada acessado");
        return ResponseEntity.ok().body(
                colaboradorService.obtemTodasOcupacoesEmpresa(
                        ((UserSS) userDetails).getColaboradorId()));
    }

    /**
     * Busca de colaborador por id
     * Este método tem como objetivo disponibilizar o endpoint de acionamento da lógica de busca de colaborador por id
     *
     * @param userDetails Dados do usuário logado na sessão atual
     * @return Retorna objeto do tipo ColaboradorResponse, que possui todas as informações relativas ao colaborador
     */
    @GetMapping("/{idColaborador}")
    @PreAuthorize("hasAnyRole('COLABORADORES')")
    @Tag(name = "Busca de colaborador por id")
    @Operation(summary = "Esse endpoint tem como objetivo realizar a busca de um colaborador pelo id recebido " +
            "pelo parâmetro", method = "GET")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "A busca de colaborador por id foi realizada com sucesso",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ColaboradorResponse.class))}),
            @ApiResponse(responseCode = "404",
                    description = "Nenhum colaborador foi encontrado com o id informado",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ObjectNotFoundException.class))}),
    })
    public ResponseEntity<ColaboradorResponse> obtemColaboradorPorId(@AuthenticationPrincipal UserDetails userDetails,
                                                                     @PathVariable("idColaborador") UUID idColaborador) {
        log.info("Endpoint de busca de colaborador por id acessado. ID recebido: {}", idColaborador);
        return ResponseEntity.ok().body(
                colaboradorService.realizaBuscaDeColaboradorPorId(
                        ((UserSS) userDetails).getColaboradorId(),
                        idColaborador));
    }

    /**
     * Atualização de colaborador
     * Este método tem como objetivo disponibilizar o endpoint de acionamento da lógica de atualização de colaborador
     * por id
     *
     * @param userDetails Dados do usuário logado na sessão atual
     * @return Retorna objeto do tipo ColaboradorResponse, que possui todas as informações relativas ao colaborador
     */
    @PutMapping("/{idColaborador}")
    @PreAuthorize("hasAnyRole('COLABORADORES')")
    @Tag(name = "Atualização de colaborador")
    @Operation(summary = "Esse endpoint tem como objetivo realizar a atualização de um colaborador na base de dados " +
            "da empresa", method = "PUT")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Colaborador atualizado com sucesso",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ColaboradorResponse.class))}),
            @ApiResponse(responseCode = "400",
                    description = "A inscrição estadual informada já existe",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = InvalidRequestException.class))}),
            @ApiResponse(responseCode = "400",
                    description = "O CPF/CNPJ informado já existe",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = InvalidRequestException.class))}),
            @ApiResponse(responseCode = "404",
                    description = "Nenhum colaborador foi encontrado com o id informado",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ObjectNotFoundException.class))}),
    })
    public ResponseEntity<ColaboradorResponse> atualizaColaborador(@AuthenticationPrincipal UserDetails userDetails,
                                                                   @RequestParam(value = "contratoColaborador", required = false) MultipartFile contratoColaborador,
                                                                   @RequestParam("colaborador") String colaborador,
                                                                   @PathVariable("idColaborador") UUID idColaborador) throws IOException {
        log.info("Método controlador de atualização de colaborador acessado");
        return ResponseEntity.ok().body(
                colaboradorService.atualizaColaborador(
                        ((UserSS) userDetails).getColaboradorId(),
                        idColaborador,
                        contratoColaborador,
                        colaborador));
    }

    /**
     * Atualização de imagem de perfil de colaborador
     * Este método tem como objetivo disponibilizar o endpoint de acionamento da lógica de atualização de imagem
     * de perfil de colaborador por id
     *
     * @param userDetails Dados do usuário logado na sessão atual
     * @return Retorna objeto do tipo ColaboradorResponse, que possui todas as informações relativas ao colaborador
     */
    @PutMapping("imagem-perfil/{id}")
    @PreAuthorize("hasAnyRole('COLABORADORES')")
    @Tag(name = "Atualização de imagem de perfil do colaborador")
    @Operation(summary = "Esse endpoint tem como objetivo realizar a atualização da imagem de perfil de um " +
            "colaborador", method = "PUT")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Colaborador atualizado com sucesso",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ColaboradorResponse.class))}),
            @ApiResponse(responseCode = "404",
                    description = "Nenhum colaborador foi encontrado com o id informado",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ObjectNotFoundException.class))}),
    })
    public ResponseEntity<ColaboradorResponse> atualizaImagemPerfilColaborador(@AuthenticationPrincipal UserDetails userDetails,
                                                                               @RequestParam(value = "imagemPerfil", required = false) MultipartFile imagemPerfil,
                                                                               @PathVariable("idColaborador") UUID idColaborador) throws IOException {
        log.info("Método controlador de atualização de imagem de perfil de colaborador acessado");
        return ResponseEntity.ok().body(
                colaboradorService.atualizaImagemPerfilColaborador(
                        ((UserSS) userDetails).getColaboradorId(),
                        idColaborador,
                        imagemPerfil));
    }

    /**
     * Remoção de colaborador por id
     * Este método tem como objetivo disponibilizar o endpoint de acionamento da lógica de remoção de colaborador
     * por id
     *
     * @param userDetails Dados do usuário logado na sessão atual
     * @return Retorna objeto do tipo ColaboradorResponse, que possui todas as informações relativas ao colaborador
     */
    @DeleteMapping("/{idColaborador}")
    @PreAuthorize("hasAnyRole('COLABORADORES')")
    @Tag(name = "Remoção de colaborador")
    @Operation(summary = "Esse endpoint tem como objetivo realizar a exclusão de um colaborador", method = "PUT")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Colaborador excluído com sucesso",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ColaboradorResponse.class))}),
            @ApiResponse(responseCode = "404",
                    description = "Nenhum colaborador foi encontrado com o id informado",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ObjectNotFoundException.class))}),
            @ApiResponse(responseCode = "400",
                    description = "Não é possível remover um colaborador que já foi excluído",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = InvalidRequestException.class))}),
    })
    public ResponseEntity<ColaboradorResponse> removeColaborador(@AuthenticationPrincipal UserDetails userDetails,
                                                                 @PathVariable("idColaborador") UUID idColaborador) {
        log.info("Método controlador de remoção de colaborador acessado");
        return ResponseEntity.ok().body(
                colaboradorService.removeColaborador(
                        ((UserSS) userDetails).getColaboradorId(),
                        idColaborador));
    }

    /**
     * Remoção de colaboradores em massa
     * Este método tem como objetivo disponibilizar o endpoint de acionamento da lógica de remoção de colaboradores
     * em massa através dos ids recebidos por parâmetro
     *
     * @param userDetails Dados do usuário logado na sessão atual
     */
    @DeleteMapping
    @PreAuthorize("hasAnyRole('COLABORADORES')")
    @Tag(name = "Remoção de colaborador em massa")
    @Operation(summary = "Esse endpoint tem como objetivo realizar a exclusão de colaboradores em massa",
            method = "DELETE")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204",
                    description = "Colaboradores excluídos com sucesso",
                    content = {@Content(mediaType = "application/json")}),
            @ApiResponse(responseCode = "404",
                    description = "Nenhum colaborador foi encontrado com o id informado",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ObjectNotFoundException.class))}),
            @ApiResponse(responseCode = "400",
                    description = "Não é possível remover um colaborador que já foi excluído",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = InvalidRequestException.class))}),
    })
    public ResponseEntity<?> removeColaboradoresEmMassa(@AuthenticationPrincipal UserDetails userDetails,
                                                        @RequestBody List<UUID> idsColaborador) {
        log.info("Método controlador de remoção de colaboradores em massa acessado");
        colaboradorService.removeColaboradoresEmMassa(
                ((UserSS) userDetails).getColaboradorId(), idsColaborador);

        return ResponseEntity.noContent().build();
    }

    /**
     * Criação de relatório de colaboradores em PDF
     * Este método tem como objetivo disponibilizar o endpoint de acionamento da lógica de criação de relatório de
     * colaboradores registrados em PDF
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
    @PreAuthorize("hasAnyRole('COLABORADORES')")
    @Operation(summary = "Esse endpoint tem como objetivo realizar a criação de um relatório em PDF com a listagem de " +
            "colaboradores solicitada", method = "POST")
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
        log.info("Método controlador de obtenção de relatório de colaboradores em PDF acessado. IDs: {}", ids);
        relatorioUtil.setaAtributosHttpServletResponseRelatorio(res, "colaboradores");
        relatorioService.exportarPdf(
                ((UserSS) userDetails).getColaboradorId(),
                res,
                ids);
    }

}
