package br.akd.svc.akadia.modules.web.cartao.models.dto.request;

import br.akd.svc.akadia.modules.web.cartao.models.enums.BandeiraCartaoEnum;
import lombok.*;
import org.hibernate.annotations.Comment;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.UUID;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class CartaoRequest {
    //TODO JAVAX VALIDATOR
    private String nomePortador;
    private String cpfCnpj;
    private Long numero;
    private Integer ccv;
    private Integer mesExpiracao;
    private Integer anoExpiracao;
    private BandeiraCartaoEnum bandeiraCartaoEnum;
}
