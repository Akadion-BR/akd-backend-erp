package br.akd.svc.akadia.modules.erp.colaboradores.acao.models.dto.response.page;

import br.akd.svc.akadia.modules.erp.colaboradores.acao.models.dto.response.AcaoResponse;
import br.akd.svc.akadia.modules.erp.colaboradores.acao.models.entity.AcaoEntity;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class AcaoPageResponse {
    List<AcaoResponse> content;
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

    public AcaoPageResponse buildPageResponse(Page<AcaoEntity> acaoEntityPage) {
        log.info("Método de conversão de acessos do tipo Entity para acessos do tipo Response acessado");

        log.info("Criando lista vazia de objetos do tipo AcessoResponse...");
        List<AcaoResponse> acoesResponse = new ArrayList<>();

        log.info("Iniciando iteração da lista de AcessoEntity obtida na busca para conversão para objetos " +
                "do tipo AcessoResponse...");
        for (AcaoEntity acao : acaoEntityPage.getContent()) {
            AcaoResponse acaoResponse = new AcaoResponse().buildFromEntity(acao);
            acoesResponse.add(acaoResponse);
        }
        log.info("Iteração finalizada com sucesso. Listagem de objetos do tipo AcessoResponse preenchida");

        log.info("Iniciando criação de objeto do tipo AcessoPageResponse, que possui todas as " +
                "informações referentes ao conteúdo da página e à paginação...");
        acaoEntityPage.getPageable();
        AcaoPageResponse acaoPageResponse = AcaoPageResponse.builder()
                .content(acoesResponse)
                .numberOfElements(acaoEntityPage.getNumberOfElements())
                .pageNumber(acaoEntityPage.getPageable().getPageNumber())
                .pageSize(acaoEntityPage.getPageable().getPageSize())
                .size(acaoEntityPage.getSize())
                .totalElements(acaoEntityPage.getTotalElements())
                .totalPages(acaoEntityPage.getTotalPages())
                .build();

        log.info("Objeto do tipo ColaboradorPageResponse criado com sucesso. Retornando objeto...");
        return acaoPageResponse;
    }
}
