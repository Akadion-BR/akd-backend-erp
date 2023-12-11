package br.akd.svc.akadia.modules.backoffice.colaboradorinterno.dto.response;

import br.akd.svc.akadia.modules.backoffice.colaboradorinterno.enums.CargoInternoEnum;
import br.akd.svc.akadia.modules.backoffice.colaboradorinterno.enums.StatusAtividadeEnum;
import br.akd.svc.akadia.modules.global.objects.telefone.response.TelefoneResponse;
import lombok.*;

import java.util.UUID;

@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ColaboradorInternoResponse {
    private UUID id;
    private String nome;
    private String email;
    private String cpf;
    private Boolean acessoSistemaLiberado = true;
    private String dataNascimento;
    private CargoInternoEnum cargoEnum;
    private StatusAtividadeEnum statusAtividadeEnum;
    private TelefoneResponse telefone;
}
