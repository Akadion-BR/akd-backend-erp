package br.akd.svc.akadia.modules.global.objects.endereco.entity;

import br.akd.svc.akadia.modules.global.objects.endereco.dto.request.EnderecoRequest;
import br.akd.svc.akadia.modules.global.objects.endereco.enums.EstadoEnum;
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
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "TB_AKD_ENDERECO")
public class EnderecoEntity {

    @Id
    @Type(type = "uuid-char")
    @GeneratedValue(generator = "UUID")
    @Comment("Chave primária do endereço - UUID")
    @Column(name = "COD_ENDERECO_END", nullable = false, updatable = false)
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    private UUID id;

    @Comment("Logradouro do endereço")
    @Column(name = "STR_LOGRADOURO_END", nullable = false, length = 100)
    private String logradouro;

    @Comment("Número do endereço")
    @Column(name = "INT_NUMERO_END", nullable = false)
    private Integer numero;

    @Comment("Bairro do endereço")
    @Column(name = "STR_BAIRRO_END", nullable = false, length = 80)
    private String bairro;

    @Comment("Cep do endereço")
    @Column(name = "STR_CODIGOPOSTAL_END", nullable = false, length = 9)
    private String codigoPostal;

    @Comment("Cidade do ENDEREÇO")
    @Column(name = "STR_CIDADE_END", nullable = false, length = 80)
    private String cidade;

    @Comment("Complemento do endereço")
    @Column(name = "STR_COMPLEMENTO_END", length = 100)
    private String complemento;

    @Enumerated(EnumType.STRING)
    @Comment("Estado do endereço")
    @Column(name = "ENM_ESTADO_END", nullable = false)
    private EstadoEnum estado;

    public EnderecoEntity buildFromRequest(EnderecoRequest enderecoRequest) {
        return enderecoRequest != null
                ? EnderecoEntity.builder()
                .logradouro(enderecoRequest.getLogradouro())
                .numero(enderecoRequest.getNumero())
                .bairro(enderecoRequest.getBairro())
                .codigoPostal(enderecoRequest.getCodigoPostal())
                .cidade(enderecoRequest.getCidade())
                .complemento(enderecoRequest.getComplemento())
                .estado(enderecoRequest.getEstado())
                .build()
                : null;
    }

}
