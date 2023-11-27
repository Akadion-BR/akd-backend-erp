package br.akd.svc.akadia.modules.erp.colaboradores.acao.services.impl;

import br.akd.svc.akadia.modules.erp.colaboradores.acao.models.dto.response.page.AcaoPageResponse;
import br.akd.svc.akadia.modules.erp.colaboradores.acao.models.entity.AcaoEntity;
import br.akd.svc.akadia.modules.erp.colaboradores.acao.models.enums.TipoAcaoEnum;
import br.akd.svc.akadia.modules.erp.colaboradores.acao.services.AcaoService;
import br.akd.svc.akadia.modules.erp.colaboradores.colaborador.models.entity.colaborador.ColaboradorEntity;
import br.akd.svc.akadia.modules.erp.colaboradores.colaborador.models.entity.colaborador.id.ColaboradorId;
import br.akd.svc.akadia.modules.erp.colaboradores.colaborador.models.enums.ModulosEnum;
import br.akd.svc.akadia.modules.erp.colaboradores.colaborador.repository.ColaboradorRepository;
import br.akd.svc.akadia.modules.erp.colaboradores.colaborador.repository.impl.ColaboradorRepositoryImpl;
import br.akd.svc.akadia.config.security.utils.SecurityUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Slf4j
@Service
public class AcaoServiceImpl implements AcaoService {

    @Autowired
    ColaboradorRepositoryImpl colaboradorRepositoryImpl;

    @Autowired
    ColaboradorRepository colaboradorRepository;

    @Autowired
    SecurityUtil securityUtil;

    public void salvaHistoricoColaborador(ColaboradorId idColaboradorSessao,
                                          UUID uuidObjeto,
                                          ModulosEnum modulo,
                                          TipoAcaoEnum tipo,
                                          String observacao) {
        log.debug("Método de salvamento de histórico do colaborador acessado");

        ColaboradorEntity colaboradorLogado = securityUtil.obtemUsuarioSessao(idColaboradorSessao);

        log.debug("Iniciando construção do objeto AcaoEntity dentro da listagem de histórico de ações do colaborador...");
        colaboradorLogado.getAcoes().add(new AcaoEntity().criaNovaAcao(uuidObjeto, modulo, tipo, observacao));

        log.debug("Realizando persistência da ação...");
        colaboradorRepositoryImpl.implementaPersistencia(colaboradorLogado);
    }

    public AcaoPageResponse obtemAcoesColaborador(Pageable pageable,
                                                  ColaboradorId idColaboradorSessao,
                                                  UUID uuidColaborador) {
        log.debug("Método de serviço obtenção de ações do colaborador de id {} acessado", uuidColaborador);

        log.debug("Acessando repositório de busca de ações");
        Page<AcaoEntity> acaoPage = colaboradorRepository.buscaAcoesPorIdColaborador(
                pageable, idColaboradorSessao.getEmpresa(), uuidColaborador);

        log.debug("Busca de ações por paginação realizada com sucesso. Acessando método de conversão dos objetos do tipo " +
                "Entity para objetos do tipo Response...");
        AcaoPageResponse acaoPageResponse = new AcaoPageResponse().buildPageResponse(acaoPage);

        log.debug("Conversão de tipagem realizada com sucesso");

        log.info("A busca paginada de ações foi realizada com sucesso");
        return acaoPageResponse;
    }

}
