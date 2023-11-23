package br.akd.svc.akadia.modules.web.cartao.models.dto.response;

import br.akd.svc.akadia.modules.web.cartao.models.enums.BandeiraCartaoEnum;
import lombok.*;

import java.util.UUID;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class CartaoResponse {
    private UUID id;
    private String nomePortador;
    private String cpfCnpj;
    private Long numero;
    private Integer ccv;
    private Integer mesExpiracao;
    private Integer anoExpiracao;
    private String tokenCartao;
    private BandeiraCartaoEnum bandeiraCartaoEnum;
}
