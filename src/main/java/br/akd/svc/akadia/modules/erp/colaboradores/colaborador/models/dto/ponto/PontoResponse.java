package br.akd.svc.akadia.modules.erp.colaboradores.colaborador.models.dto.ponto;

import br.akd.svc.akadia.modules.erp.colaboradores.colaborador.models.entity.ponto.PontoEntity;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class PontoResponse {
    private String horaEntrada;
    private String horaSaidaAlmoco;
    private String horaEntradaAlmoco;
    private String horaSaida;

    public List<PontoResponse> buildResponseListFromEntities(List<PontoEntity> pontosEntity) {
        List<PontoResponse> pontosResponse = new ArrayList<>();
        pontosEntity.forEach(pontoEntity -> pontosResponse.add(buildResponseFromEntity(pontoEntity)));
        return pontosResponse;
    }

    public PontoResponse buildResponseFromEntity(PontoEntity pontoEntity) {
        return pontoEntity != null
                ? PontoResponse.builder()
                .horaEntrada(pontoEntity.getHoraEntrada())
                .horaSaidaAlmoco(pontoEntity.getHoraSaidaAlmoco())
                .horaEntradaAlmoco(pontoEntity.getHoraEntradaAlmoco())
                .horaSaida(pontoEntity.getHoraSaida())
                .build()
                : null;
    }
}
