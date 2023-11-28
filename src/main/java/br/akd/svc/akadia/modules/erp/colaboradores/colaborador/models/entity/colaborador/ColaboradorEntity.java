package br.akd.svc.akadia.modules.erp.colaboradores.colaborador.models.entity.colaborador;

import br.akd.svc.akadia.modules.erp.colaboradores.acao.models.entity.AcaoEntity;
import br.akd.svc.akadia.modules.erp.colaboradores.acesso.models.entity.AcessoEntity;
import br.akd.svc.akadia.modules.erp.colaboradores.advertencia.models.entity.AdvertenciaEntity;
import br.akd.svc.akadia.modules.erp.colaboradores.colaborador.models.dto.colaborador.request.ColaboradorRequest;
import br.akd.svc.akadia.modules.erp.colaboradores.colaborador.models.entity.colaborador.id.ColaboradorId;
import br.akd.svc.akadia.modules.erp.colaboradores.colaborador.models.entity.configuracaoperfil.ConfiguracaoPerfilEntity;
import br.akd.svc.akadia.modules.erp.colaboradores.colaborador.models.entity.dispensa.DispensaEntity;
import br.akd.svc.akadia.modules.erp.colaboradores.colaborador.models.entity.expediente.ExpedienteEntity;
import br.akd.svc.akadia.modules.erp.colaboradores.colaborador.models.entity.ferias.FeriasEntity;
import br.akd.svc.akadia.modules.erp.colaboradores.colaborador.models.entity.ponto.PontoEntity;
import br.akd.svc.akadia.modules.erp.colaboradores.colaborador.models.enums.ModeloContratacaoEnum;
import br.akd.svc.akadia.modules.erp.colaboradores.colaborador.models.enums.ModeloTrabalhoEnum;
import br.akd.svc.akadia.modules.erp.colaboradores.colaborador.models.enums.StatusColaboradorEnum;
import br.akd.svc.akadia.modules.erp.colaboradores.colaborador.models.enums.TipoOcupacaoEnum;
import br.akd.svc.akadia.modules.global.objects.acessosistema.entity.AcessoSistemaEntity;
import br.akd.svc.akadia.modules.global.objects.arquivo.entity.ArquivoEntity;
import br.akd.svc.akadia.modules.global.objects.endereco.entity.EnderecoEntity;
import br.akd.svc.akadia.modules.global.objects.exclusao.entity.ExclusaoEntity;
import br.akd.svc.akadia.modules.global.objects.imagem.entity.ImagemEntity;
import br.akd.svc.akadia.modules.global.objects.telefone.entity.TelefoneEntity;
import br.akd.svc.akadia.modules.web.empresa.models.entity.EmpresaEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;
import org.hibernate.annotations.Comment;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.*;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Getter
@Setter
@Builder
@ToString
@DynamicUpdate
@NoArgsConstructor
@AllArgsConstructor
@IdClass(ColaboradorId.class)
@Table(name = "TB_AKD_COLABORADOR",
        uniqueConstraints = {
                @UniqueConstraint(name = "UK_MATRICULA_CLB", columnNames = {"COD_EMPRESA_CLB", "STR_MATRICULA_CLB"}),
        })
public class ColaboradorEntity {

    @Id
    @Type(type = "uuid-char")
    @GeneratedValue(generator = "UUID")
    @Comment("Chave primária do colaborador - UUID")
    @Column(name = "COD_COLABORADOR_CLB", nullable = false, updatable = false)
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    private UUID id;

    @Id
    @JsonIgnore
    @ToString.Exclude
    @ManyToOne(fetch = FetchType.LAZY)
    @Comment("Chave primária do colaborador - ID da empresa ao qual o colaborador faz parte")
    @JoinColumn(name = "COD_EMPRESA_CLB", referencedColumnName = "COD_EMPRESA_EMP", nullable = false, updatable = false)
    private EmpresaEntity empresa;

    @Comment("Data em que o cadastro do colaborador foi realizado")
    @Column(name = "DT_DATACADASTRO_CLB", nullable = false, updatable = false, length = 10)
    private String dataCadastro;

    @Comment("Hora em que o cadastro do colaborador foi realizado")
    @Column(name = "HR_HORACADASTRO_CLB", nullable = false, updatable = false, length = 18)
    private String horaCadastro;

