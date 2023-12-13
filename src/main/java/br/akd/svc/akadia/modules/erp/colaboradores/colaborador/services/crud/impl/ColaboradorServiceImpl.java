package br.akd.svc.akadia.modules.erp.colaboradores.colaborador.services.crud.impl;

import br.akd.svc.akadia.config.security.utils.SecurityUtil;
import br.akd.svc.akadia.exceptions.ObjectNotFoundException;
import br.akd.svc.akadia.modules.erp.colaboradores.acao.models.enums.TipoAcaoEnum;
import br.akd.svc.akadia.modules.erp.colaboradores.acao.services.AcaoService;
import br.akd.svc.akadia.modules.erp.colaboradores.colaborador.models.dto.colaborador.request.ColaboradorRequest;
import br.akd.svc.akadia.modules.erp.colaboradores.colaborador.models.dto.colaborador.response.ColaboradorResponse;
import br.akd.svc.akadia.modules.erp.colaboradores.colaborador.models.dto.colaborador.response.CriacaoColaboradorResponse;
import br.akd.svc.akadia.modules.erp.colaboradores.colaborador.models.dto.colaborador.response.page.ColaboradorPageResponse;
import br.akd.svc.akadia.modules.erp.colaboradores.colaborador.models.entity.colaborador.ColaboradorEntity;
import br.akd.svc.akadia.modules.erp.colaboradores.colaborador.models.entity.colaborador.id.ColaboradorId;
import br.akd.svc.akadia.modules.erp.colaboradores.colaborador.models.enums.ModulosEnum;
import br.akd.svc.akadia.modules.erp.colaboradores.colaborador.repository.ColaboradorRepository;
import br.akd.svc.akadia.modules.erp.colaboradores.colaborador.repository.impl.ColaboradorRepositoryImpl;
import br.akd.svc.akadia.modules.erp.colaboradores.colaborador.services.crud.ColaboradorService;
import br.akd.svc.akadia.modules.erp.colaboradores.colaborador.services.utils.ColaboradorServiceUtil;
import br.akd.svc.akadia.modules.erp.colaboradores.colaborador.services.validator.ColaboradorValidation;
import br.akd.svc.akadia.modules.external.empresa.EmpresaId;
import br.akd.svc.akadia.modules.global.objects.exclusao.entity.ExclusaoEntity;
import br.akd.svc.akadia.modules.global.objects.imagem.entity.ImagemEntity;
import br.akd.svc.akadia.modules.global.objects.imagem.response.ImagemResponse;
import br.akd.svc.akadia.utils.Constantes;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service
public class ColaboradorServiceImpl implements ColaboradorService {

    @Autowired
    AcaoService acaoService;

    @Autowired
    ColaboradorRepositoryImpl colaboradorRepositoryImpl;

    @Autowired
    ColaboradorRepository colaboradorRepository;

    @Autowired
    SecurityUtil securityUtil;

    @Autowired
    ColaboradorServiceUtil colaboradorServiceUtil;

    @Autowired
    ColaboradorValidation colaboradorValidation;

    String BUSCA_COLABORADOR_POR_ID = "Iniciando acesso ao método de implementação de busca de colaborador por id...";

    public String criaNovoColaborador(ColaboradorId idColaboradorSessao,
                                      MultipartFile contratoColaborador,
                                      String colaboradorEmJson) throws IOException {

        log.info("Método de serviço de criação de novo colaborador acessado");

        ColaboradorEntity colaboradorLogado = securityUtil.obtemUsuarioSessao(idColaboradorSessao);

        log.info(Constantes.VERIFICANDO_SE_COLABORADOR_PODE_ALTERAR_DADOS);
        securityUtil.verificaSePodeRealizarAlteracoes(colaboradorLogado.getAcessoSistema());

        log.info("Convertendo objeto colaborador recebido de Json para entity...");
        ColaboradorRequest colaboradorEntity = new ObjectMapper()
                .readValue(colaboradorEmJson, ColaboradorRequest.class);

        String matriculaGerada = colaboradorServiceUtil.geraMatriculaUnica();

        ColaboradorEntity colaboradorGerado = new ColaboradorEntity().buildFromRequest(
                new EmpresaId(colaboradorLogado.getIdClienteSistema(), colaboradorLogado.getIdEmpresa()),
                matriculaGerada,
                contratoColaborador,
                colaboradorEntity);

        log.info(Constantes.INICIANDO_IMPL_PERSISTENCIA_COLABORADOR);
        ColaboradorEntity colaboradorPersistido = colaboradorRepositoryImpl
                .implementaPersistencia(colaboradorGerado);

        log.info(Constantes.INICIANDO_SALVAMENTO_HISTORICO_COLABORADOR);
        acaoService.salvaHistoricoColaborador(idColaboradorSessao, colaboradorPersistido.getId(),
                ModulosEnum.COLABORADORES, TipoAcaoEnum.CRIACAO, null);

        log.info("Colaborador criado com sucesso");
        return colaboradorPersistido.getMatricula();
    }

