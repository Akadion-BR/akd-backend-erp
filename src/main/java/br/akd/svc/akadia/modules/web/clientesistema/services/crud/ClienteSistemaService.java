package br.akd.svc.akadia.modules.web.clientesistema.services.crud;

import br.akd.svc.akadia.modules.web.clientesistema.models.dto.request.atualizacao.AtualizaClienteSistemaRequest;
import br.akd.svc.akadia.modules.web.clientesistema.models.dto.request.criacao.ClienteSistemaRequest;
import br.akd.svc.akadia.modules.web.clientesistema.models.dto.response.ClienteSistemaResponse;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

public interface ClienteSistemaService {

    @Transactional
    ClienteSistemaResponse cadastraNovoCliente(ClienteSistemaRequest clienteSistemaRequest);

    @Transactional
    ClienteSistemaResponse atualizaDadosCliente(UUID uuidClienteSistema,
                                                AtualizaClienteSistemaRequest atualizaClienteSistemaRequest);


}
