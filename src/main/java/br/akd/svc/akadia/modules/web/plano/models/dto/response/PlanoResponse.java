package br.akd.svc.akadia.modules.web.plano.models.dto.response;

import br.akd.svc.akadia.modules.web.pagamento.models.enums.FormaPagamentoSistemaEnum;
import br.akd.svc.akadia.modules.web.plano.models.entity.PlanoEntity;
import br.akd.svc.akadia.modules.web.plano.models.enums.StatusPlanoEnum;
import br.akd.svc.akadia.modules.web.plano.models.enums.TipoPlanoEnum;
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
    private TipoPlanoEnum tipoPlanoEnum;
    private StatusPlanoEnum statusPlanoEnum;
    private FormaPagamentoSistemaEnum formaPagamentoSistemaEnum;

    public PlanoResponse buildFromEntity(PlanoEntity planoEntity) {
        return planoEntity != null
                ? PlanoResponse.builder()
                .id(planoEntity.getId())
                .dataContratacao(planoEntity.getDataContratacao())
                .horaContratacao(planoEntity.getHoraContratacao())
                .dataVencimento(planoEntity.getDataVencimento())
                .tipoPlanoEnum(planoEntity.getTipoPlanoEnum())
                .statusPlanoEnum(planoEntity.getStatusPlanoEnum())
                .formaPagamentoSistemaEnum(planoEntity.getFormaPagamentoSistemaEnum())
                .build()
                : null;
    }
}
