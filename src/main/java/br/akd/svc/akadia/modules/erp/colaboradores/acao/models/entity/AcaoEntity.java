package br.akd.svc.akadia.modules.erp.colaboradores.acao.models.entity;

import br.akd.svc.akadia.modules.erp.colaboradores.acao.models.enums.TipoAcaoEnum;
import br.akd.svc.akadia.modules.erp.colaboradores.colaborador.models.enums.ModulosEnum;
import lombok.*;
import org.hibernate.annotations.Comment;
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
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "TB_AKD_ACAO")
public class AcaoEntity {

    @Id
    @Type(type = "uuid-char")
    @GeneratedValue(generator = "UUID")
    @Comment("Chave primária da ação - UUID")
    @Column(name = "COD_ACAO_ACA", nullable = false, updatable = false)
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    private UUID id;

    @Comment("UUID Do objeto que foi manipulado")
    @Column(name = "COD_OBJETO_ACA", updatable = false)
    private UUID idObjeto;

    @Comment("Data em que a ação foi realizada")
    @Column(name = "DT_DATACADASTRO_ACA", nullable = false, updatable = false, length = 10)
    private String dataCadastro;

    @Comment("Hora em que a ação foi realizada")
    @Column(name = "HR_HORACADASTRO_ACA", nullable = false, updatable = false, length = 18)
    private String horaCadastro;

    @Comment("Observação sobre a ação realizada")
    @Column(name = "STR_OBSERVACAO_ACA", nullable = false, updatable = false, length = 100)
    private String observacao;

    @Enumerated(EnumType.STRING)
    @Column(name = "ENM_MODULO_ACA", nullable = false, updatable = false)
    @Comment("Módulo da ação: " +
            "0 - Tela principal, " +
            "1 - Clientes, " +
            "2 - Vendas, " +
            "3 - Lançamentos, " +
            "4 - Estoque, " +
            "5 - Despesas, " +
            "6 - Fechamentos, " +
            "7 - Patrimônios, " +
            "8 - Fornecedores, " +
            "9 - Compras, " +
            "10 - Colaboradores, " +
            "11 - Preços")
    private ModulosEnum moduloEnum;

    @Enumerated(EnumType.STRING)
    @Column(name = "ENM_TIPO_ACA", nullable = false, updatable = false)
    @Comment("Módulo da ação: " +
            "0 - Criação, " +
            "1 - Alteração, " +
            "2 - Remoção, " +
            "3 - Remoção em massa, " +
            "4 - Relatório, " +
            "5 - Outro")
    private TipoAcaoEnum tipoAcaoEnum;

    public AcaoEntity criaNovaAcao(UUID idObjeto,
                                   ModulosEnum moduloEnum,
                                   TipoAcaoEnum tipoAcao,
                                   String observacao) {
        return AcaoEntity.builder()
                .idObjeto(idObjeto)
                .dataCadastro(LocalDate.now().toString())
                .horaCadastro(LocalTime.now().toString())
                .moduloEnum(moduloEnum)
                .tipoAcaoEnum(tipoAcao)
                .observacao(observacao)
                .build();
    }
}
