package br.akd.svc.akadia.modules.web.clientesistema.proxy.models.error;

import lombok.*;
import org.springframework.util.ObjectUtils;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FeignErrorResponse {
    private List<FeignError> errors;

    public String retornaListaDeErrosComoUnicaString() {
        String mensagemErro = "";
        for (FeignError feignError : this.errors) {
            if (!ObjectUtils.isEmpty(mensagemErro))
                mensagemErro = mensagemErro + ", ";
            mensagemErro = mensagemErro + feignError.getDescription();
        }
        return mensagemErro;
    }
}
