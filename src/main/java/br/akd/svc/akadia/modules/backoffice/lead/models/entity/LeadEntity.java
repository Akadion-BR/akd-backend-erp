package br.akd.svc.akadia.modules.backoffice.lead.models.entity;

import br.akd.svc.akadia.modules.backoffice.lead.models.enums.OrigemLeadEnum;
import br.akd.svc.akadia.modules.global.telefone.entity.TelefoneEntity;
import lombok.*;
import org.hibernate.annotations.Comment;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.UUID;

@Entity
@Builder
@ToString
@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "TB_AKD_LEAD")
public class LeadEntity {

    @Id
    @Type(type = "uuid-char")
    @GeneratedValue(generator = "UUID")
    @Comment("Chave primária do lead - UUID")
    @Column(name = "COD_LEAD_LDD", nullable = false, updatable = false)
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    private UUID id;

    @Comment("Nome do lead")
    @Column(name = "STR_NOME_LDD", nullable = false, length = 70)
    private String nome;

    @Comment("E-mail do lead")
    @Column(name = "STR_EMAIL_LDD", nullable = false, length = 70)
    private String email;

    @Enumerated(EnumType.STRING)
    @Column(name = "ENM_ORIGEM_LDD", nullable = false)
    @Comment("Origem do lead: " +
            "0 - Pré-cadastro, " +
            "1 - Manual")
    private OrigemLeadEnum origemLeadEnum;

    @ToString.Exclude
    @Comment("Código do telefone do Lead")
    @OneToOne(targetEntity = TelefoneEntity.class,
            orphanRemoval = true,
            cascade = CascadeType.ALL,
            fetch = FetchType.LAZY)
    private TelefoneEntity telefone;

}
