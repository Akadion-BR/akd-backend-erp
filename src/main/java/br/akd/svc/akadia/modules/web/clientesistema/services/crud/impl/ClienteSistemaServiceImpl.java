package br.akd.svc.akadia.modules.web.clientesistema.services.crud.impl;

import br.akd.svc.akadia.exceptions.InternalErrorException;
import br.akd.svc.akadia.modules.global.cpfcnpj.models.CpfRequest;
import br.akd.svc.akadia.modules.global.cpfcnpj.service.CpfService;
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
import br.akd.svc.akadia.utils.Constantes;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Slf4j
@Service
public class ClienteSistemaServiceImpl implements ClienteSistemaService {
    @Autowired
    ClienteSistemaRepositoryImpl clienteSistemaRepositoryImpl;

    @Autowired
    ClienteSistemaValidationService clienteSistemaValidationService;

    @Autowired
    CpfService cpfService;

    @Autowired
    CriacaoClienteSistemaAsaasProxyImpl criacaoClienteSistemaAsaasProxy;

    @Autowired
    AtualizacaoClienteAsaasProxyImpl atualizacaoClienteAsaasProxy;

    @Autowired
    CriacaoPlanoAsaasProxyImpl criacaoPlanoAsaasProxy;

    @Autowired
    RemocaoPlanoAsaasProxyImpl remocaoPlanoAsaasProxy;

    @Override
    public ClienteSistemaResponse cadastraNovoCliente(ClienteSistemaRequest clienteSistemaRequest) throws JsonProcessingException {
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

        try {
            log.info("Iniciando acesso ao método de implementação de persistência do cliente...");
            ClienteSistemaEntity clientePersistido = clienteSistemaRepositoryImpl
                    .implementaPersistencia(clienteSistema);
            log.info("Persistência do cliente sistêmico realizada com sucesso");

            log.info("Iniciando acesso ao método responsável por realizar a conversão de objeto do tipo " +
                    "ClienteSistemaEntity para objeto do tipo ClienteSistemaResponse...");
            ClienteSistemaResponse clienteSistemaResponse = new ClienteSistemaResponse()
                    .buildFromEntity(clientePersistido);
            log.info(Constantes.CONVERSAO_SUCESSO);

            //TODO IMPLEMENTAR ENVIO DE E-MAIL ASSINCRONO DE BOAS-VINDAS

            log.info("Criação do cliente sistêmico realizada com sucesso. Retornando dados...");
            return clienteSistemaResponse;
        } catch (Exception e) {
            log.error("Ocorreu um erro durante a tentativa de persistência do plano. " +
                    "Erro: {}", e.getMessage());

            log.info("Iniciando acesso ao método de cancelamento do plano na integradora ASAAS " +
                    "para realização de rollback...");
            remocaoPlanoAsaasProxy.realizaCancelamentoDePlanoDeAssinaturaNaIntegradoraAsaas(
                    idAssinaturaAsaas);

            log.info("Rollback do plano na integradora ASAAS finalizado com sucesso");
            throw new InternalErrorException(Constantes.ERRO_INTERNO);
        }
    }

    @Override
    public List<ClienteSistemaResponse> buscaTodos() {
        log.debug("Método que implementa lógica de busca de todos os clientes sistêmicos acessado");
        try {
            log.info("Iniciando busca por todos os clientes sistêmicos cadastrados...");
            List<ClienteSistemaEntity> clientesEncontrados = clienteSistemaRepositoryImpl.buscaTodosClientes();
            log.info("Busca de clientes sistêmicos realizada com sucesso. Quantidade de clientes encontrados: {}",
                    clientesEncontrados.size());

            log.info("Iniciando conversão de clientes sistêmicos do tipo Entity para clientes sistêmicos do " +
                    "tipo Response...");
            List<ClienteSistemaResponse> clientesConvertidos = new ClienteSistemaResponse()
                    .buildFromList(clientesEncontrados);
            log.info(Constantes.CONVERSAO_SUCESSO);

            log.info("Obtenção de todos os clientes sistêmicos realizada com sucesso. Retornando dados obtidos...");
            return clientesConvertidos;
        } catch (Exception e) {
            log.error("Ocorreu um erro durante a obtenção de todos os clientes sistêmicos: {}", e.getMessage());
            throw new InternalErrorException(Constantes.ERRO_INTERNO);
        }
    }

