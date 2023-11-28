package br.akd.svc.akadia.modules.web.plano.models.dto.response;

import br.akd.svc.akadia.modules.web.plano.models.entity.PlanoEntity;
import lombok.*;

import java.util.UUID;

@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class PlanoResponse {
    private UUID id;
    private String dataContratacao;
    private String horaContratacao;
    private String dataVencimento;
    private String dataAgendamentoRemocao;
    private String tipoPlanoEnum;
    private String statusPlanoEnum;
    private String formaPagamentoSistemaEnum;

    public PlanoResponse buildFromEntity(PlanoEntity planoEntity) {
        return planoEntity != null
                ? PlanoResponse.builder()
                .id(planoEntity.getId())
                .dataContratacao(planoEntity.getDataContratacao())
                .horaContratacao(planoEntity.getHoraContratacao())
                .dataVencimento(planoEntity.getDataVencimento())
                .dataAgendamentoRemocao(planoEntity.getDataAgendamentoRemocao())
                .tipoPlanoEnum(planoEntity.getTipoPlanoEnum().getDesc())
                .statusPlanoEnum(planoEntity.getStatusPlanoEnum().getDesc())
                .formaPagamentoSistemaEnum(planoEntity.getFormaPagamentoSistemaEnum().getDesc())
                .build()
                : null;
    }
}
