package br.akd.svc.akadia.modules.erp.colaboradores.acesso.services;

import br.akd.svc.akadia.modules.erp.colaboradores.acesso.models.dto.response.page.AcessoPageResponse;
import br.akd.svc.akadia.modules.erp.colaboradores.colaborador.models.entity.colaborador.id.ColaboradorId;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

public interface AcessoService {

    @Transactional
    void registraAcessoColaborador(ColaboradorId idColaboradorSessao,
                                   String matricula);

    @Transactional
    AcessoPageResponse obtemAcessosColaborador(Pageable pageable,
                                               ColaboradorId idColaboradorSessao,
                                               UUID idColaborador);


}
