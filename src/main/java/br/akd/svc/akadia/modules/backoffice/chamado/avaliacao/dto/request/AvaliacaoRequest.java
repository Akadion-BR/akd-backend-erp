package br.akd.svc.akadia.modules.backoffice.chamado.avaliacao.dto.request;

import lombok.*;

@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class AvaliacaoRequest {
    private Integer nota;
    private String descricao;
}
