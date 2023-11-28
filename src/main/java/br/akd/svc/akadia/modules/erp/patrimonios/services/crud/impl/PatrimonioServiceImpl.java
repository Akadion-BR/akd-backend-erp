package br.akd.svc.akadia.modules.erp.patrimonios.services.crud.impl;

import br.akd.svc.akadia.exceptions.InvalidRequestException;
import br.akd.svc.akadia.modules.erp.colaboradores.acao.models.enums.TipoAcaoEnum;
import br.akd.svc.akadia.modules.erp.colaboradores.acao.services.AcaoService;
import br.akd.svc.akadia.modules.erp.colaboradores.colaborador.models.entity.colaborador.ColaboradorEntity;
import br.akd.svc.akadia.modules.erp.colaboradores.colaborador.models.entity.colaborador.id.ColaboradorId;
import br.akd.svc.akadia.modules.erp.colaboradores.colaborador.models.enums.ModulosEnum;
import br.akd.svc.akadia.modules.erp.patrimonios.models.dto.request.PatrimonioRequest;
import br.akd.svc.akadia.modules.erp.patrimonios.models.dto.response.PatrimonioResponse;
import br.akd.svc.akadia.modules.erp.patrimonios.models.dto.response.page.PatrimonioPageResponse;
import br.akd.svc.akadia.modules.erp.patrimonios.models.entity.PatrimonioEntity;
import br.akd.svc.akadia.modules.erp.patrimonios.repository.PatrimonioRepository;
import br.akd.svc.akadia.modules.erp.patrimonios.repository.impl.PatrimonioRepositoryImpl;
import br.akd.svc.akadia.modules.erp.patrimonios.services.crud.PatrimonioService;
import br.akd.svc.akadia.modules.erp.patrimonios.services.validator.PatrimonioValidationService;
import br.akd.svc.akadia.modules.global.objects.exclusao.entity.ExclusaoEntity;
import br.akd.svc.akadia.utils.Constantes;
import br.akd.svc.akadia.config.security.utils.SecurityUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
public class PatrimonioServiceImpl implements PatrimonioService {

    @Autowired
    PatrimonioRepository patrimonioRepository;

    @Autowired
    PatrimonioRepositoryImpl patrimonioRepositoryImpl;

    @Autowired
    PatrimonioValidationService patrimonioValidationService;

    @Autowired
    AcaoService acaoService;

    @Autowired
    SecurityUtil securityUtil;

    String INICIA_BUSCA_POR_ID = "Iniciando acesso ao método de implementação de busca de patrimônio por id...";

    public PatrimonioResponse criaNovoPatrimonio(ColaboradorId idColaboradorSessao,
                                                 PatrimonioRequest patrimonioRequest) {
        log.debug("Método de serviço de criação de novo patrimônio acessado");

        ColaboradorEntity colaboradorLogado = securityUtil
                .obtemUsuarioSessao(idColaboradorSessao);

        log.debug(Constantes.VERIFICANDO_SE_COLABORADOR_PODE_ALTERAR_DADOS);
        securityUtil.verificaSePodeRealizarAlteracoes(colaboradorLogado.getAcessoSistema());

        log.debug("Iniciando criação do objeto PatrimonioEntity...");
        PatrimonioEntity patrimonioEntity = new PatrimonioEntity()
                .buildFromRequest(colaboradorLogado, patrimonioRequest);
        log.debug("Objeto patrimonioEntity criado com sucesso");

        log.debug("Iniciando acesso ao método de implementação da persistência do patrimônio...");
        PatrimonioEntity patrimonioPersistido = patrimonioRepositoryImpl.implementaPersistencia(patrimonioEntity);

        log.debug(Constantes.INICIANDO_SALVAMENTO_HISTORICO_PATRIMONIO);
        acaoService.salvaHistoricoColaborador(idColaboradorSessao, patrimonioPersistido.getId(),
                ModulosEnum.PATRIMONIOS, TipoAcaoEnum.CRIACAO, null);

        log.debug("Patrimônio persistido com sucesso. Convertendo patrimonioEntity para patrimonioResponse...");
        PatrimonioResponse patrimonioResponse = new PatrimonioResponse().buildFromEntity(patrimonioPersistido);

        log.info("Patrimônio criado com sucesso");
        return patrimonioResponse;
    }

