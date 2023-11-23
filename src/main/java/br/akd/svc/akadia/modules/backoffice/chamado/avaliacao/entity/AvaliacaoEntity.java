package br.akd.svc.akadia.modules.backoffice.chamado.avaliacao.entity;

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
@Table(name = "TB_AKD_AVALIACAO")
public class AvaliacaoEntity {
    @Id
    @Type(type = "uuid-char")
    @GeneratedValue(generator = "UUID")
    @Comment("Chave primária da avaliação - UUID")
    @Column(name = "COD_AVALIACAO_AVL", nullable = false, updatable = false)
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    private UUID id;

    @Comment("Nota da avaliação do chamado")
    @Column(name = "INT_NOTA_AVL", nullable = false, updatable = false)
    private Integer nota;

    @Comment("Descrição da avaliação do chamado")
    @Column(name = "STR_DESCRICAO_AVL", nullable = false, updatable = false, length = 150)
    private String descricao;
}
