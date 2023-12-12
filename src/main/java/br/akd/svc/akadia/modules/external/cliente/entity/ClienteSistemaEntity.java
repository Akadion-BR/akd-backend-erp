package br.akd.svc.akadia.modules.external.cliente.entity;

import br.akd.svc.akadia.modules.external.empresa.entity.EmpresaEntity;
import br.akd.svc.akadia.modules.external.pagamento.entity.PagamentoSistemaEntity;
import br.akd.svc.akadia.modules.external.plano.entity.PlanoEntity;
import br.akd.svc.akadia.modules.global.objects.endereco.entity.EnderecoEntity;
import br.akd.svc.akadia.modules.global.objects.exclusao.entity.ExclusaoEntity;
import br.akd.svc.akadia.modules.global.objects.telefone.entity.TelefoneEntity;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;
import org.hibernate.annotations.Comment;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Getter
@Setter
@Builder
@ToString
@DynamicUpdate
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "TB_AKD_CLIENTESISTEMA",
        uniqueConstraints = {
                @UniqueConstraint(name = "UK_CODIGOASAAS_CLIENTESISTEMA", columnNames = {"COD_ASAAS_CLS"}),
                @UniqueConstraint(name = "UK_EMAIL_CLIENTESISTEMA", columnNames = {"EML_EMAIL_CLS"}),
                @UniqueConstraint(name = "UK_CPF_CLIENTESISTEMA", columnNames = {"STR_CPF_CLS"}),
        })
public class ClienteSistemaEntity {

    @Id
    @Type(type = "uuid-char")
    @GeneratedValue(generator = "UUID")
    @Column(name = "COD_CLIENTESISTEMA_CLS")
    @Comment("Chave primária do cliente do sistema - UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    private UUID id;

    @Comment("Código de identificação do cliente sistêmico na integradora ASAAS")
    @Column(name = "COD_ASAAS_CLS", nullable = false, updatable = false)
    private String codigoClienteAsaas;

    @Comment("Data em que o cadastro do cliente sistêmico foi realizado")
    @Column(name = "DT_DATACADASTRO_CLS", nullable = false, updatable = false, length = 10)
    private String dataCadastro;

    @Comment("Hora em que o cadastro do cliente sistêmico foi realizado")
    @Column(name = "HR_HORACADASTRO_CLS", nullable = false, updatable = false, length = 18)
    private String horaCadastro;

    @Comment("Data de nascimento do cliente sistêmico")
    @Column(name = "DT_DATANASCIMENTO_CLS", length = 10)
    private String dataNascimento;

    @Comment("E-mail do cliente sistêmico")
    @Column(name = "EML_EMAIL_CLS", nullable = false, length = 70)
    private String email;

    @Comment("Nome do cliente sistêmico")
    @Column(name = "STR_NOME_CLS", nullable = false, length = 70)
    private String nome;

    @Comment("Senha de acesso ao sistema do cliente sistêmico")
    @Column(name = "STR_SENHA_CLS", nullable = false, length = 72)
    private String senha;

    @Comment("CPF do cliente sistêmico")
    @Column(name = "STR_CPF_CLS", nullable = false, updatable = false, length = 14)
    private String cpf;

    @Builder.Default
    @Comment("Saldo do cliente")
    @Column(name = "DBL_SALDO_CLS", nullable = false, scale = 2)
    private Double saldo = 0.0;

    @ToString.Exclude
    @Comment("Código de exclusão do cliente sistêmico")
    @OneToOne(targetEntity = ExclusaoEntity.class,
            orphanRemoval = true,
            cascade = CascadeType.ALL,
            fetch = FetchType.LAZY)
    private ExclusaoEntity exclusao;

    @ToString.Exclude
    @Comment("Código do plano do cliente sistêmico")
    @OneToOne(targetEntity = PlanoEntity.class,
            orphanRemoval = true,
            cascade = CascadeType.ALL,
            fetch = FetchType.LAZY)
    private PlanoEntity plano;

    @ToString.Exclude
    @Comment("Código do telefone da do cliente sistêmico")
    @OneToOne(targetEntity = TelefoneEntity.class,
            orphanRemoval = true,
            cascade = CascadeType.ALL,
            fetch = FetchType.LAZY)
    private TelefoneEntity telefone;

    @ToString.Exclude
    @Comment("Código do endereço do cliente sistêmico")
    @OneToOne(targetEntity = EnderecoEntity.class,
            orphanRemoval = true,
            cascade = CascadeType.ALL,
            fetch = FetchType.LAZY)
    private EnderecoEntity endereco;

    @ToString.Exclude
    @Builder.Default
    @Comment("Lista de pagamentos realizados pelo sistêmico")
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    @OneToMany(targetEntity = PagamentoSistemaEntity.class, orphanRemoval = true, cascade = CascadeType.ALL)
    private List<PagamentoSistemaEntity> pagamentos = new ArrayList<>();

    @ToString.Exclude
    @Builder.Default
    @Comment("Lista de empresas do cliente sistêmico")
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    @OneToMany(targetEntity = EmpresaEntity.class, orphanRemoval = true, cascade = CascadeType.ALL)
    private List<EmpresaEntity> empresas = new ArrayList<>();
}
