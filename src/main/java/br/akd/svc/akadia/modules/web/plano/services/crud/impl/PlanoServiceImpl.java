package br.akd.svc.akadia.modules.web.plano.services.crud.impl;

import br.akd.svc.akadia.exceptions.InternalErrorException;
import br.akd.svc.akadia.modules.web.clientesistema.models.entity.ClienteSistemaEntity;
import br.akd.svc.akadia.modules.web.clientesistema.repository.impl.ClienteSistemaRepositoryImpl;
import br.akd.svc.akadia.modules.web.plano.models.dto.request.PlanoRequest;
import br.akd.svc.akadia.modules.web.plano.models.dto.response.PlanoResponse;
import br.akd.svc.akadia.modules.web.plano.models.entity.PlanoEntity;
import br.akd.svc.akadia.modules.web.plano.models.enums.StatusPlanoEnum;
import br.akd.svc.akadia.modules.web.plano.proxy.operations.atualizacao.impl.AtualizacaoPlanoAsaasProxyImpl;
import br.akd.svc.akadia.modules.web.plano.proxy.operations.remocao.impl.RemocaoPlanoAsaasProxyImpl;
import br.akd.svc.akadia.modules.web.plano.services.crud.PlanoService;
import br.akd.svc.akadia.modules.web.plano.services.validator.PlanoValidationService;
import br.akd.svc.akadia.utils.Constantes;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.UUID;

@Slf4j
@Service
public class PlanoServiceImpl implements PlanoService {

    @Autowired
    PlanoValidationService planoValidationService;

    @Autowired
    AtualizacaoPlanoAsaasProxyImpl atualizacaoPlanoAsaasProxy;

    @Autowired
    RemocaoPlanoAsaasProxyImpl remocaoPlanoAsaasProxy;

    @Autowired
    ClienteSistemaRepositoryImpl clienteSistemaRepositoryImpl;

    @Override
    @Transactional
    public PlanoResponse atualizaPlanoDoClienteSistemico(UUID idClienteSistema,
                                                         PlanoRequest planoRequest) throws JsonProcessingException {
        log.info("Método responsável por realizar a atualização de um plano de um cliente sistêmico buscado " +
                "por id acessado");

        log.info("Iniciando acesso ao método de busca de cliente sistêmico por id...");
        ClienteSistemaEntity clienteSistemicoEncontrado =
                clienteSistemaRepositoryImpl.implementaBuscaPorId(idClienteSistema);
        log.info("Cliente sistêmico encontrado com sucesso");

        log.info("Iniciando acesso ao método de validação do plano...");
        planoValidationService.realizaValidacoesParaAtualizacaoDePlano(planoRequest);
        log.info("Validações finalizadas com sucesso");

        ClienteSistemaEntity clientePosPersistencia;

        try {
            log.info("Realizando setagem de tipo de plano atualizada...");
            clienteSistemicoEncontrado.setPlano(new PlanoEntity()
                    .updateFromRequest(clienteSistemicoEncontrado.getPlano(), planoRequest));
            log.info("Ok");

            log.info("Iniciando persistência do cliente sistêmico atualizado...");
            clientePosPersistencia = clienteSistemaRepositoryImpl
                    .implementaPersistencia(clienteSistemicoEncontrado);
            log.info("Persistência do cliente sistêmico atualizada realizada com sucesso");
        } catch (DataIntegrityViolationException e) {
            log.error("Ocorreu um erro durante o processo de persistência no banco de dados durante " +
                    "a atualização do plano de assinatura: " + e.getMessage());
            throw new InternalErrorException(Constantes.ERRO_INTERNO);
        } catch (Exception e) {
            log.error("Ocorreu um erro durante o processo de atualização do plano de assinatura: "
                    + e.getMessage());
            throw new InternalErrorException(Constantes.ERRO_INTERNO);
        }

        log.info("Iniciando acesso ao método de atualização do plano de assinatura na integradora asaas...");
        atualizacaoPlanoAsaasProxy.realizaAtualizacaoDePlanoDeAssinaturaNaIntegradoraAsaas(
                clientePosPersistencia.getPlano());
        log.info("Plano atualizado na integradora com sucesso");

        log.info("Iniciando acesso ao método responsável por realizar a conversão de objeto do tipo PlanoEntity " +
                "para objeto do tipo PlanoResponse...");
        PlanoResponse planoResponse = new PlanoResponse()
                .buildFromEntity(clientePosPersistencia.getPlano());
        log.info("Conversão de tipagem realizada com sucesso");

        log.info("Atualização do plano do cliente sistêmico realizada com sucesso");
        return planoResponse;
    }

