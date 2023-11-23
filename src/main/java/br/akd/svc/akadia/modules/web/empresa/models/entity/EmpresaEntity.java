package br.akd.svc.akadia.modules.web.empresa.models.entity;

import br.akd.svc.akadia.modules.backoffice.chamado.models.entity.ChamadoEntity;
import br.akd.svc.akadia.modules.global.endereco.entity.EnderecoEntity;
import br.akd.svc.akadia.modules.global.exclusao.entity.ExclusaoEntity;
import br.akd.svc.akadia.modules.global.imagem.entity.ImagemEntity;
import br.akd.svc.akadia.modules.global.telefone.entity.TelefoneEntity;
import br.akd.svc.akadia.modules.web.clientesistema.models.entity.ClienteSistemaEntity;
import br.akd.svc.akadia.modules.web.empresa.models.dto.request.EmpresaRequest;
import br.akd.svc.akadia.modules.web.empresa.models.entity.fiscal.ConfigFiscalEmpresaEntity;
import br.akd.svc.akadia.modules.web.empresa.models.enums.SegmentoEmpresaEnum;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;
import org.hibernate.annotations.Comment;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
//@IdClass(EmpresaId.class)
@Table(name = "TB_AKD_EMPRESA",
        uniqueConstraints = {
                @UniqueConstraint(name = "UK_RAZAOSOCIAL_EMPRESA", columnNames = {"STR_RAZAOSOCIAL_EMP"}),
                @UniqueConstraint(name = "UK_CNPJ_EMPRESA", columnNames = {"STR_CNPJ_EMP"}),
                @UniqueConstraint(name = "UK_ENDPOINT_EMPRESA", columnNames = {"STR_ENDPOINT_EMP"}),
                @UniqueConstraint(name = "STR_INSCRICAOESTADUAL_EMP", columnNames = {"STR_INSCRICAOESTADUAL_EMP"}), //TODO CHAVE ÚNICA COMPOSTA COM IE + ESTADO
                @UniqueConstraint(name = "STR_INSCRICAOMUNICIPAL_EMP", columnNames = {"STR_INSCRICAOMUNICIPAL_EMP"}), //TODO CHAVE ÚNICA COMPOSTA COM IM + MUNICIPIO
        })
public class EmpresaEntity {

    @Id
    @Type(type = "uuid-char")
    @GeneratedValue(generator = "UUID")
    @Comment("Chave primária da empresa - UUID")
    @Column(name = "COD_EMPRESA_EMP", nullable = false, updatable = false)
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    private UUID id;

    //    @Id
//    @JsonIgnore
//    @ToString.Exclude
//    @ManyToOne(fetch = FetchType.LAZY)
//    @Comment("Chave primária da empresa - ID da empresa ao qual o cliente faz parte")
//    @JoinColumn(name = "COD_CLIENTESISTEMA_EMP", referencedColumnName = "COD_CLIENTESISTEMA_CLS", nullable = false, updatable = false)
//    private ClienteSistemaEntity clienteSistema;

    @Comment("Data em que o cadastro da empresa foi realizado")
    @Column(name = "DT_DATACADASTRO_EMP", nullable = false, updatable = false, length = 10)
    private String dataCadastro;

    @Comment("Hora em que o cadastro da empresa foi realizado")
    @Column(name = "HR_HORACADASTRO_EMP", nullable = false, updatable = false, length = 18)
    private String horaCadastro;

    @Comment("Nome da empresa")
    @Column(name = "STR_NOME_EMP", nullable = false, length = 70)
    private String nome;

    @Comment("Razão social da empresa")
    @Column(name = "STR_RAZAOSOCIAL_EMP", nullable = false, updatable = false, length = 70)
    private String razaoSocial;

    @Comment("CNPJ da empresa")
    @Column(name = "STR_CNPJ_EMP", nullable = false, updatable = false, length = 18)
    private String cnpj;

    @Comment("Endpoint da empresa")
    @Column(name = "STR_ENDPOINT_EMP", nullable = false, length = 30)
    private String endpoint;

    @Comment("E-mail da empresa")
    @Column(name = "EML_EMAIL_EMP", nullable = false, length = 70)
    private String email;

    @Comment("Nome fantasia da empresa")
    @Column(name = "STR_NOMEFANTASIA_EMP", nullable = false, length = 70)
    private String nomeFantasia;

    @Comment("Inscrição estadual da empresa")
    @Column(name = "STR_INSCRICAOESTADUAL_EMP", length = 12)
    private String inscricaoEstadual;

    @Comment("Inscrição municipal da empresa")
    @Column(name = "STR_INSCRICAOMUNICIPAL_EMP", length = 12)
    private String inscricaoMunicipal;

    @ToString.Exclude
    @Comment("Código de exclusão da empresa")
    @OneToOne(targetEntity = ExclusaoEntity.class,
            orphanRemoval = true,
            cascade = CascadeType.ALL,
            fetch = FetchType.LAZY)
    private ExclusaoEntity exclusao;

