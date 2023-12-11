package br.akd.svc.akadia.modules.global.objects.arquivo.entity;

import br.akd.svc.akadia.modules.global.objects.arquivo.enums.TipoArquivoEnum;
import lombok.*;
import org.hibernate.annotations.Comment;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.*;
import java.io.IOException;
import java.util.UUID;

@Entity
@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "TB_AKD_ARQUIVO")
public class ArquivoEntity {

    @Id
    @Type(type = "uuid-char")
    @GeneratedValue(generator = "UUID")
    @Comment("Chave primária do arquivo - UUID")
    @Column(name = "COD_ARQUIVO_ARQ", nullable = false, updatable = false)
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    private UUID id;

    @Comment("Nome do arquivo")
    @Column(name = "STR_NOME_ARQ", nullable = false, length = 70)
    private String nome;

    @Comment("Tamanho do arquivo")
    @Column(name = "LNG_TAMANHO_ARQ", nullable = false)
    private Long tamanho;

    @Enumerated(EnumType.STRING)
    @Column(name = "ENM_TIPO_IMG", nullable = false)
    @Comment("Tipo do arquivo: " +
            "0 - jpeg, " +
            "1 - png, " +
            "1 - pdf, " +
            "1 - docx")
    private TipoArquivoEnum tipo;

    @Lob
    @Comment("Dados do arquivo")
    @Column(name = "ABT_ARQUIVO_ARQ", nullable = false)
    private byte[] arquivo;

    public ArquivoEntity buildFromMultiPartFile(MultipartFile multipartFile) throws IOException {
        return multipartFile != null
                ? ArquivoEntity.builder()
                .nome(multipartFile.getOriginalFilename())
                .tipo(realizaTratamentoTipoArquivo(multipartFile.getContentType()))
                .tamanho(multipartFile.getSize())
                .arquivo(multipartFile.getBytes())
                .build()
                : null;
    }

    public TipoArquivoEnum realizaTratamentoTipoArquivo(String tipoArquivo) {

        if (tipoArquivo == null) return TipoArquivoEnum.PDF;

        switch (tipoArquivo) {
            case "application/vnd.openxmlformats-officedocument.wordprocessingml.document":
                return TipoArquivoEnum.DOCX;
            case "image/png":
                return TipoArquivoEnum.PNG;
            case "image/jpeg":
                return TipoArquivoEnum.JPG;
            default:
                return TipoArquivoEnum.PDF;
        }
    }

}
