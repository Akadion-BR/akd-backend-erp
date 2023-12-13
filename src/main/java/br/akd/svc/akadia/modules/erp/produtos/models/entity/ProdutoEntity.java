package br.akd.svc.akadia.modules.erp.produtos.models.entity;

import br.akd.svc.akadia.modules.erp.colaboradores.colaborador.models.entity.colaborador.ColaboradorEntity;
import br.akd.svc.akadia.modules.erp.compras.models.entity.CompraEntity;
import br.akd.svc.akadia.modules.erp.precos.models.entity.PrecoEntity;
import br.akd.svc.akadia.modules.erp.produtos.models.dto.request.ProdutoRequest;
import br.akd.svc.akadia.modules.erp.produtos.models.entity.id.ProdutoId;
import br.akd.svc.akadia.modules.erp.produtos.models.enums.TipoPesoEnum;
import br.akd.svc.akadia.modules.erp.produtos.models.enums.TipoProdutoEnum;
import br.akd.svc.akadia.modules.erp.produtos.models.enums.UnidadeComercialEnum;
import br.akd.svc.akadia.modules.global.objects.exclusao.entity.ExclusaoEntity;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;
import org.hibernate.annotations.Comment;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@Entity
@Builder
@ToString
@DynamicUpdate
@NoArgsConstructor
@AllArgsConstructor
@IdClass(ProdutoId.class)
@Table(name = "TB_AKD_PRODUTO",
        uniqueConstraints = {
                @UniqueConstraint(name = "UK_CODIGOINTERNO_PDT", columnNames = {"COD_EMPRESA_PDT", "STR_CODIGOINTERNO_PDT"}),
                @UniqueConstraint(name = "UK_CODIGOINTERNO_PDT", columnNames = {"COD_EMPRESA_PDT", "STR_SIGLA_PDT"})
        })
public class ProdutoEntity {

    @Id
    @Type(type = "uuid-char")
    @GeneratedValue(generator = "UUID")
    @Comment("Chave primária do produto - UUID")
    @Column(name = "COD_PRODUTO_PDT", nullable = false, updatable = false)
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    private UUID id;

    @Id
    @Type(type = "uuid-char")
    @GeneratedValue(generator = "UUID")
    @Comment("Chave primária do produto - Código do cliente sistêmico")
    @Column(name = "COD_CLIENTESISTEMA_PDT", nullable = false, updatable = false)
    private UUID idClienteSistema;

    @Id
    @Type(type = "uuid-char")
    @GeneratedValue(generator = "UUID")
    @Comment("Chave primária do produto - Código da empresa")
    @Column(name = "COD_EMPRESA_PDT", nullable = false, updatable = false)
    private UUID idEmpresa;

    @Comment("Data em que o cadastro do produto foi realizado")
    @Column(name = "DT_CADASTRO_PDT", nullable = false, updatable = false, length = 10)
    private String dataCadastro;

    @Comment("Hora em que o cadastro do produto foi realizado")
    @Column(name = "HR_CADASTRO_PDT", nullable = false, updatable = false, length = 18)
    private String horaCadastro;

    @Comment("Código interno de identificação do produto")
    @Column(name = "STR_CODIGOINTERNO_PDT", nullable = false, length = 10)
    private String codigoInterno;

    @Comment("Sigla de identificação do produto")
    @Column(name = "STR_SIGLA_PDT", nullable = false, length = 8)
    private String sigla;

    @Comment("Marca do produto")
    @Column(name = "STR_MARCA_PDT", nullable = false, length = 30)
    private String marca;

    @Comment("Descrição e detalhamento do produto")
    @Column(name = "STR_DESCRICAO_PDT", nullable = false, length = 30)
    private String descricao;

    @Comment("Categoria do produto")
    @Column(name = "STR_CATEGORIA_PDT", nullable = false, length = 30)
    private String categoria;

    @Builder.Default
    @Comment("Quantidade mínima permitida do produto em estoque")
    @Column(name = "INT_QUANTIDADEMINIMA_PDT", nullable = false)
    private Integer quantidadeMinima = 0;

