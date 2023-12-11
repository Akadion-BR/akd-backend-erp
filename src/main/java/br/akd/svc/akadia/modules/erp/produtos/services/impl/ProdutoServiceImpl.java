package br.akd.svc.akadia.modules.erp.produtos.services.impl;

import br.akd.svc.akadia.modules.erp.colaboradores.acao.models.enums.TipoAcaoEnum;
import br.akd.svc.akadia.modules.erp.colaboradores.acao.services.AcaoService;
import br.akd.svc.akadia.modules.erp.colaboradores.colaborador.models.entity.colaborador.ColaboradorEntity;
import br.akd.svc.akadia.modules.erp.colaboradores.colaborador.models.entity.colaborador.id.ColaboradorId;
import br.akd.svc.akadia.modules.erp.colaboradores.colaborador.models.enums.ModulosEnum;
import br.akd.svc.akadia.modules.erp.precos.models.entity.PrecoEntity;
import br.akd.svc.akadia.modules.erp.produtos.models.dto.request.ProdutoRequest;
import br.akd.svc.akadia.modules.erp.produtos.models.dto.response.ProdutoResponse;
import br.akd.svc.akadia.modules.erp.produtos.models.dto.response.page.ProdutoPageResponse;
import br.akd.svc.akadia.modules.erp.produtos.models.entity.ProdutoEntity;
import br.akd.svc.akadia.modules.erp.produtos.models.entity.id.ProdutoId;
import br.akd.svc.akadia.modules.erp.produtos.repository.ProdutoRepository;
import br.akd.svc.akadia.modules.erp.produtos.repository.impl.ProdutoRepositoryImpl;
import br.akd.svc.akadia.modules.erp.produtos.services.ProdutoService;
import br.akd.svc.akadia.utils.Constantes;
import br.akd.svc.akadia.config.security.utils.SecurityUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Slf4j
@Service
public class ProdutoServiceImpl implements ProdutoService {
    @Autowired
    ProdutoRepository produtoRepository;

    @Autowired
    ProdutoRepositoryImpl produtoRepositoryImpl;

    @Autowired
    AcaoService acaoService;

    @Autowired
    SecurityUtil securityUtil;

    public ProdutoResponse criaNovoProduto(ColaboradorId idColaboradorSessao,
                                           ProdutoRequest produtoRequest) {

        log.debug("Método de serviço de criação de novo produto acessado");

        ColaboradorEntity colaboradorLogado = securityUtil.obtemUsuarioSessao(idColaboradorSessao);

        log.debug(Constantes.VERIFICANDO_SE_COLABORADOR_PODE_ALTERAR_DADOS);
        securityUtil.verificaSePodeRealizarAlteracoes(colaboradorLogado.getAcessoSistema());

        log.debug("Iniciando criação do objeto ProdutoEntity...");
        ProdutoEntity produtoEntity = new ProdutoEntity()
                .buildFromRequest(colaboradorLogado, produtoRequest);

        produtoEntity.setPrecos(new PrecoEntity().realizaValidacaoCriacaoListaPrecos(
                produtoRequest.getPrecos(), colaboradorLogado));
        log.debug("Objeto produtoEntity criado com sucesso");

        log.debug("Iniciando acesso ao método de implementação da persistência do produto...");
        ProdutoEntity produtoPersistido =
                produtoRepositoryImpl.implementaPersistencia(produtoEntity);

        log.debug(Constantes.INICIANDO_SALVAMENTO_HISTORICO_PRODUTO);
        acaoService.salvaHistoricoColaborador(idColaboradorSessao, produtoPersistido.getId(),
                ModulosEnum.ESTOQUE, TipoAcaoEnum.CRIACAO, null);

        log.debug("Produto persistido com sucesso. Convertendo produtoEntity para produtoResponse...");
        ProdutoResponse produtoResponse = new ProdutoResponse().buildFromEntity(produtoPersistido);

        log.info("Produto criado com sucesso");
        return produtoResponse;
    }

    public ProdutoPageResponse realizaBuscaPaginada(ColaboradorId idColaboradorSessao,
                                                    Pageable pageable,
                                                    String campoBusca) {
        log.debug("Método de serviço de obtenção paginada de produtos acessado. Campo de busca: {}",
                campoBusca != null ? campoBusca : "Nulo");

        log.debug("Acessando repositório de busca de produtos");
        Page<ProdutoEntity> produtoPage = produtoRepository.buscaPaginadaPorProdutos(
                pageable, idColaboradorSessao.getEmpresa().getId(), campoBusca);

        log.debug("Busca de produtos por paginação realizada com sucesso. Acessando método de conversão dos " +
                "objetos do tipo Entity para objetos do tipo Response...");
        ProdutoPageResponse produtoPageResponse = new ProdutoPageResponse().constroiProdutoPageResponse(produtoPage);
        log.debug("Conversão de tipagem realizada com sucesso");

        log.info("A busca paginada de produtos foi realizada com sucesso");
        return produtoPageResponse;
    }

    public ProdutoResponse realizaBuscaPorId(ColaboradorId idColaboradorSessao,
                                             UUID idProduto) {
        log.debug("Método de serviço de obtenção de produto por id. ID recebido: {}", idProduto);

        log.debug("Acessando repositório de busca de produto por ID...");
        ProdutoEntity produtoEncontrado = produtoRepositoryImpl.implementaBuscaPorId(
                new ProdutoId(idColaboradorSessao.getEmpresa(), idProduto));

        log.debug("Busca de produtos por id realizada com sucesso. Acessando método de conversão dos objeto do tipo " +
                "Entity para objeto do tipo Response...");
        ProdutoResponse produtoResponse = new ProdutoResponse().buildFromEntity(produtoEncontrado);
        log.debug("Conversão de tipagem realizada com sucesso");

        log.info("A busca de produto por id foi realizada com sucesso");
        return produtoResponse;
    }
}
