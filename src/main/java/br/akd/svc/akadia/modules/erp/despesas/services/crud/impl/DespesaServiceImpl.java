package br.akd.svc.akadia.modules.erp.despesas.services.crud.impl;

import br.akd.svc.akadia.exceptions.InvalidRequestException;
import br.akd.svc.akadia.modules.erp.colaboradores.acao.models.enums.TipoAcaoEnum;
import br.akd.svc.akadia.modules.erp.colaboradores.acao.services.AcaoService;
import br.akd.svc.akadia.modules.erp.colaboradores.colaborador.models.entity.colaborador.ColaboradorEntity;
import br.akd.svc.akadia.modules.erp.colaboradores.colaborador.models.entity.colaborador.id.ColaboradorId;
import br.akd.svc.akadia.modules.erp.colaboradores.colaborador.models.enums.ModulosEnum;
import br.akd.svc.akadia.modules.erp.despesas.models.dto.request.DespesaRequest;
import br.akd.svc.akadia.modules.erp.despesas.models.dto.response.DespesaResponse;
import br.akd.svc.akadia.modules.erp.despesas.models.dto.response.page.DespesaPageResponse;
import br.akd.svc.akadia.modules.erp.despesas.models.entity.DespesaEntity;
import br.akd.svc.akadia.modules.erp.despesas.models.entity.id.DespesaId;
import br.akd.svc.akadia.modules.erp.despesas.models.enums.StatusDespesaEnum;
import br.akd.svc.akadia.modules.erp.despesas.repository.DespesaRepository;
import br.akd.svc.akadia.modules.erp.despesas.repository.impl.DespesaRepositoryImpl;
import br.akd.svc.akadia.modules.erp.despesas.services.crud.DespesaService;
import br.akd.svc.akadia.modules.erp.despesas.services.validator.DespesaValidationService;
import br.akd.svc.akadia.modules.global.exclusao.entity.ExclusaoEntity;
import br.akd.svc.akadia.utils.Constantes;
import br.akd.svc.akadia.utils.SecurityUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
public class DespesaServiceImpl implements DespesaService {

    @Autowired
    AcaoService acaoService;

    @Autowired
    DespesaValidationService despesaValidationService;

    @Autowired
    DespesaRepositoryImpl despesaRepositoryImpl;

    @Autowired
    DespesaRepository despesaRepository;

    @Autowired
    SecurityUtil securityUtil;

    String BUSCA_DESPESA_POR_ID = "Iniciando acesso ao método de implementação de busca de despesa por id...";

    public DespesaResponse criaNovaDespesa(ColaboradorId idColaboradorSessao,
                                           DespesaRequest despesaRequest) {

        log.debug("Método de serviço de criação de nova despesa acessado");

        ColaboradorEntity colaboradorLogado = securityUtil
                .obtemUsuarioSessao(idColaboradorSessao);

        log.debug(Constantes.VERIFICANDO_SE_COLABORADOR_PODE_ALTERAR_DADOS);
        securityUtil.verificaSePodeRealizarAlteracoes(colaboradorLogado.getAcessoSistema());

        List<DespesaEntity> recorrencias = despesaRequest.getQtdRecorrencias() > 0
                ? geraRecorrencias(colaboradorLogado, despesaRequest)
                : new ArrayList<>();

        log.debug("Iniciando criação do objeto DespesaEntity...");
        DespesaEntity despesaEntity = new DespesaEntity()
                .buildFromRequest(colaboradorLogado, recorrencias, despesaRequest);
        log.debug("Objeto despesaEntity criado com sucesso");

        log.debug("Iniciando acesso ao método de implementação da persistência da despesa...");
        DespesaEntity despesaPersistida = despesaRepositoryImpl.implementaPersistencia(despesaEntity);

        log.debug(Constantes.INICIANDO_SALVAMENTO_HISTORICO_DESPESA);
        acaoService.salvaHistoricoColaborador(idColaboradorSessao, despesaPersistida.getId(),
                ModulosEnum.DESPESAS, TipoAcaoEnum.CRIACAO, null);

        log.debug("Despesa persistida com sucesso. Convertendo despesaEntity para despesaResponse...");
        DespesaResponse despesaResponse = new DespesaResponse().buildFromEntity(despesaPersistida);

        log.info("Despesa criada com sucesso");
        return despesaResponse;
    }

