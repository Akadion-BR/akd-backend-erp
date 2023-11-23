package br.akd.svc.akadia.modules.web.empresa.services.crud;

import br.akd.svc.akadia.modules.web.empresa.models.dto.request.EmpresaRequest;
import br.akd.svc.akadia.modules.web.empresa.models.dto.response.CriaEmpresaResponse;
import br.akd.svc.akadia.modules.web.empresa.models.dto.response.EmpresaResponse;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

public interface EmpresaService {

    @Transactional
    CriaEmpresaResponse criaNovaEmpresa(UUID idClienteSistemaSessao,
                                        EmpresaRequest empresaRequest);

    @Transactional
    EmpresaResponse atualizaEmpresa(UUID idClienteSistemaSessao,
                                    UUID uuidEmpresa,
                                    EmpresaRequest empresaRequest);

    @Transactional
    EmpresaResponse removeEmpresa(UUID idClienteSistemaSessao,
                                  UUID uuidEmpresa);

}
