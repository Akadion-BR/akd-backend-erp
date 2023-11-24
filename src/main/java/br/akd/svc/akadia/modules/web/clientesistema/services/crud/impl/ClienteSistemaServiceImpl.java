package br.akd.svc.akadia.modules.web.clientesistema.services.crud.impl;

import br.akd.svc.akadia.exceptions.InternalErrorException;
import br.akd.svc.akadia.modules.web.clientesistema.models.dto.request.atualizacao.AtualizaClienteSistemaRequest;
import br.akd.svc.akadia.modules.web.clientesistema.models.dto.request.criacao.ClienteSistemaRequest;
import br.akd.svc.akadia.modules.web.clientesistema.models.dto.response.ClienteSistemaResponse;
import br.akd.svc.akadia.modules.web.clientesistema.models.entity.ClienteSistemaEntity;
import br.akd.svc.akadia.modules.web.clientesistema.proxy.impl.atualizacao.AtualizacaoClienteAsaasProxyImpl;
import br.akd.svc.akadia.modules.web.clientesistema.proxy.impl.criacao.CriacaoClienteSistemaAsaasProxyImpl;
import br.akd.svc.akadia.modules.web.clientesistema.repository.impl.ClienteSistemaRepositoryImpl;
import br.akd.svc.akadia.modules.web.clientesistema.services.crud.ClienteSistemaService;
import br.akd.svc.akadia.modules.web.clientesistema.services.validator.ClienteSistemaValidationService;
import br.akd.svc.akadia.modules.web.plano.proxy.operations.criacao.CriacaoPlanoAsaasProxyImpl;
import br.akd.svc.akadia.modules.web.plano.proxy.operations.remocao.impl.RemocaoPlanoAsaasProxyImpl;
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
    ClienteSistemaValidationService clienteSistemaValidationService;

    @Autowired
    CriacaoClienteSistemaAsaasProxyImpl criacaoClienteSistemaAsaasProxy;

    @Autowired
    AtualizacaoClienteAsaasProxyImpl atualizacaoClienteAsaasProxy;

    @Autowired
    CriacaoPlanoAsaasProxyImpl criacaoPlanoAsaasProxy;

    @Autowired
    RemocaoPlanoAsaasProxyImpl remocaoPlanoAsaasProxy;

    @Override
    public ClienteSistemaResponse cadastraNovoCliente(ClienteSistemaRequest clienteSistemaRequest) {

        log.info("Método de serviço de cadastro de novo cliente acessado");

        log.info("Iniciando validações de dados...");
        clienteSistemaValidationService.realizaValidacoesParaNovoClienteSistemico(clienteSistemaRequest);

        log.info("Iniciando acesso ao método de implementação da lógica de criação de um cliente sistêmico na integradora asaas...");
        String idClienteAsaas = criacaoClienteSistemaAsaasProxy.realizaCriacaoClienteAsaas(clienteSistemaRequest);
        log.info("Método de criação de um cliente sistêmico na integradora asaas finalizado com sucesso");

        log.info("Iniciando construção do objeto ClienteSistemaEntity...");
        ClienteSistemaEntity clienteSistema = new ClienteSistemaEntity()
                .buildFromRequest(idClienteAsaas, clienteSistemaRequest);
        log.info("Objeto ClienteSistemaEntity com sucesso");

        log.info("Iniciando acesso ao método de criação de assinatura na integradora de pagamentos...");
        String idAssinaturaAsaas = criacaoPlanoAsaasProxy.realizaCriacaoDePlanoDeAssinaturaNaIntegradoraAsaas(
                clienteSistemaRequest.getPlano(), clienteSistema.getCodigoClienteAsaas());
        log.info("Método de criação de nova assinatura na integradora asaas realizado com sucesso");

        log.info("Setando código de assinatura Asaas ao plano do cliente sistêmico...");
        clienteSistema.getPlano().setCodigoAssinaturaAsaas(idAssinaturaAsaas);
        log.info("Código de assinatura asaas setada no plano do cliente sistêmico com sucesso");

        ClienteSistemaEntity clientePersistido;
        try {
            log.info("Iniciando acesso ao método de implementação de persistência do cliente...");
            clientePersistido = clienteSistemaRepositoryImpl.implementaPersistencia(clienteSistema);
            log.info("Persistência do cliente sistêmico realizada com sucesso");
        } catch (Exception e) {
            log.error("Ocorreu um erro durante o processo de persistência do plano: {}", e.getMessage());

            log.info("Iniciando acesso ao método de cancelamento do plano na integradora ASAAS " +
                    "para realização de rollback...");
            remocaoPlanoAsaasProxy.realizaCancelamentoDePlanoDeAssinaturaNaIntegradoraAsaas(idAssinaturaAsaas);

            log.info("Rollback do plano na integradora ASAAS finalizado com sucesso");
            throw new InternalErrorException("Ocorreu um erro durante a tentativa de persistência do plano. " +
                    "Erro: " + e.getMessage());
        }

        log.info("Iniciando acesso ao método responsável por realizar a conversão de objeto do tipo " +
                "ClienteSistemaEntity para objeto do tipo ClienteSistemaResponse...");
        ClienteSistemaResponse clienteSistemaResponse = new ClienteSistemaResponse()
                .buildFromEntity(clientePersistido);
        log.info("Conversão de tipagem realizada com sucesso");

        log.info("Criação do cliente sistêmico realizada com sucesso. Retornando dados...");
        return clienteSistemaResponse;

    }

    @Override
    public ClienteSistemaResponse atualizaDadosCliente(UUID uuidClienteSistema,
                                                       AtualizaClienteSistemaRequest atualizaClienteSistemaRequest) {
        log.info("Método de serviço de atualização de dados do cliente sistêmico acessado");

        log.info("Iniciando acesso ao método de implementação de busca de cliente sistêmico por id...");
        ClienteSistemaEntity clienteSistema = clienteSistemaRepositoryImpl
                .implementaBuscaPorId(uuidClienteSistema);
        log.info("Busca de cliente sistêmico por id realizada com sucesso");

        log.info("Iniciando acesso ao método de validação dos atributos recebidos...");
        clienteSistemaValidationService.validaSeChavesUnicasJaExistemParaClienteSistemicoAtualizado(
                atualizaClienteSistemaRequest, clienteSistema);
        log.info("Validação do método de atualização de cliente sistêmico realizada com sucesso");

        log.info("Iniciando construção do objeto ClienteSistemaEntity...");
        ClienteSistemaEntity clienteAtualizado = new ClienteSistemaEntity()
                .updateFromRequest(clienteSistema, atualizaClienteSistemaRequest);
        log.info("Objeto ClienteSistemaEntity construído com sucesso");

        log.info("Iniciando acesso ao método de implementação de persistência do cliente sistêmico...");
        ClienteSistemaEntity clientePosPersistencia = clienteSistemaRepositoryImpl
                .implementaPersistencia(clienteAtualizado);
        log.info("Cliente sistêmico persistido com sucesso");

        log.info("Iniciando acesso ao método de atualização dos dados do cliente sistêmico na " +
                "integradora de pagamentos...");
        atualizacaoClienteAsaasProxy.realizaAtualizacaoClienteAsaas(
                clienteAtualizado.getCodigoClienteAsaas(), atualizaClienteSistemaRequest);
        log.info("Método de atualização do cliente sistêmico na integradora de pagamentos finalizado com sucesso");

        log.info("Iniciando acesso ao método responsável por realizar a conversão de objeto do tipo " +
                "ClienteSistemaEntity para objeto do tipo ClienteSistemaResponse...");
        ClienteSistemaResponse clienteSistemaResponse = new ClienteSistemaResponse()
                .buildFromEntity(clientePosPersistencia);
        log.info("Conversão de tipagem realizada com sucesso");

        log.info("Atualização do cliente sistêmico realizada com sucesso. Retornando dados...");
        return clienteSistemaResponse;
    }
}