    @Comment("Código de matrícula do colaborador")
    @Column(name = "STR_MATRICULA_CLB", nullable = false, updatable = false, length = 7)
    private String matricula;

    @Comment("Nome do colaborador")
    @Column(name = "STR_NOME_CLB", nullable = false, length = 70)
    private String nome;

    @Comment("Data de nascimento do colaborador")
    @Column(name = "DT_DATANASCIMENTO_CLB", length = 10)
    private String dataNascimento;

    @Comment("E-mail do colaborador")
    @Column(name = "EML_EMAIL_CLB", length = 70)
    private String email;

    @Comment("CPF ou CNPJ do colaborador")
    @Column(name = "STR_CPFCNPJ_CLB", length = 18)
    private String cpfCnpj;

    @Comment("Salário do colaborador")
    @Column(name = "DBL_SALARIO_CLB", scale = 2)
    private Double salario;

    @Comment("Data de entrada do colaborador na empresa")
    @Column(name = "DT_ENTRADAEMPRESA_CLB", length = 10)
    private String entradaEmpresa;

    @Comment("Data de saída do colaborador na empresa")
    @Column(name = "DT_SAIDAEMPRESA_CLB", length = 10)
    private String saidaEmpresa;

    @Comment("Ocupação do colaborador")
    @Column(name = "STR_OCUPACAO_CLB", length = 70)
    private String ocupacao;

    @Enumerated(EnumType.STRING)
    @Column(name = "ENM_TIPOOCUPACAO_CLB", nullable = false)
    @Comment("Tipo de ocupação do colaborador: " +
            "0 - Técnico Interno, " +
            "1 - Técnico Externo, " +
            "2 - Atendente, " +
            "3 - Gerente, " +
            "4 - Diretor, " +
            "5 - Financeiro, " +
            "6 - Contábil, " +
            "7 - Técnico, " +
            "8 - Administrativo, " +
            "9 - Marketing, " +
            "10 - Técnico de TI, " +
            "11 - Administrador, ")
    private TipoOcupacaoEnum tipoOcupacaoEnum;

    @Enumerated(EnumType.STRING)
    @Column(name = "ENM_MODELOCONTRATACAO_CLB", nullable = false)
    @Comment("Modelo de contratação do colaborador: " +
            "0 - CLT, " +
            "1 - PJ, " +
            "2 - Freelancer")
    private ModeloContratacaoEnum modeloContratacaoEnum;

    @Enumerated(EnumType.STRING)
    @Column(name = "ENM_MODELOTRABALHO_CLB", nullable = false)
    @Comment("Modelo de trabalho do colaborador: " +
            "0 - Presencial, " +
            "1 - Híbrido, " +
            "2 - Home office")
    private ModeloTrabalhoEnum modeloTrabalhoEnum;

    @Enumerated(EnumType.STRING)
    @Column(name = "ENM_STATUS_CLB", nullable = false)
    @Comment("Status do colaborador: " +
            "0 - Ativo, " +
            "1 - Afastado, " +
            "2 - Férias, " +
            "3 - Dispensado, " +
            "4 - Excluído, " +
            "5 - Freelancer")
    private StatusColaboradorEnum statusColaboradorEnum;

    @ToString.Exclude
    @Comment("Imagem de perfil do colaborador")
    @OneToOne(targetEntity = ImagemEntity.class,
            orphanRemoval = true,
            cascade = CascadeType.ALL,
            fetch = FetchType.LAZY)
    private ImagemEntity fotoPerfil;

    @ToString.Exclude
    @Comment("Contrato de contratação do colaborador")
    @OneToOne(targetEntity = ArquivoEntity.class,
            orphanRemoval = true,
            cascade = CascadeType.ALL,
            fetch = FetchType.LAZY)
    private ArquivoEntity contratoContratacao;

    @ToString.Exclude
    @Comment("Exclusão do colaborador")
    @OneToOne(targetEntity = ExclusaoEntity.class,
            orphanRemoval = true,
            cascade = CascadeType.ALL,
            fetch = FetchType.LAZY)
    private ExclusaoEntity exclusao;

    @ToString.Exclude
    @Comment("Acesso ao sistema do colaborador")
    @OneToOne(targetEntity = AcessoSistemaEntity.class,
            orphanRemoval = true,
            cascade = CascadeType.ALL,
            fetch = FetchType.LAZY)
    private AcessoSistemaEntity acessoSistema;

