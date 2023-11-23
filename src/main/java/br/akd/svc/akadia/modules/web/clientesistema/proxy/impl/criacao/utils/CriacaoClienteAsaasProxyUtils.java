package br.akd.svc.akadia.modules.web.clientesistema.proxy.impl.criacao.utils;

import br.akd.svc.akadia.exceptions.InvalidRequestException;
import br.akd.svc.akadia.modules.web.clientesistema.proxy.models.response.ClienteAsaasResponse;
import br.akd.svc.akadia.modules.web.clientesistema.utils.ConstantesClienteSistema;
import br.akd.svc.akadia.utils.Constantes;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class CriacaoClienteAsaasProxyUtils {

    public void realizaValidacaoResponseAsaas(ResponseEntity<ClienteAsaasResponse> responseEntity) {
        if (responseEntity == null) {
            log.error("O valor retornado pela integradora na criação do cliente é nulo");
            throw new InvalidRequestException(Constantes.RETORNO_INTEGRADORA_NULO);
        }

        if (responseEntity.getStatusCodeValue() != 200) {
            log.error("Ocorreu um erro no processo de criação da cliente na integradora de pagamentos: {}",
                    responseEntity.getBody());
            throw new InvalidRequestException(ConstantesClienteSistema.ERRO_CRIACAO_CLIENTE_ASAAS
                    + responseEntity.getBody());
        }

        if (responseEntity.getBody() == null) {
            log.error("O valor retornado pela integradora na criação do cliente é nulo");
            throw new InvalidRequestException(Constantes.RETORNO_INTEGRADORA_NULO);
        }
    }
}
