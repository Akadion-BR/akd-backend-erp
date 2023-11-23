package br.akd.svc.akadia.modules.backoffice.lead.models.dto.request;

import br.akd.svc.akadia.modules.backoffice.lead.models.enums.OrigemLeadEnum;
import br.akd.svc.akadia.modules.global.telefone.request.TelefoneRequest;
import lombok.*;

@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class LeadRequest {
    private String nome;
    private String email;
    private OrigemLeadEnum origemLeadEnum;
    private TelefoneRequest telefone;
}
