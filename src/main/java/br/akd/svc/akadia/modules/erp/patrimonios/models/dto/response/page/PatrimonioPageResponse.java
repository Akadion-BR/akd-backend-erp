package br.akd.svc.akadia.modules.erp.patrimonios.models.dto.response.page;

import br.akd.svc.akadia.modules.erp.patrimonios.models.dto.response.PatrimonioResponse;
import br.akd.svc.akadia.modules.erp.patrimonios.models.entity.PatrimonioEntity;
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
public class PatrimonioPageResponse {
    List<PatrimonioResponse> content;
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

    public PatrimonioPageResponse buildPatrimonioPageResponse(Page<PatrimonioEntity> patrimoniosEntity) {
        log.info("Método de conversão de patrimonios do tipo Entity para patrimonios do tipo Response acessado");

        log.info("Criando lista vazia de objetos do tipo PatrimonioResponse...");
        List<PatrimonioResponse> patrimoniosResponse = new ArrayList<>();

        log.info("Iniciando iteração da lista de PatrimonioEntity obtida na busca para conversão para objetos do tipo " +
                "PatrimonioResponse...");
        for (PatrimonioEntity patrimonio : patrimoniosEntity.getContent()) {
            PatrimonioResponse patrimonioResponse = new PatrimonioResponse().buildFromEntity(patrimonio);
            patrimoniosResponse.add(patrimonioResponse);
        }
        log.info("Iteração finalizada com sucesso. Listagem de objetos do tipo PatrimonioResponse preenchida");

        log.info("Iniciando criação de objeto do tipo PatrimonioPageResponse, que possui todas as informações referentes " +
                "ao conteúdo da página e à paginação...");
        patrimoniosEntity.getPageable();
        PatrimonioPageResponse patrimonioPageResponse = PatrimonioPageResponse.builder()
                .content(patrimoniosResponse)
                .numberOfElements(patrimoniosEntity.getNumberOfElements())
                .pageNumber(patrimoniosEntity.getPageable().getPageNumber())
                .pageSize(patrimoniosEntity.getPageable().getPageSize())
                .size(patrimoniosEntity.getSize())
                .totalElements(patrimoniosEntity.getTotalElements())
                .totalPages(patrimoniosEntity.getTotalPages())
                .build();

        log.info("Objeto do tipo PatrimonioPageResponse criado com sucesso. Retornando objeto...");
        return patrimonioPageResponse;
    }
}
