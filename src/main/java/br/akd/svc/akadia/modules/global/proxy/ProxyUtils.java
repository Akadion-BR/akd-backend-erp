package br.akd.svc.akadia.modules.global.proxy;

import br.akd.svc.akadia.exceptions.InternalErrorException;
import br.akd.svc.akadia.exceptions.InvalidRequestException;
import br.akd.svc.akadia.modules.global.proxy.enums.ProxyModuleEnum;
import br.akd.svc.akadia.modules.global.proxy.enums.ProxyOperationEnum;
import br.akd.svc.akadia.modules.web.clientesistema.proxy.models.error.FeignErrorResponse;
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
public class ProxyUtils {

    public void realizaValidacaoResponseAsaas(ResponseEntity<?> responseEntity,
                                              ProxyModuleEnum proxyModuleEnum,
                                              ProxyOperationEnum proxyOperationEnum) {
        if (responseEntity == null) {
            log.error("O valor retornado pela integradora na [{}] do(a) [{}] é nulo",
                    proxyOperationEnum.getDesc(), proxyModuleEnum.getDesc());
            throw new InternalErrorException(Constantes.ERRO_INTERNO);
        }

        if (responseEntity.getStatusCodeValue() != 200) {
            log.error("Ocorreu um erro de status de resposta no processo de [{}] do(a) [{}] na integradora " +
                            "de pagamentos. Corpo da requisição: {}",
                    proxyOperationEnum.getDesc(), proxyModuleEnum.getDesc(), responseEntity.getBody());
            throw new InternalErrorException(Constantes.ERRO_INTERNO);
        }

        if (responseEntity.getBody() == null) {
            log.error("O valor retornado pela integradora no processo de [{}] do(a) [{}}] é nulo",
                    proxyOperationEnum.getDesc(), proxyModuleEnum.getDesc());
            throw new InternalErrorException(Constantes.ERRO_INTERNO);
        }
    }

    public RuntimeException realizaTratamentoRetornoErroFeignException(FeignException feignException,
                                                                       ProxyModuleEnum proxyModuleEnum,
                                                                       ProxyOperationEnum proxyOperationEnum) throws JsonProcessingException {
        log.info("Método de tratamento de erro retornado pelo Feign Client acessado");

        log.info("Iniciando tentativa de conversão de erro em JSON string para objeto do " +
                "tipo FeignErrorResponse...");
        try {
            ObjectMapper mapper = new ObjectMapper();
            FeignErrorResponse feignErrorResponse = mapper.readValue(
                    feignException.contentUTF8(), FeignErrorResponse.class);
            return new InvalidRequestException(feignErrorResponse.retornaListaDeErrosComoUnicaString());
        } catch (UnrecognizedPropertyException unrecognizedPropertyException) {
            log.error("Ocorreu um erro interno durante o processo de [{}}] do(a) [{}] na integradora de " +
                    "pagamentos ASAAS: {}", proxyOperationEnum.getDesc(), proxyModuleEnum.getDesc(), unrecognizedPropertyException.getMessage());
            return new InternalErrorException(Constantes.ERRO_INTERNO);
        }
    }

}
