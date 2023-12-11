package br.akd.svc.akadia.modules.backoffice.lead.service.impl;

import br.akd.svc.akadia.modules.backoffice.lead.models.dto.request.LeadRequest;
import br.akd.svc.akadia.modules.backoffice.lead.models.entity.LeadEntity;
import br.akd.svc.akadia.modules.backoffice.lead.repository.impl.LeadRepositoryImpl;
import br.akd.svc.akadia.modules.backoffice.lead.service.LeadService;
import br.akd.svc.akadia.modules.web.clientesistema.services.validator.ClienteSistemaValidationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class LeadServiceImpl implements LeadService {

    @Autowired
    LeadRepositoryImpl leadRepositoryImpl;

    @Autowired
    ClienteSistemaValidationService clienteSistemaValidationService;

    @Override
    public LeadEntity criaNovoLead(LeadRequest leadRequest) {
        log.info("Método de tratamento de lead e encaminhamento para persistência acessado");

        log.info("Iniciando acesso ao método de validação se e-mail já existe...");
        clienteSistemaValidationService.validaSeEmailJaExiste(leadRequest.getEmail());
        log.info("Validação de e-mail realizada com sucesso");

        log.info("Iniciando construção do objeto lead com base no cliente recebido: {}", leadRequest);
        LeadEntity lead = new LeadEntity().buildFromRequest(leadRequest);
        log.info("Lead construído com sucesso: {}", lead);

        log.info("Iniciando acesso ao método de implementação de persistência do lead...");
        LeadEntity leadPersistido = leadRepositoryImpl.implementaPersistencia(lead);

        log.info("Lead salvo com sucesso");
        return leadPersistido;
    }

}
