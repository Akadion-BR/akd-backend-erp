package br.akd.svc.akadia.modules.web.clientesistema.models.entity;

import br.akd.svc.akadia.modules.global.objects.endereco.entity.EnderecoEntity;
import br.akd.svc.akadia.modules.global.objects.exclusao.entity.ExclusaoEntity;
import br.akd.svc.akadia.modules.global.objects.telefone.entity.TelefoneEntity;
import br.akd.svc.akadia.modules.web.clientesistema.models.dto.request.atualizacao.AtualizaClienteSistemaRequest;
import br.akd.svc.akadia.modules.web.clientesistema.models.dto.request.criacao.ClienteSistemaRequest;
import br.akd.svc.akadia.modules.web.empresa.models.entity.EmpresaEntity;
import br.akd.svc.akadia.modules.web.pagamento.models.entity.PagamentoSistemaEntity;
import br.akd.svc.akadia.modules.web.plano.models.entity.PlanoEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;
import org.hibernate.annotations.Comment;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

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
    @JsonIgnore
    @Type(type = "uuid-char")
    @GeneratedValue(generator = "UUID")
    @Comment("Chave primária do cliente do sistema - UUID")
    @Column(name = "COD_CLIENTESISTEMA_CLS", nullable = false, updatable = false)
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    private UUID id;

    @JsonIgnore
    @Comment("Código de identificação do cliente sistêmico na integradora ASAAS")
    @Column(name = "COD_ASAAS_CLS", nullable = false, updatable = false)
    private String codigoClienteAsaas;

    @JsonIgnore
    @Comment("Data em que o cadastro do cliente sistêmico foi realizado")
    @Column(name = "DT_DATACADASTRO_CLS", nullable = false, updatable = false, length = 10)
    private String dataCadastro;

    @JsonIgnore
    @Comment("Hora em que o cadastro do cliente sistêmico foi realizado")
    @Column(name = "HR_HORACADASTRO_CLS", nullable = false, updatable = false, length = 18)
    private String horaCadastro;

    @JsonIgnore
    @Comment("Data de nascimento do cliente sistêmico")
    @Column(name = "DT_DATANASCIMENTO_CLS", length = 10)
    private String dataNascimento;

    @JsonIgnore
    @Comment("E-mail do cliente sistêmico")
    @Column(name = "EML_EMAIL_CLS", nullable = false, length = 70)
    private String email;

    @JsonIgnore
    @Comment("Nome do cliente sistêmico")
    @Column(name = "STR_NOME_CLS", nullable = false, length = 70)
    private String nome;

    @JsonIgnore
    @Comment("Senha de acesso ao sistema do cliente sistêmico")
    @Column(name = "STR_SENHA_CLS", nullable = false, length = 25)
    private String senha;

    @JsonIgnore
    @Comment("CPF do cliente sistêmico")
    @Column(name = "STR_CPF_CLS", nullable = false, updatable = false, length = 14)
    private String cpf;

    @JsonIgnore
    @Comment("Saldo do cliente")
    @Column(name = "DBL_SALDO_CLS", nullable = false, scale = 2)
    private Double saldo = 0.0;

    @JsonIgnore
    @ToString.Exclude
    @Comment("Código de exclusão do cliente sistêmico")
    @OneToOne(targetEntity = PlanoEntity.class,
            orphanRemoval = true,
            cascade = CascadeType.ALL,
            fetch = FetchType.LAZY)
    private ExclusaoEntity exclusao;

    @JsonIgnore
    @ToString.Exclude
    @Comment("Código do plano do cliente sistêmico")
    @OneToOne(targetEntity = PlanoEntity.class,
            orphanRemoval = true,
            cascade = CascadeType.ALL,
            fetch = FetchType.LAZY)
    private PlanoEntity plano;

    @JsonIgnore
    @ToString.Exclude
    @Comment("Código do telefone da do cliente sistêmico")
    @OneToOne(targetEntity = TelefoneEntity.class,
            orphanRemoval = true,
            cascade = CascadeType.ALL,
            fetch = FetchType.LAZY)
    private TelefoneEntity telefone;

    @JsonIgnore
    @ToString.Exclude
    @Comment("Código do endereço do cliente sistêmico")
    @OneToOne(targetEntity = EnderecoEntity.class,
            orphanRemoval = true,
            cascade = CascadeType.ALL,
            fetch = FetchType.LAZY)
    private EnderecoEntity endereco;

    @JsonIgnore
    @ToString.Exclude
    @Builder.Default
    @Comment("Lista de pagamentos realizados pelo sistêmico")
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    @OneToMany(targetEntity = EmpresaEntity.class, orphanRemoval = true, cascade = CascadeType.ALL)
    private List<PagamentoSistemaEntity> pagamentos = new ArrayList<>();

    @JsonIgnore
    @ToString.Exclude
    @Builder.Default
    @Comment("Lista de empresas do cliente sistêmico")
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    @OneToMany(targetEntity = EmpresaEntity.class, orphanRemoval = true, cascade = CascadeType.ALL)
    private List<EmpresaEntity> empresas = new ArrayList<>();

    public void addEmpresa(EmpresaEntity empresa) {
        this.empresas.add(empresa);
    }

    public EmpresaEntity obtemEmpresaPorRazaoSocial(String razaoSocial) {
        return this.getEmpresas().stream()
                .filter(empresaFiltrada -> empresaFiltrada.getRazaoSocial().equals(razaoSocial))
                .collect(Collectors.toList()).get(0);
    }

    public ClienteSistemaEntity buildFromRequest(String idClienteAsaas,
                                                 ClienteSistemaRequest clienteSistemaRequest) {
        return clienteSistemaRequest != null
                ? ClienteSistemaEntity.builder()
                .id(null)
                .codigoClienteAsaas(idClienteAsaas)
                .dataCadastro(LocalDate.now().toString())
                .horaCadastro(LocalTime.now().toString())
                .dataNascimento(clienteSistemaRequest.getDataNascimento())
                .email(clienteSistemaRequest.getEmail())
                .nome(clienteSistemaRequest.getNome())
                .senha(clienteSistemaRequest.getSenha())
                .cpf(clienteSistemaRequest.getCpf())
                .saldo(0.00)
                .exclusao(null)
                .plano(new PlanoEntity().buildFromRequest(clienteSistemaRequest.getPlano()))
                .telefone(new TelefoneEntity().buildFromRequest(clienteSistemaRequest.getTelefone()))
                .endereco(new EnderecoEntity().buildFromRequest(clienteSistemaRequest.getEndereco()))
                .pagamentos(new ArrayList<>())
                .empresas(new ArrayList<>())
                .build()
                : null;
    }

    public ClienteSistemaEntity updateFromRequest(ClienteSistemaEntity clientePreAtualizacao,
                                                  AtualizaClienteSistemaRequest atualizaClienteSistemaRequest) {
        return atualizaClienteSistemaRequest != null
                ? ClienteSistemaEntity.builder()
                .id(clientePreAtualizacao.getId())
                .codigoClienteAsaas(clientePreAtualizacao.getCodigoClienteAsaas())
                .dataCadastro(clientePreAtualizacao.getDataCadastro())
                .horaCadastro(clientePreAtualizacao.getHoraCadastro())
                .dataNascimento(atualizaClienteSistemaRequest.getDataNascimento())
                .email(atualizaClienteSistemaRequest.getEmail())
                .nome(atualizaClienteSistemaRequest.getNome())
                .senha(atualizaClienteSistemaRequest.getSenha())
                .cpf(atualizaClienteSistemaRequest.getCpf())
                .saldo(clientePreAtualizacao.getSaldo())
                .exclusao(clientePreAtualizacao.getExclusao())
                .plano(clientePreAtualizacao.getPlano())
                .telefone(new TelefoneEntity().buildFromRequest(atualizaClienteSistemaRequest.getTelefone()))
                .endereco(new EnderecoEntity().buildFromRequest(atualizaClienteSistemaRequest.getEndereco()))
                .pagamentos(clientePreAtualizacao.getPagamentos())
                .empresas(clientePreAtualizacao.getEmpresas())
                .build()
                : null;
    }
}
