package br.akd.svc.akadia.modules.web.clientesistema.proxy.utils;

import br.akd.svc.akadia.modules.global.objects.telefone.request.TelefoneRequest;

public class ClienteAsaasProxyUtils {

    ClienteAsaasProxyUtils() {
    }

    public static String obtemTelefoneFixoCliente(TelefoneRequest telefone) {
        if (telefone == null) return null;
        return telefone.obtemPrefixoComNumeroJuntos();
    }

    public static String obtemTelefoneCelularCliente(TelefoneRequest telefone) {
        if (telefone == null) return null;
        if (telefone.getNumero().toString().length() == 9)
            return telefone.obtemPrefixoComNumeroJuntos();
        return null;
    }
}
