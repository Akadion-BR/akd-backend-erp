package br.akd.svc.akadia.services.bckoff;

import br.akd.svc.akadia.models.dto.site.mocks.ClienteSistemaDtoBuilder;
import br.akd.svc.akadia.models.entities.bckoff.mocks.LeadEntityBuilder;
import br.akd.svc.akadia.modules.backoffice.lead.repository.impl.LeadRepositoryImpl;
import br.akd.svc.akadia.modules.backoffice.lead.service.LeadService;
import br.akd.svc.akadia.modules.web.clientesistema.services.crud.ClienteSistemaService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@SpringBootTest
@ActiveProfiles("test")
@DisplayName("Service: Lead")
class LeadServiceTest {

    @InjectMocks
    LeadService leadService;

    @Mock
    ClienteSistemaService clienteSistemaService;

    @Mock
    LeadRepositoryImpl leadRepositoryImpl;

    @Test
    @DisplayName("Deve testar encaminhamento de lead para persistencia")
    void deveTestarEncaminhamentoDeLeadParaPersistencia() {

        doNothing().when(clienteSistemaService).validaSeEmailJaExiste(ClienteSistemaDtoBuilder.builder().build());

        when(leadRepositoryImpl.implementaPersistencia(LeadEntityBuilder.builder().build()))
                .thenReturn(LeadEntityBuilder.builder().build());

        leadService.encaminhaLeadParaPersistencia(ClienteSistemaDtoBuilder.builder().comTelefone().comEndereco().build());

    }

}
