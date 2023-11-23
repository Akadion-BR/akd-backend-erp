package br.akd.svc.akadia.modules.global.imagem.entity;

import br.akd.svc.akadia.modules.global.imagem.enums.TipoImagemEnum;
import br.akd.svc.akadia.modules.global.imagem.utils.ImagemUtils;
import lombok.*;
import org.hibernate.annotations.Comment;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.*;
import java.io.IOException;
import java.util.Objects;
import java.util.UUID;

@Getter
@Setter
@Entity
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "TB_AKD_IMAGEM")
public class ImagemEntity {

    @Id
    @Type(type = "uuid-char")
    @GeneratedValue(generator = "UUID")
    @Comment("Chave primária da imagem - UUID")
    @Column(name = "COD_IMAGEM_IMG", nullable = false, updatable = false)
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    private UUID id;

    @Comment("Nome do arquivo")
    @Column(name = "STR_NOME_IMG", nullable = false, length = 70)
    private String nome;

    @Comment("Tamanho da imagem")
    @Column(name = "LNG_TAMANHO_IMG", nullable = false)
    private Long tamanho;

    @Enumerated(EnumType.STRING)
    @Column(name = "ENM_TIPO_IMG", nullable = false)
    @Comment("Tipo do arquivo da imagem: " +
            "0 - Jpeg, " +
            "1 - Png")
    private TipoImagemEnum tipo;

    @Lob
    @Comment("Arquivo de imagem")
    @Type(type = "org.hibernate.type.ImageType")
    @Column(name = "ABT_ARQUIVO_IMG", nullable = false)
    private byte[] arquivo;

    public ImagemEntity constroiImagemEntity(MultipartFile multipartFile) throws IOException {
        if (multipartFile == null) return null;
        return ImagemEntity.builder()
                .nome(multipartFile.getOriginalFilename())
                .tipo(ImagemUtils.realizaTratamentoTipoDeImagem(Objects.requireNonNull(multipartFile.getContentType())))
                .tamanho(multipartFile.getSize())
                .arquivo(multipartFile.getBytes())
                .build();
    }

}
