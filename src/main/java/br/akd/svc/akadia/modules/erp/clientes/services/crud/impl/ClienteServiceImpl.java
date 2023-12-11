package br.akd.svc.akadia.modules.erp.clientes.services.crud.impl;

import br.akd.svc.akadia.exceptions.InvalidRequestException;
import br.akd.svc.akadia.modules.erp.clientes.models.dto.request.ClienteRequest;
import br.akd.svc.akadia.modules.erp.clientes.models.dto.response.ClienteResponse;
import br.akd.svc.akadia.modules.erp.clientes.models.dto.response.page.ClientePageResponse;
import br.akd.svc.akadia.modules.erp.clientes.models.entity.ClienteEntity;
import br.akd.svc.akadia.modules.erp.clientes.repository.ClienteRepository;
import br.akd.svc.akadia.modules.erp.clientes.repository.impl.ClienteRepositoryImpl;
import br.akd.svc.akadia.modules.erp.clientes.services.crud.ClienteService;
import br.akd.svc.akadia.modules.erp.clientes.services.validator.ClienteValidationService;
import br.akd.svc.akadia.modules.erp.colaboradores.acao.models.enums.TipoAcaoEnum;
import br.akd.svc.akadia.modules.erp.colaboradores.acao.services.AcaoService;
import br.akd.svc.akadia.modules.erp.colaboradores.colaborador.models.entity.colaborador.ColaboradorEntity;
import br.akd.svc.akadia.modules.erp.colaboradores.colaborador.models.entity.colaborador.id.ColaboradorId;
import br.akd.svc.akadia.modules.erp.colaboradores.colaborador.models.enums.ModulosEnum;
import br.akd.svc.akadia.modules.global.objects.exclusao.entity.ExclusaoEntity;
import br.akd.svc.akadia.utils.Constantes;
import br.akd.svc.akadia.config.security.utils.SecurityUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
public class ClienteServiceImpl implements ClienteService {

    @Autowired
    AcaoService acaoService;

    @Autowired
    ClienteValidationService clienteValidationService;

    @Autowired
    ClienteRepositoryImpl clienteRepositoryImpl;

    @Autowired
    ClienteRepository clienteRepository;

    @Autowired
    SecurityUtil securityUtil;

    String BUSCA_CLIENTE_POR_ID = "Iniciando acesso ao método de implementação de busca pelo cliente por id...";

    public ClienteResponse criaNovoCliente(ColaboradorId idColaboradorSessao,
                                           ClienteRequest clienteRequest) {

        log.debug("Método de serviço de criação de novo cliente acessado");

        ColaboradorEntity colaboradorLogado = securityUtil.obtemUsuarioSessao(idColaboradorSessao);

        log.debug(Constantes.VERIFICANDO_SE_COLABORADOR_PODE_ALTERAR_DADOS);
        securityUtil.verificaSePodeRealizarAlteracoes(colaboradorLogado.getAcessoSistema());

        log.debug("Iniciando acesso ao método de validação de chave única...");
        clienteValidationService.validaSeChavesUnicasJaExistemParaNovoCliente(
                colaboradorLogado.getEmpresa().getId(),
                clienteRequest);

        log.debug("Iniciando criação do objeto ClienteEntity...");
        ClienteEntity clienteEntity = new ClienteEntity()
                .constroiClienteEntityParaCriacao(colaboradorLogado, clienteRequest);
        log.debug("Objeto clienteEntity criado com sucesso");

        log.debug("Iniciando acesso ao método de implementação da persistência do cliente...");
        ClienteEntity clientePersistido = clienteRepositoryImpl.implementaPersistencia(clienteEntity);

        log.debug(Constantes.INICIANDO_SALVAMENTO_HISTORICO_COLABORADOR);
        acaoService.salvaHistoricoColaborador(idColaboradorSessao, clientePersistido.getId(),
                ModulosEnum.CLIENTES, TipoAcaoEnum.CRIACAO, null);

        log.debug("Cliente persistido com sucesso. Convertendo clienteEntity para clienteResponse...");
        ClienteResponse clienteResponse = new ClienteResponse()
                .buildFromEntity(clientePersistido);

        log.info("Cliente criado com sucesso");
        return clienteResponse;
    }

