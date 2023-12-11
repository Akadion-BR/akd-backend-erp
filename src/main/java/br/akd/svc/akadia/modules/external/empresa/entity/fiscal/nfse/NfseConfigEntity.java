package br.akd.svc.akadia.modules.external.empresa.entity.fiscal.nfse;

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
@Table(name = "TB_AKD_NFSECONFIG")
public class NfseConfigEntity {
    @Id
    @Type(type = "uuid-char")
    @GeneratedValue(generator = "UUID")
    @Comment("Chave primária da configuração de NFSE - UUID")
    @Column(name = "COD_NFSECONFIG_NFSECFG", nullable = false, updatable = false)
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    private UUID id;

    @Comment("Próximo número de NFSE em produção")
    @Column(name = "LNG_PROXIMONUMEROPRODUCAO_NFSECFG", nullable = false)
    private Long proximoNumeroProducao;

    @Comment("Próximo número de NFSE em homologação")
    @Column(name = "LNG_PROXIMONUMEROHOMOLOGACAO_NFSECFG", nullable = false)
    private Long proximoNumeroHomologacao;

    @Comment("Próxima série de NFSE em produção")
    @Column(name = "INT_SERIEPRODUCAO_NFSECFG", nullable = false)
    private Integer serieProducao;

    @Comment("Próxima série de NFSE em homologação")
    @Column(name = "INT_SERIEHOMOLOGACAO_NFSECFG", nullable = false)
    private Integer serieHomologacao;
}