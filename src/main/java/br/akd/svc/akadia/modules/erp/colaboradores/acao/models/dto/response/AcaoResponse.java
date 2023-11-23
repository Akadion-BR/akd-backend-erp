package br.akd.svc.akadia.modules.erp.colaboradores.acao.models.dto.response;

import br.akd.svc.akadia.modules.erp.colaboradores.acao.models.entity.AcaoEntity;
import br.akd.svc.akadia.modules.erp.colaboradores.acao.models.enums.TipoAcaoEnum;
import br.akd.svc.akadia.modules.erp.colaboradores.colaborador.models.enums.ModulosEnum;
import lombok.*;

import java.util.UUID;

@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class AcaoResponse {
    private UUID id;
    private UUID idObjeto;
    private String dataCadastro;
    private String horaCadastro;
    private String observacao;
    private ModulosEnum moduloEnum;
    private TipoAcaoEnum tipoAcaoEnum;

    public AcaoResponse buildFromEntity(AcaoEntity acaoEntity) {
        return acaoEntity != null
                ? AcaoResponse.builder()
                .id(acaoEntity.getId())
                .idObjeto(acaoEntity.getIdObjeto())
                .dataCadastro(acaoEntity.getDataCadastro())
                .horaCadastro(acaoEntity.getHoraCadastro())
                .observacao(acaoEntity.getObservacao())
                .moduloEnum(acaoEntity.getModuloEnum())
                .tipoAcaoEnum(acaoEntity.getTipoAcaoEnum())
                .build()
                : null;
    }
}
