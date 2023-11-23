package br.akd.svc.akadia.modules.web.pagamento.repository;

import br.akd.svc.akadia.modules.web.pagamento.models.entity.PagamentoSistemaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PagamentoSistemaRepository extends JpaRepository<PagamentoSistemaEntity, Long> {

    Optional<PagamentoSistemaEntity> findByCodigoPagamentoAsaas(String codigoPagamentoAsaas);

}