    public PatrimonioPageResponse realizaBuscaPaginada(ColaboradorId idColaboradorSessao,
                                                       Pageable pageable,
                                                       String campoBusca) {
        log.debug("Método de serviço de obtenção paginada de patrimônios acessado. Campo de busca: {}",
                campoBusca != null ? campoBusca : "Nulo");

        log.debug("Acessando repositório de busca de patrimonios");
        Page<PatrimonioEntity> patrimonioPage = patrimonioRepository
                .buscaPaginadaPorClientes(pageable, idColaboradorSessao.getEmpresa(), campoBusca);

        log.debug("Busca de patrimônios por paginação realizada com sucesso. Acessando método de conversão dos objetos do tipo " +
                "Entity para objetos do tipo Response...");
        PatrimonioPageResponse patrimonioPageResponse = new PatrimonioPageResponse()
                .buildPatrimonioPageResponse(patrimonioPage);
        log.debug("Conversão de tipagem realizada com sucesso");

        log.info("A busca paginada de patrimônios foi realizada com sucesso");
        return patrimonioPageResponse;
    }

    public PatrimonioResponse realizaBuscaPorId(ColaboradorId idColaboradorSessao,
                                                UUID idPatrimonio) {
        log.debug("Método de serviço de obtenção de patrimônio por id. ID recebido: {}", idPatrimonio);

        log.debug("Acessando repositório de busca de patrimônio por ID...");
        PatrimonioEntity patrimonio = patrimonioRepositoryImpl
                .implementaBuscaPorId(idColaboradorSessao.getEmpresa(), idPatrimonio);

        log.debug("Busca de patrimônios por id realizada com sucesso. Acessando método de conversão dos objeto do tipo " +
                "Entity para objeto do tipo Response...");
        PatrimonioResponse patrimonioResponse = new PatrimonioResponse().buildFromEntity(patrimonio);
        log.debug("Conversão de tipagem realizada com sucesso");

        log.info("A busca de patrimonio por id foi realizada com sucesso");
        return patrimonioResponse;
    }

    public PatrimonioResponse atualizaObjeto(ColaboradorId idColaboradorSessao,
                                             UUID idPatrimonio,
                                             PatrimonioRequest patrimonioRequest) {
        log.debug("Método de serviço de atualização de patrimônio acessado");

        ColaboradorEntity colaboradorLogado = securityUtil
                .obtemUsuarioSessao(idColaboradorSessao);

        log.debug(Constantes.VERIFICANDO_SE_COLABORADOR_PODE_ALTERAR_DADOS);
        securityUtil.verificaSePodeRealizarAlteracoes(colaboradorLogado.getAcessoSistema());

        log.debug(INICIA_BUSCA_POR_ID);
        PatrimonioEntity patrimonioEncontrado = patrimonioRepositoryImpl
                .implementaBuscaPorId(idColaboradorSessao.getEmpresa(), idPatrimonio);

        log.debug("Iniciando acesso ao método de validação de alteração de dados de patrimônio excluído...");
        patrimonioValidationService
                .validaSeObjetoEstaExcluido(patrimonioEncontrado, "Não é possível atualizar um patrimônio excluído");

        log.debug("Iniciando criação do objeto PatrimonioEntity...");
        PatrimonioEntity patrimonioAtualizado = new PatrimonioEntity()
                .updateFromRequest(patrimonioEncontrado, patrimonioRequest);
        log.debug("Objeto patrimônio construído com sucesso");

        log.debug("Iniciando acesso ao método de implementação da persistência do patrimônio...");
        PatrimonioEntity patrimonioPersistido = patrimonioRepositoryImpl
                .implementaPersistencia(patrimonioAtualizado);

        log.debug(Constantes.INICIANDO_SALVAMENTO_HISTORICO_COLABORADOR);
        acaoService.salvaHistoricoColaborador(idColaboradorSessao, patrimonioPersistido.getId(),
                ModulosEnum.PATRIMONIOS, TipoAcaoEnum.ALTERACAO, null);

        log.debug("Patrimônio persistido com sucesso. Convertendo Entity para Response...");
        PatrimonioResponse patrimonioResponse = new PatrimonioResponse().buildFromEntity(patrimonioPersistido);

        log.info("Patrimônio criado com sucesso");
        return patrimonioResponse;
    }

