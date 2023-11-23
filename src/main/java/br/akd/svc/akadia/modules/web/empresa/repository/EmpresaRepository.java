package br.akd.svc.akadia.modules.web.empresa.repository;

import br.akd.svc.akadia.modules.web.empresa.models.entity.EmpresaEntity;
import br.akd.svc.akadia.modules.web.empresa.models.entity.id.EmpresaId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EmpresaRepository extends JpaRepository<EmpresaEntity, EmpresaId> {

    Optional<EmpresaEntity> findByCnpj(String cnpj);
    Optional<EmpresaEntity> findByEndpoint(String endpoint);
    Optional<EmpresaEntity> findByRazaoSocial(String razaoSocial);
    Optional<EmpresaEntity> findByInscricaoEstadual(String inscricaoEstadual);
    Optional<EmpresaEntity> findByInscricaoMunicipal(String inscricaoMunicipal);

}
