package br.akd.svc.akadia.modules.backoffice.chamado.mensagem.anexo.entity;

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
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "TB_AKD_ANEXO")
public class AnexoMensagemEntity {

    @Id
    @Type(type = "uuid-char")
    @GeneratedValue(generator = "UUID")
    @Comment("Chave primária do anexo - UUID")
    @Column(name = "COD_ANEXO_ANX", nullable = false, updatable = false)
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    private UUID id;

    //TODO TESTAR PERSISTÊNCIA DO ARQUIVO
    @Lob
    @Comment("Arquivo do anexo")
    @Column(name = "ARQ_DADOS_ANX", nullable = false)
    private byte[] dados;

    @Comment("Nome do anexo")
    @Column(name = "STR_NOME_ANX", nullable = false, updatable = false, length = 50)
    private String nome;

    @Comment("Tipo do anexo")
    @Column(name = "STR_TIPO_ANX", nullable = false, updatable = false, length = 50)
    private String tipo;
}
