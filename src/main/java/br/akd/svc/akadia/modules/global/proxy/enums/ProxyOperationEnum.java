package br.akd.svc.akadia.modules.global.proxy.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ProxyOperationEnum {
    CRIACAO (0, "CRIAÇÃO"),
    CONSULTA (1, "CONSULTA"),
    ATUALIZACAO (2, "ATUALIZAÇÃO"),
    REMOCAO (3, "REMOÇÃO");

    private final int code;
    private final String desc;
}
