package br.akd.svc.akadia.modules.erp.colaboradores.colaborador.models.entity.ponto;

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
@Table(name = "TB_AKD_PONTO")
public class PontoEntity {
    @Id
    @Type(type = "uuid-char")
    @GeneratedValue(generator = "UUID")
    @Comment("Chave primária do ponto - UUID")
    @Column(name = "COD_PONTO_PNT", nullable = false, updatable = false)
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    private UUID id;

    @Comment("Hora de entrada")
    @Column(name = "HR_ENTRADA_PNT", nullable = false, length = 18)
    private String horaEntrada;

    @Comment("Hora de saída pra o horário de almoço")
    @Column(name = "HR_SAIDAALMOCO_PNT", nullable = false, length = 18)
    private String horaSaidaAlmoco;

    @Comment("Hora de retorno do horário de almoço")
    @Column(name = "HR_ENTRADAALMOCO_PNT", nullable = false, length = 18)
    private String horaEntradaAlmoco;

    @Comment("Hora de saída")
    @Column(name = "HR_SAIDA_PNT", nullable = false, length = 18)
    private String horaSaida;
}
