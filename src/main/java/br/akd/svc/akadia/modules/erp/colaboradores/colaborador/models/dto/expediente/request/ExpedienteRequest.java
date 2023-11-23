package br.akd.svc.akadia.modules.erp.colaboradores.colaborador.models.dto.expediente.request;

import br.akd.svc.akadia.modules.erp.colaboradores.colaborador.models.enums.EscalaEnum;
import lombok.*;

@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ExpedienteRequest {
    //TODO JAVAX VALIDATOR
    private String horaEntrada;
    private String horaSaidaAlmoco;
    private String horaEntradaAlmoco;
    private String horaSaida;
    private String cargaHorariaSemanal;
    private EscalaEnum escalaEnum;
}
