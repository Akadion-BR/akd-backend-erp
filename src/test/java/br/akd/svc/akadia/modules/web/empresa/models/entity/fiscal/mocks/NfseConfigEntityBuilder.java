package br.akd.svc.akadia.modules.web.empresa.models.entity.fiscal.mocks;

import br.akd.svc.akadia.modules.web.empresa.models.entity.fiscal.nfse.NfseConfigEntity;

public class NfseConfigEntityBuilder {

    NfseConfigEntityBuilder() {
    }

    NfseConfigEntity nfseConfigEntity;

    public static NfseConfigEntityBuilder builder() {
        NfseConfigEntityBuilder builder = new NfseConfigEntityBuilder();
        builder.nfseConfigEntity = new NfseConfigEntity();
        builder.nfseConfigEntity.setId(1L);
        builder.nfseConfigEntity.setProximoNumeroProducao(1L);
        builder.nfseConfigEntity.setProximoNumeroHomologacao(1L);
        builder.nfseConfigEntity.setSerieProducao(1);
        builder.nfseConfigEntity.setSerieHomologacao(1);
        return builder;
    }

    public NfseConfigEntity build() {
        return nfseConfigEntity;
    }

}
