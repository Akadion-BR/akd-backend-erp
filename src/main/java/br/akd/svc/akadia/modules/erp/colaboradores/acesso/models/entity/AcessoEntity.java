package br.akd.svc.akadia.modules.erp.colaboradores.acesso.models.entity;

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
@AllArgsConstructor
@Table(name = "TB_AKD_ACESSO")
public class AcessoEntity {

    public AcessoEntity() {
        this.id = null;
        this.dataCadastro = LocalDate.now().toString();
        this.horaCadastro = LocalTime.now().toString();
    }

    @Id
    @Type(type = "uuid-char")
    @GeneratedValue(generator = "UUID")
    @Comment("Chave primária do acesso- UUID")
    @Column(name = "COD_ACESSO_ACS", nullable = false, updatable = false)
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    private UUID id;

    @Comment("Data em que o ACESSO foi realizado")
    @Column(name = "DT_DATACADASTRO_ACS", nullable = false, updatable = false, length = 10)
    private String dataCadastro;

    @Comment("Hora em que o ACESSO foi realizado")
    @Column(name = "HR_HORACADASTRO_ACS", nullable = false, updatable = false, length = 18)
    private String horaCadastro;
}
