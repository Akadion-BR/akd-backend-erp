package br.akd.svc.akadia.modules.erp.colaboradores.colaborador.models.dto.colaborador.response;

import lombok.*;

@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class CriacaoColaboradorResponse {
    private String matricula;
    private String senhaAcesso;
}