    public ClientePageResponse realizaBuscaPaginadaPorClientes(Pageable pageable,
                                                               ColaboradorId idColaboradorSessao,
                                                               String campoBusca) {
        log.debug("Método de serviço de obtenção paginada de clientes acessado. Campo de busca: {}",
                campoBusca != null ? campoBusca : "Nulo");

        log.debug("Acessando repositório de busca de clientes");
        Page<ClienteEntity> clientePage = clienteRepository
                .buscaPaginadaPorClientes(pageable, idColaboradorSessao.getEmpresa(), campoBusca);

        log.debug("Busca de clientes por paginação realizada com sucesso. Acessando método de conversão dos objetos do tipo " +
                "Entity para objetos do tipo Response...");
        ClientePageResponse clientePageResponse = new ClientePageResponse()
                .constroiClientePageResponse(clientePage);
        log.debug("Conversão de tipagem realizada com sucesso");

        log.info("A busca paginada de clientes foi realizada com sucesso");
        return clientePageResponse;
    }

    public ClienteResponse realizaBuscaDeClientePorId(ColaboradorId idColaboradorSessao,
                                                      UUID uuidCliente) {
        log.debug("Método de serviço de obtenção de cliente por id. ID recebido: {}", uuidCliente);

        ColaboradorEntity colaboradorLogado = securityUtil.obtemUsuarioSessao(idColaboradorSessao);

        log.debug("Acessando repositório de busca de cliente por ID...");
        ClienteEntity cliente = clienteRepositoryImpl
                .implementaBuscaPorId(colaboradorLogado.getEmpresa().getId(), uuidCliente);

        log.debug("Busca de clientes por id realizada com sucesso. Acessando método de conversão dos objeto do tipo " +
                "Entity para objeto do tipo Response...");
        ClienteResponse clienteResponse = new ClienteResponse().buildFromEntity(cliente);
        log.debug("Conversão de tipagem realizada com sucesso");

        log.info("A busca de cliente por id foi realizada com sucesso");
        return clienteResponse;
    }

    public ClienteResponse atualizaCliente(ColaboradorId idColaboradorSessao,
                                           UUID uuidCliente,
                                           ClienteRequest clienteRequest) {
        log.debug("Método de serviço de atualização de cliente acessado");

        ColaboradorEntity colaboradorLogado = securityUtil.obtemUsuarioSessao(idColaboradorSessao);

        log.debug(Constantes.VERIFICANDO_SE_COLABORADOR_PODE_ALTERAR_DADOS);
        securityUtil.verificaSePodeRealizarAlteracoes(colaboradorLogado.getAcessoSistema());

        log.debug(BUSCA_CLIENTE_POR_ID);
        ClienteEntity clienteEncontrado = clienteRepositoryImpl.implementaBuscaPorId(uuidCliente, colaboradorLogado.getEmpresa().getId());

        log.debug("Iniciando acesso ao método de validação de alteração de dados de cliente excluído...");
        clienteValidationService.validaSeClienteEstaExcluido(clienteEncontrado, "Não é possível atualizar um cliente excluído");

        log.debug("Iniciando acesso ao método de validação de chave única...");
        clienteValidationService.validaSeChavesUnicasJaExistemParaClienteAtualizado(
                colaboradorLogado.getEmpresa().getId(),
                clienteRequest,
                clienteEncontrado);

        log.debug("Iniciando criação do objeto ClienteEntity...");
        ClienteEntity novoClienteAtualizado = new ClienteEntity()
                .atualizaEntidadeComAtributosRequest(clienteEncontrado, clienteRequest);
        log.debug("Objeto cliente construído com sucesso");

        log.debug("Iniciando acesso ao método de implementação da persistência do cliente...");
        ClienteEntity clientePersistido = clienteRepositoryImpl.implementaPersistencia(novoClienteAtualizado);

        log.debug(Constantes.INICIANDO_SALVAMENTO_HISTORICO_COLABORADOR);
        acaoService.salvaHistoricoColaborador(idColaboradorSessao, clientePersistido.getId(),
                ModulosEnum.CLIENTES, TipoAcaoEnum.ALTERACAO, null);

        log.debug("Cliente persistido com sucesso. Convertendo clienteEntity para clienteResponse...");
        ClienteResponse clienteResponse = new ClienteResponse()
                .buildFromEntity(clientePersistido);

        log.info("Cliente criado com sucesso");
        return clienteResponse;
    }

