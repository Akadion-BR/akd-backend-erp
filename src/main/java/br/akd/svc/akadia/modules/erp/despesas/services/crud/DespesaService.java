package br.akd.svc.akadia.modules.erp.despesas.services.crud;

import br.akd.svc.akadia.modules.erp.colaboradores.colaborador.models.entity.colaborador.id.ColaboradorId;
import br.akd.svc.akadia.modules.erp.despesas.models.dto.request.DespesaRequest;
import br.akd.svc.akadia.modules.erp.despesas.models.dto.response.DespesaResponse;
import br.akd.svc.akadia.modules.erp.despesas.models.dto.response.page.DespesaPageResponse;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

public interface DespesaService {

    @Transactional
    DespesaResponse criaNovaDespesa(ColaboradorId idColaboradorSessao,
                                    DespesaRequest despesaRequest);

    DespesaPageResponse realizaBuscaPaginadaPorDespesas(ColaboradorId idColaboradorSessao,
                                                        Pageable pageable,
                                                        String mesAno,
                                                        String campoBusca);

    DespesaResponse realizaBuscaDeDespesaPorId(ColaboradorId idColaboradorSessao,
                                               UUID idDespesa);

    @Transactional
    DespesaResponse atualizaDespesa(ColaboradorId idColaboradorSessao,
                                    UUID idDespesa,
                                    DespesaRequest despesaRequest);

    @Transactional
    DespesaResponse removeDespesa(ColaboradorId idColaboradorSessao,
                                  UUID idDespesa,
                                  Boolean removeRecorrencia);

    @Transactional
    void removeDespesasEmMassa(ColaboradorId idColaboradorSessao,
                               List<UUID> ids);


}
