package br.akd.svc.akadia.modules.erp.colaboradores.advertencia.models.entity;

import br.akd.svc.akadia.modules.erp.colaboradores.advertencia.models.dto.request.AdvertenciaRequest;
import br.akd.svc.akadia.modules.erp.colaboradores.advertencia.models.enums.StatusAdvertenciaEnum;
import br.akd.svc.akadia.modules.global.arquivo.entity.ArquivoEntity;
import lombok.*;
import org.hibernate.annotations.Comment;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.*;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

@Entity
@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "TB_AKD_ADVERTENCIA")
public class AdvertenciaEntity {
    @Id
    @Type(type = "uuid-char")
    @GeneratedValue(generator = "UUID")
    @Comment("Chave primária da advertência - UUID")
    @Column(name = "COD_ADVERTENCIA_ADV", nullable = false, updatable = false)
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    private UUID id;

    @Comment("Data em que o cadastro da advertência foi realizado")
    @Column(name = "DT_DATACADASTRO_ADV", nullable = false, updatable = false, length = 10)
    private String dataCadastro;

    @Comment("Hora em que o cadastro da advertência foi realizado")
    @Column(name = "HR_HORACADASTRO_ADV", nullable = false, updatable = false, length = 18)
    private String horaCadastro;

    @Comment("Motivo pelo qual a advertência foi gerada")
    @Column(name = "STR_MOTIVO_ADV", nullable = false, updatable = false, length = 100)
    private String motivo;

    @Comment("Descrição da advertência")
    @Column(name = "STR_DESCRICAO_ADV", nullable = false, updatable = false, length = 300)
    private String descricao;

    @Enumerated(EnumType.STRING)
    @Column(name = "ENM_STATUS_ADV", nullable = false)
    @Comment("Status da advertência: " +
            "0 - Pendente de assinatura, " +
            "1 - Assinada")
    private StatusAdvertenciaEnum statusAdvertencia;

    @ToString.Exclude
    @Comment("Arquivo com a advertência assinada")
    @OneToOne(targetEntity = ArquivoEntity.class,
            orphanRemoval = true,
            cascade = CascadeType.ALL,
            fetch = FetchType.LAZY)
    private ArquivoEntity advertenciaAssinada;

    public AdvertenciaEntity buildFromRequest(AdvertenciaRequest advertenciaRequest,
                                              MultipartFile contratoAdvertencia) throws IOException {
        return advertenciaRequest != null
                ? AdvertenciaEntity.builder()
                .id(null)
                .dataCadastro(LocalDate.now().toString())
                .horaCadastro(LocalTime.now().toString())
                .motivo(advertenciaRequest.getMotivo())
                .descricao(advertenciaRequest.getDescricao())
                .statusAdvertencia(advertenciaRequest.getStatusAdvertencia())
                .advertenciaAssinada(new ArquivoEntity().buildFromMultiPartFile(contratoAdvertencia))
                .build()
                : null;
    }
}
