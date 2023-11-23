package br.akd.svc.akadia.modules.erp.colaboradores.colaborador.models.dto.ferias;

import br.akd.svc.akadia.modules.erp.colaboradores.colaborador.models.entity.ferias.FeriasEntity;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class FeriasResponse {
    private String dataCadastro;
    private String horaCadastro;
    private Integer totalDias;
    private String dataInicio;
    private String dataFim;

    public List<FeriasResponse> buildResponseListFromEntities(List<FeriasEntity> feriasEntities) {
        List<FeriasResponse> feriasResponses = new ArrayList<>();
        feriasEntities.forEach(feriasEntity -> feriasResponses.add(buildResponseFromEntity(feriasEntity)));
        return feriasResponses;
    }

    public FeriasResponse buildResponseFromEntity(FeriasEntity feriasEntity) {
        return feriasEntity != null
                ? FeriasResponse.builder()
                .dataCadastro(feriasEntity.getDataCadastro())
                .horaCadastro(feriasEntity.getHoraCadastro())
                .totalDias(feriasEntity.getTotalDias())
                .dataInicio(feriasEntity.getDataInicio())
                .dataFim(feriasEntity.getDataFim())
                .build()
                : null;
    }
}
