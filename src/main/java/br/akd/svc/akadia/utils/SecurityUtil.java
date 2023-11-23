package br.akd.svc.akadia.utils;

import br.akd.svc.akadia.exceptions.UnauthorizedAccessException;
import br.akd.svc.akadia.modules.erp.colaboradores.colaborador.models.entity.colaborador.ColaboradorEntity;
import br.akd.svc.akadia.modules.erp.colaboradores.colaborador.models.entity.colaborador.id.ColaboradorId;
import br.akd.svc.akadia.modules.erp.colaboradores.colaborador.models.enums.PermissaoEnum;
import br.akd.svc.akadia.modules.erp.colaboradores.colaborador.repository.ColaboradorRepository;
import br.akd.svc.akadia.modules.global.acessosistema.entity.AcessoSistemaEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class SecurityUtil {

    @Autowired
    ColaboradorRepository colaboradorRepository;

    SecurityUtil() {
    }

    public void verificaSePodeRealizarAlteracoes(AcessoSistemaEntity acesso) {
        if (!acesso.getPermissaoEnum().equals(PermissaoEnum.LEITURA_AVANCADA_ALTERACAO)
                && !acesso.getPermissaoEnum().equals(PermissaoEnum.LEITURA_BASICA_ALTERACAO))
            throw new UnauthorizedAccessException("Você não possui permissão para realizar alterações nos dados do sistema. " +
                    "Entre em contato com o administrador e tente novamente");
    }

    public ColaboradorEntity obtemUsuarioSessao(ColaboradorId colaboradorId) {
        log.info("Realizando a busca do colaborador logado na sessão atual pelo id do principal do token: {}",
                colaboradorId);
        ColaboradorEntity colaboradorLogado = colaboradorRepository.findById(colaboradorId)
                .orElseThrow(() -> {
                    log.error("Nenhum colaborador foi encontrado através do id informado pelo token: {}", colaboradorId);
                    return new UnauthorizedAccessException("O token enviado é inválido");
                });
        log.info("Busca da empresa realizada com sucesso");

        return colaboradorLogado;
    }

}