    @Override
    @Transactional
    public PlanoResponse cancelaPlanoDoClienteSistemico(UUID idClienteSistema) throws JsonProcessingException {
        log.info("Método responsável por realizar o cancelamento de um plano de um cliente sistêmico buscado " +
                "por id acessado");

        log.info("Iniciando acesso ao método de busca de cliente sistêmico por id...");
        ClienteSistemaEntity clienteSistemicoEncontrado =
                clienteSistemaRepositoryImpl.implementaBuscaPorId(idClienteSistema);
        log.info("Cliente sistêmico encontrado com sucesso");

        log.info("Obtendo plano do cliente sistêmico encontrado...");
        PlanoEntity planoClienteSistemico = clienteSistemicoEncontrado.getPlano();

        ClienteSistemaEntity clientePosPersistencia;

        try {
            log.info("O plano iterado possui status {}", planoClienteSistemico.getStatusPlanoEnum());
            if (StatusPlanoEnum.ATIVO.equals(planoClienteSistemico.getStatusPlanoEnum())
                    || StatusPlanoEnum.PERIODO_DE_TESTES.equals(planoClienteSistemico.getStatusPlanoEnum())) {
                log.info("Realizando setagem de data de agendamento para remoção do plano...");
                planoClienteSistemico.setDataAgendamentoRemocao(LocalDate.parse(
                        planoClienteSistemico.getDataVencimento()).plusDays(1L).toString());
                log.info("Setagem de data de agendamento para remoção realizada com sucesso");
            } else {
                log.info("Realizando setagem de de status do plano para inativo...");
                planoClienteSistemico.setStatusPlanoEnum(StatusPlanoEnum.INATIVO);
                log.info("Setagem de status de plano como inativo realizada");
            }

            log.info("Realizando setagem de tipo de plano atualizada...");
            clienteSistemicoEncontrado.setPlano(planoClienteSistemico);
            log.info("Ok");

            log.info("Iniciando persistência do cliente sistêmico atualizado...");
            clientePosPersistencia = clienteSistemaRepositoryImpl
                    .implementaPersistencia(clienteSistemicoEncontrado);
            log.info("Persistência do cliente sistêmico atualizada realizada com sucesso");
        } catch (DataIntegrityViolationException e) {
            log.error("Ocorreu um erro durante o processo de persistência no banco de dados durante " +
                    "a atualização do plano de assinatura: " + e.getMessage());
            throw new InternalErrorException(Constantes.ERRO_INTERNO);
        } catch (Exception e) {
            log.error("Ocorreu um erro durante o processo de atualização do plano de assinatura: "
                    + e.getMessage());
            throw new InternalErrorException(Constantes.ERRO_INTERNO);
        }

        log.info("Iniciando acesso ao método de cancelamento do plano de assinatura na integradora asaas...");
        remocaoPlanoAsaasProxy.realizaCancelamentoDePlanoDeAssinaturaNaIntegradoraAsaas(
                clientePosPersistencia.getPlano().getCodigoAssinaturaAsaas());
        log.info("Plano cancelado na integradora com sucesso");

        log.info("Iniciando conversão de PlanoEntity para PlanoResponse...");
        PlanoResponse planoResponse = new PlanoResponse().buildFromEntity(clientePosPersistencia.getPlano());
        log.info("Conversão de tipagem para PlanoResponse realizada com sucesso");

        log.info("Cancelamento do plano realizado com sucesso");
        return planoResponse;
    }
}