    @Builder.Default
    @Comment("Quantidade do produto em que, no ato da venda, definirá se o preço aplicado será de atacado")
    @Column(name = "INT_QUANTIDADEATACADO_PDT", nullable = false)
    private Integer quantidadeAtacado = 0;

    @Builder.Default
    @Comment("Quantidade atual do produto em estoque")
    @Column(name = "INT_QUANTIDADE_PDT", nullable = false)
    private Integer quantidade = 0;

    @Comment("Código NCM (Nomenclatura comum do Mercosul) do produto")
    @Column(name = "INT_CODIGONCM_PDT")
    private Integer codigoNcm;

    @Builder.Default
    @Comment("Peso unitário do produto")
    @Column(name = "DBL_PESOUNITARIO_PDT", nullable = false, scale = 2)
    private Double pesoUnitario = 0.0;

    @Comment("Nome do responsável pela persistência do objeto")
    @Column(name = "STR_RESPONSAVEL_PDT", nullable = false, length = 70)
    private String nomeResponsavel;

    @Enumerated(EnumType.STRING)
    @Comment("Tipo do produto em valores constantes: " +
            "1 - BATERIA (Bateria), " +
            "2 - SUCATA (Sucata), " +
            "3 - OUTRO (Outro)")
    @Column(name = "ENM_TIPO_PDT", nullable = false)
    private TipoProdutoEnum tipoProduto;

    @Enumerated(EnumType.STRING)
    @Comment("Unidade comercial do produto em valores constantes: " +
            "1 - UN (Unidade), " +
            "2 - KG (Kilo)")
    @Column(name = "ENM_UNIDADECOMERCIAL_PDT", nullable = false)
    private UnidadeComercialEnum unidadeComercial;

    @Enumerated(EnumType.STRING)
    @Comment("Unidade de medida para peso do produto: " +
            "1 - G (Grama), " +
            "2 - KG (Kilo)")
    @Column(name = "ENM_TIPOPESO_PDT", nullable = false)
    private TipoPesoEnum tipoPeso;

    @ToString.Exclude
    @Comment("Código de exclusão do produto")
    @OneToOne(targetEntity = ExclusaoEntity.class,
            orphanRemoval = true,
            cascade = CascadeType.ALL,
            fetch = FetchType.LAZY)
    private ExclusaoEntity exclusao;

    @Builder.Default
    @ToString.Exclude
    @Comment("Preços vinculados ao produto")
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    @OneToMany(targetEntity = PrecoEntity.class, orphanRemoval = true, cascade = CascadeType.ALL)
    private List<PrecoEntity> precos = new ArrayList<>();

    @Builder.Default
    @ToString.Exclude
    @Comment("Compras vinculados ao produto")
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    @OneToMany(targetEntity = CompraEntity.class, orphanRemoval = true, cascade = CascadeType.ALL)
    private List<CompraEntity> historicoCompras = new ArrayList<>();

    public ProdutoEntity buildFromRequest(ColaboradorEntity colaboradorLogado,
                                          ProdutoRequest produtoRequest) {
        return ProdutoEntity.builder()
                .id(null)
                .idClienteSistema(colaboradorLogado.getIdClienteSistema())
                .idEmpresa(colaboradorLogado.getIdEmpresa())
                .dataCadastro(LocalDate.now().toString())
                .horaCadastro(LocalTime.now().toString())
                .codigoInterno(produtoRequest.getCodigoInterno())
                .sigla(produtoRequest.getSigla())
                .marca(produtoRequest.getMarca())
                .descricao(produtoRequest.getDescricao())
                .categoria(produtoRequest.getCategoria())
                .quantidadeMinima(produtoRequest.getQuantidadeMinima())
                .quantidadeAtacado(produtoRequest.getQuantidadeAtacado())
                .quantidade(0)
                .codigoNcm(produtoRequest.getCodigoNcm())
                .pesoUnitario(produtoRequest.getPesoUnitario())
                .nomeResponsavel(colaboradorLogado.getNome())
                .tipoProduto(produtoRequest.getTipoProduto())
                .unidadeComercial(produtoRequest.getUnidadeComercial())
                .tipoPeso(produtoRequest.getTipoPeso())
                .exclusao(null)
                .precos(new ArrayList<>())
                .historicoCompras(new ArrayList<>())
                .build();
    }
}