    public PatrimonioResponse removeObjeto(ColaboradorId idColaboradorSessao,
                                           UUID uuidPatrimonio) {
        log.debug("Método de serviço de remoção de patrimônio acessado");

        log.debug(INICIA_BUSCA_POR_ID);
        PatrimonioEntity patrimonioEncontrado = patrimonioRepositoryImpl
                .implementaBuscaPorId(idColaboradorSessao.getEmpresa(), uuidPatrimonio);

        log.debug("Iniciando acesso ao método de validação de exclusão de patrimônio que já foi excluído...");
        patrimonioValidationService
                .validaSeObjetoEstaExcluido(patrimonioEncontrado,
                        "O patrimônio selecionado já foi excluído");

        log.debug("Atualizando objeto Exclusao do patrimônio com dados referentes à sua exclusão...");
        ExclusaoEntity exclusaoEntity = new ExclusaoEntity().constroiObjetoExclusao();

        patrimonioEncontrado.setExclusao(exclusaoEntity);
        log.debug("Objeto Exclusao do patrimônio de id {} setado com sucesso", uuidPatrimonio);

        log.debug("Persistindo patrimônio excluído no banco de dados...");
        PatrimonioEntity patrimonioExcluido = patrimonioRepositoryImpl
                .implementaPersistencia(patrimonioEncontrado);

        log.debug(Constantes.INICIANDO_SALVAMENTO_HISTORICO_COLABORADOR);
        acaoService.salvaHistoricoColaborador(idColaboradorSessao, null,
                ModulosEnum.PATRIMONIOS, TipoAcaoEnum.REMOCAO, null);

        log.info("Patrimônio excluído com sucesso");
        return new PatrimonioResponse().buildFromEntity(patrimonioExcluido);
    }

    public void removeEmMassa(ColaboradorId idColaboradorSessao,
                              List<UUID> idsPatrimonio) {
        log.debug("Método de serviço de remoção de patrimônios em massa acessado");

        List<PatrimonioEntity> patrimoniosEncontrados = new ArrayList<>();

        for (UUID idPatrimonio : idsPatrimonio) {
            log.debug(INICIA_BUSCA_POR_ID);
            PatrimonioEntity patrimonioEncontrado = patrimonioRepositoryImpl
                    .implementaBuscaPorId(idColaboradorSessao.getEmpresa(), idPatrimonio);

            patrimoniosEncontrados.add(patrimonioEncontrado);
        }

        log.debug("Iniciando acesso ao método de validação de exclusão de patrimônio que já foi excluído...");
        for (PatrimonioEntity patrimonio : patrimoniosEncontrados) {
            patrimonioValidationService.validaSeObjetoEstaExcluido(patrimonio,
                    "O patrimonio selecionado já foi excluído");
            log.debug("Atualizando objeto Exclusao do patrimonio com dados referentes à sua exclusão...");
            ExclusaoEntity exclusao = new ExclusaoEntity().constroiObjetoExclusao();

            patrimonio.setExclusao(exclusao);
            log.debug("Objeto Exclusao do patrimonio de id {} setado com sucesso", patrimonio.getId());
        }

        log.debug("Verificando se listagem de patrimônios encontrados está preenchida...");
        if (!patrimoniosEncontrados.isEmpty()) {
            log.debug("Persistindo patrimônio excluído no banco de dados...");
            patrimonioRepositoryImpl.implementaPersistenciaEmMassa(patrimoniosEncontrados);

            log.debug(Constantes.INICIANDO_SALVAMENTO_HISTORICO_COLABORADOR);
            acaoService.salvaHistoricoColaborador(idColaboradorSessao, null,
                    ModulosEnum.PATRIMONIOS, TipoAcaoEnum.REMOCAO_EM_MASSA,
                    patrimoniosEncontrados.size() + " Itens removidos");
        } else throw new InvalidRequestException("Nenhum patrimônio foi encontrado para remoção");

        log.info("Patrimônios excluídos com sucesso");
    }
}
