package br.akd.svc.akadia.modules.backoffice.chamado.models.dto.request;

import br.akd.svc.akadia.modules.backoffice.chamado.models.enums.CategoriaChamadoEnum;
import lombok.*;

@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ChamadoRequest {
    private String titulo;
    private String descricao;
    private CategoriaChamadoEnum categoriaChamadoEnum;
}
