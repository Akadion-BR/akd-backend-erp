package br.akd.svc.akadia.modules.erp.produtos.services;

import br.akd.svc.akadia.modules.erp.colaboradores.colaborador.models.entity.colaborador.id.ColaboradorId;
import br.akd.svc.akadia.modules.erp.produtos.models.dto.request.ProdutoRequest;
import br.akd.svc.akadia.modules.erp.produtos.models.dto.response.ProdutoResponse;
import br.akd.svc.akadia.modules.erp.produtos.models.dto.response.page.ProdutoPageResponse;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

public interface ProdutoService {

    @Transactional
    ProdutoResponse criaNovoProduto(ColaboradorId idColaboradorSessao,
                                    ProdutoRequest produtoRequest);

    ProdutoPageResponse realizaBuscaPaginada(ColaboradorId idColaboradorSessao,
                                             Pageable pageable,
                                             String campoBusca);

    ProdutoResponse realizaBuscaPorId(ColaboradorId idColaboradorSessao,
                                      UUID idProduto);

}
