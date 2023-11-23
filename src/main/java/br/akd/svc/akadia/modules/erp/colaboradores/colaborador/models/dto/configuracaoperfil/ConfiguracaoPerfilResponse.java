package br.akd.svc.akadia.modules.erp.colaboradores.colaborador.models.dto.configuracaoperfil;

import br.akd.svc.akadia.modules.erp.colaboradores.colaborador.models.entity.configuracaoperfil.ConfiguracaoPerfilEntity;
import lombok.*;

import java.util.UUID;

@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ConfiguracaoPerfilResponse {
    private UUID id;
    private String dataUltimaAtualizacao;
    private String horaUltimaAtualizacao;
    private String temaTelaEnum;

    public ConfiguracaoPerfilResponse buildFromEntity(ConfiguracaoPerfilEntity configuracaoPerfilEntity) {
        return configuracaoPerfilEntity != null
                ? ConfiguracaoPerfilResponse.builder()
                .id(configuracaoPerfilEntity.getId())
                .dataUltimaAtualizacao(configuracaoPerfilEntity.getDataUltimaAtualizacao())
                .horaUltimaAtualizacao(configuracaoPerfilEntity.getHoraUltimaAtualizacao())
                .temaTelaEnum(configuracaoPerfilEntity.getTemaTelaEnum().getDesc())
                .build()
                : null;
    }
}