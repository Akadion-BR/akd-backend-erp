package br.akd.svc.akadia.modules.erp.colaboradores.colaborador.services.validator;

import br.akd.svc.akadia.exceptions.InvalidRequestException;
import br.akd.svc.akadia.modules.erp.colaboradores.colaborador.models.entity.colaborador.ColaboradorEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class ColaboradorValidation {

    public void validaSeColaboradorEstaExcluido(ColaboradorEntity colaborador,
                                                String mensagemCasoEstejaExcluido) {
        log.debug("Método de validação de colaborador excluído acessado");
        if (colaborador.getExclusao() != null) {
            log.debug("colaborador de id {}: Validação de colaborador já excluído falhou. Não é possível realizar operações " +
                    "em um colaborador que já foi excluído.", colaborador.getId());
            throw new InvalidRequestException(mensagemCasoEstejaExcluido);
        }
        log.debug("O colaborador de id {} não está excluído", colaborador.getId());
    }

}
