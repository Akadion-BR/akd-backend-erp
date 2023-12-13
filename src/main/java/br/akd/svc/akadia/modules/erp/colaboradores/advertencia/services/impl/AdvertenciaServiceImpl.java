package br.akd.svc.akadia.modules.erp.colaboradores.advertencia.services.impl;

import br.akd.svc.akadia.config.security.utils.SecurityUtil;
import br.akd.svc.akadia.exceptions.ObjectNotFoundException;
import br.akd.svc.akadia.modules.erp.colaboradores.acao.models.enums.TipoAcaoEnum;
import br.akd.svc.akadia.modules.erp.colaboradores.acao.services.AcaoService;
import br.akd.svc.akadia.modules.erp.colaboradores.advertencia.models.dto.request.AdvertenciaRequest;
import br.akd.svc.akadia.modules.erp.colaboradores.advertencia.models.dto.response.AdvertenciaPageResponse;
import br.akd.svc.akadia.modules.erp.colaboradores.advertencia.models.entity.AdvertenciaEntity;
import br.akd.svc.akadia.modules.erp.colaboradores.advertencia.models.enums.StatusAdvertenciaEnum;
import br.akd.svc.akadia.modules.erp.colaboradores.advertencia.services.AdvertenciaService;
import br.akd.svc.akadia.modules.erp.colaboradores.advertencia.services.report.AdvertenciaRelatorioService;
import br.akd.svc.akadia.modules.erp.colaboradores.colaborador.models.entity.colaborador.ColaboradorEntity;
import br.akd.svc.akadia.modules.erp.colaboradores.colaborador.models.entity.colaborador.id.ColaboradorId;
import br.akd.svc.akadia.modules.erp.colaboradores.colaborador.models.enums.ModulosEnum;
import br.akd.svc.akadia.modules.erp.colaboradores.colaborador.repository.ColaboradorRepository;
import br.akd.svc.akadia.modules.erp.colaboradores.colaborador.repository.impl.ColaboradorRepositoryImpl;
import br.akd.svc.akadia.modules.global.objects.arquivo.entity.ArquivoEntity;
import br.akd.svc.akadia.utils.Constantes;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Slf4j
@Service
public class AdvertenciaServiceImpl implements AdvertenciaService {
    @Autowired
    ColaboradorRepositoryImpl colaboradorRepositoryImpl;

    @Autowired
    ColaboradorRepository colaboradorRepository;

    @Autowired
    AdvertenciaRelatorioService advertenciaRelatorioService;

    @Autowired
    AcaoService acaoService;

    @Autowired
    SecurityUtil securityUtil;

    String NENHUMA_ADVERTENCIA_ENCONTRADA = "Nenhuma advertência foi encontrada no colaborador atual";
    String ATUALIZA_COLABORADOR_COM_ADVERTENCIA = "Realizando atualização do objeto colaborador com advertência acoplada...";
    String ACESSA_METODO_BUSCA_ADVERTENCIA = "Iniciando acesso ao método de busca de advertência por id na lista de advertências do cliente...";

