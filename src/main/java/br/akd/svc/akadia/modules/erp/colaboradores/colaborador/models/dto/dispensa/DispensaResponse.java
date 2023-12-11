package br.akd.svc.akadia.modules.erp.colaboradores.colaborador.models.dto.dispensa;

import br.akd.svc.akadia.modules.erp.colaboradores.colaborador.models.entity.dispensa.DispensaEntity;
import br.akd.svc.akadia.modules.global.objects.arquivo.entity.ArquivoEntity;
import lombok.*;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class DispensaResponse {
    private UUID id;
    private String dataDispensa;
    private String motivo;
    private Boolean listaNegra = false;
    private ArquivoEntity contratoDispensa;
    private List<ArquivoEntity> anexos;

    public DispensaResponse buildFromEntity(DispensaEntity dispensaEntity) {
        return dispensaEntity != null
                ? DispensaResponse.builder()
                .id(dispensaEntity.getId())
                .dataDispensa(dispensaEntity.getDataDispensa())
                .motivo(dispensaEntity.getMotivo())
                .listaNegra(dispensaEntity.getListaNegra())
                .contratoDispensa(dispensaEntity.getContratoDispensa())
                .anexos(dispensaEntity.getAnexos())
                .build()
                : null;
    }
}
