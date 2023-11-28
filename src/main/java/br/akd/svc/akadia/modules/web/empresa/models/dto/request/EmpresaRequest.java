package br.akd.svc.akadia.modules.web.empresa.models.dto.request;

import br.akd.svc.akadia.modules.global.objects.endereco.dto.request.EnderecoRequest;
import br.akd.svc.akadia.modules.global.objects.telefone.request.TelefoneRequest;
import br.akd.svc.akadia.modules.web.empresa.models.dto.fiscal.request.ConfigFiscalEmpresaRequest;
import br.akd.svc.akadia.modules.web.empresa.models.enums.SegmentoEmpresaEnum;
import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class EmpresaRequest {

    //TODO ADICIONAR VALIDATORS

    private String nome;
    private String razaoSocial;
    private String cnpj;
    private String endpoint;
    private String email;
    private String nomeFantasia;
    private String inscricaoEstadual;
    private String inscricaoMunicipal;
    private String nomeResponsavel;
    private String cpfResponsavel;
    private SegmentoEmpresaEnum segmentoEmpresaEnum;
    private TelefoneRequest telefone;
    private EnderecoRequest endereco;
    private ConfigFiscalEmpresaRequest configFiscalEmpresa;
}