    public void geraAdvertenciaColaborador(ColaboradorId idColaboradorSessao,
                                           HttpServletResponse res,
                                           UUID idColaboradorAlvo,
                                           MultipartFile contratoAdvertencia,
                                           String advertenciaEmJson) throws IOException {

        log.debug("Método de serviço de criação de nova advertência acessado");

        ColaboradorEntity colaboradorLogado = securityUtil.obtemUsuarioSessao(idColaboradorSessao);

        log.debug(Constantes.VERIFICANDO_SE_COLABORADOR_PODE_ALTERAR_DADOS);
        securityUtil.verificaSePodeRealizarAlteracoes(colaboradorLogado.getAcessoSistema());

        log.debug("Convertendo objeto advertência recebido de Json para entity...");
        AdvertenciaRequest advertenciaRequest = new ObjectMapper()
                .readValue(advertenciaEmJson, AdvertenciaRequest.class);

        log.info("Iniciando setagem de dados da advertência...");
        AdvertenciaEntity advertenciaEntity = new AdvertenciaEntity()
                .buildFromRequest(advertenciaRequest, contratoAdvertencia);
        log.info("Setagem de dados realizada com sucesso");

        log.debug("Obtendo colaborador pelo id ({})...", idColaboradorAlvo);
        ColaboradorEntity colaboradorEncontrado = colaboradorRepositoryImpl.implementaBuscaPorId(
                idColaboradorSessao.getIdEmpresa(), colaboradorLogado.getId());

        log.debug("Adicionando advertência criada ao objeto colaborador...");
        colaboradorEncontrado.addAdvertencia(advertenciaEntity);

        log.debug(ATUALIZA_COLABORADOR_COM_ADVERTENCIA);
        colaboradorRepositoryImpl.implementaPersistencia(colaboradorEncontrado);

        log.debug("Persistência realizada com sucesso");

        log.debug("Iniciando acesso ao método de exportação de PDF para gerar a advertência em PDF...");
        advertenciaRelatorioService.exportarPdf(res, colaboradorLogado, colaboradorEncontrado, advertenciaEntity);

        log.debug(Constantes.INICIANDO_SALVAMENTO_HISTORICO_COLABORADOR);
        acaoService.salvaHistoricoColaborador(idColaboradorSessao, colaboradorEncontrado.getId(),
                ModulosEnum.COLABORADORES, TipoAcaoEnum.ALTERACAO, "Advertência criada");

        log.info("Requisição finalizada com sucesso");
    }

    public AdvertenciaPageResponse obtemAdvertenciasPaginadasColaborador(Pageable pageable,
                                                                         ColaboradorId idColaboradorSessao,
                                                                         UUID idColaborador) {

        log.debug("Método de serviço obtenção de advertências do colaborador de id {} acessado", idColaborador);

        log.debug("Acessando repositório de busca de advertências");
        Page<AdvertenciaEntity> advertenciaPage = colaboradorRepository.buscaAdvertenciasPorIdColaborador(
                pageable, idColaboradorSessao.getIdEmpresa(), idColaboradorSessao.getId());

        log.debug("Busca de advertências por paginação realizada com sucesso. Acessando método de conversão dos objetos do tipo " +
                "Entity para objetos do tipo Response...");
        AdvertenciaPageResponse advertenciaPageResponse = new AdvertenciaPageResponse()
                .buildPageResponse(advertenciaPage);
        log.debug("Conversão de tipagem realizada com sucesso");

        log.info("A busca paginada de advertências foi realizada com sucesso");
        return advertenciaPageResponse;
    }

    public void geraPdfPadraoAdvertencia(ColaboradorId idColaboradorSessao,
                                         HttpServletResponse res,
                                         UUID idColaborador,
                                         UUID idAdvertencia) throws IOException {

        log.debug("Método de serviço de obtenção de PDF padrão da advertência acessado");

        ColaboradorEntity colaboradorLogado = securityUtil.obtemUsuarioSessao(idColaboradorSessao);

        log.debug("Obtendo colaborador pelo id ({})...", idColaborador);
        ColaboradorEntity colaborador = colaboradorRepositoryImpl.implementaBuscaPorId(
                idColaborador, colaboradorLogado.getIdEmpresa());

        log.debug(ACESSA_METODO_BUSCA_ADVERTENCIA);
        AdvertenciaEntity advertenciaEntity = realizaBuscaAdvertenciaPorIdNaListaDeAdvertenciasDoColaborador(
                colaborador.getAdvertencias(), idAdvertencia);

        log.debug("Iniciando exportação do PDF padrão");
        advertenciaRelatorioService.exportarPdf(res, colaboradorLogado, colaborador, advertenciaEntity);

        log.debug(Constantes.INICIANDO_SALVAMENTO_HISTORICO_COLABORADOR);
        acaoService.salvaHistoricoColaborador(idColaboradorSessao, colaborador.getId(),
                ModulosEnum.COLABORADORES, TipoAcaoEnum.RELATORIO, "Arquivo padrão de advertência gerado");

        log.info("Exportação do PDF padrão da advertência de id {} realizado com sucesso", idAdvertencia);
    }

