package br.akd.svc.akadia.modules.web.clientesistema.services.crud.impl;

import br.akd.svc.akadia.modules.web.clientesistema.models.dto.request.ClienteSistemaRequest;
import br.akd.svc.akadia.modules.web.clientesistema.models.entity.ClienteSistemaEntity;
import br.akd.svc.akadia.modules.web.clientesistema.proxy.impl.atualizacao.AtualizacaoClienteAsaasProxyImpl;
import br.akd.svc.akadia.modules.web.clientesistema.proxy.impl.criacao.CriacaoClienteSistemaAsaasProxyImpl;
import br.akd.svc.akadia.modules.web.clientesistema.repository.impl.ClienteSistemaRepositoryImpl;
import br.akd.svc.akadia.modules.web.clientesistema.services.crud.ClienteSistemaService;
import br.akd.svc.akadia.modules.web.clientesistema.services.validator.ClienteSistemaValidationService;
import br.akd.svc.akadia.modules.web.plano.services.PlanoService;
import br.akd.svc.akadia.modules.web.proxy.asaas.responses.assinatura.AssinaturaResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Slf4j
@Service
public class ClienteSistemaServiceImpl implements ClienteSistemaService {
    @Autowired
    ClienteSistemaRepositoryImpl clienteSistemaRepositoryImpl;

    @Autowired
    PlanoService planoService;

    @Autowired
    ClienteSistemaValidationService clienteSistemaValidationService;

    @Autowired
    CriacaoClienteSistemaAsaasProxyImpl criacaoClienteSistemaAsaasProxy;

    @Autowired
    AtualizacaoClienteAsaasProxyImpl atualizacaoClienteAsaasProxy;

    //TODO TIRAR TRANSACTIONAL DO REPOSITORY IMPL E INSERIR NOS MÉTODOS DA INTERFACE SERVICE

    public ClienteSistemaEntity cadastraNovoCliente(ClienteSistemaRequest clienteSistemaRequest) {

        log.debug("Método de serviço de cadastro de novo cliente acessado. Cliente recebido: {}", clienteSistemaRequest);

        log.debug("Iniciando validações de dados...");
        clienteSistemaValidationService.realizaValidacoesParaNovoClienteSistemico(clienteSistemaRequest);

        log.debug("Iniciando construção do objeto ClienteSistemaEntity...");
        ClienteSistemaEntity clienteSistema = new ClienteSistemaEntity()
                .buildFromRequest(clienteSistemaRequest);
        log.debug("Objeto ClienteSistemaEntity construído: {}", clienteSistema);

        log.debug("Iniciando acesso ao método de cadastro de cliente na integradora de pagamentos...");
        clienteSistema.setCodigoClienteAsaas(criacaoClienteSistemaAsaasProxy
                .realizaCriacaoClienteAsaas(clienteSistemaRequest));

        log.debug("Iniciando acesso ao método de criação de assinatura na integradora de pagamentos...");
//        AssinaturaResponse assinaturaResponse = planoService
//                .criaAssinaturaAsaas(clienteSistema); //TODO AJUSTAR LÓGICA DE PROXY DE CRIAÇÃO DE NOVO PLANO NA INTEGRADORA
        AssinaturaResponse assinaturaResponse = null;

        log.debug("Setando código de assinatura Asaas ao plano do cliente...");
        clienteSistema.getPlano().setCodigoAssinaturaAsaas(assinaturaResponse.getId());

        if (assinaturaResponse.getCreditCard() != null)
            clienteSistema.getCartao().setTokenCartao(assinaturaResponse.getCreditCard().getCreditCardToken());

        log.debug("Iniciando acesso ao método de implementação de persistência do cliente...");
        ClienteSistemaEntity clientePersistido = clienteSistemaRepositoryImpl.implementaPersistencia(clienteSistema);

        //TODO CRIAR ROLLBACK NA INTEGRADORA CASO OCORRA EXCEPTION

        log.info("Criação do cliente realizada com sucesso");
        return clientePersistido;
    }

    public ClienteSistemaEntity atualizaDadosCliente(UUID uuidClienteSistema,
                                                     ClienteSistemaRequest clienteSistemaRequest) {

        //TODO IMPLEMENTAR VALIDAÇÃO DE NÃO PERMITIR ALTERAÇÃO DE ITEM EXCLUÍDO

        log.debug("Método de serviço de atualização de dados do cliente acessado");

        log.debug("Iniciando acesso ao método de implementação de busca de cliente por id...");
        ClienteSistemaEntity clienteSistema = clienteSistemaRepositoryImpl
                .implementaBuscaPorId(uuidClienteSistema);

        clienteSistemaValidationService.validaSeChavesUnicasJaExistemParaClienteSistemicoAtualizado(
                clienteSistemaRequest, clienteSistema);

        log.debug("Iniciando construção do objeto ClienteSistemaEntity...");
        ClienteSistemaEntity clienteAtualizado = new ClienteSistemaEntity()
                .updateFromRequest(clienteSistema, clienteSistemaRequest);
        log.debug("Objeto ClienteSistemaEntity construído com sucesso");

        log.debug("Iniciando acesso ao método de implementação de persistência do cliente...");
        ClienteSistemaEntity clientePosPersistencia = clienteSistemaRepositoryImpl
                .implementaPersistencia(clienteAtualizado);

        log.debug("Iniciando acesso ao método de atualização dos dados do cliente na integradora de pagamentos...");
        atualizacaoClienteAsaasProxy.realizaAtualizacaoClienteAsaas(
                clienteAtualizado.getCodigoClienteAsaas(), clienteSistemaRequest);

        //TODO CRIAR ROLLBACK NA INTEGRADORA CASO OCORRA EXCEPTION

        log.info("Atualização do cliente realizada com sucesso");
        return clientePosPersistencia;
    }
}
