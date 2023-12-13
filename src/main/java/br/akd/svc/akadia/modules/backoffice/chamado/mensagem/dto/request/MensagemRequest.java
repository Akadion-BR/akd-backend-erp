package br.akd.svc.akadia.modules.backoffice.chamado.mensagem.dto.request;

import br.akd.svc.akadia.modules.backoffice.chamado.mensagem.anexo.dto.request.AnexoMensagemRequest;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class MensagemRequest {
    private String conteudo;
    private List<AnexoMensagemRequest> anexos = new ArrayList<>();
}
