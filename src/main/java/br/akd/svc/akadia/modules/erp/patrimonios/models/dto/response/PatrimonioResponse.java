package br.akd.svc.akadia.modules.erp.patrimonios.models.dto.response;

import br.akd.svc.akadia.modules.erp.patrimonios.models.entity.PatrimonioEntity;
import lombok.*;

import java.util.UUID;

@Data
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class PatrimonioResponse {
    private UUID id;
    private String dataCadastro;
    private String horaCadastro;
    private String dataEntrada;
    private String descricao;
    private Double valor;
    private String tipoPatrimonio;

    public PatrimonioResponse buildFromEntity(PatrimonioEntity patrimonioEntity) {
        return patrimonioEntity != null
                ? PatrimonioResponse.builder()
                .id(patrimonioEntity.getId())
                .dataCadastro(patrimonioEntity.getDataCadastro())
                .horaCadastro(patrimonioEntity.getHoraCadastro())
                .dataEntrada(patrimonioEntity.getDataEntrada())
                .descricao(patrimonioEntity.getDescricao())
                .valor(patrimonioEntity.getValor())
                .tipoPatrimonio(patrimonioEntity.getTipoPatrimonio().getDesc())
                .build()
                : null;
    }
}