    public ColaboradorPageResponse realizaBuscaPaginadaPorColaboradores(ColaboradorId idColaboradorSessao,
                                                                        Pageable pageable,
                                                                        String campoBusca) {
        log.info("Método de serviço de obtenção paginada de colaboradores acessado");

        log.info("Acessando repositório de busca de colaboradores");
        Page<ColaboradorEntity> colaboradorPage = colaboradorRepository.buscaPaginadaPorColaboradores(
                pageable, idColaboradorSessao.getIdEmpresa(), campoBusca);

        log.info("Busca de colaboradores por paginação realizada com sucesso. Acessando método de conversão dos " +
                "objetos do tipo Entity para objetos do tipo Response...");
        ColaboradorPageResponse colaboradorPageResponse =
                new ColaboradorPageResponse().buildPageResponse(colaboradorPage);
        log.info("Conversão de tipagem realizada com sucesso");

        log.info("A busca paginada de colaboradores foi realizada com sucesso");
        return colaboradorPageResponse;
    }

    public ImagemResponse obtemImagemPerfilColaborador(ColaboradorId idColaboradorSessao,
                                                       UUID idColaborador) {
        log.info("Método de serviço obtenção de imagem de perfil de colaborador por id acessado");

        log.info("Iniciando acesso ao método responsável por implementar a lógica de busca pela iamgem de perfil do " +
                "colaborador no banco de dados...");
        ImagemEntity imagemEntity = colaboradorRepositoryImpl
                .implementaBuscaDeImagemDePerfilPorId(idColaboradorSessao.getIdEmpresa(), idColaborador);
        log.info("Objeto ImagemEntity obtido com sucesso");

        log.info("Iniciando acesso ao método responsável pela conversão de objeto do tipo ImagemEntity para objeto " +
                "do tipo ImagemResponse...");
        ImagemResponse imagemResponse = new ImagemResponse().buildFromEntity(imagemEntity);

        log.info("Conversão de tipagem realizada com sucesso. Retornando objeto ImagemResponse...");
        return imagemResponse;
    }

    public List<String> obtemTodasOcupacoesEmpresa(ColaboradorId idColaboradorSessao) {
        log.info("Método responsável por obter todas as ocupações de uma determinada empresa acessado");

        log.info("Iniciando acesso ao repositório de busca de ocupações da empresa...");
        List<String> ocupacoesEmpresa = colaboradorRepositoryImpl
                .implementaBuscaPorTodasAsOcupacoesDaEmpresa(idColaboradorSessao.getIdEmpresa());
        log.info("Obtenção das ocupações da empresa realizada com sucesso");

        log.info("Iniciando ordenação dos dados obtidos...");
        return new LinkedHashSet<>(ocupacoesEmpresa).stream()
                .sorted()
                .collect(Collectors.toList());
    }

    public ColaboradorResponse realizaBuscaDeColaboradorPorId(ColaboradorId idColaboradorSessao,
                                                              UUID idColaborador) {
        log.info("Método de serviço de obtenção de colaborador por id. ID recebido: {}", idColaborador);

        log.info("Acessando repositório de busca de colaborador por ID...");
        ColaboradorEntity colaborador = colaboradorRepositoryImpl
                .implementaBuscaPorId(idColaboradorSessao.getIdEmpresa(), idColaborador);

        log.info("Busca de colaboradores por id realizada com sucesso. Acessando método de conversão dos objeto do tipo " +
                "Entity para objeto do tipo Response...");
        ColaboradorResponse colaboradorResponse = new ColaboradorResponse().buildFromEntity(colaborador);
        log.info("Conversão de tipagem realizada com sucesso");

        log.info("A busca de colaborador por id foi realizada com sucesso");
        return colaboradorResponse;
    }

