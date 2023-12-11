package br.akd.svc.akadia.modules.global.objects.arquivo.dto;

import br.akd.svc.akadia.modules.global.objects.arquivo.entity.ArquivoEntity;
import lombok.*;

@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ArquivoResponse {
    private String nome;
    private Long tamanho;
    private String tipo;
    private byte[] arquivo;

    public ArquivoResponse buildFromEntity(ArquivoEntity arquivoEntity) {
        return arquivoEntity != null
                ? ArquivoResponse.builder()
                .nome(arquivoEntity.getNome())
                .tamanho(arquivoEntity.getTamanho())
                .tipo(arquivoEntity.getTipo().getDesc())
                .arquivo(arquivoEntity.getArquivo())
                .build()
                : null;
    }
}
