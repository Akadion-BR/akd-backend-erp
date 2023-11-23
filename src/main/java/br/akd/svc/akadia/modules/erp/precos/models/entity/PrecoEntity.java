package br.akd.svc.akadia.modules.erp.precos.models.entity;

import br.akd.svc.akadia.exceptions.InvalidRequestException;
import br.akd.svc.akadia.modules.erp.colaboradores.colaborador.models.entity.colaborador.ColaboradorEntity;
import br.akd.svc.akadia.modules.erp.precos.models.dto.request.PrecoRequest;
import br.akd.svc.akadia.modules.erp.precos.models.entity.id.PrecoId;
import br.akd.svc.akadia.modules.erp.precos.models.enums.TipoPrecoEnum;
import br.akd.svc.akadia.modules.web.empresa.models.entity.EmpresaEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.hibernate.annotations.Comment;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@Entity
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
@IdClass(PrecoId.class)
@Table(name = "TB_AKD_PRECO")
public class PrecoEntity {

    @Id
    @Type(type = "uuid-char")
    @GeneratedValue(generator = "UUID")
    @Comment("Chave primária do preço - UUID")
    @Column(name = "COD_PRECO_PRC", nullable = false, updatable = false)
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    private UUID id;

    @Id
    @JsonIgnore
    @ToString.Exclude
    @ManyToOne(fetch = FetchType.LAZY)
    @Comment("Chave primária do preço - ID da empresa ao qual o preço faz parte")
    @JoinColumn(name = "COD_EMPRESA_PRC", referencedColumnName = "COD_EMPRESA_EMP", nullable = false, updatable = false)
    private EmpresaEntity empresa;

    @Comment("Data em que o cadastro do preço foi realizado")
    @Column(name = "DT_CADASTRO_PRC", nullable = false, updatable = false, length = 10)
    private String dataCadastro;

    @Comment("Hora em que o cadastro do preço foi realizado")
    @Column(name = "HR_CADASTRO_PRC", nullable = false, updatable = false, length = 18)
    private String horaCadastro;

    @Comment("Valor do preço")
    @Column(name = "DBL_VALOR_PRC", nullable = false, scale = 2)
    private Double valor = 0.0;

    @Comment("Observação sobre o preço")
    @Column(name = "STR_OBSERVACAO_PRC", length = 120)
    private String observacao;

    @Comment("Nome do responsável pela persistência do objeto")
    @Column(name = "STR_RESPONSAVEL_PRC", nullable = false, length = 70)
    private String nomeResponsavel;

    @Enumerated(EnumType.STRING)
    @Column(name = "ENM_TIPO_PRC", nullable = false)
    @Comment("Tipo do preço em valores constantes: " +
            "1 - VAREJO (Varejo), " +
            "2 - ATACADO (Atacado)")
    private TipoPrecoEnum tipoPreco;

    //TODO TIRAR DAQUI
    @Transient
    public List<PrecoEntity> realizaValidacaoCriacaoListaPrecos(List<PrecoRequest> precosRequest,
                                                                ColaboradorEntity colaboradorLogado) {
        if (precosRequest.size() != 2)
            throw new InvalidRequestException("A lista de preços do produto deve possuir dois preços");

        boolean contemPrecoVarejo = false;
        boolean contemPrecoAtacado = false;

        for (PrecoRequest preco : precosRequest) {
            if (preco.getTipoPreco().equals(TipoPrecoEnum.VAREJO)) contemPrecoVarejo = true;
            if (preco.getTipoPreco().equals(TipoPrecoEnum.ATACADO)) contemPrecoAtacado = true;
        }

        if (!contemPrecoVarejo || !contemPrecoAtacado)
            throw new InvalidRequestException("A lista de preços do produto deve possuir um preço de atacado e um preço de varejo");

        List<PrecoEntity> precos = new ArrayList<>();

        for (PrecoRequest precoRequest : precosRequest) {

            PrecoEntity precoEntity = PrecoEntity.builder()
                    .dataCadastro(LocalDate.now().toString())
                    .horaCadastro(LocalDate.now().toString())
                    .valor(precoRequest.getValor())
                    .observacao(precoRequest.getObservacao())
                    .tipoPreco(precoRequest.getTipoPreco())
                    .nomeResponsavel(colaboradorLogado.getNome())
                    .empresa(colaboradorLogado.getEmpresa())
                    .build();

            precos.add(precoEntity);
        }

        return precos;
    }
}
