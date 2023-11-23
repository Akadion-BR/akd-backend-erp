package br.akd.svc.akadia.modules.erp.produtos.controllers;

import br.akd.svc.akadia.config.security.UserSS;
import br.akd.svc.akadia.exceptions.InvalidRequestException;
import br.akd.svc.akadia.exceptions.ObjectNotFoundException;
import br.akd.svc.akadia.modules.erp.clientes.models.dto.response.ClienteResponse;
import br.akd.svc.akadia.modules.erp.produtos.models.dto.request.ProdutoRequest;
import br.akd.svc.akadia.modules.erp.produtos.models.dto.response.ProdutoResponse;
import br.akd.svc.akadia.modules.erp.produtos.models.dto.response.page.ProdutoPageResponse;
import br.akd.svc.akadia.modules.erp.produtos.services.ProdutoService;
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

import javax.validation.Valid;
import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.UUID;

/**
 * ProdutoController
 * Esta classe fornece os endpoints para acessar as regras lógicas de negócio referentes à entidade ProdutoEntity
 *
 * @author Gabriel Lagrota
 */
@Slf4j
@CrossOrigin
@RestController
@RequestMapping("/api/sistema/v1/estoque")
@Produces({MediaType.APPLICATION_JSON, "application/json"})
@Consumes({MediaType.APPLICATION_JSON, "application/json"})
public class ProdutoController {

    @Autowired
    ProdutoService produtoService;

    /**
     * Criação de novo produto
     * Esse endpoint tem como objetivo realizar a criação de um novo produto na base dados da empresa
     *
     * @param userDetails    Dados do usuário logado na sessão atual
     * @param produtoRequest Objeto contendo todos os atributos necessários para a criação de um novo produto
     * @return Retorna objeto Produto criado convertido para o tipo ProdutoResponse
     * @throws InvalidRequestException Exception lançada caso ocorra alguma falha interna na criação do produto
     */
    @PostMapping
    @PreAuthorize("hasAnyRole('ESTOQUE')")
    @Tag(name = "Criação de novo produto")
    @Operation(summary = "Esse endpoint tem como objetivo realizar a criação de um novo produto na base dados " +
            "da empresa", method = "POST")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201",
                    description = "Produto persistido com sucesso",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ClienteResponse.class))}),
            @ApiResponse(responseCode = "400",
                    description = "O produto informado já existe",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = InvalidRequestException.class))}),
            @ApiResponse(responseCode = "400",
                    description = "Erro de requisição inválida",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = InvalidRequestException.class))}),
    })
    public ResponseEntity<ProdutoResponse> criaNovoProduto(@AuthenticationPrincipal UserDetails userDetails,
                                                           @Valid @RequestBody ProdutoRequest produtoRequest) {
        log.info("Método controlador de criação de novo produto acessado");
        return ResponseEntity.status(HttpStatus.CREATED).body(
                produtoService.criaNovoProduto(
                        ((UserSS) userDetails).getColaboradorId(),
                        produtoRequest));
    }

    /**
     * Busca paginada por produtos cadastrados
     * Esse endpoint tem como objetivo realizar a busca paginada de produtos cadastrados na empresa do usuário que
     * acionou a requisição com os filtros de busca enviados pelo usuário
     *
     * @param userDetails Dados do usuário logado na sessão atual
     * @param busca       Parâmetro opcional. Recebe uma string para busca de produtos por atributos específicos
     * @param pageable    Contém especificações da paginação, como tamanho da página, página atual, etc
     * @return Retorna objeto do tipo ProdutoPageResponse, que possui informações da paginação e a lista de produtos
     * encontrados inserida em seu body
     */
    @GetMapping
    @PreAuthorize("hasAnyRole('ESTOQUE')")
    @Tag(name = "Busca paginada por produtos cadastrados")
    @Operation(summary = "Esse endpoint tem como objetivo realizar a busca paginada de produtos cadastrados na " +
            "empresa do usuário que acionou a requisição com os filtros de busca enviados pelo usuário",
            method = "GET")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "A busca paginada de produtos foi realizada com sucesso",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ProdutoPageResponse.class))}),
    })
    public ResponseEntity<ProdutoPageResponse> obtemProdutosPaginados(@AuthenticationPrincipal UserDetails userDetails,
                                                                      @RequestParam(value = "busca", required = false) String busca,
                                                                      Pageable pageable) {
        log.info("Endpoint de busca paginada por produtos acessado. Filtros de busca: {}", busca);
        return ResponseEntity.ok().body(
                produtoService.realizaBuscaPaginada(
                        ((UserSS) userDetails).getColaboradorId(),
                        pageable,
                        busca));
    }

    /**
     * Busca de produto por id
     * Esse endpoint tem como objetivo realizar a busca de um produto pelo id recebido pelo parâmetro
     *
     * @param userDetails Dados do usuário logado na sessão atual
     * @param idProduto   Id do produto a ser buscado
     * @return Retorna objeto Produto encontrado convertido para o tipo ProdutoResponse
     */
    @GetMapping("/{idProduto}")
    @Tag(name = "Busca de produto por id")
    @PreAuthorize("hasAnyRole('ESTOQUE')")
    @Operation(summary = "Esse endpoint tem como objetivo realizar a busca de um produto pelo id recebido pelo " +
            "parâmetro", method = "GET")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "A busca de produto por id foi realizada com sucesso",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ProdutoResponse.class))}),
            @ApiResponse(responseCode = "404",
                    description = "Nenhum produto foi encontrado com o id informado",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ObjectNotFoundException.class))})
    })
    public ResponseEntity<ProdutoResponse> obtemObjetoPorId(@AuthenticationPrincipal UserDetails userDetails,
                                                            @PathVariable("idProduto") UUID idProduto) {
        log.info("Endpoint de busca de produto por id acessado. ID recebido: {}", idProduto);
        return ResponseEntity.ok().body(
                produtoService.realizaBuscaPorId(
                        ((UserSS) userDetails).getColaboradorId(),
                        idProduto));
    }

}
