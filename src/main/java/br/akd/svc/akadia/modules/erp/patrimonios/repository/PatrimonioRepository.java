package br.akd.svc.akadia.modules.erp.patrimonios.repository;

import br.akd.svc.akadia.modules.erp.patrimonios.models.entity.PatrimonioEntity;
import br.akd.svc.akadia.modules.erp.patrimonios.models.entity.id.PatrimonioId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface PatrimonioRepository extends JpaRepository<PatrimonioEntity, PatrimonioId> {

    @Query("SELECT p FROM PatrimonioEntity p " +
            "WHERE p.id = ?2 " +
            "AND p.idEmpresa = ?1 " +
            "AND p.exclusao IS NULL")
    Optional<PatrimonioEntity> buscaPorId(UUID idEmpresa,
                                          UUID idPatrimonio);

    @Query("SELECT p FROM PatrimonioEntity p " +
            "WHERE p.idEmpresa = ?1 " +
            "AND p.exclusao IS NULL")
    List<PatrimonioEntity> buscaTodos(UUID idEmpresa);

    @Query("SELECT p FROM PatrimonioEntity p " +
            "WHERE p.idEmpresa = ?1 " +
            "AND (?2 IS NULL OR (upper(p.descricao) LIKE ?2% and p.exclusao IS NULL))")
    Page<PatrimonioEntity> buscaPaginadaPorClientes(Pageable pageable,
                                                    UUID idEmpresa,
                                                    String busca);

}
