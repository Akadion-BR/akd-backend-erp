package br.akd.svc.akadia.modules.web.empresa.models.dto.response;

import br.akd.svc.akadia.modules.backoffice.chamado.models.dto.response.ChamadoResponse;
import br.akd.svc.akadia.modules.global.endereco.dto.response.EnderecoResponse;
import br.akd.svc.akadia.modules.global.imagem.response.ImagemResponse;
import br.akd.svc.akadia.modules.global.telefone.response.TelefoneResponse;
import br.akd.svc.akadia.modules.web.empresa.models.dto.fiscal.response.ConfigFiscalEmpresaResponse;
import br.akd.svc.akadia.modules.web.empresa.models.entity.EmpresaEntity;
import lombok.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class EmpresaResponse {
    private UUID id;
    private String dataCadastro;
    private String horaCadastro;
    private String nome;
    private String razaoSocial;
    private String cnpj;
    private String endpoint;
    private String email;
    private String nomeFantasia;
    private String inscricaoEstadual;
    private String inscricaoMunicipal;
    private String segmentoEmpresaEnum;
    private ImagemResponse logo;
    private TelefoneResponse telefone;
    private EnderecoResponse endereco;
    private ConfigFiscalEmpresaResponse configFiscalEmpresa;
    private List<ChamadoResponse> chamados = new ArrayList<>();

    public EmpresaResponse buildFromEntity(EmpresaEntity empresaEntity) {
        return empresaEntity != null
                ? EmpresaResponse.builder()
                .id(empresaEntity.getId())
                .dataCadastro(empresaEntity.getDataCadastro())
                .horaCadastro(empresaEntity.getHoraCadastro())
                .nome(empresaEntity.getNome())
                .razaoSocial(empresaEntity.getRazaoSocial())
                .cnpj(empresaEntity.getCnpj())
                .endpoint(empresaEntity.getEndpoint())
                .email(empresaEntity.getEmail())
                .nomeFantasia(empresaEntity.getNomeFantasia())
                .inscricaoEstadual(empresaEntity.getInscricaoEstadual())
                .inscricaoMunicipal(empresaEntity.getInscricaoMunicipal())
                .segmentoEmpresaEnum(empresaEntity.getSegmentoEmpresaEnum().toString())
                .logo(new ImagemResponse()
                        .buildFromEntity(empresaEntity.getLogo()))
                .telefone(new TelefoneResponse()
                        .buildFromEntity(empresaEntity.getTelefone()))
                .endereco(new EnderecoResponse()
                        .buildFromEntity(empresaEntity.getEndereco()))
                .configFiscalEmpresa(new ConfigFiscalEmpresaResponse()
                        .buildFromEntity(empresaEntity.getConfigFiscalEmpresa()))
                .chamados(new ChamadoResponse()
                        .buildListFromEntity(empresaEntity.getChamados()))
                .build()
                : null;
    }
}
