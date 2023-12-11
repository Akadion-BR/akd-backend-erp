package br.akd.svc.akadia.modules.erp.colaboradores.colaborador.repository.impl;

import br.akd.svc.akadia.exceptions.ObjectNotFoundException;
import br.akd.svc.akadia.modules.erp.colaboradores.colaborador.models.entity.colaborador.ColaboradorEntity;
import br.akd.svc.akadia.modules.erp.colaboradores.colaborador.models.entity.colaborador.id.ColaboradorId;
import br.akd.svc.akadia.modules.erp.colaboradores.colaborador.repository.ColaboradorRepository;
import br.akd.svc.akadia.modules.external.empresa.entity.EmpresaEntity;
import br.akd.svc.akadia.modules.external.empresa.entity.id.EmpresaId;
import br.akd.svc.akadia.modules.global.objects.imagem.entity.ImagemEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
public class ColaboradorRepositoryImpl {

    @Autowired
    ColaboradorRepository colaboradorRepository;

    public List<ColaboradorEntity> implementaBuscaPorTodos(UUID id) {
        log.debug("Método de serviço que implementa busca por todos os colaboradores cadastrados em uma empresa acessado");
        return colaboradorRepository.buscaTodos(id);
    }

    public ColaboradorEntity implementaPersistencia(ColaboradorEntity colaborador) {
        log.debug("Método de serviço que implementa persistência do colaborador acessado");
        return colaboradorRepository.save(colaborador);
    }

    public void implementaPersistenciaEmMassa(List<ColaboradorEntity> colaboradores) {
        log.debug("Método de serviço que implementa persistência em massa do colaborador acessado");
        colaboradorRepository.saveAll((colaboradores));
    }

    public ColaboradorEntity implementaBuscaPorId(UUID uuidEmpresa,
                                                  UUID uuidColaborador) {

        log.debug("Método que implementa busca de colaborador por id acessado. Id: {}", uuidColaborador);
        Optional<ColaboradorEntity> colaboradorOptional =
                colaboradorRepository.buscaPorId(uuidEmpresa, uuidColaborador);

        ColaboradorEntity colaboradorEntity;
        if (colaboradorOptional.isPresent()) {
            colaboradorEntity = colaboradorOptional.get();
            log.debug("Colaborador encontrado: {}", colaboradorEntity);
        } else {
            log.warn("Nenhum colaborador foi encontrado com o id {}", uuidColaborador);
            throw new ObjectNotFoundException("Nenhum colaborador foi encontrado com o id informado");
        }
        log.debug("Retornando o colaborador encontrado...");
        return colaboradorEntity;
    }

    public ColaboradorEntity implementaBuscaPorMatricula(UUID uuidEmpresa,
                                                         String matricula) {

        log.debug("Método que implementa busca de colaborador por matrícula acessado. Matrícula: {}", matricula);
        Optional<ColaboradorEntity> colaboradorOptional =
                colaboradorRepository.buscaPorMatricula(uuidEmpresa, matricula);

        ColaboradorEntity colaboradorEntity;
        if (colaboradorOptional.isPresent()) {
            colaboradorEntity = colaboradorOptional.get();
            log.debug("Colaborador encontrado: {}", colaboradorEntity);
        } else {
            log.warn("Nenhum colaborador foi encontrado com a matrícula {}", matricula);
            throw new ObjectNotFoundException("Nenhum colaborador foi encontrado com o id informado");
        }
        log.debug("Retornando o colaborador encontrado...");
        return colaboradorEntity;
    }

    public ImagemEntity implementaBuscaDeImagemDePerfilPorId(UUID uuidEmpresa,
                                                             UUID uuidColaborador) {
        log.debug("Método que implementa busca de imagem de perfil de colaborador por id acessado. Id: {}", uuidColaborador);

        Optional<ImagemEntity> imagemEntityOptional =
                colaboradorRepository.buscaImagemPerfilPorId(uuidEmpresa, uuidColaborador);

        ImagemEntity imagemEntity;
        if (imagemEntityOptional.isPresent()) {
            imagemEntity = imagemEntityOptional.get();
            log.debug("Imagem de perfil encontrada: {}", imagemEntity.getNome());
        } else {
            log.warn("Nenhuma imagem de perfil foi encontrada com o id {}", uuidColaborador);
            throw new ObjectNotFoundException("Nenhuma imagem de perfil foi encontrada com o id informado");
        }
        log.debug("Retornando a imagem de perfil encontrada...");
        return imagemEntity;
    }

    public List<String> implementaBuscaPorTodasAsOcupacoesDaEmpresa(UUID uuidEmpresa) {
        return colaboradorRepository.buscaTodasOcupacoesDaEmpresa(uuidEmpresa);
    }

    public List<ColaboradorEntity> implementaBuscaPorIdEmMassa(EmpresaId empresaId,
                                                               List<UUID> ids) {
        log.debug("Método que implementa busca de colaborador por id em massa acessado. Ids: {}", ids.toString());

        List<ColaboradorId> colaboradorIds = new ArrayList<>();
        ids.forEach(uuid -> colaboradorIds.add(new ColaboradorId(empresaId, uuid)));

        List<ColaboradorEntity> colaboradores =
                colaboradorRepository.findAllById(colaboradorIds);

        if (!colaboradores.isEmpty()) {
            log.debug("{} Colaboradores encontrados", colaboradores.size());
        } else {
            log.warn("Nenhum colaborador foi encontrado");
            throw new ObjectNotFoundException("Nenhum colaborador foi encontrado com os ids informados");
        }
        log.debug("Retornando os colaboradores encontrados...");
        return colaboradores;
    }

}
