package br.akd.svc.akadia.modules.backoffice.colaboradorinterno.entity;

import br.akd.svc.akadia.modules.backoffice.colaboradorinterno.enums.CargoInternoEnum;
import br.akd.svc.akadia.modules.backoffice.colaboradorinterno.enums.StatusAtividadeEnum;
import br.akd.svc.akadia.modules.global.telefone.entity.TelefoneEntity;
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
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "TB_AKD_COLABORADORINTERNO",
        uniqueConstraints = {
                @UniqueConstraint(name = "UK_CPF_CLIN", columnNames = {"STR_CPF_CLIN"}),
        })
public class ColaboradorInternoEntity {

    @Id
    @Type(type = "uuid-char")
    @GeneratedValue(generator = "UUID")
    @Comment("Chave primária do colaborador interno - UUID")
    @Column(name = "COD_COLABORADORINTERNO_CLIN", nullable = false, updatable = false)
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    private UUID id;

    @Comment("Data em que o cadastro do colaborador interno foi realizado")
    @Column(name = "DT_DATACADASTRO_CLIN", nullable = false, updatable = false, length = 10)
    private String dataCadastro;

    @Comment("Hora em que o cadastro do colaborador interno foi realizado")
    @Column(name = "HR_HORACADASTRO_CLIN", nullable = false, updatable = false, length = 18)
    private String horaCadastro;

    @Comment("Nome do colaborador interno")
    @Column(name = "STR_NOME_CLIN", nullable = false, length = 70)
    private String nome;

    @Comment("E-mail do colaborador interno")
    @Column(name = "EML_NOME_CLIN", nullable = false, length = 70)
    private String email;

    @Comment("Cpf do cliente interno")
    @Column(name = "STR_CPF_CLIN", nullable = false, length = 14)
    private String cpf;

    @Comment("Acesso ao sistema liberado")
    @Column(name = "BOL_ACESSOSISTEMALIBERADO_CLIN", nullable = false)
    private Boolean acessoSistemaLiberado = true;

    @Comment("Data de nascimento do colaborador interno")
    @Column(name = "DT_DATANASCIMENTO_CLIN", nullable = false, length = 10)
    private String dataNascimento;

    @Enumerated(EnumType.STRING)
    @Column(name = "ENM_CARGO_CLIN", nullable = false)
    @Comment("Cargo do colaborador: " +
            "0 - Suporte, " +
            "1 - Desenvolvedor, " +
            "2 - Marketing, " +
            "3 - Comercial, " +
            "4 - Gestão")
    private CargoInternoEnum cargoEnum;

    @Enumerated(EnumType.STRING)
    @Column(name = "ENM_STATUS_CLIN", nullable = false)
    @Comment("Status do colaborador: " +
            "0 - Ativo, " +
            "1 - Férias, " +
            "2 - Inativo")
    private StatusAtividadeEnum statusAtividadeEnum;

    @ToString.Exclude
    @Comment("Código do telefone do colaborador interno")
    @OneToOne(targetEntity = TelefoneEntity.class,
            orphanRemoval = true,
            cascade = CascadeType.ALL,
            fetch = FetchType.LAZY)
    private TelefoneEntity telefone;
}
