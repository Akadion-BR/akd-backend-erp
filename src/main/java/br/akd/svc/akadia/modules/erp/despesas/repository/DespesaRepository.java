package br.akd.svc.akadia.modules.erp.despesas.repository;

import br.akd.svc.akadia.modules.erp.despesas.models.entity.DespesaEntity;
import br.akd.svc.akadia.modules.erp.despesas.models.entity.id.DespesaId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface DespesaRepository extends JpaRepository<DespesaEntity, DespesaId> {

    @Query("SELECT c FROM DespesaEntity c " +
            "WHERE c.id = ?2 " +
            "AND c.idEmpresa = ?1 " +
            "AND c.exclusao IS NULL")
    Optional<DespesaEntity> buscaPorId(UUID idEmpresa,
                                       UUID idDespesa);

    @Query("SELECT d FROM DespesaEntity d " +
            "WHERE d.idEmpresa = ?1 " +
            "AND d.exclusao IS NULL " +
            "AND d.dataAgendamento BETWEEN ?2 AND ?3" +
            "OR d.idEmpresa = ?1 " +
            "AND d.exclusao IS NULL " +
            "AND d.dataPagamento BETWEEN ?2 AND ?3")
    Page<DespesaEntity> buscaPorDespesas(Pageable pageable,
                                         UUID idEmpresa,
                                         String dataInicio,
                                         String dataFim);

    @Query("SELECT d FROM DespesaEntity d " +
            "WHERE UPPER(d.descricao) LIKE ?4% " +
            "AND d.idEmpresa = ?1 " +
            "AND d.exclusao IS NULL " +
            "AND d.dataAgendamento BETWEEN ?2 and ?3" +
            "OR upper(d.descricao) LIKE ?4% " +
            "AND d.idEmpresa = ?1 " +
            "AND d.exclusao IS NULL " +
            "AND d.dataPagamento BETWEEN ?2 and ?3")
    Page<DespesaEntity> buscaPorDespesasTypeAhead(Pageable pageable,
                                                  UUID idEmpresa,
                                                  String dataInicio,
                                                  String dataFim,
                                                  String busca);

    @Query("SELECT d FROM DespesaEntity d " +
            "WHERE d.idEmpresa = ?1 " +
            "AND d.exclusao IS NULL")
    List<DespesaEntity> buscaTodos(UUID idEmpresa);
}
