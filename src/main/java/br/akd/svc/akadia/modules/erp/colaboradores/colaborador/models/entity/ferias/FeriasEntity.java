package br.akd.svc.akadia.modules.erp.colaboradores.colaborador.models.entity.ferias;

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
@Table(name = "TB_AKD_FERIAS")
public class FeriasEntity {
    @Id
    @Type(type = "uuid-char")
    @GeneratedValue(generator = "UUID")
    @Comment("Chave primária das férias - UUID")
    @Column(name = "COD_FERIAS_FRS", nullable = false, updatable = false)
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    private UUID id;

    @Comment("Data de cadastro")
    @Column(name = "DT_DATACADASTRO_FRS", nullable = false, length = 10)
    private String dataCadastro;

    @Comment("Hora de cadastro")
    @Column(name = "HR_HORACADASTRO_FRS", nullable = false, length = 18)
    private String horaCadastro;

    @Comment("Quantidade total de dias")
    @Column(name = "INT_TOTALDIAS_FRS", nullable = false)
    private Integer totalDias;

    @Comment("Data de início das férias")
    @Column(name = "DT_DATAINICIO_FRS", nullable = false)
    private String dataInicio;

    @Comment("Data de término das férias")
    @Column(name = "DT_DATAFIM_FRS", nullable = false)
    private String dataFim;
}
