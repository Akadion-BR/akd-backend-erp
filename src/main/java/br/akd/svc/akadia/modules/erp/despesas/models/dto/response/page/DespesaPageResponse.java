package br.akd.svc.akadia.modules.erp.despesas.models.dto.response.page;

import br.akd.svc.akadia.modules.erp.despesas.models.dto.response.DespesaResponse;
import br.akd.svc.akadia.modules.erp.despesas.models.entity.DespesaEntity;
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
public class DespesaPageResponse {
    List<DespesaResponse> content;
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

    public DespesaPageResponse buildDespesaPageResponse(Page<DespesaEntity> despesasEntity) {
        log.info("Método de conversão de despesas do tipo Entity para despesas do tipo Response acessado");

        log.info("Criando lista vazia de objetos do tipo DespesaResponse...");
        List<DespesaResponse> despesasResponse = new ArrayList<>();

        log.info("Iniciando iteração da lista de DespesaEntity obtida na busca para conversão para objetos do tipo " +
                "DespesaResponse...");
        for (DespesaEntity despesa : despesasEntity.getContent()) {
            DespesaResponse despesaResponse = new DespesaResponse().buildFromEntity(despesa);
            despesasResponse.add(despesaResponse);
        }
        log.info("Iteração finalizada com sucesso. Listagem de objetos do tipo DespesaResponse preenchida");

        log.info("Iniciando criação de objeto do tipo DespesaPageResponse, que possui todas as informações referentes " +
                "ao conteúdo da página e à paginação...");
        despesasEntity.getPageable();
        DespesaPageResponse despesaPageResponse = DespesaPageResponse.builder()
                .content(despesasResponse)
                .numberOfElements(despesasEntity.getNumberOfElements())
                .pageNumber(despesasEntity.getPageable().getPageNumber())
                .pageSize(despesasEntity.getPageable().getPageSize())
                .size(despesasEntity.getSize())
                .totalElements(despesasEntity.getTotalElements())
                .totalPages(despesasEntity.getTotalPages())
                .build();

        log.info("Objeto do tipo DespesaPageResponse criado com sucesso. Retornando objeto...");
        return despesaPageResponse;
    }
}
