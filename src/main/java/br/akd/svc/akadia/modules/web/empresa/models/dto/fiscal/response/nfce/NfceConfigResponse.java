package br.akd.svc.akadia.modules.web.empresa.models.dto.fiscal.response.nfce;

import br.akd.svc.akadia.modules.web.empresa.models.entity.fiscal.nfce.NfceConfigEntity;
import lombok.*;

@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class NfceConfigResponse {
    private Long proximoNumeroProducao;
    private Long proximoNumeroHomologacao;
    private Integer serieProducao;
    private Integer serieHomologacao;
    private String cscProducao;
    private String cscHomologacao;
    private Long idTokenProducao;
    private Long idTokenHomologacao;

    public NfceConfigResponse buildFromEntity(NfceConfigEntity nfceConfigEntity) {
        return nfceConfigEntity != null
                ? NfceConfigResponse.builder()
                .proximoNumeroProducao(nfceConfigEntity.getProximoNumeroProducao())
                .proximoNumeroHomologacao(nfceConfigEntity.getProximoNumeroHomologacao())
                .serieProducao(nfceConfigEntity.getSerieProducao())
                .serieHomologacao(nfceConfigEntity.getSerieHomologacao())
                .cscProducao(nfceConfigEntity.getCscProducao())
                .cscHomologacao(nfceConfigEntity.getCscHomologacao())
                .idTokenProducao(nfceConfigEntity.getIdTokenProducao())
                .idTokenHomologacao(nfceConfigEntity.getIdTokenHomologacao())
                .build()
                : null;
    }
}
