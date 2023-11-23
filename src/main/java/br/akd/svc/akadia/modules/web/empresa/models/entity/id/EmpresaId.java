package br.akd.svc.akadia.modules.web.empresa.models.entity.id;

import lombok.*;
import org.hibernate.annotations.Type;

import java.io.Serializable;
import java.util.UUID;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class EmpresaId implements Serializable {
    @Type(type = "uuid-char")
    private UUID clienteSistema;
    private UUID id;
}