    public ClienteResponse removeCliente(ColaboradorId idColaboradorSessao,
                                         UUID uuidCliente) {
        log.debug("Método de serviço de remoção de cliente acessado");

        ColaboradorEntity colaboradorLogado = securityUtil.obtemUsuarioSessao(idColaboradorSessao);

        log.debug(BUSCA_CLIENTE_POR_ID);
        ClienteEntity clienteEncontrado = clienteRepositoryImpl
                .implementaBuscaPorId(colaboradorLogado.getEmpresa().getId(), uuidCliente);

        log.debug("Iniciando acesso ao método de validação de exclusão de cliente que já foi excluído...");
        clienteValidationService.validaSeClienteEstaExcluido(clienteEncontrado,
                "O cliente selecionado já foi excluído");

        log.debug("Atualizando objeto Exclusao do cliente com dados referentes à sua exclusão...");
        ExclusaoEntity exclusaoEntity = new ExclusaoEntity().constroiObjetoExclusao();

        clienteEncontrado.setExclusao(exclusaoEntity);
        log.debug("Objeto Exclusao do cliente de id {} setado com sucesso", uuidCliente);

        log.debug("Persistindo cliente excluído no banco de dados...");
        ClienteEntity clienteExcluido = clienteRepositoryImpl.implementaPersistencia(clienteEncontrado);

        log.debug(Constantes.INICIANDO_SALVAMENTO_HISTORICO_COLABORADOR);
        acaoService.salvaHistoricoColaborador(idColaboradorSessao, null,
                ModulosEnum.CLIENTES, TipoAcaoEnum.REMOCAO, null);

        log.info("Cliente excluído com sucesso");
        return new ClienteResponse().buildFromEntity(clienteExcluido);
    }

    public void removeClientesEmMassa(ColaboradorId idColaboradorSessao,
                                      List<UUID> idClientes) {
        log.debug("Método de serviço de remoção de cliente acessado");

        ColaboradorEntity colaboradorLogado = securityUtil.obtemUsuarioSessao(idColaboradorSessao);

        List<ClienteEntity> clientesEncontrados = new ArrayList<>();

        for (UUID uuidCliente : idClientes) {
            log.debug(BUSCA_CLIENTE_POR_ID);
            ClienteEntity clienteEncontrado = clienteRepositoryImpl
                    .implementaBuscaPorId(colaboradorLogado.getEmpresa().getId(), uuidCliente);
            clientesEncontrados.add(clienteEncontrado);
        }

        log.debug("Iniciando acesso ao método de validação de exclusão de cliente que já foi excluído...");
        for (ClienteEntity cliente : clientesEncontrados) {
            clienteValidationService.validaSeClienteEstaExcluido(cliente,
                    "O cliente selecionado já foi excluído");
            log.debug("Atualizando objeto Exclusao do cliente com dados referentes à sua exclusão...");
            ExclusaoEntity exclusao = new ExclusaoEntity().constroiObjetoExclusao();

            cliente.setExclusao(exclusao);
            log.debug("Objeto Exclusao do cliente de id {} setado com sucesso", cliente.getId());
        }

        log.debug("Verificando se listagem de clientes encontrados está preenchida...");
        if (!clientesEncontrados.isEmpty()) {
            log.debug("Persistindo cliente excluído no banco de dados...");
            clienteRepositoryImpl.implementaPersistenciaEmMassa(clientesEncontrados);

            log.debug(Constantes.INICIANDO_SALVAMENTO_HISTORICO_COLABORADOR);
            acaoService.salvaHistoricoColaborador(idColaboradorSessao, null,
                    ModulosEnum.CLIENTES, TipoAcaoEnum.REMOCAO_EM_MASSA, clientesEncontrados.size() + " Itens removidos");
        } else throw new InvalidRequestException("Nenhum cliente foi encontrado para remoção");

        log.info("Clientes excluídos com sucesso");
    }

}
