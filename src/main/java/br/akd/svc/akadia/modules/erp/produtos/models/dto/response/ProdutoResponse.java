package br.akd.svc.akadia.modules.erp.produtos.models.dto.response;

import br.akd.svc.akadia.modules.erp.produtos.models.entity.ProdutoEntity;
import lombok.*;

import java.util.UUID;

@Data
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ProdutoResponse {
    private UUID id;
    private String dataCadastro;
    private String horaCadastro;
    private String codigoInterno;
    private String sigla;
    private String marca;
    private String descricao;
    private String categoria;
    private Integer quantidadeMinima;
    private Integer quantidadeAtacado;
    private Integer quantidade;
    private Integer codigoNcm;
    private Double pesoUnitario;
    private String tipoProduto;
    private String unidadeComercial;
    private String tipoPeso;
    private String nomeColaboradorResponsavel;

    public ProdutoResponse buildFromEntity(ProdutoEntity produtoEntity) {
        return produtoEntity != null
                ? ProdutoResponse.builder()
                .id(produtoEntity.getId())
                .dataCadastro(produtoEntity.getDataCadastro())
                .horaCadastro(produtoEntity.getHoraCadastro())
                .codigoInterno(produtoEntity.getCodigoInterno())
                .sigla(produtoEntity.getSigla())
                .marca(produtoEntity.getMarca())
                .descricao(produtoEntity.getDescricao())
                .categoria(produtoEntity.getCategoria())
                .quantidadeMinima(produtoEntity.getQuantidadeMinima())
                .quantidadeAtacado(produtoEntity.getQuantidadeAtacado())
                .quantidade(produtoEntity.getQuantidade())
                .codigoNcm(produtoEntity.getCodigoNcm())
                .pesoUnitario(produtoEntity.getPesoUnitario())
                .tipoProduto(produtoEntity.getTipoProduto().getDesc())
                .unidadeComercial(produtoEntity.getUnidadeComercial().getDesc())
                .tipoPeso(produtoEntity.getTipoPeso().getDesc())
                .nomeColaboradorResponsavel(produtoEntity.getNomeResponsavel())
                .build()
                : null;
    }
}
