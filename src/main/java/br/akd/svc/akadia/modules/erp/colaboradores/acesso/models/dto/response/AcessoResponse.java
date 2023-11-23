package br.akd.svc.akadia.modules.erp.colaboradores.acesso.models.dto.response;

import br.akd.svc.akadia.modules.erp.colaboradores.acesso.models.entity.AcessoEntity;
import br.akd.svc.akadia.modules.erp.colaboradores.colaborador.models.enums.ModulosEnum;
import br.akd.svc.akadia.modules.global.acessosistema.entity.AcessoSistemaEntity;
import lombok.*;
import org.hibernate.annotations.Comment;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class AcessoResponse {
    private UUID id;
    private String dataCadastro;
    private String horaCadastro;

    public AcessoResponse buildFromEntity(AcessoEntity acessoEntity) {
        return acessoEntity != null
                ? AcessoResponse.builder()
                .dataCadastro(acessoEntity.getDataCadastro())
                .horaCadastro(acessoEntity.getHoraCadastro())
                .build()
                : null;
    }
}