    public ColaboradorResponse atualizaColaborador(ColaboradorId idColaboradorSessao,
                                                   UUID idColaborador,
                                                   MultipartFile contratoColaborador,
                                                   String colaboradorEmJson) throws IOException {
        log.info("Método de serviço de atualização de colaborador acessado");

        ColaboradorEntity colaboradorLogado = securityUtil.obtemUsuarioSessao(idColaboradorSessao);

        log.info(Constantes.VERIFICANDO_SE_COLABORADOR_PODE_ALTERAR_DADOS);
        securityUtil.verificaSePodeRealizarAlteracoes(colaboradorLogado.getAcessoSistema());

        log.info("Convertendo objeto colaborador recebido de Json para entity...");
        ColaboradorRequest colaboradorRequest = new ObjectMapper()
                .readValue(colaboradorEmJson, ColaboradorRequest.class);

        log.info(BUSCA_COLABORADOR_POR_ID);
        ColaboradorEntity colaboradorEncontrado = colaboradorRepositoryImpl
                .implementaBuscaPorId(idColaboradorSessao.getIdEmpresa(), idColaborador);

        log.info("Iniciando acesso ao método de validação de alteração de dados de colaborador excluído...");
        colaboradorValidation.validaSeColaboradorEstaExcluido(
                colaboradorEncontrado, "Não é possível atualizar um colaborador excluído");

        log.info("Iniciando criação do objeto ColaboradorEntity...");
        ColaboradorEntity novoColaboradorAtualizado = new ColaboradorEntity()
                .updateFromRequest(contratoColaborador, colaboradorEncontrado, colaboradorRequest);
        log.info("Objeto colaborador construído com sucesso");

        log.info(Constantes.INICIANDO_IMPL_PERSISTENCIA_COLABORADOR);
        ColaboradorEntity colaboradorPersistido = colaboradorRepositoryImpl
                .implementaPersistencia(novoColaboradorAtualizado);

        log.info(Constantes.INICIANDO_SALVAMENTO_HISTORICO_COLABORADOR);
        acaoService.salvaHistoricoColaborador(idColaboradorSessao, colaboradorPersistido.getId(),
                ModulosEnum.COLABORADORES, TipoAcaoEnum.ALTERACAO, null);

        log.info("Colaborador persistido com sucesso. Convertendo colaboradorEntity para colaboradorResponse...");
        ColaboradorResponse colaboradorResponse = new ColaboradorResponse()
                .buildFromEntity(colaboradorPersistido);

        log.info("Colaborador criado com sucesso");
        return colaboradorResponse;
    }

    public ColaboradorResponse atualizaImagemPerfilColaborador(ColaboradorId idColaboradorSessao,
                                                               UUID idColaborador,
                                                               MultipartFile fotoPerfil) throws IOException {

        log.info("Método de serviço de atualização de foto de perfil de colaborador acessado");

        ColaboradorEntity colaboradorLogado = securityUtil.obtemUsuarioSessao(idColaboradorSessao);

        log.info(Constantes.VERIFICANDO_SE_COLABORADOR_PODE_ALTERAR_DADOS);
        securityUtil.verificaSePodeRealizarAlteracoes(colaboradorLogado.getAcessoSistema());

        log.info("Iniciando construção do objeto ImagemEntity da foto de perfil do colaborador...");
        ImagemEntity fotoPerfilEntity = new ImagemEntity().constroiImagemEntity(fotoPerfil);

        log.info("Acessando repositório de busca de colaborador por ID...");
        ColaboradorEntity colaborador = colaboradorRepositoryImpl
                .implementaBuscaPorId(idColaborador, colaboradorLogado.getId());

        log.info("Acoplando foto de perfil ao objeto do colaborador...");
        colaborador.setFotoPerfil(fotoPerfilEntity);

        log.info(Constantes.INICIANDO_IMPL_PERSISTENCIA_COLABORADOR);
        ColaboradorEntity colaboradorPersistido = colaboradorRepositoryImpl.implementaPersistencia(colaborador);

        if (fotoPerfil != null) {
            log.info(Constantes.INICIANDO_SALVAMENTO_HISTORICO_COLABORADOR);
            acaoService.salvaHistoricoColaborador(idColaboradorSessao, colaboradorPersistido.getId(),
                    ModulosEnum.COLABORADORES, TipoAcaoEnum.ALTERACAO, "Alteração da foto de perfil do colaborador "
                            + colaboradorPersistido.getNome());
        } else {
            log.info(Constantes.INICIANDO_SALVAMENTO_HISTORICO_COLABORADOR);
            acaoService.salvaHistoricoColaborador(idColaboradorSessao, colaboradorPersistido.getId(),
                    ModulosEnum.COLABORADORES, TipoAcaoEnum.REMOCAO, "Remoção da foto de perfil do colaborador "
                            + colaboradorPersistido.getNome());
        }

        return new ColaboradorResponse().buildFromEntity(colaboradorPersistido);
    }

