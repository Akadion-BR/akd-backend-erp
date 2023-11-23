package br.akd.svc.akadia.modules.erp.colaboradores.colaborador.models.entity.colaborador.id;

import lombok.*;
import org.hibernate.annotations.Type;

import java.io.Serializable;
import java.util.UUID;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ColaboradorId implements Serializable {
    @Type(type = "uuid-char")
    private UUID empresa;
    private UUID id;
}
