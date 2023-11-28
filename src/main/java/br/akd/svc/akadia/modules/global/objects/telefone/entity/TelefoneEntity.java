package br.akd.svc.akadia.modules.global.objects.telefone.entity;

import br.akd.svc.akadia.modules.global.objects.telefone.enums.TipoTelefoneEnum;
import br.akd.svc.akadia.modules.global.objects.telefone.request.TelefoneRequest;
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
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "TB_AKD_TELEFONE")
public class TelefoneEntity {

    @Id
    @Type(type = "uuid-char")
    @GeneratedValue(generator = "UUID")
    @Comment("Chave primária do telefone - UUID")
    @Column(name = "COD_TELEFONE_TEL", nullable = false, updatable = false)
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    private UUID id;

    @Comment("Prefixo do telefone")
    @Column(name = "STR_PREFIXO_TEL", nullable = false, length = 2)
    private Integer prefixo;

    @Comment("Número do telefone")
    @Column(name = "STR_NUMERO_TEL", nullable = false, length = 9)
    private Integer numero;

    @Enumerated(EnumType.STRING)
    @Comment("Tipo do telefone")
    @Column(name = "ENM_TIPO_TEL", nullable = false)
    private TipoTelefoneEnum tipoTelefone;

    public String obtemTelefoneCompleto() {
        return "(" + this.prefixo + ") " + this.numero;
    }

    public TelefoneEntity buildFromRequest(TelefoneRequest telefoneRequest) {
        return telefoneRequest != null
                ? TelefoneEntity.builder()
                .prefixo(telefoneRequest.getPrefixo())
                .numero(telefoneRequest.getNumero())
                .tipoTelefone(telefoneRequest.getTipoTelefone())
                .build()
                : null;
    }
}
