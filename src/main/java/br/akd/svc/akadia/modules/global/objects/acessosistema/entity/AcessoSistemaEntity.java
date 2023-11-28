package br.akd.svc.akadia.modules.global.objects.acessosistema.entity;


import br.akd.svc.akadia.modules.erp.colaboradores.colaborador.models.dto.colaborador.request.ColaboradorRequest;
import br.akd.svc.akadia.modules.erp.colaboradores.colaborador.models.entity.colaborador.ColaboradorEntity;
import br.akd.svc.akadia.modules.erp.colaboradores.colaborador.models.enums.ModulosEnum;
import br.akd.svc.akadia.modules.erp.colaboradores.colaborador.models.enums.PermissaoEnum;
import br.akd.svc.akadia.modules.global.objects.acessosistema.dto.request.AcessoSistemaRequest;
import lombok.*;
import org.hibernate.annotations.Comment;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.util.ObjectUtils;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "TB_AKD_ACESSOSISTEMA")
public class AcessoSistemaEntity {
    @Id
    @Type(type = "uuid-char")
    @GeneratedValue(generator = "UUID")
    @Comment("Chave primária do acesso ao sistema - UUID")
    @Column(name = "COD_ACESSOSISTEMA_ACS", nullable = false, updatable = false)
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    private UUID id;

    @Comment("Acesso ao sistema ativo")
    @Column(name = "BOL_ATIVO_ACS", nullable = false)
    private Boolean acessoSistemaAtivo = true;

    @Comment("Senha de acesso")
    @Column(name = "STR_SENHA_ACS", nullable = false, length = 70)
    private String senha;

    @Comment("Senha de acesso criptografada")
    @Column(name = "STR_SENHACRIPTOGRAFADA_ACS", nullable = false, length = 70)
    private String senhaCriptografada;

    @Enumerated(EnumType.STRING)
    @Column(name = "ENM_PERMISSAO_ACS", nullable = false)
    @Comment("Permissão de acesso: " +
            "0 - Leitura básica, " +
            "1 - Leitura avançada, " +
            "2 - Leitura básica com alteração, " +
            "3 - Leitura avançada com alteração")
    private PermissaoEnum permissaoEnum;

    @Builder.Default
    @ToString.Exclude
    @ElementCollection(fetch = FetchType.EAGER)
    @Comment("Privilégios de acesso ao sistema")
    @CollectionTable(name = "TB_AKD_ACESSOSISTEMA_PRIVILEGIOS")
    protected Set<ModulosEnum> privilegios = new HashSet<>();

    public AcessoSistemaEntity buildFromRequest(AcessoSistemaRequest acessoSistemaRequest) {
        return acessoSistemaRequest != null
                ? AcessoSistemaEntity.builder()
                .acessoSistemaAtivo(acessoSistemaRequest.getAcessoSistemaAtivo())
                .senha(acessoSistemaRequest.getSenha())
                .senhaCriptografada(criptografaSenhaAcesso(acessoSistemaRequest.getSenha()))
                .permissaoEnum(acessoSistemaRequest.getPermissaoEnum())
                .privilegios(acessoSistemaRequest.getPrivilegios())
                .build()
                : null;
    }

    public String criptografaSenhaAcesso(String senha) {
        return !ObjectUtils.isEmpty(senha)
                ? new BCryptPasswordEncoder().encode(senha)
                : null;
    }

    //TODO TESTAR SE ESTÁ FUNCIONAL
    public AcessoSistemaEntity updateFromRequest(ColaboradorEntity colaboradorPreAtualizacao,
                                                 ColaboradorRequest colaboradorNovo) {
        if (Boolean.FALSE.equals(colaboradorNovo.getAcessoSistema().getAcessoSistemaAtivo())) return
                AcessoSistemaEntity.builder()
                        .acessoSistemaAtivo(false)
                        .senha(null)
                        .senhaCriptografada(null)
                        .permissaoEnum(PermissaoEnum.LEITURA_BASICA)
                        .privilegios(new HashSet<>())
                        .build();
        else {
            return AcessoSistemaEntity.builder()
                    .senha(ObjectUtils.isEmpty(colaboradorNovo.getAcessoSistema().getSenha())
                            ? colaboradorPreAtualizacao.getAcessoSistema().getSenha()
                            : colaboradorNovo.getAcessoSistema().getSenha())
                    .senhaCriptografada(new BCryptPasswordEncoder().encode(colaboradorNovo.getAcessoSistema().getSenha()))
                    .acessoSistemaAtivo(colaboradorNovo.getAcessoSistema().getAcessoSistemaAtivo())
                    .permissaoEnum(colaboradorNovo.getAcessoSistema().getPermissaoEnum())
                    .privilegios(colaboradorNovo.getAcessoSistema().getPrivilegios() != null
                            ? new HashSet<>(colaboradorNovo.getAcessoSistema().getPrivilegios())
                            : new HashSet<>())
                    .build();
        }
    }
}
