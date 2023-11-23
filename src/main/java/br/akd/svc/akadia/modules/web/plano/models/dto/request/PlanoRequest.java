package br.akd.svc.akadia.modules.web.plano.models.dto.request;

import br.akd.svc.akadia.modules.web.pagamento.models.enums.FormaPagamentoSistemaEnum;
import br.akd.svc.akadia.modules.web.plano.models.enums.TipoPlanoEnum;
import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class PlanoRequest {
    //TODO JAVAX VALIDATOR
    private TipoPlanoEnum tipoPlanoEnum;
    private FormaPagamentoSistemaEnum formaPagamentoSistemaEnum;
}