    public DespesaPageResponse realizaBuscaPaginadaPorDespesas(ColaboradorId idColaboradorSessao,
                                                               Pageable pageable,
                                                               String mesAno,
                                                               String campoBusca) {
        log.debug("Método de serviço de obtenção paginada de despesas acessado. Campo de busca: {}",
                campoBusca != null ? campoBusca : "Nulo");

        String dataInicio = mesAno + "-01";
        String dataFim = mesAno + "-31";

        log.debug("Acessando repositório de busca de despesas");
        Page<DespesaEntity> despesaPage = campoBusca != null && !campoBusca.isEmpty()
                ? despesaRepository
                .buscaPorDespesasTypeAhead(pageable, idColaboradorSessao.getEmpresa(), dataInicio, dataFim, campoBusca)
                : despesaRepository
                .buscaPorDespesas(pageable, idColaboradorSessao.getEmpresa(), dataInicio, dataFim);

        log.debug("Busca de despesas por paginação realizada com sucesso. Acessando método de conversão dos objetos do tipo " +
                "Entity para objetos do tipo Response...");
        DespesaPageResponse despesaPageResponse = new DespesaPageResponse().buildDespesaPageResponse(despesaPage);
        log.debug("Conversão de tipagem realizada com sucesso");

        log.info("A busca paginada de despesas foi realizada com sucesso");
        return despesaPageResponse;
    }

    public DespesaResponse realizaBuscaDeDespesaPorId(ColaboradorId idColaboradorSessao,
                                                      UUID idDespesa) {
        log.debug("Método de serviço de obtenção de despesa por id. ID recebido: {}", idDespesa);

        log.debug("Acessando repositório de busca de despesa por ID...");
        DespesaEntity despesa = despesaRepositoryImpl
                .implementaBuscaPorId(idColaboradorSessao.getEmpresa(), idDespesa);

        log.debug("Busca de despesas por id realizada com sucesso. Acessando método de conversão dos objeto do tipo " +
                "Entity para objeto do tipo Response...");
        DespesaResponse despesaResponse = new DespesaResponse().buildFromEntity(despesa);
        log.debug("Conversão de tipagem realizada com sucesso");

        log.info("A busca de despesa por id foi realizada com sucesso");
        return despesaResponse;
    }

    //TODO ESTE MÉTODO DEVERÁ SER MIGRADO PARA UTILS
    public List<DespesaEntity> geraRecorrencias(ColaboradorEntity colaboradorLogado,
                                                DespesaRequest despesaRequest) {
        //TODO LOGGEAR
        List<DespesaEntity> despesas = new ArrayList<>();
        for (int i = 1; i <= despesaRequest.getQtdRecorrencias(); i++) {

            LocalDate dataDespesa;
            if (despesaRequest.getStatusDespesa().equals(StatusDespesaEnum.PAGO)) {
                dataDespesa = LocalDate.parse(despesaRequest.getDataPagamento()).plusMonths(i);
            } else {
                dataDespesa = LocalDate.parse(despesaRequest.getDataAgendamento()).plusMonths(i);
            }

            DespesaEntity despesa = new DespesaEntity()
                    .buildRecorrencia(colaboradorLogado, dataDespesa.toString(), i, despesaRequest);

            despesas.add(despesa);

        }

        return despesas;
    }

    public DespesaResponse atualizaDespesa(ColaboradorId idColaboradorSessao,
                                           UUID idDespesa,
                                           DespesaRequest despesaRequest) {
        log.debug("Método de serviço de atualização de despesa acessado");

        ColaboradorEntity colaboradorLogado = securityUtil
                .obtemUsuarioSessao(idColaboradorSessao);

        log.debug(Constantes.VERIFICANDO_SE_COLABORADOR_PODE_ALTERAR_DADOS);
        securityUtil.verificaSePodeRealizarAlteracoes(colaboradorLogado.getAcessoSistema());

        log.debug(BUSCA_DESPESA_POR_ID);
        DespesaEntity despesaEncontrada = despesaRepositoryImpl
                .implementaBuscaPorId(idColaboradorSessao.getEmpresa(), idDespesa);

        log.debug("Iniciando acesso ao método de validação de alteração de dados de despesa excluída...");
        despesaValidationService.validaSeDespesaEstaExcluida(despesaEncontrada,
                "Não é possível atualizar um despesa excluída");

        log.debug("Iniciando criação do objeto DespesaEntity...");
        DespesaEntity novaDespesaAtualizada = new DespesaEntity()
                .updateFromRequest(despesaEncontrada, despesaRequest);
        log.debug("Objeto despesa construído com sucesso");

        log.debug("Iniciando acesso ao método de implementação da persistência da despesa...");
        DespesaEntity despesaPersistida = despesaRepositoryImpl
                .implementaPersistencia(novaDespesaAtualizada);

        log.debug(Constantes.INICIANDO_SALVAMENTO_HISTORICO_COLABORADOR);
        acaoService.salvaHistoricoColaborador(idColaboradorSessao, despesaPersistida.getId(),
                ModulosEnum.DESPESAS, TipoAcaoEnum.ALTERACAO, null);

        log.debug("Despesa persistida com sucesso. Convertendo DespesaEntity para DespesaResponse...");
        DespesaResponse despesaResponse = new DespesaResponse().buildFromEntity(despesaPersistida);

        log.info("Despesa atualizada com sucesso");
        return despesaResponse;
    }

