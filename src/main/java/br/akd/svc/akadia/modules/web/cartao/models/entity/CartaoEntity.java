package br.akd.svc.akadia.modules.web.cartao.models.entity;

import br.akd.svc.akadia.modules.web.cartao.models.enums.BandeiraCartaoEnum;
import lombok.*;
import org.hibernate.annotations.Comment;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.UUID;

@Entity
@Builder
@ToString
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "TB_AKD_CARTAO")
public class CartaoEntity {

    @Id
    @Type(type = "uuid-char")
    @GeneratedValue(generator = "UUID")
    @Comment("Chave primária do cartão - UUID")
    @Column(name = "COD_CARTAO_CRT", nullable = false, updatable = false)
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    private UUID id;

    @Comment("Nome do portador do cartão")
    @Column(name = "STR_NOME_CRT", nullable = false, length = 70)
    private String nomePortador;

    @Comment("CPF ou CNPJ do portador do cartão")
    @Column(name = "STR_CPFCNPJ_CRT", nullable = false, length = 18)
    private String cpfCnpj;

    @Comment("Número do cartão")
    @Column(name = "LNG_NUMERO_CRT", nullable = false)
    private Long numero;

    @Comment("Código de segurança do cartão")
    @Column(name = "INT_CCV_CRT", nullable = false)
    private Integer ccv;

    @Comment("Mês de expiração do cartão")
    @Column(name = "INT_MESEXPIRACAO_CRT", nullable = false)
    private Integer mesExpiracao;

    @Comment("Ano de expiração do cartão")
    @Column(name = "INT_ANOEXPIRACAO_CRT", nullable = false)
    private Integer anoExpiracao;

    @Comment("Token do cartão")
    @Column(name = "STR_TOKEN_CRT")
    private String tokenCartao;

    @Enumerated(EnumType.STRING)
    @Comment("Bandeira do cartão: " +
            "0 - Mastercard, " +
            "1 - Visa, " +
            "2 - Elo, " +
            "3 - American Express, " +
            "4 - Hipercard")
    @Column(name = "ENM_BANDEIRA_CRT", nullable = false)
    private BandeiraCartaoEnum bandeiraCartaoEnum;

}
