package br.akd.svc.akadia.modules.backoffice.chamado.mensagem.entity;

import br.akd.svc.akadia.modules.backoffice.chamado.mensagem.anexo.entity.AnexoMensagemEntity;
import br.akd.svc.akadia.modules.backoffice.chamado.mensagem.enums.CaminhoMensagemEnum;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;
import org.hibernate.annotations.Comment;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

import javax.persistence.*;
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
@Table(name = "TB_AKD_MENSAGEM")
public class MensagemEntity {

    @Id
    @Type(type = "uuid-char")
    @GeneratedValue(generator = "UUID")
    @Comment("Chave primária da mensagem - UUID")
    @Column(name = "COD_MENSAGEM_MSG", nullable = false, updatable = false)
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    private UUID id;

    @Comment("Data em que a mensagem foi enviada")
    @Column(name = "DT_DATAENVIO_MSG", nullable = false, updatable = false, length = 10)
    private String dataEnvio;

    @Comment("Hora em que a mensagem foi enviada")
    @Column(name = "HR_HORAENVIO_MSG", nullable = false, updatable = false, length = 18)
    private String horaEnvio;

    @Comment("Mensagem visualizada")
    @Column(name = "BOL_VISUALIZADA_MSG", nullable = false)
    private Boolean visualizada = false;

    @Comment("Mensagem respondida")
    @Column(name = "BOL_RESPONDIDA_MSG", nullable = false)
    private Boolean respondida = false;

    @Comment("Conteúdo da mensagem")
    @Column(name = "STR_CONTEUDO_MSG", nullable = false, updatable = false, length = 150)
    private String conteudo;

    @Enumerated(EnumType.STRING)
    @Column(name = "ENM_CAMINHO_MSG", nullable = false)
    @Comment("Caminho da mensagem: " +
            "0 - Suporte para cliente, " +
            "1 - Cliente para suporte")
    private CaminhoMensagemEnum caminhoMensagemEnum;

    @ToString.Exclude
    @Builder.Default
    @Comment("Anexos da mensagem")
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    @OneToMany(targetEntity = AnexoMensagemEntity.class, orphanRemoval = true, cascade = CascadeType.ALL)
    private List<AnexoMensagemEntity> anexos = new ArrayList<>();
}
