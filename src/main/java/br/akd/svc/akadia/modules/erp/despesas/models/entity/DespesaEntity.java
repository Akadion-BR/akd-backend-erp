package br.akd.svc.akadia.modules.erp.despesas.models.entity;

import br.akd.svc.akadia.modules.erp.colaboradores.colaborador.models.entity.colaborador.ColaboradorEntity;
import br.akd.svc.akadia.modules.erp.despesas.models.dto.request.DespesaRequest;
import br.akd.svc.akadia.modules.erp.despesas.models.entity.id.DespesaId;
import br.akd.svc.akadia.modules.erp.despesas.models.enums.StatusDespesaEnum;
import br.akd.svc.akadia.modules.erp.despesas.models.enums.TipoDespesaEnum;
import br.akd.svc.akadia.modules.erp.despesas.models.enums.TipoRecorrenciaDespesaEnum;
import br.akd.svc.akadia.modules.external.empresa.entity.EmpresaEntity;
import br.akd.svc.akadia.modules.global.objects.exclusao.entity.ExclusaoEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;
import org.hibernate.annotations.Comment;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
@IdClass(DespesaId.class)
@Table(name = "TB_AKD_DESPESA")
public class DespesaEntity {

    @Id
    @Type(type = "uuid-char")
    @GeneratedValue(generator = "UUID")
    @Comment("Chave primária do cliente - UUID")
    @Column(name = "COD_DESPESA_DSP", nullable = false, updatable = false)
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    private UUID id;

    @Id
    @JsonIgnore
    @ToString.Exclude
    @ManyToOne(fetch = FetchType.LAZY)
    @Comment("Chave primária da despesa - ID da empresa ao qual a despesa faz parte")
    @JoinColumn(name = "COD_EMPRESA_DSP", referencedColumnName = "COD_EMPRESA_EMP", nullable = false, updatable = false)
    private EmpresaEntity empresa;

    @Comment("Data em que o cadastro da despesa foi realizado")
    @Column(name = "DT_CADASTRO_DSP", nullable = false, updatable = false, length = 10)
    private String dataCadastro;

    @Comment("Hora em que o cadastro da despesa foi realizado")
    @Column(name = "HR_CADASTRO_DSP", nullable = false, updatable = false, length = 18)
    private String horaCadastro;

    @Comment("Data em que o pagamento da despesa foi realizado")
    @Column(name = "DT_PAGAMENTO_DSP", length = 10)
    private String dataPagamento;

    @Comment("Data de agendamento da despesa")
    @Column(name = "DT_AGENDAMENTO_DSP", length = 10)
    private String dataAgendamento;

    @Comment("Descrição da despesa")
    @Column(name = "STR_DESCRICAO_DSP", nullable = false, length = 70)
    private String descricao;

    @Comment("Valor da despesa")
    @Column(name = "DBL_VALOR_DSP", nullable = false, scale = 2)
    private Double valor;

    @Comment("Observação da despesa")
    @Column(name = "STR_OBSERVACAO_DSP", nullable = false, length = 120)
    private String observacao;

    @Comment("Responsável por persistir a despesa")
    @Column(name = "STR_RESPONSAVEL_DSP", nullable = false, updatable = false, length = 70)
    private String nomeResponsavel;

    @Enumerated(EnumType.STRING)
    @Column(name = "ENM_TIPORECORRENCIA_DSP", nullable = false)
    @Comment("Tipo de recorrência da despesa: " +
            "0 - Sem recorrência, " +
            "1 - Principal, " +
            "2 - Herdeira")
    private TipoRecorrenciaDespesaEnum tipoRecorrencia;

    @Enumerated(EnumType.STRING)
    @Column(name = "ENM_STATUS_DSP", nullable = false)
    @Comment("Status da despesa: " +
            "0 - Pago, " +
            "1 - Pendente")
    private StatusDespesaEnum statusDespesa;

    @Enumerated(EnumType.STRING)
    @Column(name = "ENM_TIPO_DSP", nullable = false)
    @Comment("Tipo da despesa: " +
            "0 - Fixo, " +
            "1 - Variável, " +
            "2 - Investimento")
    private TipoDespesaEnum tipoDespesa;

    @ToString.Exclude
    @Comment("Código de exclusão da despesa")
    @OneToOne(targetEntity = ExclusaoEntity.class,
            orphanRemoval = true,
            cascade = CascadeType.ALL,
            fetch = FetchType.LAZY)
    private ExclusaoEntity exclusao;

