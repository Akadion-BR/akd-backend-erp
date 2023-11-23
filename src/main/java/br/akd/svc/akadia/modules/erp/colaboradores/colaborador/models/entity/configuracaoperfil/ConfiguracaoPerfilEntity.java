package br.akd.svc.akadia.modules.erp.colaboradores.colaborador.models.entity.configuracaoperfil;

import br.akd.svc.akadia.modules.erp.colaboradores.colaborador.models.enums.TemaTelaEnum;
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
@AllArgsConstructor
@Table(name = "TB_AKD_CONFIGURACAOPERFIL")
public class ConfiguracaoPerfilEntity {
    @Id
    @Type(type = "uuid-char")
    @GeneratedValue(generator = "UUID")
    @Comment("Chave primária de configuração de perfil do colaborador - UUID")
    @Column(name = "COD_CONFIGURACAOPERFIL_CFP", nullable = false, updatable = false)
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    private UUID id;

    @Comment("Data em que a última atualização da tabela foi realizada")
    @Column(name = "DT_ULTIMAATUALIZACAO_CFP", nullable = false, updatable = false, length = 10)
    private String dataUltimaAtualizacao;

    @Comment("Hora em que a última atualização da tabela foi realizada")
    @Column(name = "HR_ULTIMAATUALIZACAO_CFP", nullable = false, updatable = false, length = 18)
    private String horaUltimaAtualizacao;

    @Enumerated(EnumType.STRING)
    @Column(name = "ENM_TEMATELA_CFP", nullable = false)
    @Comment("Tema da tela a ser exibida no sistema: " +
            "0 - Tela clara, " +
            "1 - Tela escura")
    private TemaTelaEnum temaTelaEnum = TemaTelaEnum.TELA_CLARA;

    public ConfiguracaoPerfilEntity() {
        this.id = null;
        this.setDataUltimaAtualizacao(LocalDate.now().toString());
        this.setHoraUltimaAtualizacao(LocalTime.now().toString());
        this.setTemaTelaEnum(TemaTelaEnum.TELA_CLARA);
    }
}
