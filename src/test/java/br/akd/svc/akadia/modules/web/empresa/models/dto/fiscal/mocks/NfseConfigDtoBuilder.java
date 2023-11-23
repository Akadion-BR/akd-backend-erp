package br.akd.svc.akadia.modules.web.empresa.models.dto.fiscal.mocks;

public class NfseConfigDtoBuilder {

    NfseConfigDtoBuilder() {
    }

    NfseConfigDto nfseConfigDto;

    public static NfseConfigDtoBuilder builder() {
        NfseConfigDtoBuilder builder = new NfseConfigDtoBuilder();
        builder.nfseConfigDto = new NfseConfigDto();
        builder.nfseConfigDto.setId(1L);
        builder.nfseConfigDto.setProximoNumeroProducao(1L);
        builder.nfseConfigDto.setProximoNumeroHomologacao(1L);
        builder.nfseConfigDto.setSerieProducao(1);
        builder.nfseConfigDto.setSerieHomologacao(1);
        return builder;
    }

    public NfseConfigDto build() {
        return nfseConfigDto;
    }

}