    @ToString.Exclude
    @Comment("Configuração de perfil do colaborador")
    @OneToOne(targetEntity = ConfiguracaoPerfilEntity.class,
            orphanRemoval = true,
            cascade = CascadeType.ALL,
            fetch = FetchType.LAZY)
    private ConfiguracaoPerfilEntity configuracaoPerfil;

    @ToString.Exclude
    @Comment("Endereço do colaborador")
    @OneToOne(targetEntity = EnderecoEntity.class,
            orphanRemoval = true,
            cascade = CascadeType.ALL,
            fetch = FetchType.LAZY)
    private EnderecoEntity endereco;

    @ToString.Exclude
    @Comment("Telefone do colaborador")
    @OneToOne(targetEntity = TelefoneEntity.class,
            orphanRemoval = true,
            cascade = CascadeType.ALL,
            fetch = FetchType.LAZY)
    private TelefoneEntity telefone;

    @ToString.Exclude
    @Comment("Expediente do colaborador")
    @OneToOne(targetEntity = ExpedienteEntity.class,
            orphanRemoval = true,
            cascade = CascadeType.ALL,
            fetch = FetchType.LAZY)
    private ExpedienteEntity expediente;

    @ToString.Exclude
    @Comment("Dispensa do colaborador")
    @OneToOne(targetEntity = DispensaEntity.class,
            orphanRemoval = true,
            cascade = CascadeType.ALL,
            fetch = FetchType.LAZY)
    private DispensaEntity dispensa;

    @Builder.Default
    @ToString.Exclude
    @Comment("Registros de ponto do colaborador")
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    @OneToMany(targetEntity = PontoEntity.class, orphanRemoval = true, cascade = CascadeType.ALL)
    private List<PontoEntity> pontos = new ArrayList<>();

    @Builder.Default
    @ToString.Exclude
    @Comment("Férias do colaborador")
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    @OneToMany(targetEntity = FeriasEntity.class, orphanRemoval = true, cascade = CascadeType.ALL)
    private List<FeriasEntity> historicoFerias = new ArrayList<>();

    @Builder.Default
    @ToString.Exclude
    @Comment("Advertências do colaborador")
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    @OneToMany(targetEntity = AdvertenciaEntity.class, orphanRemoval = true, cascade = CascadeType.ALL)
    private List<AdvertenciaEntity> advertencias = new ArrayList<>();

    @Builder.Default
    @ToString.Exclude
    @Comment("Registros de acessos do colaborador")
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    @OneToMany(targetEntity = AcessoEntity.class, orphanRemoval = true, cascade = CascadeType.ALL)
    private List<AcessoEntity> acessos = new ArrayList<>();

    @Builder.Default
    @ToString.Exclude
    @Comment("Ações do colaborador")
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    @OneToMany(targetEntity = AcaoEntity.class, orphanRemoval = true, cascade = CascadeType.ALL)
    private List<AcaoEntity> acoes = new ArrayList<>();

    public void addAcesso() {
        this.acessos.add(new AcessoEntity());
    }

    public void addAdvertencia(AdvertenciaEntity advertenciaEntity) {
        this.advertencias.add(advertenciaEntity);
    }

    public void removeAdvertencia(AdvertenciaEntity advertenciaEntity) {
        //TODO VALIDAR SE MÉTODO ESTÁ FUNCIONAL
        this.advertencias.remove(advertenciaEntity);
    }

