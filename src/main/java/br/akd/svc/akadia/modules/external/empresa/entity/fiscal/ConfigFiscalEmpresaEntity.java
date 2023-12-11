package br.akd.svc.akadia.modules.external.empresa.entity.fiscal;

import br.akd.svc.akadia.modules.external.empresa.entity.fiscal.nfce.NfceConfigEntity;
import br.akd.svc.akadia.modules.external.empresa.entity.fiscal.nfe.NfeConfigEntity;
import br.akd.svc.akadia.modules.external.empresa.entity.fiscal.nfse.NfseConfigEntity;
import br.akd.svc.akadia.modules.external.empresa.enums.fiscal.OrientacaoDanfeEnum;
import br.akd.svc.akadia.modules.external.empresa.enums.fiscal.RegimeTributarioEnum;
import lombok.*;
import org.hibernate.annotations.Comment;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.UUID;

@Entity
@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "TB_AKD_CFGFISCAL")
public class ConfigFiscalEmpresaEntity {

    //TODO ADICIONAR VALIDAÇÕES DE DADOS CADASTRADOS DA EMPRESA PRA TIPOS DE NF HABILITADOS. POR EX: NFSE NÃO EXIGE IE CADASTRADA, AGORA NFE E NFCE EXIGEM

    @Id
    @Type(type = "uuid-char")
    @GeneratedValue(generator = "UUID")
    @Comment("Chave primária da configuração fiscal - UUID")
    @Column(name = "COD_CONFIGFISCAL_CFF")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    private UUID id;

    @Comment("Impostos discriminados na nota fiscal")
    @Column(name = "BOL_DISCRIMINAIMPOSTOS_CFF", nullable = false)
    private Boolean discriminaImpostos = false;

    @Comment("NFE Habilitada")
    @Column(name = "BOL_HABILITANFE_CFF", nullable = false)
    private Boolean habilitaNfe = false;

    @Comment("NFCE Habilitada")
    @Column(name = "BOL_HABILITANFCE_CFF", nullable = false)
    private Boolean habilitaNfce = false;

    @Comment("NFSE Habilitada")
    @Column(name = "BOL_HABILITANFSE_CFF", nullable = false)
    private Boolean habilitaNfse = false;

    @Comment("Envio de e-mail com nota fiscal ao destinatário habilitado")
    @Column(name = "BOL_HABILITAENVIOEMAILDESTINATARIO_CFF", nullable = false)
    private Boolean habilitaEnvioEmailDestinatario = false;

    @Comment("Exibe recibo na DANFE")
    @Column(name = "BOL_EXIBERECIBODANFE_CFF", nullable = false)
    private Boolean exibeReciboNaDanfe = false;

    @Comment("CNPJ da contabilidade")
    @Column(name = "STR_CNPJCONTABILIDADE_CFF", length = 18)
    private String cnpjContabilidade;

    @Comment("Certificado digital")
    @Column(name = "ARQ_CERTIFICADODIGITAL_CFF", nullable = false)
    private byte[] certificadoDigital;

    @Comment("CNPJ da contabilidade")
    @Column(name = "STR_SENHACERTIFICADODIGITAL_CFF", nullable = false, length = 50)
    private String senhaCertificadoDigital;

    @Enumerated(EnumType.STRING)
    @Comment("Orientação da DANFE: 0 - Retrato, 1 - Paisagem")
    @Column(name = "ENM_ORIENTACAODANFE_CFF", nullable = false)
    private OrientacaoDanfeEnum orientacaoDanfeEnum = OrientacaoDanfeEnum.PORTRAIT;

    @Enumerated(EnumType.STRING)
    @Comment("Regime tributário: " +
            "0 - Simples nacional, " +
            "1 - Simples nacional - Excesso de sublimite de receita bruta, " +
            "2 - Regime normal")
    @Column(name = "ENM_REGIMETRIBUTARIO_CFF", nullable = false)
    private RegimeTributarioEnum regimeTributarioEnum;

    @ToString.Exclude
    @Comment("Código da configuração de NFE")
    @OneToOne(targetEntity = NfeConfigEntity.class,
            orphanRemoval = true,
            cascade = CascadeType.ALL,
            fetch = FetchType.LAZY)
    @JoinColumn(name = "COD_NFECONFIG_CFF",
            referencedColumnName = "COD_NFECONFIG_NFECFG",
            foreignKey = @ForeignKey(name = "FK_NFECONFIG_CONFIGFISCAL"))
    private NfeConfigEntity nfeConfig;

    @ToString.Exclude
    @Comment("Código da configuração de NFCE")
    @OneToOne(targetEntity = NfceConfigEntity.class,
            orphanRemoval = true,
            cascade = CascadeType.ALL,
            fetch = FetchType.LAZY)
    @JoinColumn(name = "COD_NFCECONFIG_CFF",
            referencedColumnName = "COD_NFCECONFIG_NFCECFG",
            foreignKey = @ForeignKey(name = "FK_NFCECONFIG_CONFIGFISCAL"))
    private NfceConfigEntity nfceConfig;

    @ToString.Exclude
    @Comment("Código da configuração de NFSE")
    @OneToOne(targetEntity = NfseConfigEntity.class,
            orphanRemoval = true,
            cascade = CascadeType.ALL,
            fetch = FetchType.LAZY)
    @JoinColumn(name = "COD_NFSECONFIG_CFF",
            referencedColumnName = "COD_NFSECONFIG_NFSECFG",
            foreignKey = @ForeignKey(name = "FK_NFSECONFIG_CONFIGFISCAL"))
    private NfseConfigEntity nfseConfig;
}
