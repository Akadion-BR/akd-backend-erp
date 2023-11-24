package br.akd.svc.akadia.modules.backoffice.lead.service;

import br.akd.svc.akadia.modules.backoffice.lead.models.entity.LeadEntity;
import br.akd.svc.akadia.modules.backoffice.lead.models.enums.OrigemLeadEnum;
import br.akd.svc.akadia.modules.backoffice.lead.repository.impl.LeadRepositoryImpl;
import br.akd.svc.akadia.modules.global.telefone.entity.TelefoneEntity;
import br.akd.svc.akadia.modules.web.clientesistema.models.dto.request.criacao.ClienteSistemaRequest;
import br.akd.svc.akadia.modules.web.clientesistema.services.validator.ClienteSistemaValidationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class LeadService {

    @Autowired
    LeadRepositoryImpl leadRepositoryImpl;

    @Autowired
    ClienteSistemaValidationService clienteSistemaValidationService;

    //TODO TRANSFORMAR EM INTERFACE E IMPLEMENTAR A INTERFACE

    public LeadEntity encaminhaLeadParaPersistencia(ClienteSistemaRequest clienteSistemaRequest) {
        log.debug("Método de tratamento de lead e encaminhamento para persistência acessado");

        log.debug("Iniciando construção do objeto lead com base no cliente recebido: {}", clienteSistemaRequest);
        LeadEntity lead = (LeadEntity.builder()
                .origemLeadEnum(OrigemLeadEnum.PRE_CADASTRO)
                .email(clienteSistemaRequest.getEmail())
                .telefone(new TelefoneEntity().buildFromRequest(clienteSistemaRequest.getTelefone()))
                .build());
        log.debug("Lead construído com sucesso: {}", lead);

        log.debug("Iniciando acesso ao método de validação se e-mail já existe...");
        clienteSistemaValidationService.validaSeEmailJaExiste(clienteSistemaRequest.getEmail());

        log.debug("Iniciando acesso ao método de implementação de persistência do lead...");
        LeadEntity leadPersistido = leadRepositoryImpl.implementaPersistencia(lead);

        log.info("Lead persistido com sucesso...");
        log.debug("Lead persistido: {}", leadPersistido);
        return leadPersistido;
    }

}
