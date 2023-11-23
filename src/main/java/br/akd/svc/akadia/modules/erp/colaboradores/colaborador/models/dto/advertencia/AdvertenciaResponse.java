package br.akd.svc.akadia.modules.erp.colaboradores.colaborador.models.dto.advertencia;

import br.akd.svc.akadia.modules.erp.colaboradores.advertencia.models.entity.AdvertenciaEntity;
import lombok.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class AdvertenciaResponse {
    private UUID id;
    private String dataCadastro;
    private String horaCadastro;
    private String motivo;
    private String descricao;
    private String statusAdvertencia;

    public List<AdvertenciaResponse> buildResponseListFromEntities(List<AdvertenciaEntity> advertenciaEntities) {
        List<AdvertenciaResponse> advertenciaResponses = new ArrayList<>();
        advertenciaEntities.forEach(advertenciaEntity -> advertenciaResponses.add(buildResponseFromEntity(advertenciaEntity)));
        return advertenciaResponses;
    }

    public AdvertenciaResponse buildResponseFromEntity(AdvertenciaEntity advertenciaEntity) {
        return advertenciaEntity != null
                ? AdvertenciaResponse.builder()
                .id(advertenciaEntity.getId())
                .dataCadastro(advertenciaEntity.getDataCadastro())
                .horaCadastro(advertenciaEntity.getHoraCadastro())
                .motivo(advertenciaEntity.getMotivo())
                .descricao(advertenciaEntity.getDescricao())
                .statusAdvertencia(advertenciaEntity.getStatusAdvertencia().getDesc())
                .build()
                : null;
    }
}
