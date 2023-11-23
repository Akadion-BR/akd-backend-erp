package br.akd.svc.akadia.modules.erp.colaboradores.colaborador.models.entity.expediente;

import br.akd.svc.akadia.modules.erp.colaboradores.colaborador.models.dto.expediente.request.ExpedienteRequest;
import br.akd.svc.akadia.modules.erp.colaboradores.colaborador.models.enums.EscalaEnum;
import lombok.*;
import org.hibernate.annotations.Comment;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.UUID;

@Entity
@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "TB_AKD_EXPEDIENTE")
public class ExpedienteEntity {
    @Id
    @Type(type = "uuid-char")
    @GeneratedValue(generator = "UUID")
    @Comment("Chave primária do expediente - UUID")
    @Column(name = "COD_EXPEDIENTE_EXP", nullable = false, updatable = false)
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    private UUID id;

    @Comment("Hora de entrada")
    @Column(name = "HR_ENTRADA_EXP", nullable = false, length = 18)
    private String horaEntrada;

    @Comment("Hora de saída pra o horário de almoço")
    @Column(name = "HR_SAIDAALMOCO_EXP", nullable = false, length = 18)
    private String horaSaidaAlmoco;

    @Comment("Hora de retorno do horário de almoço")
    @Column(name = "HR_ENTRADAALMOCO_EXP", nullable = false, length = 18)
    private String horaEntradaAlmoco;

    @Comment("Hora de saída")
    @Column(name = "HR_SAIDA_EXP", nullable = false, length = 18)
    private String horaSaida;

    @Comment("Total de horas semanais")
    @Column(name = "STR_CARGAHORARIASEMANAL_EXP", nullable = false, length = 18)
    private String cargaHorariaSemanal;

    @Enumerated(EnumType.STRING)
    @Column(name = "ENM_ESCALA_EXP", nullable = false)
    @Comment("Escala do expediente: " +
            "0 - Escala indefinida, " +
            "1 - 5x1, " +
            "2 - 6x1, " +
            "3 - 12x36")
    private EscalaEnum escalaEnum;

    public ExpedienteEntity buildFromRequest(ExpedienteRequest expedienteRequest) {
        return expedienteRequest != null
                ? ExpedienteEntity.builder()
                .horaEntrada(expedienteRequest.getHoraEntrada())
                .horaSaidaAlmoco(expedienteRequest.getHoraSaidaAlmoco())
                .horaEntradaAlmoco(expedienteRequest.getHoraEntradaAlmoco())
                .horaSaida(expedienteRequest.getHoraSaida())
                .cargaHorariaSemanal(expedienteRequest.getCargaHorariaSemanal())
                .escalaEnum(expedienteRequest.getEscalaEnum())
                .build()
                : null;
    }
}
