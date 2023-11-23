package br.akd.svc.akadia.modules.erp.patrimonios.services.crud;

import br.akd.svc.akadia.modules.erp.colaboradores.colaborador.models.entity.colaborador.id.ColaboradorId;
import br.akd.svc.akadia.modules.erp.patrimonios.models.dto.request.PatrimonioRequest;
import br.akd.svc.akadia.modules.erp.patrimonios.models.dto.response.PatrimonioResponse;
import br.akd.svc.akadia.modules.erp.patrimonios.models.dto.response.page.PatrimonioPageResponse;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

public interface PatrimonioService {

    @Transactional
    PatrimonioResponse criaNovoPatrimonio(ColaboradorId idColaboradorSessao,
                                          PatrimonioRequest patrimonioRequest);

    PatrimonioPageResponse realizaBuscaPaginada(ColaboradorId idColaboradorSessao,
                                                Pageable pageable,
                                                String campoBusca);

    PatrimonioResponse realizaBuscaPorId(ColaboradorId idColaboradorSessao,
                                         UUID idPatrimonio);

    @Transactional
    PatrimonioResponse atualizaObjeto(ColaboradorId idColaboradorSessao,
                                      UUID idPatrimonio,
                                      PatrimonioRequest patrimonioRequest);

    @Transactional
    PatrimonioResponse removeObjeto(ColaboradorId idColaboradorSessao,
                                    UUID uuidPatrimonio);

    @Transactional
    void removeEmMassa(ColaboradorId idColaboradorSessao,
                       List<UUID> idsPatrimonio);
}
