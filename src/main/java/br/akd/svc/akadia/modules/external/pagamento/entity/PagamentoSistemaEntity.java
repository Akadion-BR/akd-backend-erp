package br.akd.svc.akadia.modules.external.pagamento.entity;

import br.akd.svc.akadia.modules.external.pagamento.enums.FormaPagamentoSistemaEnum;
import br.akd.svc.akadia.modules.external.pagamento.enums.StatusPagamentoSistemaEnum;
import lombok.*;
import org.hibernate.annotations.Comment;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.UUID;

@Entity
@Builder
@ToString
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "TB_AKD_PAGAMENTOSISTEMA",
        uniqueConstraints = {
                @UniqueConstraint(name = "UK_CODIGOASAAS_PGS", columnNames = {"COD_ASAAS_PGS"})
        })
public class PagamentoSistemaEntity {

    @Id
    @Type(type = "uuid-char")
    @GeneratedValue(generator = "UUID")
    @Comment("Chave primária do pagamento sistêmico - UUID")
    @Column(name = "COD_PAGAMENTOSISTEMA_PGS", nullable = false, updatable = false)
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    private UUID id;

    @Comment("Código de identificação do pagamento sistêmico na integradora ASAAS")
    @Column(name = "COD_ASAAS_PGS", nullable = false, updatable = false)
    private String codigoPagamentoAsaas;

    @Comment("Data em que o cadastro do pagamento sistêmico foi realizado")
    @Column(name = "DT_CADASTRO_PGS", nullable = false, updatable = false, length = 10)
    private String dataCadastro;

    @Comment("Hora em que o cadastro do pagamento sistêmico foi realizado")
    @Column(name = "HR_CADASTRO_PGS", nullable = false, updatable = false, length = 18)
    private String horaCadastro;

    @Comment("Data em que o pagamento sistêmico foi realizado")
    @Column(name = "DT_PAGAMENTO_PGS", nullable = false, length = 10)
    private String dataPagamento;

    @Comment("Hora em que o pagamento sistêmico foi realizado")
    @Column(name = "HR_PAGAMENTO_PGS", nullable = false, length = 18)
    private String horaPagamento;

    @Comment("Data de vencimento do pagamento sistêmico")
    @Column(name = "DT_VENCIMENTO_PGS", nullable = false, length = 10)
    private String dataVencimento;

    @Comment("Valor bruto do pagamento")
    @Column(name = "DBL_VALOR_PGS", nullable = false, scale = 2)
    private Double valor;

    @Comment("Valor líquido do pagamento")
    @Column(name = "DBL_VALORLIQUIDO_PGS", nullable = false, scale = 2)
    private Double valorLiquido;

    @Comment("Descrição do pagamento")
    @Column(name = "STR_DESCRICAO_PGS", nullable = false, scale = 2)
    private String descricao;

    @Comment("Link de pagamento do pagamento")
    @Column(name = "STR_LINKPAGAMENTO_PGT")
    private String linkCobranca;

    @Comment("Link do boleto do pagamento")
    @Column(name = "STR_LINKBOLETO_PGT")
    private String linkBoleto;

    @Comment("Link do comprovante de pagamento do pagamento")
    @Column(name = "STR_LINKCOMPROVANTE_PGT")
    private String linkComprovante;

    @Enumerated(EnumType.STRING)
    @Comment("Forma de pagamento: " +
            "0 - Boleto, " +
            "1 - Cartão de crédito, " +
            "2 - Pix")
    @Column(name = "ENM_FORMAPAGAMENTO_PGS", nullable = false)
    private FormaPagamentoSistemaEnum formaPagamentoSistemaEnum;

    @Enumerated(EnumType.STRING)
    @Comment("Status do pagamento: " +
            "0 - Aprovado, " +
            "1 - Reprovado, " +
            "2 - Pendente, " +
            "3 - Atrasado, " +
            "4 - Cancelado")
    @Column(name = "ENM_STATUS_PGS", nullable = false)
    private StatusPagamentoSistemaEnum statusPagamentoSistemaEnum;

}
