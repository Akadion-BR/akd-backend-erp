package br.akd.svc.akadia.modules.backoffice.lead.repository;

import br.akd.svc.akadia.modules.backoffice.lead.models.entity.LeadEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LeadRepository extends JpaRepository<LeadEntity, Long> {}
