package br.akd.svc.akadia.modules.web.empresa.models.entity.fiscal.id;

import lombok.*;
import org.hibernate.annotations.Type;

import java.io.Serializable;
import java.util.UUID;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ConfigFiscalId implements Serializable {
    @Type(type = "uuid-char")
    private UUID empresa;
    private UUID id;
}