package br.akd.svc.akadia.modules.erp.colaboradores.acesso.models.dto.response.page;

import br.akd.svc.akadia.modules.erp.colaboradores.acesso.models.dto.response.AcessoResponse;
import br.akd.svc.akadia.modules.erp.colaboradores.acesso.models.entity.AcessoEntity;
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
public class AcessoPageResponse {
    List<AcessoResponse> content;
    Boolean empty;
    Boolean first;
    Boolean last;
    Integer number;
    Integer numberOfElements;
    Long offset;
    Integer pageNumber;
    Integer pageSize;
    Boolean paged;
    Boolean unpaged;
    Integer size;
    Long totalElements;
    Integer totalPages;

    public AcessoPageResponse buildPageResponse(Page<AcessoEntity> acessoPage) {
        log.info("Método de conversão de acessos do tipo Entity para acessos do tipo Response acessado");

        log.info("Criando lista vazia de objetos do tipo AcessoResponse...");
        List<AcessoResponse> acessosResponse = new ArrayList<>();

        log.info("Iniciando iteração da lista de AcessoEntity obtida na busca para conversão para objetos " +
                "do tipo AcessoResponse...");
        for (AcessoEntity acesso : acessoPage.getContent()) {
            AcessoResponse acessoResponse = new AcessoResponse().buildFromEntity(acesso);
            acessosResponse.add(acessoResponse);
        }
        log.info("Iteração finalizada com sucesso. Listagem de objetos do tipo AcessoResponse preenchida");

        log.info("Iniciando criação de objeto do tipo AcessoPageResponse, que possui todas as " +
                "informações referentes ao conteúdo da página e à paginação...");
        acessoPage.getPageable();
        AcessoPageResponse acessoPageResponse = AcessoPageResponse.builder()
                .content(acessosResponse)
                .numberOfElements(acessoPage.getNumberOfElements())
                .pageNumber(acessoPage.getPageable().getPageNumber())
                .pageSize(acessoPage.getPageable().getPageSize())
                .size(acessoPage.getSize())
                .totalElements(acessoPage.getTotalElements())
                .totalPages(acessoPage.getTotalPages())
                .build();

        log.info("Objeto do tipo ColaboradorPageResponse criado com sucesso. Retornando objeto...");
        return acessoPageResponse;
    }
}
