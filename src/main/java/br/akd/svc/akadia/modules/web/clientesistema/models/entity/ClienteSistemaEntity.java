package br.akd.svc.akadia.modules.web.clientesistema.models.entity;

import br.akd.svc.akadia.modules.global.endereco.entity.EnderecoEntity;
import br.akd.svc.akadia.modules.global.exclusao.entity.ExclusaoEntity;
import br.akd.svc.akadia.modules.global.telefone.entity.TelefoneEntity;
import br.akd.svc.akadia.modules.web.cartao.models.entity.CartaoEntity;
import br.akd.svc.akadia.modules.web.clientesistema.models.dto.request.ClienteSistemaRequest;
import br.akd.svc.akadia.modules.web.empresa.models.entity.EmpresaEntity;
import br.akd.svc.akadia.modules.web.pagamento.models.entity.PagamentoSistemaEntity;
import br.akd.svc.akadia.modules.web.plano.models.entity.PlanoEntity;
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
@Builder
@ToString
@Getter
@Setter
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
    @Comment("Chave primária do cliente do sistema - UUID")
    @Column(name = "COD_CLIENTESISTEMA_CLS", nullable = false, updatable = false)
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
    @Column(name = "STR_SENHA_CLS", nullable = false, length = 25)
    private String senha;

    @Comment("CPF do cliente sistêmico")
    @Column(name = "STR_CPF_CLS", nullable = false, updatable = false, length = 14)
    private String cpf;

    @Comment("Saldo do cliente")
    @Column(name = "DBL_SALDO_CLS", nullable = false, scale = 2)
    private Double saldo = 0.0;

    @ToString.Exclude
    @Comment("Código de exclusão do cliente sistêmico")
    @OneToOne(targetEntity = PlanoEntity.class,
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
    @Comment("Código do cartão do cliente sistêmico")
    @OneToOne(targetEntity = CartaoEntity.class,
            orphanRemoval = true,
            cascade = CascadeType.ALL,
            fetch = FetchType.LAZY)
    private CartaoEntity cartao;

    @ToString.Exclude
    @Builder.Default
    @Comment("Lista de pagamentos realizados pelo sistêmico")
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    @OneToMany(targetEntity = EmpresaEntity.class, orphanRemoval = true, cascade = CascadeType.ALL)
    private List<PagamentoSistemaEntity> pagamentos = new ArrayList<>();

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

    public ClienteSistemaEntity buildFromRequest(ClienteSistemaRequest clienteSistemaRequest) {
        return clienteSistemaRequest != null
                ? ClienteSistemaEntity.builder()
                .id(null)
                .codigoClienteAsaas(null)
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
                .cartao(null)
                .pagamentos(new ArrayList<>())
                .empresas(new ArrayList<>())
                .build()
                : null;
    }

    public ClienteSistemaEntity updateFromRequest(ClienteSistemaEntity clientePreAtualizacao,
                                                  ClienteSistemaRequest clienteRequest) {
        return clienteRequest != null
                ? ClienteSistemaEntity.builder()
                .id(clientePreAtualizacao.getId())
                .codigoClienteAsaas(clientePreAtualizacao.getCodigoClienteAsaas())
                .dataCadastro(clientePreAtualizacao.getDataCadastro())
                .horaCadastro(clientePreAtualizacao.getHoraCadastro())
                .dataNascimento(clienteRequest.getDataNascimento())
                .email(clienteRequest.getEmail())
                .nome(clienteRequest.getNome())
                .senha(clienteRequest.getSenha())
                .cpf(clienteRequest.getCpf())
                .saldo(clientePreAtualizacao.getSaldo())
                .exclusao(clientePreAtualizacao.getExclusao())
                .plano(clientePreAtualizacao.getPlano())
                .telefone(new TelefoneEntity().buildFromRequest(clienteRequest.getTelefone()))
                .endereco(new EnderecoEntity().buildFromRequest(clienteRequest.getEndereco()))
                .cartao(clientePreAtualizacao.getCartao())
                .pagamentos(clientePreAtualizacao.getPagamentos())
                .empresas(clientePreAtualizacao.getEmpresas())
                .build()
                : null;
    }
}
