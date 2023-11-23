package br.akd.svc.akadia.modules.backoffice.chamado.mensagem.anexo.dto.response;

import br.akd.svc.akadia.modules.backoffice.chamado.mensagem.anexo.entity.AnexoMensagemEntity;
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
public class AnexoMensagemResponse {
    private UUID id;
    private byte[] dados;
    private String nome;
    private String tipo;

    public AnexoMensagemResponse buildFromEntity(AnexoMensagemEntity anexoMensagemEntity) {
        return anexoMensagemEntity != null
                ? AnexoMensagemResponse.builder()
                .id(anexoMensagemEntity.getId())
                .dados(anexoMensagemEntity.getDados())
                .nome(anexoMensagemEntity.getNome())
                .tipo(anexoMensagemEntity.getTipo())
                .build()
                : null;
    }

    public List<AnexoMensagemResponse> buildListFromEntity(List<AnexoMensagemEntity> anexoMensagemEntities) {
        List<AnexoMensagemResponse> anexoMensagemResponses = new ArrayList<>();

        anexoMensagemEntities.forEach(
                anexoMensagemEntity -> anexoMensagemResponses.add(buildFromEntity(anexoMensagemEntity)));

        return anexoMensagemResponses;
    }
}
