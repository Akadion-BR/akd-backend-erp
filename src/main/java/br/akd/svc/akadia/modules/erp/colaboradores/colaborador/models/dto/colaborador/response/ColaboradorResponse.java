package br.akd.svc.akadia.modules.erp.colaboradores.colaborador.models.dto.colaborador.response;

import br.akd.svc.akadia.modules.erp.colaboradores.colaborador.models.dto.advertencia.AdvertenciaResponse;
import br.akd.svc.akadia.modules.erp.colaboradores.colaborador.models.dto.configuracaoperfil.ConfiguracaoPerfilResponse;
import br.akd.svc.akadia.modules.erp.colaboradores.colaborador.models.dto.dispensa.DispensaResponse;
import br.akd.svc.akadia.modules.erp.colaboradores.colaborador.models.dto.expediente.response.ExpedienteResponse;
import br.akd.svc.akadia.modules.erp.colaboradores.colaborador.models.dto.ferias.FeriasResponse;
import br.akd.svc.akadia.modules.erp.colaboradores.colaborador.models.dto.ponto.PontoResponse;
import br.akd.svc.akadia.modules.erp.colaboradores.colaborador.models.entity.colaborador.ColaboradorEntity;
import br.akd.svc.akadia.modules.global.objects.arquivo.dto.ArquivoResponse;
import br.akd.svc.akadia.modules.global.objects.endereco.dto.response.EnderecoResponse;
import br.akd.svc.akadia.modules.global.objects.imagem.response.ImagemResponse;
import br.akd.svc.akadia.modules.global.objects.telefone.response.TelefoneResponse;
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
public class ColaboradorResponse {
    private UUID id;
    private String dataCadastro;
    private String horaCadastro;
    private String matricula;
    private String nome;
    private String dataNascimento;
    private String email;
    private String cpfCnpj;
    private Double salario;
    private String entradaEmpresa;
    private String saidaEmpresa;
    private String ocupacao;
    private String tipoOcupacaoEnum;
    private String modeloContratacaoEnum;
    private String modeloTrabalhoEnum;
    private String statusColaboradorEnum;
    private ImagemResponse fotoPerfil;
    private ConfiguracaoPerfilResponse configuracaoPerfil;
    private ArquivoResponse contratoContratacao;
    private EnderecoResponse endereco;
    private TelefoneResponse telefone;
    private ExpedienteResponse expediente;
    private DispensaResponse dispensa;
    private List<PontoResponse> pontos = new ArrayList<>();
    private List<FeriasResponse> historicoFerias = new ArrayList<>();
    private List<AdvertenciaResponse> advertencias = new ArrayList<>();

    public ColaboradorResponse buildFromEntity(ColaboradorEntity colaborador) {
        return colaborador != null
                ? ColaboradorResponse.builder()
                .id(colaborador.getId())
                .dataCadastro(colaborador.getDataCadastro())
                .horaCadastro(colaborador.getHoraCadastro())
                .matricula(colaborador.getMatricula())
                .nome(colaborador.getNome())
                .dataNascimento(colaborador.getDataNascimento())
                .email(colaborador.getEmail())
                .cpfCnpj(colaborador.getCpfCnpj())
                .salario(colaborador.getSalario())
                .entradaEmpresa(colaborador.getEntradaEmpresa())
                .saidaEmpresa(colaborador.getSaidaEmpresa())
                .ocupacao(colaborador.getOcupacao())
                .tipoOcupacaoEnum(colaborador.getTipoOcupacaoEnum().getDesc())
                .modeloContratacaoEnum(colaborador.getModeloContratacaoEnum().getDesc())
                .modeloTrabalhoEnum(colaborador.getModeloTrabalhoEnum().getDesc())
                .statusColaboradorEnum(colaborador.getStatusColaboradorEnum().getDesc())
                .fotoPerfil(new ImagemResponse().buildFromEntity(colaborador.getFotoPerfil()))
                .configuracaoPerfil(new ConfiguracaoPerfilResponse().buildFromEntity(colaborador.getConfiguracaoPerfil()))
                .contratoContratacao(new ArquivoResponse().buildFromEntity(colaborador.getContratoContratacao()))
                .endereco(new EnderecoResponse().buildFromEntity(colaborador.getEndereco()))
                .telefone(new TelefoneResponse().buildFromEntity(colaborador.getTelefone()))
                .expediente(new ExpedienteResponse().buildFromEntity(colaborador.getExpediente()))
                .dispensa(new DispensaResponse().buildFromEntity(colaborador.getDispensa()))
                .pontos(new PontoResponse().buildResponseListFromEntities(colaborador.getPontos()))
                .historicoFerias(new FeriasResponse().buildResponseListFromEntities(colaborador.getHistoricoFerias()))
                .advertencias(new AdvertenciaResponse().buildResponseListFromEntities(colaborador.getAdvertencias()))
                .build()
                : null;
    }
}
