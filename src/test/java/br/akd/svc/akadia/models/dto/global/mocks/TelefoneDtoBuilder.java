package br.akd.svc.akadia.models.dto.global.mocks;

public class TelefoneDtoBuilder {

    TelefoneDtoBuilder(){}
    TelefoneDto telefoneDto;

    public static TelefoneDtoBuilder builder() {
        TelefoneDtoBuilder builder = new TelefoneDtoBuilder();
        builder.telefoneDto = new TelefoneDto();
        builder.telefoneDto.setId(1L);
        builder.telefoneDto.setPrefixo("11");
        builder.telefoneDto.setNumero("979815415");
        builder.telefoneDto.setTipoTelefone(TipoTelefoneEnum.MOVEL_WHATSAPP);
        return builder;
    }

    public TelefoneDto build() {
        return telefoneDto;
    }
}
