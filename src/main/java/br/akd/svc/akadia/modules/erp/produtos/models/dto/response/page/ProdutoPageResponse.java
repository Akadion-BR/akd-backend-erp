package br.akd.svc.akadia.modules.erp.produtos.models.dto.response.page;

import br.akd.svc.akadia.modules.erp.produtos.models.dto.response.ProdutoResponse;
import br.akd.svc.akadia.modules.erp.produtos.models.entity.ProdutoEntity;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Setter
@Getter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ProdutoPageResponse {
    List<ProdutoResponse> content;
    Boolean empty;
    Boolean first;
    Boolean last;
    Integer number;
    Integer numberOfElements;
    Integer pageNumber;
    Integer pageSize;
    Boolean paged;
    Boolean unpaged;
    Integer size;
    Long totalElements;
    Integer totalPages;

    public ProdutoPageResponse constroiProdutoPageResponse(Page<ProdutoEntity> produtosEntity) {
        log.info("Método de conversão de clientes do tipo Entity para clientes do tipo Response acessado");

        log.info("Criando lista vazia de objetos do tipo ProdutoResponse...");
        List<ProdutoResponse> clientesResponse = new ArrayList<>();

        log.info("Iniciando iteração da lista de ProdutoEntity obtida na busca para conversão para objetos do tipo " +
                "ProdutoResponse...");
        for (ProdutoEntity produto : produtosEntity.getContent()) {
            ProdutoResponse produtoResponse = new ProdutoResponse().buildFromEntity(produto);
            clientesResponse.add(produtoResponse);
        }
        log.info("Iteração finalizada com sucesso. Listagem de objetos do tipo ProdutoResponse preenchida");

        log.info("Iniciando criação de objeto do tipo ProdutoPageResponse, que possui todas as informações referentes " +
                "ao conteúdo da página e à paginação...");
        produtosEntity.getPageable();
        ProdutoPageResponse produtoPageResponse = ProdutoPageResponse.builder()
                .content(clientesResponse)
                .numberOfElements(produtosEntity.getNumberOfElements())
                .pageNumber(produtosEntity.getPageable().getPageNumber())
                .pageSize(produtosEntity.getPageable().getPageSize())
                .size(produtosEntity.getSize())
                .totalElements(produtosEntity.getTotalElements())
                .totalPages(produtosEntity.getTotalPages())
                .build();

        log.info("Objeto do tipo ProdutoPageResponse criado com sucesso. Retornando objeto...");
        return produtoPageResponse;
    }
}
