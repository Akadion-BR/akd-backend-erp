package br.akd.svc.akadia.modules.web.empresa.models.dto.fiscal.response.nfse;

import br.akd.svc.akadia.modules.web.empresa.models.entity.fiscal.nfse.NfseConfigEntity;
import lombok.*;

@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class NfseConfigResponse {
    private Long proximoNumeroProducao;
    private Long proximoNumeroHomologacao;
    private Integer serieProducao;
    private Integer serieHomologacao;

    public NfseConfigResponse buildFromEntity(NfseConfigEntity nfseConfigEntity) {
        return nfseConfigEntity != null
                ? NfseConfigResponse.builder()
                .proximoNumeroProducao(nfseConfigEntity.getProximoNumeroProducao())
                .proximoNumeroHomologacao(nfseConfigEntity.getProximoNumeroHomologacao())
                .serieProducao(nfseConfigEntity.getSerieProducao())
                .serieHomologacao(nfseConfigEntity.getSerieHomologacao())
                .build()
                : null;
    }
}
