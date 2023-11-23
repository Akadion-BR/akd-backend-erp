package br.akd.svc.akadia.modules.erp.colaboradores.acesso.services.impl;

import br.akd.svc.akadia.modules.erp.colaboradores.acesso.models.dto.response.page.AcessoPageResponse;
import br.akd.svc.akadia.modules.erp.colaboradores.acesso.models.entity.AcessoEntity;
import br.akd.svc.akadia.modules.erp.colaboradores.acesso.services.AcessoService;
import br.akd.svc.akadia.modules.erp.colaboradores.colaborador.models.entity.colaborador.ColaboradorEntity;
import br.akd.svc.akadia.modules.erp.colaboradores.colaborador.models.entity.colaborador.id.ColaboradorId;
import br.akd.svc.akadia.modules.erp.colaboradores.colaborador.repository.ColaboradorRepository;
import br.akd.svc.akadia.modules.erp.colaboradores.colaborador.repository.impl.ColaboradorRepositoryImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Slf4j
@Service
public class AcessoServiceImpl implements AcessoService {

    @Autowired
    ColaboradorRepositoryImpl colaboradorRepositoryImpl;

    @Autowired
    ColaboradorRepository colaboradorRepository;

    public void registraAcessoColaborador(ColaboradorId idColaboradorSessao,
                                          String matricula) {
        log.debug("Método responsável por realizar a criação do log de acessos do colaborador acessado");

        log.debug("Realizando a busca do colaborador pela matrícula informada: {}...", matricula);
        ColaboradorEntity colaborador = colaboradorRepositoryImpl.implementaBuscaPorMatricula(
                idColaboradorSessao.getEmpresa(), matricula);

        log.debug("Adicionando objeto Acesso ao colaborador...");
        colaborador.addAcesso();

        log.debug("Realizando persistência do colaborador com acesso registrado...");
        colaboradorRepositoryImpl.implementaPersistencia(colaborador);

        log.debug("Colaborador atualizado com novo login cadastrado com sucesso");
    }

    public AcessoPageResponse obtemAcessosColaborador(Pageable pageable,
                                                      ColaboradorId idColaboradorSessao,
                                                      UUID idColaborador) {

        log.debug("Método de serviço obtenção de acessos do colaborador de id {} acessado", idColaborador);

        log.debug("Acessando repositório de busca de acessos");
        Page<AcessoEntity> acessoPage = colaboradorRepository.buscaAcessosPorIdColaborador(
                pageable, idColaboradorSessao.getId(), idColaborador);

        log.debug("Busca de acessos por paginação realizada com sucesso. Acessando método de conversão dos objetos do tipo " +
                "Entity para objetos do tipo Response...");
        AcessoPageResponse acessoPageResponse = new AcessoPageResponse().buildPageResponse(acessoPage);

        log.debug("Conversão de tipagem realizada com sucesso");

        log.info("A busca paginada de acessos foi realizada com sucesso");
        return acessoPageResponse;
    }

}
