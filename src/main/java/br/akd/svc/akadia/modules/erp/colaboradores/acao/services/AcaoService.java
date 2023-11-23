package br.akd.svc.akadia.modules.erp.colaboradores.acao.services;

import br.akd.svc.akadia.modules.erp.colaboradores.acao.models.dto.response.page.AcaoPageResponse;
import br.akd.svc.akadia.modules.erp.colaboradores.acao.models.enums.TipoAcaoEnum;
import br.akd.svc.akadia.modules.erp.colaboradores.colaborador.models.entity.colaborador.id.ColaboradorId;
import br.akd.svc.akadia.modules.erp.colaboradores.colaborador.models.enums.ModulosEnum;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

public interface AcaoService {

    @Transactional
    void salvaHistoricoColaborador(ColaboradorId idColaboradorSessao,
                                   UUID uuidObjeto,
                                   ModulosEnum modulo,
                                   TipoAcaoEnum tipo,
                                   String observacao);

    AcaoPageResponse obtemAcoesColaborador(Pageable pageable,
                                           ColaboradorId idColaboradorSessao,
                                           UUID uuidColaborador);
}
