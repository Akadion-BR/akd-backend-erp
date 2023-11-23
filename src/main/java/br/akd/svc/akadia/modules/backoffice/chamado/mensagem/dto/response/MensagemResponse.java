package br.akd.svc.akadia.modules.backoffice.chamado.mensagem.dto.response;

import br.akd.svc.akadia.modules.backoffice.chamado.mensagem.anexo.dto.response.AnexoMensagemResponse;
import br.akd.svc.akadia.modules.backoffice.chamado.mensagem.entity.MensagemEntity;
import lombok.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class MensagemResponse {
    private UUID id;
    private String dataEnvio;
    private String horaEnvio;
    private Boolean visualizada = false;
    private Boolean respondida = false;
    private String conteudo;
    private String caminhoMensagemEnum;
    private List<AnexoMensagemResponse> anexos = new ArrayList<>();

    public MensagemResponse buildFromEntity(MensagemEntity mensagemEntity) {
        return mensagemEntity != null
                ? MensagemResponse.builder()
                .id(mensagemEntity.getId())
                .dataEnvio(mensagemEntity.getDataEnvio())
                .horaEnvio(mensagemEntity.getHoraEnvio())
                .visualizada(mensagemEntity.getVisualizada())
                .respondida(mensagemEntity.getRespondida())
                .conteudo(mensagemEntity.getConteudo())
                .caminhoMensagemEnum(mensagemEntity.getCaminhoMensagemEnum().getDesc())
                .anexos(new AnexoMensagemResponse().buildListFromEntity(mensagemEntity.getAnexos()))
                .build()
                : null;
    }

    public List<MensagemResponse> buildListFromEntity(List<MensagemEntity> mensagemEntities) {
        List<MensagemResponse> mensagemResponses = new ArrayList<>();
        mensagemEntities.forEach(mensagem -> mensagemResponses.add(buildFromEntity(mensagem)));
        return mensagemResponses;
    }
}