    @ToString.Exclude
    @Comment("Código da imagem de perfil da empresa")
    @OneToOne(targetEntity = ImagemEntity.class,
            orphanRemoval = true,
            cascade = CascadeType.ALL,
            fetch = FetchType.LAZY)
    private ImagemEntity logo;

    @Enumerated(EnumType.STRING)
    @Comment("Tipo de segmento da empresa: 0 - Baterias automotivas")
    @Column(name = "ENM_SEGMENTOEMPRESA_EMP", nullable = false)
    private SegmentoEmpresaEnum segmentoEmpresaEnum;

    @ToString.Exclude
    @Comment("Código do telefone da empresa")
    @OneToOne(targetEntity = TelefoneEntity.class,
            orphanRemoval = true,
            cascade = CascadeType.ALL,
            fetch = FetchType.LAZY)
    private TelefoneEntity telefone;

    @ToString.Exclude
    @Comment("Código do endereço da empresa")
    @OneToOne(targetEntity = EnderecoEntity.class,
            orphanRemoval = true,
            cascade = CascadeType.ALL,
            fetch = FetchType.LAZY)
    private EnderecoEntity endereco;

    @ToString.Exclude
    @Comment("Código da configuração fiscal da empresa")
    @OneToOne(targetEntity = ConfigFiscalEmpresaEntity.class,
            orphanRemoval = true,
            cascade = CascadeType.ALL,
            fetch = FetchType.LAZY)
    private ConfigFiscalEmpresaEntity configFiscalEmpresa;

    //TODO VALIDAR
    @Builder.Default
    @ToString.Exclude
    @Comment("Chamados de suporte técnico da empresa")
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    @OneToMany(targetEntity = ChamadoEntity.class, orphanRemoval = true, cascade = CascadeType.ALL)
    private List<ChamadoEntity> chamados = new ArrayList<>();

    public EmpresaEntity buildFromRequest(ClienteSistemaEntity clienteSistema,
                                          EmpresaRequest empresaRequest) {
        return EmpresaEntity.builder()
//                .clienteSistema(clienteSistema)
                .dataCadastro(LocalDate.now().toString())
                .horaCadastro(LocalTime.now().toString())
                .nome(empresaRequest.getNome())
                .razaoSocial(empresaRequest.getRazaoSocial())
                .cnpj(empresaRequest.getCnpj())
                .endpoint(empresaRequest.getEndpoint())
                .email(empresaRequest.getEmail())
                .nomeFantasia(empresaRequest.getNomeFantasia())
                .inscricaoEstadual(empresaRequest.getInscricaoEstadual())
                .inscricaoMunicipal(empresaRequest.getInscricaoMunicipal())
                .exclusao(null)
                .logo(null)
                .segmentoEmpresaEnum(empresaRequest.getSegmentoEmpresaEnum())
                .telefone(new TelefoneEntity()
                        .buildFromRequest(empresaRequest.getTelefone()))
                .endereco(new EnderecoEntity()
                        .buildFromRequest(empresaRequest.getEndereco()))
                .configFiscalEmpresa(new ConfigFiscalEmpresaEntity()
                        .buildFromRequest(empresaRequest.getConfigFiscalEmpresa()))
                .chamados(new ArrayList<>())
                .build();
    }

    public EmpresaEntity updateFromRequest(EmpresaEntity empresaPreAtualizacao,
                                           EmpresaRequest empresaRequest) {
        return EmpresaEntity.builder()
                .id(empresaPreAtualizacao.getId())
//                .clienteSistema(empresaPreAtualizacao.getClienteSistema())
                .dataCadastro(empresaPreAtualizacao.getDataCadastro())
                .horaCadastro(empresaPreAtualizacao.getHoraCadastro())
                .nome(empresaRequest.getNome())
                .razaoSocial(empresaPreAtualizacao.getRazaoSocial())
                .cnpj(empresaPreAtualizacao.getCnpj())
                .endpoint(empresaRequest.getEndpoint())
                .email(empresaRequest.getEmail())
                .nomeFantasia(empresaRequest.getNomeFantasia())
                .inscricaoEstadual(empresaRequest.getInscricaoEstadual())
                .inscricaoMunicipal(empresaRequest.getInscricaoMunicipal())
                .exclusao(empresaPreAtualizacao.getExclusao())
                .logo(empresaPreAtualizacao.getLogo())
                .segmentoEmpresaEnum(empresaRequest.getSegmentoEmpresaEnum())
                .telefone(new TelefoneEntity()
                        .buildFromRequest(empresaRequest.getTelefone()))
                .endereco(new EnderecoEntity()
                        .buildFromRequest(empresaRequest.getEndereco()))
                .configFiscalEmpresa(new ConfigFiscalEmpresaEntity()
                        .buildFromRequest(empresaRequest.getConfigFiscalEmpresa()))
                .chamados(new ArrayList<>())
                .build();
    }
}