    public DespesaResponse removeDespesa(ColaboradorId idColaboradorSessao,
                                         UUID idDespesa,
                                         Boolean removeRecorrencia) {
        log.debug("Método de serviço de remoção de despesa acessado");

        log.debug(BUSCA_DESPESA_POR_ID);
        DespesaEntity despesaEncontrada = despesaRepositoryImpl
                .implementaBuscaPorId(idColaboradorSessao.getEmpresa(), idDespesa);

        log.debug("Iniciando acesso ao método de validação de exclusão de despesa que já foi excluído...");
        despesaValidationService.validaSeDespesaEstaExcluida(despesaEncontrada,
                Constantes.DESPESA_JA_EXCLUIDA);

        log.debug(Constantes.ATUALIZANDO_EXCLUSAO_DESPESA);
        ExclusaoEntity exclusaoEntity = new ExclusaoEntity().constroiObjetoExclusao();

        despesaEncontrada.setExclusao(exclusaoEntity);
        log.debug(Constantes.OBJETO_EXCLUSAO_DESPESA_SETADO_COM_SUCESSO, idDespesa);

        log.debug(Constantes.PERSISTINDO_DESPESA_EXCLUIDA);
        DespesaEntity despesaExcluida = despesaRepositoryImpl
                .implementaPersistencia(despesaEncontrada);

        if (Boolean.TRUE.equals(removeRecorrencia))
            removeRecorrenciasEmMassa(despesaExcluida.getRecorrencias());

        log.debug(Constantes.INICIANDO_SALVAMENTO_HISTORICO_DESPESA);
        acaoService.salvaHistoricoColaborador(idColaboradorSessao, null,
                ModulosEnum.DESPESAS, TipoAcaoEnum.REMOCAO, null);

        DespesaResponse despesaResponse = new DespesaResponse()
                .buildFromEntity(despesaExcluida);

        log.info("Despesa excluída com sucesso");
        return despesaResponse;
    }

    //TODO ESTE MÉTODO DEVERÁ SER MIGRADO PARA UTILS
    public void removeRecorrenciasEmMassa(List<DespesaEntity> despesas) {
        log.debug("Método de serviço de remoção de recorrências em massa acessado");

        log.debug("Iniciando acesso ao método de validação de exclusão de despesa que já foi excluída...");
        for (DespesaEntity despesa : despesas) {
            log.debug(Constantes.ATUALIZANDO_EXCLUSAO_DESPESA);
            ExclusaoEntity exclusao = new ExclusaoEntity().constroiObjetoExclusao();
            despesa.setExclusao(exclusao);
            log.debug(Constantes.OBJETO_EXCLUSAO_DESPESA_SETADO_COM_SUCESSO, despesa.getId());
        }

        log.debug("Verificando se listagem de despesas encontradas está preenchida...");
        if (!despesas.isEmpty()) {
            log.debug(Constantes.PERSISTINDO_DESPESA_EXCLUIDA);
            despesaRepositoryImpl.implementaPersistenciaEmMassa(despesas);
        } else throw new InvalidRequestException("Nenhuma despesa foi encontrada para remoção");

        log.info("Despesas excluídas com sucesso");
    }

    public void removeDespesasEmMassa(ColaboradorId idColaboradorSessao,
                                      List<UUID> ids) {
        log.debug("Método de serviço de remoção de despesas em massa acessado");

        List<DespesaId> despesaIds = new ArrayList<>();
        ids.forEach(id -> despesaIds.add(new DespesaId(idColaboradorSessao.getEmpresa(), id)));

        List<DespesaEntity> despesasEncontradas = despesaRepository.findAllById(despesaIds);

        log.debug("Iniciando acesso ao método de validação de exclusão de despesa que já foi excluída...");
        for (DespesaEntity despesa : despesasEncontradas) {

            despesaValidationService.validaSeDespesaEstaExcluida(despesa,
                    Constantes.DESPESA_JA_EXCLUIDA);

            log.debug(Constantes.ATUALIZANDO_EXCLUSAO_DESPESA);
            ExclusaoEntity exclusao = new ExclusaoEntity().constroiObjetoExclusao();
            despesa.setExclusao(exclusao);
            log.debug(Constantes.OBJETO_EXCLUSAO_DESPESA_SETADO_COM_SUCESSO, despesa.getId());
        }

        log.debug("Verificando se listagem de despesas encontradas está preenchida...");
        if (!despesasEncontradas.isEmpty()) {
            log.debug(Constantes.PERSISTINDO_DESPESA_EXCLUIDA);
            despesaRepositoryImpl.implementaPersistenciaEmMassa(despesasEncontradas);

            log.debug(Constantes.INICIANDO_SALVAMENTO_HISTORICO_DESPESA);
            acaoService.salvaHistoricoColaborador(idColaboradorSessao, null,
                    ModulosEnum.DESPESAS, TipoAcaoEnum.REMOCAO_EM_MASSA, despesasEncontradas.size() + " Itens removidos");
        } else throw new InvalidRequestException("Nenhuma despesa foi encontrada para remoção");

        log.info("Despesas excluídas com sucesso");
    }

}
