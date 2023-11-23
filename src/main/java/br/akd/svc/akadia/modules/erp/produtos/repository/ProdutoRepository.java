package br.akd.svc.akadia.modules.erp.produtos.repository;

import br.akd.svc.akadia.modules.erp.produtos.models.entity.ProdutoEntity;
import br.akd.svc.akadia.modules.erp.produtos.models.entity.id.ProdutoId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ProdutoRepository extends JpaRepository<ProdutoEntity, ProdutoId> {

    @Query("SELECT p FROM ProdutoEntity p " +
            "WHERE p.empresa.id = ?1 " +
            "AND (?2 IS NULL OR (upper(p.descricao) LIKE ?2% and p.exclusao IS NULL " +
            "OR upper(p.categoria) LIKE ?2% and p.exclusao IS NULL " +
            "OR upper(p.marca) LIKE ?2% and p.exclusao IS NULL " +
            "OR upper(p.sigla) LIKE ?2% and p.exclusao IS NULL " +
            "OR upper(p.codigoInterno) LIKE ?2% and p.exclusao IS NULL))")
    Page<ProdutoEntity> buscaPaginadaPorProdutos(Pageable pageable,
                                                 UUID uuidEmpresa,
                                                 String busca);
}
