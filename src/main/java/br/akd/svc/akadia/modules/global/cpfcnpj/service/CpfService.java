package br.akd.svc.akadia.modules.global.cpfcnpj.service;

import br.akd.svc.akadia.exceptions.InvalidRequestException;
import br.akd.svc.akadia.utils.Constantes;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class CpfService {

    public void realizaValidacaoCpf(String cpf) {

        String cpfSemCaracteresEspeciais = cpf.replaceAll(
                Constantes.REGEX_CARACTERES_ESPECIAIS_CPFCNPJ, "");

        String digitosVerificadores = cpfSemCaracteresEspeciais.substring(9, 11);
        String digitos = cpfSemCaracteresEspeciais.substring(0, 9);

        realizaCalculoDigitoVerificador(
                10,
                String.valueOf(digitosVerificadores.charAt(0)),
                digitos
        );

        realizaCalculoDigitoVerificador(
                11,
                String.valueOf(digitosVerificadores.charAt(1)),
                digitos.concat(String.valueOf(digitosVerificadores.charAt(0)))
        );
    }

    public void realizaCalculoDigitoVerificador(int digitoCalculoIteracao,
                                                String digitoVerificador,
                                                String digitosCpf) {

        int somaValoresMultiplicacao = 0;

        for (int i = 0; i < digitosCpf.length(); i++) {
            int digitoAtual = Integer.parseInt(String.valueOf(digitosCpf.charAt(i)));
            somaValoresMultiplicacao += digitoAtual * digitoCalculoIteracao;
            digitoCalculoIteracao--;
        }

        int restoDivisaoDaSomaPorOnze = somaValoresMultiplicacao % 11;

        int onzeMenosRestoDaDivisao = (restoDivisaoDaSomaPorOnze == 0 || restoDivisaoDaSomaPorOnze == 1)
                ? 0
                : 11 - restoDivisaoDaSomaPorOnze;

        if (!Objects.equals(digitoVerificador, String.valueOf(onzeMenosRestoDaDivisao)))
            throw new InvalidRequestException("O CPF enviado é inválido");
    }

}
