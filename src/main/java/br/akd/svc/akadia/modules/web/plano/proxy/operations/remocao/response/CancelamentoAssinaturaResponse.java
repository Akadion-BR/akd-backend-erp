package br.akd.svc.akadia.modules.web.plano.proxy.operations.remocao.response;

import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class CancelamentoAssinaturaResponse {
    private Boolean deleted;
    private String id;
}
