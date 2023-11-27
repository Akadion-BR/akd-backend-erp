package br.akd.svc.akadia.modules.web.plano.services.crud.impl;

import br.akd.svc.akadia.modules.web.cartao.models.dto.request.CartaoRequest;
import br.akd.svc.akadia.modules.web.cartao.models.entity.CartaoEntity;
import br.akd.svc.akadia.modules.web.clientesistema.models.entity.ClienteSistemaEntity;
import br.akd.svc.akadia.modules.web.clientesistema.repository.impl.ClienteSistemaRepositoryImpl;
import br.akd.svc.akadia.modules.web.plano.models.dto.request.PlanoRequest;
import br.akd.svc.akadia.modules.web.plano.models.dto.response.PlanoResponse;
import br.akd.svc.akadia.modules.web.plano.models.entity.PlanoEntity;
import br.akd.svc.akadia.modules.web.plano.proxy.operations.atualizacao.impl.AtualizacaoPlanoAsaasProxyImpl;
import br.akd.svc.akadia.modules.web.plano.proxy.operations.remocao.impl.RemocaoPlanoAsaasProxyImpl;
import br.akd.svc.akadia.modules.web.plano.services.crud.PlanoService;
import br.akd.svc.akadia.modules.web.plano.services.validator.PlanoValidationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Slf4j
@Service
public class PlanoServiceImpl implements PlanoService {

    @Autowired
    PlanoValidationService planoValidationService;

    @Autowired
    AtualizacaoPlanoAsaasProxyImpl atualizacaoPlanoAsaasProxy;

    @Autowired
    RemocaoPlanoAsaasProxyImpl remocaoPlanoAsaasProxy;

    @Autowired
    ClienteSistemaRepositoryImpl clienteSistemaRepositoryImpl;

    @Override
    public PlanoResponse atualizaPlanoDoClienteSistemico(UUID idClienteSistema,
                                                         PlanoRequest planoRequest,
                                                         CartaoRequest cartaoRequest) {
        log.info("Método responsável por realizar a atualização de um plano de um cliente sistêmico buscado " +
                "por id acessado");

        log.info("Iniciando acesso ao método de busca de cliente sistêmico por id...");
        ClienteSistemaEntity clienteSistemicoEncontrado =
                clienteSistemaRepositoryImpl.implementaBuscaPorId(idClienteSistema);
        log.info("Cliente sistêmico encontrado com sucesso");

        log.info("Iniciando acesso ao método de validação do plano...");
        planoValidationService.realizaValidacoesParaAtualizacaoDePlano(planoRequest, cartaoRequest);
        log.info("Validações finalizadas com sucesso");

        log.info("Iniciando setagem de atributos atualizados no cliente sistêmico encontrado...");
        log.info("Realizando setagem de tipo de plano atualizada...");
        clienteSistemicoEncontrado.getPlano().setTipoPlanoEnum(planoRequest.getTipoPlanoEnum());
        log.info("Ok");
        log.info("Realizando setagem de forma de pagamento atualizada...");
        clienteSistemicoEncontrado.getPlano().setFormaPagamentoSistemaEnum(planoRequest.getFormaPagamentoSistemaEnum());
        log.info("Ok");
        log.info("Realizando setagem de cartão atualizado...");
        clienteSistemicoEncontrado.setCartao(new CartaoEntity().buildFromRequest(cartaoRequest));
        log.info("Ok");

        log.info("Iniciando persistência do cliente sistêmico atualizado...");
        ClienteSistemaEntity clienteSistemaPersistido =
                clienteSistemaRepositoryImpl.implementaPersistencia(clienteSistemicoEncontrado);
        log.info("Persistência do cliente sistêmico atualizada realizada com sucesso");

        log.info("Obtendo plano do cliente sistemico atualizado...");
        PlanoEntity planoClienteSistemico = clienteSistemaPersistido.getPlano();

        log.info("Iniciando acesso ao método de atualização do plano de assinatura na integradora asaas...");
        atualizacaoPlanoAsaasProxy.realizaAtualizacaoDePlanoDeAssinaturaNaIntegradoraAsaas(planoClienteSistemico);
        log.info("Plano atualizado na integradora com sucesso");

        log.info("Iniciando acesso ao método responsável por realizar a conversão de objeto do tipo PlanoEntity " +
                "para objeto do tipo PlanoResponse...");
        PlanoResponse planoResponse = new PlanoResponse().buildFromEntity(planoClienteSistemico);
        log.info("Conversão de tipagem realizada com sucesso");

        log.info("Atualização do plano do cliente sistêmico realizada com sucesso");
        return planoResponse;
    }

    @Override
    public PlanoResponse cancelaPlanoDoClienteSistemico(UUID idClienteSistema) {
        log.info("Método responsável por realizar o cancelamento de um plano de um cliente sistêmico buscado " +
                "por id acessado");

        log.info("Iniciando acesso ao método de busca de cliente sistêmico por id...");
        ClienteSistemaEntity clienteSistemicoEncontrado =
                clienteSistemaRepositoryImpl.implementaBuscaPorId(idClienteSistema);
        log.info("Cliente sistêmico encontrado com sucesso");

        log.info("Obtendo plano do cliente sistêmico encontrado...");
        PlanoEntity planoClienteSistemico = clienteSistemicoEncontrado.getPlano();

        log.info("Iniciando acesso ao método de cancelamento do plano de assinatura na integradora asaas...");
        remocaoPlanoAsaasProxy.realizaCancelamentoDePlanoDeAssinaturaNaIntegradoraAsaas(
                planoClienteSistemico.getCodigoAssinaturaAsaas());
        log.info("Plano cancelado na integradora com sucesso");

        log.info("Iniciando conversão de PlanoEntity para PlanoResponse...");
        PlanoResponse planoResponse = new PlanoResponse().buildFromEntity(planoClienteSistemico);
        log.info("Conversão de tipagem para PlanoResponse realizada com sucesso");

        log.info("Cancelamento do plano realizado com sucesso");
        return planoResponse;
    }
}
