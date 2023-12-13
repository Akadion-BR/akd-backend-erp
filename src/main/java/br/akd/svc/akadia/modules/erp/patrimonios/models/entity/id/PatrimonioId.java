package br.akd.svc.akadia.modules.erp.patrimonios.models.entity.id;

import lombok.*;
import org.hibernate.annotations.Type;

import java.io.Serializable;
import java.util.UUID;

@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class PatrimonioId implements Serializable {
    @Type(type = "uuid-char")
    private UUID idClienteSistema;
    @Type(type = "uuid-char")
    private UUID idEmpresa;
    @Type(type = "uuid-char")
    private UUID id;
}
