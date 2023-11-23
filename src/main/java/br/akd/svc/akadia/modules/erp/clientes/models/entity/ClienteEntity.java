package br.akd.svc.akadia.modules.erp.clientes.models.entity;

import br.akd.svc.akadia.modules.erp.clientes.models.dto.request.ClienteRequest;
import br.akd.svc.akadia.modules.erp.clientes.models.entity.id.ClienteId;
import br.akd.svc.akadia.modules.erp.clientes.models.enums.StatusClienteEnum;
import br.akd.svc.akadia.modules.erp.clientes.models.enums.TipoPessoaEnum;
import br.akd.svc.akadia.modules.erp.colaboradores.colaborador.models.entity.colaborador.ColaboradorEntity;
import br.akd.svc.akadia.modules.global.endereco.entity.EnderecoEntity;
import br.akd.svc.akadia.modules.global.exclusao.entity.ExclusaoEntity;
import br.akd.svc.akadia.modules.global.telefone.entity.TelefoneEntity;
import br.akd.svc.akadia.modules.web.empresa.models.entity.EmpresaEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.hibernate.annotations.Comment;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

@Entity
@Getter
@Setter
@Builder
@ToString
@DynamicUpdate
@NoArgsConstructor
@AllArgsConstructor
@IdClass(ClienteId.class)
@Table(name = "TB_AKD_CLIENTE",
        uniqueConstraints = {
                @UniqueConstraint(name = "UK_CPFCNPJ_CLI", columnNames = {"COD_EMPRESA_CLI", "STR_CPFCNPJ_CLI"}),
                @UniqueConstraint(name = "UK_INSCRICAOESTADUAL_CLI", columnNames = {"COD_EMPRESA_CLI", "STR_INSCRICAOESTADUAL_CLI"})
        })
public class ClienteEntity {

    @Id
    @Type(type = "uuid-char")
    @GeneratedValue(generator = "UUID")
    @Comment("Chave primária do cliente - UUID")
    @Column(name = "COD_CLIENTE_CLI", nullable = false, updatable = false)
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    private UUID id;

    @Id
    @JsonIgnore
    @ToString.Exclude
    @ManyToOne(fetch = FetchType.LAZY)
    @Comment("Chave primária do cliente - ID da empresa ao qual o cliente faz parte")
    @JoinColumn(name = "COD_EMPRESA_CLI", referencedColumnName = "COD_EMPRESA_EMP", nullable = false, updatable = false)
    private EmpresaEntity empresa;

    @Comment("Data em que o cadastro do cliente foi realizado")
    @Column(name = "DT_CADASTRO_CLI", nullable = false, updatable = false, length = 10)
    private String dataCadastro;

    @Comment("Hora em que o cadastro do cliente foi realizado")
    @Column(name = "HR_CADASTRO_CLI", nullable = false, updatable = false, length = 18)
    private String horaCadastro;

    @Comment("Caso a pessoa seja FÍSICA, a data de nascimento poderá ser informada")
    @Column(name = "DT_NASCIMENTO_CLI", length = 10)
    private String dataNascimento;

    @Comment("Nome do cliente")
    @Column(name = "STR_NOME_CLI", nullable = false, length = 70)
    private String nome;

    @Comment("CPF ou CNPJ do cliente")
    @Column(name = "STR_CPFCNPJ_CLI", length = 18)
    private String cpfCnpj;

    @Comment("CPF ou CNPJ do cliente")
    @Column(name = "STR_INSCRICAOESTADUAL_CLI", length = 18)
    private String inscricaoEstadual;

    @Comment("E-mail do cliente")
    @Column(name = "EML_EMAIL_CLI", length = 70)
    private String email;

    @Comment("Responsável por persistir o cliente")
    @Column(name = "STR_NOMERESPONSAVEL_CLI", nullable = false, updatable = false, length = 70)
    private String nomeResponsavel;

