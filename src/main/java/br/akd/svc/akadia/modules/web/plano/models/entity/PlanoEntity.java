package br.akd.svc.akadia.modules.web.plano.models.entity;

import br.akd.svc.akadia.modules.web.pagamento.models.enums.FormaPagamentoSistemaEnum;
import br.akd.svc.akadia.modules.web.plano.models.dto.request.PlanoRequest;
import br.akd.svc.akadia.modules.web.plano.models.enums.StatusPlanoEnum;
import br.akd.svc.akadia.modules.web.plano.models.enums.TipoPlanoEnum;
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
@Builder
@ToString
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "TB_AKD_PLANO",
        uniqueConstraints = {
                @UniqueConstraint(name = "UK_ASAAS_PLANO", columnNames = {"COD_ASAAS_PLN"})
        })
public class PlanoEntity {
    @Id
    @JsonIgnore
    @Type(type = "uuid-char")
    @GeneratedValue(generator = "UUID")
    @Comment("Chave primária do plano - UUID")
    @Column(name = "COD_PLANO_PLN", nullable = false, updatable = false)
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    private UUID id;

    @JsonIgnore
    @Comment("Código do plano na integradora ASAAS")
    @Column(name = "COD_ASAAS_PLN", nullable = false, updatable = false)
    private String codigoAssinaturaAsaas;

    @JsonIgnore
    @Comment("Data em que o cadastro do plano foi realizado")
    @Column(name = "DT_DATACADASTRO_PLN", nullable = false, updatable = false, length = 10)
    private String dataContratacao;

    @JsonIgnore
    @Comment("Hora em que o cadastro do plano foi realizado")
    @Column(name = "HR_HORACADASTRO_PLN", nullable = false, updatable = false, length = 18)
    private String horaContratacao;

    @JsonIgnore
    @Comment("Próxima data de vencimento do plano")
    @Column(name = "DT_DATAVENCIMENTO_PLN", nullable = false, length = 10)
    private String dataVencimento;

    @Comment("Data de agendamento para a remoção do plano. Só deve estar preenchido caso o plano tenha sido removido " +
            "mas possua algum pagamento ativo")
    @Column(name = "DT_AGENDAMENTOREMOCAO_PLN", length = 10)
    private String dataAgendamentoRemocao;

    @JsonIgnore
    @Enumerated(EnumType.STRING)
    @Column(name = "ENM_TIPO_PLANO", nullable = false)
    @Comment("Tipo do plano: " +
            "0 - Basic, " +
            "1 - Standart, " +
            "2 - Pro")
    private TipoPlanoEnum tipoPlanoEnum;

    @JsonIgnore
    @Enumerated(EnumType.STRING)
    @Column(name = "ENM_STATUS_PLANO", nullable = false)
    @Comment("Status do plano: " +
            "0 - Ativo, " +
            "1 - Inativo, " +
            "2 - Período de testes")
    private StatusPlanoEnum statusPlanoEnum;

    @JsonIgnore
    @Enumerated(EnumType.STRING)
    @Column(name = "ENM_FORMAPAGAMENTO_PLANO", nullable = false)
    @Comment("Forma de pagamento do plano: " +
            "0 - Boleto, " +
            "1 - Pix")
    private FormaPagamentoSistemaEnum formaPagamentoSistemaEnum;

    public PlanoEntity buildFromRequest(PlanoRequest planoRequest) {
        return planoRequest != null
                ? PlanoEntity.builder()
                .id(null)
                .codigoAssinaturaAsaas(null)
                .dataContratacao(LocalDate.now().toString())
                .horaContratacao(LocalTime.now().toString())
                .dataVencimento(LocalDate.now().plusDays(7L).toString())
                .dataAgendamentoRemocao(null)
                .tipoPlanoEnum(planoRequest.getTipoPlano())
                .statusPlanoEnum(StatusPlanoEnum.PERIODO_DE_TESTES)
                .formaPagamentoSistemaEnum(planoRequest.getFormaPagamentoSistema())
                .build()
                : null;
    }

    public PlanoEntity updateFromRequest(PlanoEntity planoEncontrado,
                                         PlanoRequest planoRequest) {
        return planoRequest != null
                ? PlanoEntity.builder()
                .id(planoEncontrado.getId())
                .codigoAssinaturaAsaas(planoEncontrado.getCodigoAssinaturaAsaas())
                .dataContratacao(planoEncontrado.getDataContratacao())
                .horaContratacao(planoEncontrado.getHoraContratacao())
                .dataVencimento(planoEncontrado.getDataVencimento())
                .dataAgendamentoRemocao(planoEncontrado.getDataAgendamentoRemocao())
                .tipoPlanoEnum(planoRequest.getTipoPlano())
                .statusPlanoEnum(planoEncontrado.getStatusPlanoEnum())
                .formaPagamentoSistemaEnum(planoRequest.getFormaPagamentoSistema())
                .build()
                : null;
    }
}
