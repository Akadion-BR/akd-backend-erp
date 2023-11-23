package br.akd.svc.akadia.modules.backoffice.chamado.avaliacao.dto.response;

import br.akd.svc.akadia.modules.backoffice.chamado.avaliacao.entity.AvaliacaoEntity;
import lombok.*;

import java.util.UUID;

@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class AvaliacaoResponse {
    private UUID id;
    private Integer nota;
    private String descricao;

    public AvaliacaoResponse buildFromEntity(AvaliacaoEntity avaliacaoEntity) {
        return avaliacaoEntity != null
                ? AvaliacaoResponse.builder()
                .id(avaliacaoEntity.getId())
                .nota(avaliacaoEntity.getNota())
                .descricao(avaliacaoEntity.getDescricao())
                .build()
                : null;
    }
}
