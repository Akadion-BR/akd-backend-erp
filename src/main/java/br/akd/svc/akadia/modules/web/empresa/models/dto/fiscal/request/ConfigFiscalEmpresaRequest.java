package br.akd.svc.akadia.modules.web.empresa.models.dto.fiscal.request;

import br.akd.svc.akadia.modules.web.empresa.models.dto.fiscal.request.nfce.NfceConfigRequest;
import br.akd.svc.akadia.modules.web.empresa.models.dto.fiscal.request.nfe.NfeConfigRequest;
import br.akd.svc.akadia.modules.web.empresa.models.dto.fiscal.request.nfse.NfseConfigRequest;
import br.akd.svc.akadia.modules.web.empresa.models.enums.fiscal.OrientacaoDanfeEnum;
import br.akd.svc.akadia.modules.web.empresa.models.enums.fiscal.RegimeTributarioEnum;
import lombok.*;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class ConfigFiscalEmpresaRequest {

    //TODO JAVAX VALIDATOR

    private Boolean discriminaImpostos;
    private Boolean habilitaNfe;
    private Boolean habilitaNfce;
    private Boolean habilitaNfse;
    private Boolean habilitaEnvioEmailDestinatario;
    private Boolean exibeReciboNaDanfe;
    private String cnpjContabilidade;
    private String senhaCertificadoDigital;
    private byte[] certificadoDigital;
    private OrientacaoDanfeEnum orientacaoDanfeEnum;
    private RegimeTributarioEnum regimeTributarioEnum;
    private NfeConfigRequest nfeConfig;
    private NfceConfigRequest nfceConfig;
    private NfseConfigRequest nfseConfig;
}
