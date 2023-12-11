package br.akd.svc.akadia.modules.erp.clientes.models.entity.id;

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
public class ClienteId implements Serializable {
    private EmpresaId empresa;
    @Type(type = "uuid-char")
    private UUID id;
}
