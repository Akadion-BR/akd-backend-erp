package br.akd.svc.akadia.modules.erp.colaboradores.colaborador.models.entity.dispensa;

import br.akd.svc.akadia.modules.global.objects.arquivo.entity.ArquivoEntity;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;
import org.hibernate.annotations.Comment;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.List;
import java.util.UUID;

@Entity
@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "TB_AKD_DISPENSA")
public class DispensaEntity {
    @Id
    @Type(type = "uuid-char")
    @GeneratedValue(generator = "UUID")
    @Comment("Chave primária da dispensa - UUID")
    @Column(name = "COD_DISPENSA_DSP", nullable = false, updatable = false)
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    private UUID id;

    @Comment("Data da dispensa")
    @Column(name = "DT_DATA_DSP", nullable = false, length = 10)
    private String dataDispensa;

    @Comment("Motivo da dispensa")
    @Column(name = "STR_MOTIVO_DSP", nullable = false, length = 300)
    private String motivo;

    @Comment("Lista negra")
    @Column(name = "BOL_LISTANEGRA_DSP", nullable = false)
    private Boolean listaNegra = false;

    @ToString.Exclude
    @Comment("Contrato de dispensa")
    @OneToOne(targetEntity = ArquivoEntity.class,
            orphanRemoval = true,
            cascade = CascadeType.ALL,
            fetch = FetchType.LAZY)
    private ArquivoEntity contratoDispensa;

    @ToString.Exclude
    @Comment("Anexos da dispensa")
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    @OneToMany(targetEntity = ArquivoEntity.class, orphanRemoval = true, cascade = CascadeType.ALL)
    private List<ArquivoEntity> anexos;
}
