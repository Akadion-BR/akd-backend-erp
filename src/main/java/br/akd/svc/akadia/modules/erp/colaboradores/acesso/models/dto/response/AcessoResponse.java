package br.akd.svc.akadia.modules.erp.colaboradores.acesso.models.dto.response;

import br.akd.svc.akadia.modules.erp.colaboradores.acesso.models.entity.AcessoEntity;
import lombok.*;

import java.util.UUID;

@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class AcessoResponse {
    private UUID id;
    private String dataCadastro;
    private String horaCadastro;

    public AcessoResponse buildFromEntity(AcessoEntity acessoEntity) {
        return acessoEntity != null
                ? AcessoResponse.builder()
                .dataCadastro(acessoEntity.getDataCadastro())
                .horaCadastro(acessoEntity.getHoraCadastro())
                .build()
                : null;
    }
}
