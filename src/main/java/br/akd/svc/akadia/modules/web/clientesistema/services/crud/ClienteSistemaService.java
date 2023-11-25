package br.akd.svc.akadia.modules.web.clientesistema.services.crud;

import br.akd.svc.akadia.modules.web.clientesistema.models.dto.request.atualizacao.AtualizaClienteSistemaRequest;
import br.akd.svc.akadia.modules.web.clientesistema.models.dto.request.criacao.ClienteSistemaRequest;
import br.akd.svc.akadia.modules.web.clientesistema.models.dto.response.ClienteSistemaResponse;
import com.fasterxml.jackson.core.JsonProcessingException;

import java.util.UUID;

public interface ClienteSistemaService {

    ClienteSistemaResponse cadastraNovoCliente(ClienteSistemaRequest clienteSistemaRequest) throws JsonProcessingException;

    ClienteSistemaResponse atualizaDadosCliente(UUID uuidClienteSistema,
                                                AtualizaClienteSistemaRequest atualizaClienteSistemaRequest) throws JsonProcessingException;


}
