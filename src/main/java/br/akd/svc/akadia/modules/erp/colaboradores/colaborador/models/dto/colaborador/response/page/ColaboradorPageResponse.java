package br.akd.svc.akadia.modules.erp.colaboradores.colaborador.models.dto.colaborador.response.page;

import br.akd.svc.akadia.modules.erp.colaboradores.colaborador.models.dto.colaborador.response.ColaboradorResponse;
import br.akd.svc.akadia.modules.erp.colaboradores.colaborador.models.entity.colaborador.ColaboradorEntity;
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
public class ColaboradorPageResponse {
    List<ColaboradorResponse> content;
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

    public ColaboradorPageResponse buildPageResponse(Page<ColaboradorEntity> colaboradorPage) {
        log.info("Método de conversão de colaboradores do tipo Entity para colaboradores do tipo Response acessado");

        log.info("Criando lista vazia de objetos do tipo ColaboradorResponse...");
        List<ColaboradorResponse> colaboradoresResponse = new ArrayList<>();

        log.info("Iniciando iteração da lista de ColaboradorEntity obtida na busca para conversão para objetos " +
                "do tipo ColaboradorResponse...");
        for (ColaboradorEntity colaborador : colaboradorPage.getContent()) {
            ColaboradorResponse colaboradorResponse = new ColaboradorResponse().buildFromEntity(colaborador);
            colaboradoresResponse.add(colaboradorResponse);
        }
        log.info("Iteração finalizada com sucesso. Listagem de objetos do tipo ColaboradorResponse preenchida");

        log.info("Iniciando criação de objeto do tipo ColaboradorPageResponse, que possui todas as " +
                "informações referentes ao conteúdo da página e à paginação...");
        colaboradorPage.getPageable();
        ColaboradorPageResponse colaboradorPageResponse = ColaboradorPageResponse.builder()
                .content(colaboradoresResponse)
                .numberOfElements(colaboradorPage.getNumberOfElements())
                .pageNumber(colaboradorPage.getPageable().getPageNumber())
                .pageSize(colaboradorPage.getPageable().getPageSize())
                .size(colaboradorPage.getSize())
                .totalElements(colaboradorPage.getTotalElements())
                .totalPages(colaboradorPage.getTotalPages())
                .build();

        log.info("Objeto do tipo ColaboradorPageResponse criado com sucesso. Retornando objeto...");
        return colaboradorPageResponse;
    }
}