    @Enumerated(EnumType.STRING)
    @Column(name = "ENM_STATUS_CLI", nullable = false)
    @Comment("Status atual do cliente: " +
            "0 - Comum, " +
            "1 - Em débito, " +
            "2 - Vip, " +
            "3 - Atenção")
    private StatusClienteEnum statusCliente;

    @Enumerated(EnumType.STRING)
    @Column(name = "ENM_TIPOPESSOA_CLI", nullable = false)
    @Comment("Tipo de pessoa do cliente: " +
            "0 - Física, " +
            "1 - Jurídica")
    private TipoPessoaEnum tipoPessoa;

    @ToString.Exclude
    @Comment("Código de exclusão do cliente")
    @OneToOne(targetEntity = ExclusaoEntity.class,
            orphanRemoval = true,
            cascade = CascadeType.ALL,
            fetch = FetchType.LAZY)
    private ExclusaoEntity exclusao;

    @ToString.Exclude
    @Comment("Código do endereço do cliente")
    @OneToOne(targetEntity = EnderecoEntity.class,
            orphanRemoval = true,
            cascade = CascadeType.ALL,
            fetch = FetchType.LAZY)
    private EnderecoEntity endereco;

    @ToString.Exclude
    @Comment("Código do telefone do cliente")
    @OneToOne(targetEntity = TelefoneEntity.class,
            orphanRemoval = true,
            cascade = CascadeType.ALL,
            fetch = FetchType.LAZY)
    private TelefoneEntity telefone;

    public ClienteEntity constroiClienteEntityParaCriacao(ColaboradorEntity colaboradorLogado,
                                                          ClienteRequest clienteRequest) {
        return ClienteEntity.builder()
                .empresa(colaboradorLogado.getEmpresa())
                .dataCadastro(LocalDate.now().toString())
                .horaCadastro(LocalTime.now().toString())
                .dataNascimento(clienteRequest.getDataNascimento())
                .nome(clienteRequest.getNome().toUpperCase())
                .cpfCnpj(clienteRequest.getCpfCnpj())
                .email(clienteRequest.getEmail())
                .statusCliente(clienteRequest.getStatusCliente())
                .tipoPessoa(clienteRequest.getTipoPessoa())
                .exclusao(null)
                .endereco(clienteRequest.getEndereco() == null
                        ? null
                        : new EnderecoEntity().buildFromRequest(clienteRequest.getEndereco()))
                .telefone(clienteRequest.getTelefone() == null
                        ? null
                        : new TelefoneEntity().buildFromRequest(clienteRequest.getTelefone()))
                .nomeResponsavel(colaboradorLogado.getNome())
                .build();
    }

    public ClienteEntity atualizaEntidadeComAtributosRequest(ClienteEntity clienteEncontrado,
                                                             ClienteRequest clienteAtualizado) {
        return ClienteEntity.builder()
                .empresa(clienteEncontrado.getEmpresa())
                .id(clienteEncontrado.getId())
                .dataCadastro(clienteEncontrado.getDataCadastro())
                .horaCadastro(clienteEncontrado.getHoraCadastro())
                .dataNascimento(clienteAtualizado.getDataNascimento())
                .nome(clienteAtualizado.getNome().toUpperCase())
                .cpfCnpj(clienteAtualizado.getCpfCnpj())
                .email(clienteAtualizado.getEmail())
                .statusCliente(clienteAtualizado.getStatusCliente())
                .tipoPessoa(clienteAtualizado.getTipoPessoa())
                .exclusao(clienteEncontrado.getExclusao())
                .endereco(clienteAtualizado.getEndereco() == null
                        ? null
                        : new EnderecoEntity().buildFromRequest(clienteAtualizado.getEndereco()))
                .telefone(clienteAtualizado.getEndereco() == null
                        ? null
                        : new TelefoneEntity().buildFromRequest(clienteAtualizado.getTelefone()))
                .nomeResponsavel(clienteEncontrado.getNomeResponsavel())
                .build();

    }
}
