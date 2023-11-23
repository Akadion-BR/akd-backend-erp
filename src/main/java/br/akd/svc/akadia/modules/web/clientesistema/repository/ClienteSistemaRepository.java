package br.akd.svc.akadia.modules.web.clientesistema.repository;

import br.akd.svc.akadia.modules.web.clientesistema.models.entity.ClienteSistemaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ClienteSistemaRepository extends JpaRepository<ClienteSistemaEntity, UUID> {

    Optional<ClienteSistemaEntity> findByEmail(String email);

    Optional<ClienteSistemaEntity> findByCpf(String cpf);

    Optional<ClienteSistemaEntity> findByCodigoClienteAsaas(String codigoClienteAsaas);

    @Query("SELECT COUNT(c)>0 FROM ClienteSistemaEntity c " +
            "WHERE c.email = ?1 " +
            "AND c.exclusao IS NULL")
    boolean verificaSeClienteAtivoJaExisteComEmailInformado(String email);

    @Query("SELECT COUNT(c)>0 FROM ClienteSistemaEntity c " +
            "WHERE c.cpf = ?1 " +
            "AND c.exclusao IS NULL")
    boolean verificaSeClienteAtivoJaExisteComCpfInformado(String email);

    @Query("Select c From ClienteSistemaEntity c " +
            "WHERE c.plano.statusPlanoEnum = ?1 " +
            "AND c.plano.dataVencimento < ?2 ")
    List<ClienteSistemaEntity> buscaPorClientesComPlanosVencidosAtivos(String statusPlanoEnum,
                                                                       String dataVencimento);

}
