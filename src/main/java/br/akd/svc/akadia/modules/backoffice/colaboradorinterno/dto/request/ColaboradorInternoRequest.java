package br.akd.svc.akadia.modules.backoffice.colaboradorinterno.dto.request;

import br.akd.svc.akadia.modules.backoffice.colaboradorinterno.enums.CargoInternoEnum;
import br.akd.svc.akadia.modules.backoffice.colaboradorinterno.enums.StatusAtividadeEnum;
import br.akd.svc.akadia.modules.global.telefone.request.TelefoneRequest;
import lombok.*;

@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ColaboradorInternoRequest {
    private String nome;
    private String email;
    private String cpf;
    private Boolean acessoSistemaLiberado = true;
    private String dataNascimento;
    private CargoInternoEnum cargoEnum;
    private StatusAtividadeEnum statusAtividadeEnum;
    private TelefoneRequest telefone;
}
