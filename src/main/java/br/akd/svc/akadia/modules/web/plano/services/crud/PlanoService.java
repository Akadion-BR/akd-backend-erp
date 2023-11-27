package br.akd.svc.akadia.modules.web.plano.services.crud;

import br.akd.svc.akadia.modules.web.cartao.models.dto.request.CartaoRequest;
import br.akd.svc.akadia.modules.web.plano.models.dto.request.PlanoRequest;
import br.akd.svc.akadia.modules.web.plano.models.dto.response.PlanoResponse;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

public interface PlanoService {
    @Transactional
    PlanoResponse atualizaPlanoDoClienteSistemico(UUID idClienteSistema,
                                                  PlanoRequest planoRequest,
                                                  CartaoRequest cartaoRequest);

    @Transactional
    PlanoResponse cancelaPlanoDoClienteSistemico(UUID idClienteSistema);
}
