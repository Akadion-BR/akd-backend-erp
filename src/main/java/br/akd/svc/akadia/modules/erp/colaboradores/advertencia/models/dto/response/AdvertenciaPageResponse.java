package br.akd.svc.akadia.modules.erp.colaboradores.advertencia.models.dto.response;

import br.akd.svc.akadia.modules.erp.colaboradores.advertencia.models.entity.AdvertenciaEntity;
import br.akd.svc.akadia.modules.erp.colaboradores.colaborador.models.dto.advertencia.AdvertenciaResponse;
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
public class AdvertenciaPageResponse {
    List<AdvertenciaResponse> content;
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

    public AdvertenciaPageResponse buildPageResponse(Page<AdvertenciaEntity> advertenciaPage) {
        log.info("Método de conversão de advertencias do tipo Entity para advertencias do tipo Response acessado");

        log.info("Criando lista vazia de objetos do tipo AdvertenciaResponse...");
        List<AdvertenciaResponse> advertenciasResponse = new ArrayList<>();

        log.info("Iniciando iteração da lista de AdvertenciaEntity obtida na busca para conversão para objetos " +
                "do tipo AdvertenciaResponse...");
        for (AdvertenciaEntity advertencia : advertenciaPage.getContent()) {
            AdvertenciaResponse advertenciaResponse = new AdvertenciaResponse().buildResponseFromEntity(advertencia);
            advertenciasResponse.add(advertenciaResponse);
        }
        log.info("Iteração finalizada com sucesso. Listagem de objetos do tipo AdvertenciaResponse preenchida");

        log.info("Iniciando criação de objeto do tipo AdvertenciaPageResponse, que possui todas as " +
                "informações referentes ao conteúdo da página e à paginação...");
        advertenciaPage.getPageable();
        AdvertenciaPageResponse advertenciaPageResponse = AdvertenciaPageResponse.builder()
                .content(advertenciasResponse)
                .numberOfElements(advertenciaPage.getNumberOfElements())
                .pageNumber(advertenciaPage.getPageable().getPageNumber())
                .pageSize(advertenciaPage.getPageable().getPageSize())
                .size(advertenciaPage.getSize())
                .totalElements(advertenciaPage.getTotalElements())
                .totalPages(advertenciaPage.getTotalPages())
                .build();

        log.info("Objeto do tipo AdvertenciaPageResponse criado com sucesso. Retornando objeto...");
        return advertenciaPageResponse;
    }
}