    @Override
    public void realizaValidacaoCpf(CpfRequest cpfRequest) {
        log.info("Método responsável por implementar a lógica de validação de CPF acessado");

        log.info("Iniciando validação dos dígitos verificadores do CPF...");
        cpfService.realizaValidacaoCpf(cpfRequest.getCpf());
        log.info("Validação dos dígitos verificadores do CPF realizada com sucesso");

        log.info("Iniciando acesso ao método de validação se CPF já existe...");
        clienteSistemaValidationService.validaSeCpfJaExiste(cpfRequest.getCpf());
        log.info("Validação de duplicidade de CPF realizada com sucesso");
    }

    @Override
    @Transactional
    public ClienteSistemaResponse atualizaDadosCliente(UUID uuidClienteSistema,
                                                       AtualizaClienteSistemaRequest atualizaClienteSistemaRequest) throws JsonProcessingException {

        log.info("Iniciando acesso ao método de implementação de busca de cliente sistêmico por id...");
        ClienteSistemaEntity clienteSistema = clienteSistemaRepositoryImpl
                .implementaBuscaPorId(uuidClienteSistema);
        log.info("Busca de cliente sistêmico por id realizada com sucesso");

        log.info("Iniciando acesso ao método de validação dos atributos recebidos...");
        clienteSistemaValidationService.realizaValidacoesParaAtualizacaoDeClienteSistemico(
                atualizaClienteSistemaRequest, clienteSistema);
        log.info("Validação do método de atualização de cliente sistêmico realizada com sucesso");

        ClienteSistemaEntity clienteAtualizado;
        ClienteSistemaEntity clientePosPersistencia;

        try {
            log.info("Método de serviço de atualização de dados do cliente sistêmico acessado");

            log.info("Iniciando construção do objeto ClienteSistemaEntity...");
            clienteAtualizado = new ClienteSistemaEntity()
                    .updateFromRequest(clienteSistema, atualizaClienteSistemaRequest);
            log.info("Objeto ClienteSistemaEntity construído com sucesso");

            log.info("Iniciando acesso ao método de implementação de persistência do cliente sistêmico...");
            clientePosPersistencia = clienteSistemaRepositoryImpl
                    .implementaPersistencia(clienteAtualizado);
            log.info("Cliente sistêmico persistido com sucesso");

        } catch (DataIntegrityViolationException e) {
            log.error("Ocorreu um erro durante o processo de persistência no banco de dados durante " +
                    "a atualização do cliente sistêmico: " + e.getMessage());
            throw new InternalErrorException(Constantes.ERRO_INTERNO);
        } catch (Exception e) {
            log.error("Ocorreu um erro durante o processo de atualização do cliente sistêmico: "
                    + e.getMessage());
            throw new InternalErrorException(Constantes.ERRO_INTERNO);
        }

        log.info("Iniciando acesso ao método de atualização dos dados do cliente sistêmico na " +
                "integradora de pagamentos...");
        atualizacaoClienteAsaasProxy.realizaAtualizacaoClienteAsaas(
                clienteAtualizado.getCodigoClienteAsaas(), atualizaClienteSistemaRequest);
        log.info("Método de atualização do cliente sistêmico na integradora de pagamentos finalizado com sucesso");

        log.info("Iniciando acesso ao método responsável por realizar a conversão de objeto do tipo " +
                "ClienteSistemaEntity para objeto do tipo ClienteSistemaResponse...");
        ClienteSistemaResponse clienteSistemaResponse = new ClienteSistemaResponse()
                .buildFromEntity(clientePosPersistencia);
        log.info(Constantes.CONVERSAO_SUCESSO);

        log.info("Atualização do cliente sistêmico realizada com sucesso. Retornando dados...");
        return clienteSistemaResponse;
    }
}
