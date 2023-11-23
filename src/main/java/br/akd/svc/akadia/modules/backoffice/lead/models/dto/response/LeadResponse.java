package br.akd.svc.akadia.modules.backoffice.lead.models.dto.response;

import br.akd.svc.akadia.modules.backoffice.lead.models.enums.OrigemLeadEnum;
import br.akd.svc.akadia.modules.global.telefone.response.TelefoneResponse;
import lombok.*;

import java.util.UUID;

@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class LeadResponse {
    private UUID id;
    private String nome;
    private String email;
    private OrigemLeadEnum origemLeadEnum;
    private TelefoneResponse telefone;
}
