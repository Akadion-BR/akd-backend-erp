package br.akd.svc.akadia.modules.erp.colaboradores.advertencia.models.dto.request;

import br.akd.svc.akadia.modules.erp.colaboradores.advertencia.models.enums.StatusAdvertenciaEnum;
import lombok.*;

@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class AdvertenciaRequest {
    private String motivo;
    private String descricao;
    private StatusAdvertenciaEnum statusAdvertencia;
}
