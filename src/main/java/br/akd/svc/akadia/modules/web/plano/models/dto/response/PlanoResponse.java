package br.akd.svc.akadia.modules.web.plano.models.dto.response;

import br.akd.svc.akadia.modules.web.pagamento.models.enums.FormaPagamentoSistemaEnum;
import br.akd.svc.akadia.modules.web.plano.models.enums.StatusPlanoEnum;
import br.akd.svc.akadia.modules.web.plano.models.enums.TipoPlanoEnum;
import lombok.*;

import java.util.UUID;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class PlanoResponse {
    private UUID id;
    private String codigoAssinaturaAsaas;
    private String dataContratacao;
    private String horaContratacao;
    private String dataVencimento;
    private TipoPlanoEnum tipoPlanoEnum;
    private StatusPlanoEnum statusPlanoEnum;
    private FormaPagamentoSistemaEnum formaPagamentoSistemaEnum;
}
