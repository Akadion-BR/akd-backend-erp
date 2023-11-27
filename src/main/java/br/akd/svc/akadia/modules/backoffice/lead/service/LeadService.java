package br.akd.svc.akadia.modules.backoffice.lead.service;

import br.akd.svc.akadia.modules.backoffice.lead.models.dto.request.LeadRequest;
import br.akd.svc.akadia.modules.backoffice.lead.models.entity.LeadEntity;

public interface LeadService {

    LeadEntity criaNovoLead(LeadRequest leadRequest);

}
