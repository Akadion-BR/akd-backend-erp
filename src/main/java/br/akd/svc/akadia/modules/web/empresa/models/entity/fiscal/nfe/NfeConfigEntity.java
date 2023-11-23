package br.akd.svc.akadia.modules.web.empresa.models.entity.fiscal.nfe;

import br.akd.svc.akadia.modules.web.empresa.models.dto.fiscal.request.nfe.NfeConfigRequest;
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
@Table(name = "TB_AKD_NFECONFIG")
public class NfeConfigEntity {
    @Id
    @Type(type = "uuid-char")
    @GeneratedValue(generator = "UUID")
    @Comment("Chave primária da configuração de NFE - UUID")
    @Column(name = "COD_NFECONFIG_NFECFG", nullable = false, updatable = false)
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    private UUID id;

    @Comment("Próximo número de NFE em produção")
    @Column(name = "LNG_PROXIMONUMEROPRODUCAO_NFECFG", nullable = false)
    private Long proximoNumeroProducao;

    @Comment("Próximo número de NFE em homologação")
    @Column(name = "LNG_PROXIMONUMEROHOMOLOGACAO_NFECFG", nullable = false)
    private Long proximoNumeroHomologacao;

    @Comment("Próxima série de NFE em produção")
    @Column(name = "INT_SERIEPRODUCAO_NFECFG", nullable = false)
    private Integer serieProducao;

    @Comment("Próxima série de NFE em homologação")
    @Column(name = "INT_SERIEHOMOLOGACAO_NFECFG", nullable = false)
    private Integer serieHomologacao;

    public NfeConfigEntity buildFromRequest(NfeConfigRequest nfeConfigRequest) {
        return nfeConfigRequest != null
                ? NfeConfigEntity.builder()
                .proximoNumeroProducao(nfeConfigRequest.getProximoNumeroProducao())
                .proximoNumeroHomologacao(nfeConfigRequest.getProximoNumeroHomologacao())
                .serieProducao(nfeConfigRequest.getSerieProducao())
                .serieHomologacao(nfeConfigRequest.getSerieHomologacao())
                .build()
                : null;
    }
}
