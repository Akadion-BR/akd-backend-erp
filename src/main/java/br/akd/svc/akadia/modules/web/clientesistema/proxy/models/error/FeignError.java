package br.akd.svc.akadia.modules.web.clientesistema.proxy.models.error;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FeignError {
    String code;
    String description;
}
