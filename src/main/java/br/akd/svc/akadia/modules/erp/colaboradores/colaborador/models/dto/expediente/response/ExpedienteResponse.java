package br.akd.svc.akadia.modules.erp.colaboradores.colaborador.models.dto.expediente.response;

import br.akd.svc.akadia.modules.erp.colaboradores.colaborador.models.entity.expediente.ExpedienteEntity;
import lombok.*;

@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ExpedienteResponse {
    private String horaEntrada;
    private String horaSaidaAlmoco;
    private String horaEntradaAlmoco;
    private String horaSaida;
    private String cargaHorariaSemanal;
    private String escalaEnum;

    public ExpedienteResponse buildFromEntity(ExpedienteEntity expedienteEntity) {
        return expedienteEntity != null
                ? ExpedienteResponse.builder()
                .horaEntrada(expedienteEntity.getHoraEntrada())
                .horaSaidaAlmoco(expedienteEntity.getHoraSaida())
                .horaEntradaAlmoco(expedienteEntity.getHoraEntradaAlmoco())
                .horaSaida(expedienteEntity.getHoraSaida())
                .cargaHorariaSemanal(expedienteEntity.getCargaHorariaSemanal())
                .escalaEnum(expedienteEntity.getEscalaEnum().getDesc())
                .build()
                : null;
    }

}