    @Builder.Default
    @ToString.Exclude
    @Comment("Recorrências da despesa")
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    @OneToMany(targetEntity = DespesaEntity.class, orphanRemoval = true, cascade = CascadeType.ALL)
    private List<DespesaEntity> recorrencias = new ArrayList<>();

    public DespesaEntity buildFromRequest(ColaboradorEntity colaboradorSessao,
                                          List<DespesaEntity> recorrencias,
                                          DespesaRequest despesaRequest) {
        //TODO MELHORAR MÉTODO
        return despesaRequest != null
                ? DespesaEntity.builder()
                .id(null)
                .empresa(colaboradorSessao.getEmpresa())
                .dataCadastro(LocalDate.now().toString())
                .horaCadastro(LocalTime.now().toString())
                .dataPagamento(despesaRequest.getDataPagamento() == null
                        ? "Agendado"
                        : despesaRequest.getDataPagamento())
                .dataAgendamento(despesaRequest.getDataAgendamento() == null
                        ? "Pago"
                        : despesaRequest.getDataAgendamento())
                .descricao(despesaRequest.getDescricao())
                .valor(despesaRequest.getValor())
                .observacao(despesaRequest.getQtdRecorrencias() > 0
                        ? "Possui " + despesaRequest.getQtdRecorrencias() + " recorrência(s)"
                        : "Não possui recorrências")
                .nomeResponsavel(colaboradorSessao.getNome())
                .tipoRecorrencia(despesaRequest.getQtdRecorrencias() > 0
                        ? TipoRecorrenciaDespesaEnum.PRINCIPAL
                        : TipoRecorrenciaDespesaEnum.SEM_RECORRENCIA)
                .statusDespesa(despesaRequest.getStatusDespesa())
                .tipoDespesa(despesaRequest.getTipoDespesa())
                .exclusao(null)
                .recorrencias(recorrencias)
                .build()
                : null;
    }

    public DespesaEntity buildRecorrencia(ColaboradorEntity colaboradorSessao,
                                          String dataAgendamento,
                                          int indiceRecorrencia,
                                          DespesaRequest despesaRequest) {
        return DespesaEntity.builder()
                .empresa(colaboradorSessao.getEmpresa())
                .dataCadastro(LocalDate.now().toString())
                .horaCadastro(LocalTime.now().toString())
                .dataPagamento("Agendado")
                .dataAgendamento(dataAgendamento)
                .descricao(despesaRequest.getDescricao())
                .valor(despesaRequest.getValor())
                .observacao("Recorrência " + indiceRecorrencia + " de " + despesaRequest.getQtdRecorrencias())
                .tipoRecorrencia(TipoRecorrenciaDespesaEnum.HERDEIRA)
                .statusDespesa(StatusDespesaEnum.PENDENTE)
                .tipoDespesa(despesaRequest.getTipoDespesa())
                .exclusao(null)
                .nomeResponsavel(colaboradorSessao.getNome())
                .recorrencias(new ArrayList<>())
                .build();
    }

    public DespesaEntity updateFromRequest(DespesaEntity despesaPreAtualizacao,
                                           DespesaRequest despesaRequest) {
        //TODO MELHORAR MÉTODO
        return despesaRequest != null
                ? DespesaEntity.builder()
                .id(despesaPreAtualizacao.getId())
                .empresa(despesaPreAtualizacao.getEmpresa())
                .dataCadastro(despesaPreAtualizacao.getDataCadastro())
                .horaCadastro(despesaPreAtualizacao.getHoraCadastro())
                .dataPagamento(despesaRequest.getDataPagamento() == null
                        ? "Agendado"
                        : despesaRequest.getDataPagamento())
                .dataAgendamento(despesaRequest.getDataAgendamento() == null
                        ? "Pago"
                        : despesaRequest.getDataAgendamento())
                .descricao(despesaRequest.getDescricao())
                .valor(despesaRequest.getValor())
                .observacao(despesaPreAtualizacao.getObservacao())
                .tipoRecorrencia(despesaPreAtualizacao.getTipoRecorrencia())
                .statusDespesa(despesaRequest.getStatusDespesa())
                .tipoDespesa(despesaRequest.getTipoDespesa())
                .exclusao(null)
                .nomeResponsavel(despesaPreAtualizacao.getNomeResponsavel())
                .recorrencias(despesaPreAtualizacao.getRecorrencias())
                .build()
                : null;
    }
}