    public ArquivoEntity obtemAnexoAdvertencia(ColaboradorId idColaboradorSessao,
                                               UUID idColaborador,
                                               UUID idAdvertencia) {

        log.debug("Método de serviço de obtenção de anexo de advertência acessado");

        log.debug("Obtendo colaborador pelo id: {}...", idColaborador);
        ColaboradorEntity colaborador = colaboradorRepositoryImpl.implementaBuscaPorId(
                idColaboradorSessao.getIdEmpresa(), idColaboradorSessao.getId());

        log.debug(ACESSA_METODO_BUSCA_ADVERTENCIA);
        AdvertenciaEntity advertenciaEntity = realizaBuscaAdvertenciaPorIdNaListaDeAdvertenciasDoColaborador(
                colaborador.getAdvertencias(), idAdvertencia);

        log.debug(Constantes.INICIANDO_SALVAMENTO_HISTORICO_COLABORADOR);
        acaoService.salvaHistoricoColaborador(idColaboradorSessao, colaborador.getId(),
                ModulosEnum.COLABORADORES, TipoAcaoEnum.OUTRO, "Download de anexo de advertência");

        log.info("Obtenção de anexo de advertência de id {} finalizado com sucesso", idAdvertencia);
        return advertenciaEntity.getAdvertenciaAssinada();
    }

    public void alteraStatusAdvertencia(ColaboradorId idColaboradorSessao,
                                        StatusAdvertenciaEnum statusAdvertenciaEnum,
                                        UUID idColaborador,
                                        UUID idAdvertencia) {

        log.debug("Método de serviço de alteração de status da advertência acessado");

        ColaboradorEntity colaboradorLogado = securityUtil.obtemUsuarioSessao(idColaboradorSessao);

        log.debug(Constantes.VERIFICANDO_SE_COLABORADOR_PODE_ALTERAR_DADOS);
        securityUtil.verificaSePodeRealizarAlteracoes(colaboradorLogado.getAcessoSistema());

        log.debug("Obtendo colaborador pelo id {}...", idColaborador);
        ColaboradorEntity colaborador = colaboradorRepositoryImpl.implementaBuscaPorId(
                idColaboradorSessao.getIdEmpresa(), idColaboradorSessao.getId());

        log.debug(ACESSA_METODO_BUSCA_ADVERTENCIA);
        AdvertenciaEntity advertenciaEntity = realizaBuscaAdvertenciaPorIdNaListaDeAdvertenciasDoColaborador(
                colaborador.getAdvertencias(), idAdvertencia);

        log.debug("Setando status atualizado da advertência...");
        advertenciaEntity.setStatusAdvertencia(statusAdvertenciaEnum);

        log.debug(ATUALIZA_COLABORADOR_COM_ADVERTENCIA);
        colaboradorRepositoryImpl.implementaPersistencia(colaborador);

        log.debug(Constantes.INICIANDO_SALVAMENTO_HISTORICO_COLABORADOR);
        acaoService.salvaHistoricoColaborador(idColaboradorSessao, colaborador.getId(),
                ModulosEnum.COLABORADORES, TipoAcaoEnum.ALTERACAO,
                "Status da advertência de motivo [" + advertenciaEntity.getMotivo()
                        + "] alterado para " + statusAdvertenciaEnum.getDesc());

        log.info("Atualização de status da advertência de id {} finalizado com sucesso", idAdvertencia);
    }

