package br.akd.svc.akadia.modules.erp.despesas.models.entity.id;


import br.akd.svc.akadia.modules.external.empresa.entity.id.EmpresaId;
import lombok.*;
import org.hibernate.annotations.Type;

import java.io.Serializable;
import java.util.UUID;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class DespesaId implements Serializable {
    @Type(type = "uuid-char")
    private EmpresaId empresa;
    private UUID id;
}
