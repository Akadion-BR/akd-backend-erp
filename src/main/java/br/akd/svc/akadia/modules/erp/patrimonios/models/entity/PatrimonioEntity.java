package br.akd.svc.akadia.modules.erp.patrimonios.models.entity;

import br.akd.svc.akadia.modules.erp.colaboradores.colaborador.models.entity.colaborador.ColaboradorEntity;
import br.akd.svc.akadia.modules.erp.patrimonios.models.dto.request.PatrimonioRequest;
import br.akd.svc.akadia.modules.erp.patrimonios.models.entity.id.PatrimonioId;
import br.akd.svc.akadia.modules.erp.patrimonios.models.enums.TipoPatrimonioEnum;
import br.akd.svc.akadia.modules.external.empresa.entity.EmpresaEntity;
import br.akd.svc.akadia.modules.global.objects.exclusao.entity.ExclusaoEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.hibernate.annotations.Comment;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

@Entity
@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
@IdClass(PatrimonioId.class)
@Table(name = "TB_AKD_PATRIMONIO")
public class PatrimonioEntity {

    @Id
    @Type(type = "uuid-char")
    @GeneratedValue(generator = "UUID")
    @Comment("Chave primária do patrimônio - UUID")
    @Column(name = "COD_PATRIMONIO_PTR", nullable = false, updatable = false)
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    private UUID id;

    @Id
    @JsonIgnore
    @ToString.Exclude
    @ManyToOne(fetch = FetchType.LAZY)
    @Comment("Chave primária do patrimônio - ID da empresa ao qual o patrimônio faz parte")
    @JoinColumn(name = "COD_EMPRESA_PTR", referencedColumnName = "COD_EMPRESA_EMP")
    @JoinColumn(name = "COD_CLIENTESISTEMA_PTR", referencedColumnName = "COD_CLIENTESISTEMA_EMP")
    private EmpresaEntity empresa;

    @Comment("Data em que o cadastro do patrimônio foi realizado")
    @Column(name = "DT_CADASTRO_PTR", nullable = false, updatable = false, length = 10)
    private String dataCadastro;

    @Comment("Hora em que o cadastro do patrimônio foi realizado")
    @Column(name = "HR_CADASTRO_PTR", nullable = false, updatable = false, length = 18)
    private String horaCadastro;

    @Comment("Data de entrada do patrimônio")
    @Column(name = "DT_ENTRADA_PTR", length = 10)
    private String dataEntrada;

    @Comment("Descrição do patrimônio")
    @Column(name = "STR_DESCRICAO_PTR", nullable = false, length = 70)
    private String descricao;

    @Comment("Valor do patrimônio")
    @Column(name = "DBL_VALOR_PTR", nullable = false, scale = 2)
    private Double valor;

    @Comment("Nome do responsável pelo cadastro")
    @Column(name = "STR_RESPONSAVEL_PTR", nullable = false, updatable = false, length = 70)
    private String nomeResponsavel;

    @Enumerated(EnumType.STRING)
    @Column(name = "ENM_TIPO_PTR", nullable = false)
    @Comment("Tipo do patrimônio: " +
            "0 - Ativo, " +
            "1 - A receber, " +
            "2 - Passivo")
    private TipoPatrimonioEnum tipoPatrimonio;

    @ToString.Exclude
    @Comment("Código de exclusão do patrimônio")
    @OneToOne(targetEntity = ExclusaoEntity.class,
            orphanRemoval = true,
            cascade = CascadeType.ALL,
            fetch = FetchType.LAZY)
    private ExclusaoEntity exclusao;

    public PatrimonioEntity buildFromRequest(ColaboradorEntity colaboradorSessao,
                                             PatrimonioRequest patrimonioRequest) {
        return patrimonioRequest != null
                ? PatrimonioEntity.builder()
                .id(null)
                .empresa(colaboradorSessao.getEmpresa())
                .dataCadastro(LocalDate.now().toString())
                .horaCadastro(LocalTime.now().toString())
                .dataEntrada(patrimonioRequest.getDataEntrada())
                .descricao(patrimonioRequest.getDescricao())
                .valor(patrimonioRequest.getValor())
                .nomeResponsavel(colaboradorSessao.getNome())
                .tipoPatrimonio(patrimonioRequest.getTipoPatrimonio())
                .exclusao(null)
                .build()
                : null;
    }

    public PatrimonioEntity updateFromRequest(PatrimonioEntity patrimonioPreAtualizacao,
                                              PatrimonioRequest patrimonioRequest) {
        return patrimonioRequest != null
                ? PatrimonioEntity.builder()
                .id(patrimonioPreAtualizacao.getId())
                .empresa(patrimonioPreAtualizacao.getEmpresa())
                .dataCadastro(patrimonioPreAtualizacao.getDataCadastro())
                .horaCadastro(patrimonioPreAtualizacao.getHoraCadastro())
                .dataEntrada(patrimonioRequest.getDataEntrada())
                .descricao(patrimonioRequest.getDescricao())
                .valor(patrimonioRequest.getValor())
                .nomeResponsavel(patrimonioPreAtualizacao.getNomeResponsavel())
                .tipoPatrimonio(patrimonioRequest.getTipoPatrimonio())
                .exclusao(null)
                .build()
                : null;
    }

}
