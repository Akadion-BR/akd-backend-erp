package br.akd.svc.akadia.modules.web.clientesistema.services.validator;

import br.akd.svc.akadia.exceptions.InvalidRequestException;
import br.akd.svc.akadia.modules.global.objects.exclusao.entity.ExclusaoEntity;
import br.akd.svc.akadia.modules.web.clientesistema.models.dto.request.atualizacao.AtualizaClienteSistemaRequest;
import br.akd.svc.akadia.modules.web.clientesistema.models.dto.request.criacao.ClienteSistemaRequest;
import br.akd.svc.akadia.modules.web.clientesistema.models.entity.ClienteSistemaEntity;
import br.akd.svc.akadia.modules.web.clientesistema.repository.ClienteSistemaRepository;
import br.akd.svc.akadia.modules.web.empresa.models.entity.EmpresaEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class ClienteSistemaValidationService {

    @Autowired
    ClienteSistemaRepository clienteSistemaRepository;

    public void validaSeCadastroDeNovaEmpresaIraExcederLimiteDeEmpresasPorCliente(ClienteSistemaEntity clienteSistema) {

        log.debug("Realizando filtro por empresas ativas do cliente...");
        List<EmpresaEntity> empresasAtivasCliente =
                clienteSistema.getEmpresas().stream()
                        .filter((EmpresaEntity filtroEmpresa) -> filtroEmpresa.getExclusao() == null)
                        .collect(Collectors.toList());

        log.debug("Empresas ativas do cliente: {}", empresasAtivasCliente);

        log.debug("Verificando se cliente já possui quantidade limite de empresas cadastradas para o seu plano...");
        if (clienteSistema.getPlano().getTipoPlanoEnum().getQtdLimiteEmpresasCadastradas()
                <= clienteSistema.getEmpresas().size()) {
            log.warn("O cliente já possui o número máximo de empresas cadastradas: {}. O permitido é: {}",
                    empresasAtivasCliente.size(), clienteSistema.getPlano().getTipoPlanoEnum().getQtdLimiteEmpresasCadastradas());
            throw new InvalidRequestException("Este cliente já possui o número máximo de empresas cadastradas em seu plano: "
                    + clienteSistema.getPlano().getTipoPlanoEnum().getQtdLimiteEmpresasCadastradas() + " (max) com "
                    + empresasAtivasCliente.size() + " empresas cadastradas");
        }
    }

    public void realizaValidacoesParaNovoClienteSistemico(ClienteSistemaRequest clienteSistemaRequest) {
        validaSeEmailJaExiste(clienteSistemaRequest.getEmail());
        validaSeCpfJaExiste(clienteSistemaRequest.getCpf());
    }

    public void realizaValidacoesParaAtualizacaoDeClienteSistemico(AtualizaClienteSistemaRequest atualizaClienteSistemaRequest,
                                                                   ClienteSistemaEntity clientePreAtualizacao) {
        validaSeClienteSistemicoEstaExcluido(clientePreAtualizacao.getExclusao());
        validaSeChavesUnicasJaExistemParaClienteSistemicoAtualizado(
                atualizaClienteSistemaRequest, clientePreAtualizacao);
    }

    public void validaSeClienteSistemicoEstaExcluido(ExclusaoEntity exclusaoEntity) {
        log.info("Método de validação de planoEntity excluído acessado");
        if (exclusaoEntity != null) {
            log.info("Validação de cliente sistêmico já excluído falhou. Não é possível realizar " +
                    "operações em um cliente sistêmico que já foi excluído.");
            throw new InvalidRequestException("Não é possível realizar operações em um cliente " +
                    "sistêmico que já foi excluído");
        }
        log.info("A verificação de item excluído passou com sucesso");
    }

    public void validaSeChavesUnicasJaExistemParaClienteSistemicoAtualizado(AtualizaClienteSistemaRequest atualizaClienteSistemaRequest,
                                                                            ClienteSistemaEntity clientePreAtualizacao) {
        log.debug("Método de validação de chave única para atualização de cliente acessado...");
        if (atualizaClienteSistemaRequest.getEmail() != null && clientePreAtualizacao.getEmail() == null
                || atualizaClienteSistemaRequest.getEmail() != null
                && !clientePreAtualizacao.getEmail().equals(atualizaClienteSistemaRequest.getEmail()))
            validaSeEmailJaExiste(atualizaClienteSistemaRequest.getEmail());
    }

    public void validaSeEmailJaExiste(String email) {
        log.debug("Método de validação de chave única de e-mail acessado");

        if (Boolean.TRUE.equals(clienteSistemaRepository.verificaSeClienteAtivoJaExisteComEmailInformado(email))) {
            log.warn("O e-mail informado já existe: {}", email);
            throw new InvalidRequestException("O e-mail informado já existe");
        }
        log.debug("Validação de chave única de e-mail... OK");
    }

    public void validaSeCpfJaExiste(String cpf) {
        log.debug("Método de validação de chave única de CPF acessado");

        if (Boolean.TRUE.equals(clienteSistemaRepository.verificaSeClienteAtivoJaExisteComCpfInformado(cpf))) {
            log.warn("O CPF informado já existe: {}", cpf);
            throw new InvalidRequestException("O CPF informado já existe");
        }
        log.debug("Validação de chave única de CPF... OK");
    }

}
