package br.akd.svc.akadia.modules.web.clientesistema.services.crud;

import br.akd.svc.akadia.modules.web.clientesistema.models.dto.request.ClienteSistemaRequest;
import br.akd.svc.akadia.modules.web.clientesistema.models.entity.ClienteSistemaEntity;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

public interface ClienteSistemaService {

    @Transactional
    ClienteSistemaEntity cadastraNovoCliente(ClienteSistemaRequest clienteSistemaRequest);

    @Transactional
    ClienteSistemaEntity atualizaDadosCliente(UUID uuidClienteSistema,
                                              ClienteSistemaRequest clienteSistemaRequest);


}
