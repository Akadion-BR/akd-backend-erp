package br.akd.svc.akadia.modules.erp.clientes.repository;

import br.akd.svc.akadia.modules.erp.clientes.models.entity.ClienteEntity;
import br.akd.svc.akadia.modules.erp.clientes.models.entity.id.ClienteId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ClienteRepository extends JpaRepository<ClienteEntity, ClienteId> {

    @Query("SELECT COUNT(c)>0 FROM ClienteEntity c " +
            "WHERE c.idEmpresa = ?1 " +
            "AND c.cpfCnpj = ?2 " +
            "AND c.exclusao IS NULL")
    boolean verificaSeClienteAtivoJaExisteComCpfCnpjInformado(UUID uuidEmpresaSessao,
                                                              String cpfCnpj);

    @Query("SELECT COUNT(c)>0 FROM ClienteEntity c " +
            "WHERE c.idEmpresa = ?1 " +
            "AND c.inscricaoEstadual = ?2 " +
            "AND c.exclusao IS NULL")
    boolean verificaSeClienteAtivoJaExisteComInscricaoEstadualInformada(UUID uuidEmpresaSessao,
                                                                        String inscricaoEstadual);

    @Query("SELECT c FROM ClienteEntity c " +
            "WHERE c.id = ?2 " +
            "AND c.idEmpresa = ?1 " +
            "AND c.exclusao IS NULL")
    Optional<ClienteEntity> buscaPorId(UUID uuidEmpresa,
                                       UUID uuidCliente);

    @Query("SELECT c FROM ClienteEntity c " +
            "WHERE c.cpfCnpj = ?2 " +
            "and c.idEmpresa = ?1 " +
            "and c.exclusao IS NULL")
    Optional<ClienteEntity> buscaPorCpfCnpjIdenticoNaEmpresaDaSessaoAtual(UUID uuidEmpresa,
                                                                          String cpfCnpj);

    @Query("SELECT c FROM ClienteEntity c " +
            "WHERE c.inscricaoEstadual = ?2 " +
            "AND c.idEmpresa = ?1 " +
            "AND c.exclusao IS NULL")
    Optional<ClienteEntity> buscaPorInscricaoEstadualIdenticaNaEmpresaDaSessaoAtual(UUID uuidEmpresa,
                                                                                    String inscricaoEstadual);

    @Query("SELECT c FROM ClienteEntity c " +
            "WHERE c.idEmpresa = ?1 " +
            "AND c.exclusao IS NULL")
    List<ClienteEntity> buscaTodos(UUID uuidEmpresa);

    @Query("SELECT c FROM ClienteEntity c " +
            "WHERE c.idEmpresa = ?1 " +
            "AND (?2 IS NULL OR (upper(c.nome) LIKE ?2% and c.exclusao IS NULL " +
            "OR upper(c.email) LIKE ?2% and c.exclusao IS NULL " +
            "OR upper(c.cpfCnpj) LIKE ?2% and c.exclusao IS NULL))")
    Page<ClienteEntity> buscaPaginadaPorClientes(Pageable pageable,
                                                 UUID uuidEmpresa,
                                                 String busca);
}
