package br.akd.svc.akadia.modules.global.imagem.response;

import br.akd.svc.akadia.modules.global.imagem.entity.ImagemEntity;
import lombok.*;
import lombok.extern.slf4j.Slf4j;

@Data
@Slf4j
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ImagemResponse {
    private String nome;
    private Long tamanho;
    private String tipo;
    private byte[] arquivo;

    public ImagemResponse buildFromEntity(ImagemEntity imagemEntity) {
        return imagemEntity != null
                ? ImagemResponse.builder()
                .nome(imagemEntity.getNome())
                .tamanho(imagemEntity.getTamanho())
                .tipo(imagemEntity.getTipo().toString())
                .arquivo(imagemEntity.getArquivo())
                .build()
                : null;
    }

}