    public void anexaArquivoAdvertencia(ColaboradorId idColaboradorSessao,
                                        MultipartFile anexo,
                                        UUID idColaborador,
                                        UUID idAdvertencia) throws IOException {

        log.debug("Método de serviço de anexação de PDF padrão na advertência acessado");

        ColaboradorEntity colaboradorLogado = securityUtil.obtemUsuarioSessao(idColaboradorSessao);

        log.debug(Constantes.VERIFICANDO_SE_COLABORADOR_PODE_ALTERAR_DADOS);
        securityUtil.verificaSePodeRealizarAlteracoes(colaboradorLogado.getAcessoSistema());

        log.debug("Obtendo colaborador pelo id {}...", idColaborador);
        ColaboradorEntity colaborador = colaboradorRepositoryImpl.implementaBuscaPorId(
                idColaborador, colaboradorLogado.getIdEmpresa());

        log.debug(ACESSA_METODO_BUSCA_ADVERTENCIA);
        AdvertenciaEntity advertenciaEntity = realizaBuscaAdvertenciaPorIdNaListaDeAdvertenciasDoColaborador(
                colaborador.getAdvertencias(), idAdvertencia);

        log.debug("Setando contrato na advertência do colaborador...");
        advertenciaEntity.setAdvertenciaAssinada(new ArquivoEntity().buildFromMultiPartFile(anexo));

        log.debug(ATUALIZA_COLABORADOR_COM_ADVERTENCIA);
        colaboradorRepositoryImpl.implementaPersistencia(colaborador);

        log.debug(Constantes.INICIANDO_SALVAMENTO_HISTORICO_COLABORADOR);
        acaoService.salvaHistoricoColaborador(idColaboradorSessao, colaborador.getId(),
                ModulosEnum.COLABORADORES, TipoAcaoEnum.ALTERACAO, "Anexo adicionado à advertência");

        log.info("Atualização de anexo da advertência de id {} finalizado com sucesso", idAdvertencia);
    }

    public void removerAdvertencia(ColaboradorId idColaboradorSessao,
                                   UUID idColaborador,
                                   UUID idAdvertencia) {

        log.debug("Método de serviço de remoção de advertência acessado");

        ColaboradorEntity colaboradorLogado = securityUtil.obtemUsuarioSessao(idColaboradorSessao);

        log.debug(Constantes.VERIFICANDO_SE_COLABORADOR_PODE_ALTERAR_DADOS);
        securityUtil.verificaSePodeRealizarAlteracoes(colaboradorLogado.getAcessoSistema());

        log.debug("Obtendo colaborador pelo id: {}...", idColaborador);
        ColaboradorEntity colaborador = colaboradorRepositoryImpl.implementaBuscaPorId(idColaborador,
                colaboradorLogado.getIdEmpresa());

        log.debug(ACESSA_METODO_BUSCA_ADVERTENCIA);
        AdvertenciaEntity advertenciaEntity = realizaBuscaAdvertenciaPorIdNaListaDeAdvertenciasDoColaborador(
                colaborador.getAdvertencias(), idAdvertencia);

        log.debug("Removendo advertência...");
        colaborador.removeAdvertencia(advertenciaEntity);

        log.debug(ATUALIZA_COLABORADOR_COM_ADVERTENCIA);
        colaboradorRepositoryImpl.implementaPersistencia(colaborador);

        log.debug(Constantes.INICIANDO_SALVAMENTO_HISTORICO_COLABORADOR);
        acaoService.salvaHistoricoColaborador(idColaboradorSessao, colaborador.getId(),
                ModulosEnum.COLABORADORES, TipoAcaoEnum.REMOCAO, "Remoção de advertência realizada");

        log.info("Remoção de advertência de id {} finalizado com sucesso", idAdvertencia);
    }

    public AdvertenciaEntity realizaBuscaAdvertenciaPorIdNaListaDeAdvertenciasDoColaborador(List<AdvertenciaEntity> advertenciaList,
                                                                                            UUID idAdvertenciaBuscada) {

        log.debug("Método de busca de advertência por id na lista de advertências do colaborador acessado");

        AdvertenciaEntity advertenciaEntity = null;

        log.debug("Iniciando iteração por todas as advertências do colaborador...");
        for (AdvertenciaEntity advertencia : advertenciaList) {
            log.debug("Verificando se advertência de id {} possui o mesmo id recebido por parâmetro: ({})",
                    advertencia.getId(), idAdvertenciaBuscada);
            if (Objects.equals(advertencia.getId(), idAdvertenciaBuscada)) {
                log.debug("A advertência possui o mesmo id");
                advertenciaEntity = advertencia;
            }
        }

        if (advertenciaEntity == null) {
            log.error(NENHUMA_ADVERTENCIA_ENCONTRADA);
            throw new ObjectNotFoundException(NENHUMA_ADVERTENCIA_ENCONTRADA);
        }

        return advertenciaEntity;
    }

}
