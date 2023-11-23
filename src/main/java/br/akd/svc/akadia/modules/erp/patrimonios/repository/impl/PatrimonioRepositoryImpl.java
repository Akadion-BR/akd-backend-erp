package br.akd.svc.akadia.modules.erp.patrimonios.repository.impl;

import br.akd.svc.akadia.exceptions.ObjectNotFoundException;
import br.akd.svc.akadia.modules.erp.patrimonios.models.entity.PatrimonioEntity;
import br.akd.svc.akadia.modules.erp.patrimonios.models.entity.id.PatrimonioId;
import br.akd.svc.akadia.modules.erp.patrimonios.repository.PatrimonioRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
public class PatrimonioRepositoryImpl {

    @Autowired
    PatrimonioRepository patrimonioRepository;

    public PatrimonioEntity implementaPersistencia(PatrimonioEntity patrimonioEntity) {
        log.debug("Método de serviço que implementa persistência do objeto");
        return patrimonioRepository.save(patrimonioEntity);
    }

    public void implementaPersistenciaEmMassa(List<PatrimonioEntity> patrimonioNovo) {
        log.debug("Método de serviço que implementa persistência em massa do patrimônio acessado");
        patrimonioRepository.saveAll((patrimonioNovo));
    }

    public PatrimonioEntity implementaBuscaPorId(UUID idEmpresa,
                                                 UUID idPatrimonio) {
        log.debug("Método que implementa busca de patrimônio por id acessado. Id: {}", idPatrimonio);

        Optional<PatrimonioEntity> patrimonioOptional = patrimonioRepository
                .buscaPorId(idEmpresa, idPatrimonio);

        PatrimonioEntity patrimonioEntity;
        if (patrimonioOptional.isPresent()) {
            patrimonioEntity = patrimonioOptional.get();
            log.debug("Patrimônio encontrado: {}", patrimonioEntity);
        } else {
            log.warn("Nenhum patrimônio foi encontrado com o id {}", idPatrimonio);
            throw new ObjectNotFoundException("Nenhum patrimônio foi encontrado com o id informado");
        }
        log.debug("Retornando o patrimônio encontrado...");
        return patrimonioEntity;
    }

    public List<PatrimonioEntity> implementaBuscaPorIdEmMassa(UUID uuidEmpresa,
                                                              List<UUID> ids) {
        log.debug("Método que implementa busca de patrimônios por id em massa acessado. Ids: {}", ids.toString());

        List<PatrimonioId> patrimoniosId = new ArrayList<>();
        ids.forEach(id -> patrimoniosId.add(new PatrimonioId(uuidEmpresa, id)));

        List<PatrimonioEntity> patrimonios =
                patrimonioRepository.findAllById(patrimoniosId);

        if (!patrimonios.isEmpty()) {
            log.debug("{} Patrimônios encontrados", patrimonios.size());
        } else {
            log.warn("Nenhum patrimonio foi encontrado");
            throw new ObjectNotFoundException("Nenhum patrimônio foi encontrado com os ids informados");
        }
        log.debug("Retornando os patrimonios encontrados...");
        return patrimonios;
    }

    public List<PatrimonioEntity> implementaBuscaPorTodos(UUID idEmpresa) {
        log.debug("Método de serviço que implementa busca por todos os patrimonios cadastrados em uma empresa acessado");
        return patrimonioRepository.buscaTodos(idEmpresa);
    }
}
