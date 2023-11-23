package br.akd.svc.akadia.modules.backoffice.chamado.mensagem.anexo.dto.request;

import lombok.*;

@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class AnexoMensagemRequest {
    //TODO JAVAX VALIDATOR
    private byte[] dados;
    private String nome;
    private String tipo;
}
