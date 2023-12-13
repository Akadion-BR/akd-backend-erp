package br.akd.svc.akadia.modules.erp.despesas.repository.impl;

import br.akd.svc.akadia.exceptions.ObjectNotFoundException;
import br.akd.svc.akadia.modules.erp.despesas.models.entity.DespesaEntity;
import br.akd.svc.akadia.modules.erp.despesas.models.entity.id.DespesaId;
import br.akd.svc.akadia.modules.erp.despesas.repository.DespesaRepository;
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
public class DespesaRepositoryImpl {

    @Autowired
    DespesaRepository despesaRepository;

    public DespesaEntity implementaPersistencia(DespesaEntity despesaEntity) {
        log.debug("Método de serviço que implementa persistência do despesaEntity");
        return despesaRepository.save(despesaEntity);
    }

    public void implementaPersistenciaEmMassa(List<DespesaEntity> despesas) {
        log.debug("Método de serviço que implementa persistência em massa de despesas acessado");
        despesaRepository.saveAll((despesas));
    }

    public DespesaEntity implementaBuscaPorId(UUID idEmpresa,
                                              UUID idDespesa) {
        log.debug("Método que implementa busca de despesa por id acessado. Id: {}", idDespesa);

        Optional<DespesaEntity> despesaOptional = despesaRepository.buscaPorId(idEmpresa, idDespesa);

        DespesaEntity despesaEntity;
        if (despesaOptional.isPresent()) {
            despesaEntity = despesaOptional.get();
            log.debug("Despesa encontrada: {}", despesaEntity);
        } else {
            log.warn("Nenhuma despesa foi encontrada com o id {}", idDespesa);
            throw new ObjectNotFoundException("Nenhuma despesa foi encontrada com o id informado");
        }
        log.debug("Retornando a despesa encontrada...");
        return despesaEntity;
    }

    public List<DespesaEntity> implementaBuscaPorTodos(UUID idEmpresa) {
        log.debug("Método que implementa busca por todas as despesas acessado");
        return despesaRepository.buscaTodos(idEmpresa);
    }

    public List<DespesaEntity> implementaBuscaPorIdEmMassa(EmpresaId empresaId,
                                                           List<UUID> ids) {
        log.debug("Método que implementa busca de despesa por id em massa acessado. Ids: {}", ids.toString());

        List<DespesaId> despesasId = new ArrayList<>();
        ids.forEach(id -> despesasId.add(
                DespesaId.builder()
                        .idClienteSistema(empresaId.getClienteSistema())
                        .idEmpresa(empresaId.getId())
                        .id(id)
                        .build()));

        List<DespesaEntity> despesas = despesaRepository.findAllById(despesasId);

        if (!despesas.isEmpty()) {
            log.debug("{} Despesas encontradas", despesas.size());
        } else {
            log.warn("Nenhuma despesa foi encontrada");
            throw new ObjectNotFoundException("Nenhuma despesa foi encontrada com os ids informados");
        }
        log.debug("Retornando as despesas encontradas...");
        return despesas;
    }

}
