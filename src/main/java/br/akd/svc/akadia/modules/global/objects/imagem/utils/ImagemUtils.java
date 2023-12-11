package br.akd.svc.akadia.modules.global.objects.imagem.utils;

import br.akd.svc.akadia.exceptions.InvalidRequestException;
import br.akd.svc.akadia.modules.global.objects.imagem.enums.TipoImagemEnum;

public class ImagemUtils {

    ImagemUtils() {
    }

    public static TipoImagemEnum realizaTratamentoTipoDeImagem(String tipoImagem) {
        if (tipoImagem.equals("image/png")) {
            return TipoImagemEnum.PNG;
        } else if (tipoImagem.equals("image/jpeg")) {
            return TipoImagemEnum.JPG;
        } else {
            throw new InvalidRequestException("O arquivo enviado deve ser uma imagem");
        }

    }
}
