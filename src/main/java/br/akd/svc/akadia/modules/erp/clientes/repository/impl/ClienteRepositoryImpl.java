package br.akd.svc.akadia.modules.erp.clientes.repository.impl;

import br.akd.svc.akadia.exceptions.ObjectNotFoundException;
import br.akd.svc.akadia.modules.erp.clientes.models.entity.ClienteEntity;
import br.akd.svc.akadia.modules.erp.clientes.models.entity.id.ClienteId;
import br.akd.svc.akadia.modules.erp.clientes.repository.ClienteRepository;
import br.akd.svc.akadia.modules.external.empresa.EmpresaId;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
public class ClienteRepositoryImpl {

    @Autowired
    ClienteRepository repository;

    public ClienteEntity implementaPersistencia(ClienteEntity cliente) {
        log.debug("Método de serviço que implementa persistência do cliente");
        return repository.save(cliente);
    }

    public void implementaPersistenciaEmMassa(List<ClienteEntity> clientes) {
        log.debug("Método de serviço que implementa persistência em massa do cliente acessado");
        repository.saveAll((clientes));
    }

    public List<ClienteEntity> implementaBuscaPorTodos(UUID uuidEmpresa) {
        log.debug("Método que implementa busca por todos os clientes acessado");
        return repository.buscaTodos(uuidEmpresa);
    }

    public ClienteEntity implementaBuscaPorId(UUID uuidEmpresa,
                                              UUID uuidCliente) {
        log.debug("Método que implementa busca de cliente por id acessado. Id: {}", uuidCliente);

        Optional<ClienteEntity> clienteOptional =
                repository.buscaPorId(uuidEmpresa, uuidCliente);

        ClienteEntity clienteEntity;
        if (clienteOptional.isPresent()) {
            clienteEntity = clienteOptional.get();
            log.debug("Cliente encontrado: {}", clienteEntity);
        } else {
            log.warn("Nenhum cliente foi encontrado com o id {}", uuidCliente);
            throw new ObjectNotFoundException("Nenhum cliente foi encontrado com o id informado");
        }
        log.debug("Retornando o cliente encontrado...");
        return clienteEntity;
    }

    public List<ClienteEntity> implementaBuscaPorIdEmMassa(EmpresaId empresaId,
                                                           List<UUID> ids) {
        log.debug("Método que implementa busca de cliente por id em massa acessado. Ids: {}", ids.toString());

        List<ClienteId> clienteIds = new ArrayList<>();
        ids.forEach(id -> clienteIds.add(
                ClienteId.builder()
                        .idClienteSistema(empresaId.getClienteSistema())
                        .idEmpresa(empresaId.getId())
                        .id(id)
                        .build()));

        List<ClienteEntity> clientes = repository.findAllById(clienteIds);

        if (!clientes.isEmpty()) {
            log.debug("{} Clientes encontrados", clientes.size());
        } else {
            log.warn("Nenhum cliente foi encontrado");
            throw new ObjectNotFoundException("Nenhum cliente foi encontrado com os ids informados");
        }
        log.debug("Retornando os clientes encontrados...");
        return clientes;
    }

    public Optional<ClienteEntity> implementaBuscaPorCpfCnpjIdentico(UUID uuidEmpresa,
                                                                     String cpfCnpj) {
        log.debug("Método que implementa busca de cliente por CPF/CNPJ acessado. CPF/CNPJ: {}", cpfCnpj);
        return repository.buscaPorCpfCnpjIdenticoNaEmpresaDaSessaoAtual(uuidEmpresa, cpfCnpj);
    }

    public Optional<ClienteEntity> implementaBuscaPorInscricaoEstadualIdentica(UUID uuidEmpresa,
                                                                               String inscricaoEstadual) {
        log.debug("Método que implementa busca de cliente por Inscrição estadual acessado. IE: {}", inscricaoEstadual);
        return repository.buscaPorInscricaoEstadualIdenticaNaEmpresaDaSessaoAtual(uuidEmpresa, inscricaoEstadual);
    }

}