    public ColaboradorResponse removeColaborador(ColaboradorId idColaboradorSessao,
                                                 UUID idColaborador) {
        log.info("Método de serviço de remoção de colaborador acessado");

        log.info(BUSCA_COLABORADOR_POR_ID);
        ColaboradorEntity colaboradorEncontrado = colaboradorRepositoryImpl
                .implementaBuscaPorId(idColaboradorSessao.getId(), idColaborador);

        log.info("Iniciando acesso ao método de validação de exclusão de colaborador que já foi excluído...");
        colaboradorValidation.validaSeColaboradorEstaExcluido(
                colaboradorEncontrado, "O colaborador selecionado já foi excluído");

        log.info("Atualizando objeto ExclusaoColaborador do colaborador com dados referentes à sua exclusão...");
        ExclusaoEntity exclusao = new ExclusaoEntity().constroiObjetoExclusao();

        log.info("Setando objeto exclusão criado no colaborador encontrado...");
        colaboradorEncontrado.setExclusao(exclusao);

        log.info("Persistindo colaborador excluído no banco de dados...");
        ColaboradorEntity colaboradorExcluido = colaboradorRepositoryImpl
                .implementaPersistencia(colaboradorEncontrado);

        log.info(Constantes.INICIANDO_SALVAMENTO_HISTORICO_COLABORADOR);
        acaoService.salvaHistoricoColaborador(idColaboradorSessao, null, ModulosEnum.COLABORADORES,
                TipoAcaoEnum.REMOCAO, null);

        log.info("Colaborador excluído com sucesso");
        return new ColaboradorResponse().buildFromEntity(colaboradorExcluido);
    }

    public void removeColaboradoresEmMassa(ColaboradorId idColaboradorSessao,
                                           List<UUID> uuidsColaborador) {
        log.info("Método de serviço de remoção de colaborador acessado");

        List<ColaboradorEntity> colaboradoresEncontrados = new ArrayList<>();

        for (UUID id : uuidsColaborador) {
            log.info(BUSCA_COLABORADOR_POR_ID);
            ColaboradorEntity colaboradorEncontrado = colaboradorRepositoryImpl
                    .implementaBuscaPorId(idColaboradorSessao.getIdEmpresa(), id);
            colaboradoresEncontrados.add(colaboradorEncontrado);
        }

        colaboradoresEncontrados.forEach(colaborador -> {
            colaboradorValidation.validaSeColaboradorEstaExcluido(
                    colaborador, "O Colaborador selecionado já foi excluído");
            log.info("Atualizando objeto Exclusao do colaborador com dados referentes à sua exclusão...");
            colaborador.setExclusao(new ExclusaoEntity().constroiObjetoExclusao());
            log.info("Objeto ExclusaoColaborador do colaborador de id {} setado com sucesso", colaborador.getId());
        });

        log.info("Verificando se listagem de colaboradores encontrados está preenchida...");
        if (!colaboradoresEncontrados.isEmpty()) {
            log.info("Persistindo colaborador excluído no banco de dados...");
            colaboradorRepositoryImpl.implementaPersistenciaEmMassa(colaboradoresEncontrados);

            log.info(Constantes.INICIANDO_SALVAMENTO_HISTORICO_COLABORADOR);
            acaoService.salvaHistoricoColaborador(idColaboradorSessao, null, ModulosEnum.COLABORADORES,
                    TipoAcaoEnum.REMOCAO_EM_MASSA, colaboradoresEncontrados.size() + " Itens removidos");
        } else throw new ObjectNotFoundException("Nenhum colaborador foi encontrado para remoção");

        log.info("colaboradores excluídos com sucesso");
    }


    @Override
//    @Transactional
    public CriacaoColaboradorResponse criaColaboradorAdminParaNovaEmpresa(EmpresaId empresaId) {
        log.info("Método de criação de colaborador ADMIN DEFAULT para nova empresa acessado");

        log.info("Iniciando construção de objeto colaborador entity...");
        ColaboradorEntity colaboradorCriado = new ColaboradorEntity().buildRoot(
                colaboradorServiceUtil, empresaId);
        log.info("ColaboradorEntity criado com sucesso");

        log.info("Iniciando acesso ao método de implementação da persistência do colaborador...");
        ColaboradorEntity colaboradorPersistido = colaboradorRepositoryImpl.implementaPersistencia(colaboradorCriado);
        log.info("Colaborador persistido com sucesso. Retornando...");

        return new CriacaoColaboradorResponse(
                colaboradorPersistido.getMatricula(), colaboradorPersistido.getAcessoSistema().getSenha());
    }


}