    public ColaboradorEntity buildFromRequest(EmpresaEntity empresaSessao,
                                              String matriculaGerada,
                                              MultipartFile contratoContratacao,
                                              ColaboradorRequest colaboradorRequest) throws IOException {
        return colaboradorRequest != null
                ? ColaboradorEntity.builder()
                .empresa(empresaSessao)
                .dataCadastro(LocalDate.now().toString())
                .horaCadastro(LocalTime.now().toString())
                .matricula(matriculaGerada)
                .nome(colaboradorRequest.getNome())
                .dataNascimento(colaboradorRequest.getDataNascimento())
                .email(colaboradorRequest.getEmail())
                .cpfCnpj(colaboradorRequest.getCpfCnpj())
                .salario(colaboradorRequest.getSalario())
                .entradaEmpresa(colaboradorRequest.getEntradaEmpresa())
                .saidaEmpresa(colaboradorRequest.getSaidaEmpresa())
                .ocupacao(colaboradorRequest.getOcupacao())
                .tipoOcupacaoEnum(colaboradorRequest.getTipoOcupacaoEnum())
                .modeloContratacaoEnum(colaboradorRequest.getModeloContratacaoEnum())
                .modeloTrabalhoEnum(colaboradorRequest.getModeloTrabalhoEnum())
                .statusColaboradorEnum(colaboradorRequest.getStatusColaboradorEnum())
                .fotoPerfil(null)
                .contratoContratacao(new ArquivoEntity().buildFromMultiPartFile(contratoContratacao))
                .exclusao(null)
                .acessoSistema(new AcessoSistemaEntity().buildFromRequest(colaboradorRequest.getAcessoSistema()))
                .configuracaoPerfil(new ConfiguracaoPerfilEntity())
                .endereco(new EnderecoEntity().buildFromRequest(colaboradorRequest.getEndereco()))
                .telefone(new TelefoneEntity().buildFromRequest(colaboradorRequest.getTelefone()))
                .expediente(new ExpedienteEntity().buildFromRequest(colaboradorRequest.getExpediente()))
                .dispensa(null)
                .pontos(new ArrayList<>())
                .historicoFerias(new ArrayList<>())
                .advertencias(new ArrayList<>())
                .acessos(new ArrayList<>())
                .acoes(new ArrayList<>())
                .build()
                : null;
    }

    public ColaboradorEntity updateFromRequest(MultipartFile contratoContratacao,
                                               ColaboradorEntity colaboradorPreAtualizacao,
                                               ColaboradorRequest colaboradorRequest) throws IOException {
        return colaboradorRequest != null
                ? ColaboradorEntity.builder()
                .id(colaboradorPreAtualizacao.getId())
                .empresa(colaboradorPreAtualizacao.getEmpresa())
                .dataCadastro(colaboradorPreAtualizacao.getDataCadastro())
                .horaCadastro(colaboradorPreAtualizacao.getHoraCadastro())
                .matricula(colaboradorPreAtualizacao.getMatricula())
                .nome(colaboradorRequest.getNome())
                .dataNascimento(colaboradorRequest.getDataNascimento())
                .email(colaboradorRequest.getEmail())
                .cpfCnpj(colaboradorRequest.getCpfCnpj())
                .salario(colaboradorRequest.getSalario())
                .entradaEmpresa(colaboradorRequest.getEntradaEmpresa())
                .saidaEmpresa(colaboradorRequest.getSaidaEmpresa())
                .ocupacao(colaboradorRequest.getOcupacao())
                .tipoOcupacaoEnum(colaboradorRequest.getTipoOcupacaoEnum())
                .modeloContratacaoEnum(colaboradorRequest.getModeloContratacaoEnum())
                .modeloTrabalhoEnum(colaboradorRequest.getModeloTrabalhoEnum())
                .statusColaboradorEnum(colaboradorRequest.getStatusColaboradorEnum())
                .fotoPerfil(colaboradorPreAtualizacao.getFotoPerfil())
                .contratoContratacao(new ArquivoEntity().buildFromMultiPartFile(contratoContratacao))
                .exclusao(colaboradorPreAtualizacao.getExclusao())
                .acessoSistema(new AcessoSistemaEntity().updateFromRequest(colaboradorPreAtualizacao, colaboradorRequest))
                .configuracaoPerfil(colaboradorPreAtualizacao.getConfiguracaoPerfil())
                .endereco(new EnderecoEntity().buildFromRequest(colaboradorRequest.getEndereco()))
                .telefone(new TelefoneEntity().buildFromRequest(colaboradorRequest.getTelefone()))
                .expediente(new ExpedienteEntity().buildFromRequest(colaboradorRequest.getExpediente()))
                .dispensa(colaboradorPreAtualizacao.getDispensa())
                .pontos(colaboradorPreAtualizacao.getPontos())
                .historicoFerias(colaboradorPreAtualizacao.getHistoricoFerias())
                .advertencias(colaboradorPreAtualizacao.getAdvertencias())
                .acessos(colaboradorPreAtualizacao.getAcessos())
                .acoes(colaboradorPreAtualizacao.getAcoes())
                .build()
                : null;
    }
}
