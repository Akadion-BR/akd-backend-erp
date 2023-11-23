package br.akd.svc.akadia.modules.web.empresa.models.dto.fiscal.response;

import br.akd.svc.akadia.modules.web.empresa.models.dto.fiscal.response.nfce.NfceConfigResponse;
import br.akd.svc.akadia.modules.web.empresa.models.dto.fiscal.response.nfe.NfeConfigResponse;
import br.akd.svc.akadia.modules.web.empresa.models.dto.fiscal.response.nfse.NfseConfigResponse;
import br.akd.svc.akadia.modules.web.empresa.models.entity.fiscal.ConfigFiscalEmpresaEntity;
import br.akd.svc.akadia.modules.web.empresa.models.enums.fiscal.OrientacaoDanfeEnum;
import br.akd.svc.akadia.modules.web.empresa.models.enums.fiscal.RegimeTributarioEnum;
import lombok.*;

@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ConfigFiscalEmpresaResponse {
    private Boolean discriminaImpostos;
    private Boolean habilitaNfe;
    private Boolean habilitaNfce;
    private Boolean habilitaNfse;
    private Boolean habilitaEnvioEmailDestinatario;
    private Boolean exibeReciboNaDanfe;
    private String cnpjContabilidade;
    private OrientacaoDanfeEnum orientacaoDanfeEnum;
    private RegimeTributarioEnum regimeTributarioEnum;
    private NfeConfigResponse nfeConfig;
    private NfceConfigResponse nfceConfig;
    private NfseConfigResponse nfseConfig;

    public ConfigFiscalEmpresaResponse buildFromEntity(ConfigFiscalEmpresaEntity configFiscalEmpresaEntity) {
        return configFiscalEmpresaEntity != null
                ? ConfigFiscalEmpresaResponse.builder()
                .discriminaImpostos(configFiscalEmpresaEntity.getDiscriminaImpostos())
                .habilitaNfe(configFiscalEmpresaEntity.getHabilitaNfe())
                .habilitaNfce(configFiscalEmpresaEntity.getHabilitaNfce())
                .habilitaNfse(configFiscalEmpresaEntity.getHabilitaNfse())
                .habilitaEnvioEmailDestinatario(configFiscalEmpresaEntity.getHabilitaEnvioEmailDestinatario())
                .exibeReciboNaDanfe(configFiscalEmpresaEntity.getExibeReciboNaDanfe())
                .cnpjContabilidade(configFiscalEmpresaEntity.getCnpjContabilidade())
                .orientacaoDanfeEnum(configFiscalEmpresaEntity.getOrientacaoDanfeEnum())
                .regimeTributarioEnum(configFiscalEmpresaEntity.getRegimeTributarioEnum())
                .nfeConfig(new NfeConfigResponse().buildFromEntity(configFiscalEmpresaEntity.getNfeConfig()))
                .nfceConfig(new NfceConfigResponse().buildFromEntity(configFiscalEmpresaEntity.getNfceConfig()))
                .nfseConfig(new NfseConfigResponse().buildFromEntity(configFiscalEmpresaEntity.getNfseConfig()))
                .build()
                : null;
    }
}
