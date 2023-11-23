package br.akd.svc.akadia.modules.web.empresa.models.dto.fiscal.response.nfe;

import br.akd.svc.akadia.modules.web.empresa.models.entity.fiscal.nfe.NfeConfigEntity;
import lombok.*;

@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class NfeConfigResponse {
    private Long proximoNumeroProducao;
    private Long proximoNumeroHomologacao;
    private Integer serieProducao;
    private Integer serieHomologacao;

    public NfeConfigResponse buildFromEntity(NfeConfigEntity nfeConfigEntity) {
        return nfeConfigEntity != null
                ? NfeConfigResponse.builder()
                .proximoNumeroProducao(nfeConfigEntity.getProximoNumeroProducao())
                .proximoNumeroHomologacao(nfeConfigEntity.getProximoNumeroHomologacao())
                .serieProducao(nfeConfigEntity.getSerieProducao())
                .serieHomologacao(nfeConfigEntity.getSerieHomologacao())
                .build()
                : null;
    }
}
