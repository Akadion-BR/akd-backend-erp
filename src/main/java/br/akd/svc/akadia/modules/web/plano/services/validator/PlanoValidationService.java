package br.akd.svc.akadia.modules.web.plano.services.validator;

import br.akd.svc.akadia.modules.web.plano.models.dto.request.PlanoRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class PlanoValidationService {

    public void realizaValidacoesParaAtualizacaoDePlano(PlanoRequest planoRequest) {
        log.info("Método responsável por acionar as validações de atualização do plano acessado");
    }

}
