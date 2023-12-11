package br.akd.svc.akadia.modules.external.empresa.entity.fiscal.nfce;

import lombok.*;
import org.hibernate.annotations.Comment;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.UUID;

@Entity
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Table(name = "TB_AKD_NFCECONFIG")
public class NfceConfigEntity {

    @Id
    @Type(type = "uuid-char")
    @GeneratedValue(generator = "UUID")
    @Comment("Chave primária da configuração de NFCE - UUID")
    @Column(name = "COD_NFCECONFIG_NFCECFG", nullable = false, updatable = false)
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    private UUID id;

    @Comment("Próximo número de NFCE em produção")
    @Column(name = "LNG_PROXIMONUMEROPRODUCAO_NFCECFG", nullable = false)
    private Long proximoNumeroProducao;

    @Comment("Próximo número de NFCE em homologação")
    @Column(name = "LNG_PROXIMONUMEROHOMOLOGACAO_NFCECFG", nullable = false)
    private Long proximoNumeroHomologacao;

    @Comment("Próxima série de NFCE em produção")
    @Column(name = "INT_SERIEPRODUCAO_NFCECFG", nullable = false)
    private Integer serieProducao;

    @Comment("Próxima série de NFCE em homologação")
    @Column(name = "INT_SERIEHOMOLOGACAO_NFCECFG", nullable = false)
    private Integer serieHomologacao;

    @Comment("CSC de produção")
    @Column(name = "STR_CSCPRODUCAO_NFCECFG", nullable = false)
    private String cscProducao;

    @Comment("CSC de homologação")
    @Column(name = "STR_CSCHOMOLOGACAO_NFCECFG", nullable = false)
    private String cscHomologacao;

    @Comment("ID do token de produção")
    @Column(name = "LONG_IDTOKENPRODUCAO_NFCECFG", nullable = false)
    private Long idTokenProducao;

    @Comment("ID do token de homologação")
    @Column(name = "LONG_IDTOKENHOMOLOGACAO_NFCECFG", nullable = false)
    private Long idTokenHomologacao;
}
