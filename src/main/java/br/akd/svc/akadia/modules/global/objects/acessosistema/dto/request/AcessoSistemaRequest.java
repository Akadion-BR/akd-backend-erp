package br.akd.svc.akadia.modules.global.objects.acessosistema.dto.request;

import br.akd.svc.akadia.modules.erp.colaboradores.colaborador.models.enums.ModulosEnum;
import br.akd.svc.akadia.modules.erp.colaboradores.colaborador.models.enums.PermissaoEnum;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class AcessoSistemaRequest {
    private Boolean acessoSistemaAtivo = true;
    private String senha;
    private PermissaoEnum permissaoEnum;
    protected Set<ModulosEnum> privilegios = new HashSet<>();
}
