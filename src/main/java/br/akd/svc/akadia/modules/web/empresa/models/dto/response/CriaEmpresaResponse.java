package br.akd.svc.akadia.modules.web.empresa.models.dto.response;

import br.akd.svc.akadia.modules.erp.colaboradores.colaborador.models.dto.colaborador.response.ColaboradorResponse;
import lombok.*;

import java.util.UUID;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class CriaEmpresaResponse {
    private UUID uuidClienteResponsavelEmpresa;
    private ColaboradorResponse colaboradorCriado;
    private EmpresaResponse empresaCriada;
}
