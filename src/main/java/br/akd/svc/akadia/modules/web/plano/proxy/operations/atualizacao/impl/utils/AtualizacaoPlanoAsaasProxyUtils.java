package br.akd.svc.akadia.modules.web.plano.proxy.operations.atualizacao.impl.utils;

import br.akd.svc.akadia.exceptions.InternalErrorException;
import br.akd.svc.akadia.exceptions.InvalidRequestException;
import br.akd.svc.akadia.modules.web.clientesistema.proxy.models.error.FeignErrorResponse;
import br.akd.svc.akadia.modules.web.plano.proxy.operations.atualizacao.models.response.AtualizaAssinaturaAsaasResponse;
import br.akd.svc.akadia.utils.Constantes;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.exc.UnrecognizedPropertyException;
import feign.FeignException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class AtualizacaoPlanoAsaasProxyUtils {
    AtualizacaoPlanoAsaasProxyUtils() {
    }

    public void realizaValidacaoResponseAsaas(ResponseEntity<AtualizaAssinaturaAsaasResponse> responseEntity) {
        if (responseEntity == null) {
            log.error("O valor retornado pela integradora na atualização do plano é nulo");
            throw new InternalErrorException(Constantes.ERRO_INTERNO);
        }

        if (responseEntity.getStatusCodeValue() != 200) {
            log.error("Ocorreu um erro de status de resposta no processo de atualização do plano na integradora de " +
                    "pagamentos: {}", responseEntity.getBody());
            throw new InternalErrorException(Constantes.ERRO_INTERNO);
        }

        if (responseEntity.getBody() == null) {
            log.error("O valor retornado pela integradora na atualização do plano é nulo");
            throw new InternalErrorException(Constantes.ERRO_INTERNO);
        }
    }

    public RuntimeException realizaTratamentoRetornoErroFeignException(FeignException feignException)
            throws JsonProcessingException {
        log.info("Método de tratamento de erro retornado pelo Feign Client acessado");

        log.info("Iniciando tentativa de conversão de erro em JSON string para objeto do " +
                "tipo FeignErrorResponse...");
        try {
            ObjectMapper mapper = new ObjectMapper();
            FeignErrorResponse feignErrorResponse = mapper.readValue(
                    feignException.contentUTF8(), FeignErrorResponse.class);
            return new InvalidRequestException(feignErrorResponse.retornaListaDeErrosComoUnicaString());
        } catch (UnrecognizedPropertyException unrecognizedPropertyException) {
            log.error("Ocorreu um erro interno durante a atualização do plano na integradora de " +
                    "pagamentos ASAAS: {}", unrecognizedPropertyException.getMessage());
            return new InternalErrorException(Constantes.ERRO_INTERNO);
        }
    }
}
