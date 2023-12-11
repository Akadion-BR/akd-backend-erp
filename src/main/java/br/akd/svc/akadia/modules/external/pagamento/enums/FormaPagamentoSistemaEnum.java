package br.akd.svc.akadia.modules.external.pagamento.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum FormaPagamentoSistemaEnum {

    BOLETO (0, "Boleto"),
    PIX (1, "Pix");

    private final int code;
    private final String desc;

}
