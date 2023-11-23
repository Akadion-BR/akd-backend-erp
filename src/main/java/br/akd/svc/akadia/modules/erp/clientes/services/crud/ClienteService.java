package br.akd.svc.akadia.modules.erp.clientes.services.crud;

import br.akd.svc.akadia.modules.erp.clientes.models.dto.request.ClienteRequest;
import br.akd.svc.akadia.modules.erp.clientes.models.dto.response.ClienteResponse;
import br.akd.svc.akadia.modules.erp.clientes.models.dto.response.page.ClientePageResponse;
import br.akd.svc.akadia.modules.erp.colaboradores.colaborador.models.entity.colaborador.id.ColaboradorId;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

public interface ClienteService {

    @Transactional
    ClienteResponse criaNovoCliente(ColaboradorId idColaboradorSessao,
                                    ClienteRequest clienteRequest);

    ClientePageResponse realizaBuscaPaginadaPorClientes(Pageable pageable,
                                                        ColaboradorId idColaboradorSessao,
                                                        String campoBusca);

    ClienteResponse realizaBuscaDeClientePorId(ColaboradorId idColaboradorSessao,
                                               UUID uuidCliente);

    @Transactional
    ClienteResponse atualizaCliente(ColaboradorId idColaboradorSessao,
                                    UUID uuidCliente,
                                    ClienteRequest clienteRequest);

    @Transactional
    ClienteResponse removeCliente(ColaboradorId idColaboradorSessao,
                                  UUID uuidCliente);

    @Transactional
    void removeClientesEmMassa(ColaboradorId idColaboradorSessao,
                               List<UUID> idClientes);
}