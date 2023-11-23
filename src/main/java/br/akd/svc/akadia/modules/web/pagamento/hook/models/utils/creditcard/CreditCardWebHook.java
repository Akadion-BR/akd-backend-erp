package br.akd.svc.akadia.modules.web.pagamento.hook.models.utils.creditcard;

import lombok.*;

@ToString
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CreditCardWebHook {
    private String creditCardNumber;
    private String creditCardBrand;
    private String creditCardToken;
}
