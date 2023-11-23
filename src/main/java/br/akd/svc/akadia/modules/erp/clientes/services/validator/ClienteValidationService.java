package br.akd.svc.akadia.modules.erp.clientes.services.validator;

import br.akd.svc.akadia.exceptions.InvalidRequestException;
import br.akd.svc.akadia.modules.erp.clientes.models.dto.request.ClienteRequest;
import br.akd.svc.akadia.modules.erp.clientes.models.entity.ClienteEntity;
import br.akd.svc.akadia.modules.erp.clientes.repository.ClienteRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Slf4j
@Service
public class ClienteValidationService {

    @Autowired
    ClienteRepository clienteRepository;

    public void validaSeChavesUnicasJaExistemParaNovoCliente(UUID uuidEmpresa,
                                                             ClienteRequest clienteRequest) {
        log.debug("Método de validação de chave única de cliente acessado...");
        if (clienteRequest.getCpfCnpj() != null)
            validaSeCpfCnpjJaExiste(uuidEmpresa, clienteRequest.getCpfCnpj());
        if (clienteRequest.getInscricaoEstadual() != null)
            validaSeInscricaoEstadualJaExiste(uuidEmpresa, clienteRequest.getInscricaoEstadual());
    }

    public void validaSeChavesUnicasJaExistemParaClienteAtualizado(UUID uuidEmpresa,
                                                                   ClienteRequest clienteRequest,
                                                                   ClienteEntity clientePreAtualizacao) {
        log.debug("Método de validação de chave única para atualização de cliente acessado...");
        if (clienteRequest.getCpfCnpj() != null && clientePreAtualizacao.getCpfCnpj() == null
                || clienteRequest.getCpfCnpj() != null
                && !clientePreAtualizacao.getCpfCnpj().equals(clienteRequest.getCpfCnpj()))
            validaSeCpfCnpjJaExiste(uuidEmpresa, clienteRequest.getCpfCnpj());
        if (clienteRequest.getInscricaoEstadual() != null && clientePreAtualizacao.getInscricaoEstadual() == null
                || clienteRequest.getInscricaoEstadual() != null
                && !clientePreAtualizacao.getInscricaoEstadual().equalsIgnoreCase(clienteRequest.getInscricaoEstadual()))
            validaSeInscricaoEstadualJaExiste(uuidEmpresa, clienteRequest.getInscricaoEstadual());
    }

    public void validaSeCpfCnpjJaExiste(UUID uuidEmpresa,
                                        String cpfCnpj) {
        log.debug("Método de validação de chave única de CPF/CNPJ acessado");

        if (Boolean.TRUE.equals(clienteRepository.verificaSeClienteAtivoJaExisteComCpfCnpjInformado(uuidEmpresa, cpfCnpj))) {
            String mensagemErro = cpfCnpj.length() == 14
                    ? "O CPF informado já existe"
                    : "O CNPJ informado já existe";
            log.warn(mensagemErro + ": {}", cpfCnpj);
            throw new InvalidRequestException(mensagemErro);
        }
        log.debug("Validação de chave única de CPF/CNPJ... OK");
    }

    public void validaSeInscricaoEstadualJaExiste(UUID uuidEmpresa,
                                                  String inscricaoEstadual) {
        log.debug("Método de validação de chave única de INSCRIÇÃO ESTADUAL acessado");

        if (Boolean.TRUE.equals(clienteRepository.verificaSeClienteAtivoJaExisteComInscricaoEstadualInformada(uuidEmpresa, inscricaoEstadual))) {
            String mensagemErro = inscricaoEstadual.length() == 14
                    ? "O CPF informado já existe"
                    : "O CNPJ informado já existe";
            log.warn(mensagemErro + ": {}", inscricaoEstadual);
            throw new InvalidRequestException(mensagemErro);
        }
        log.debug("Validação de chave única de INSCRIÇÃO ESTADUAL... OK");
    }

    public void validaSeClienteEstaExcluido(ClienteEntity cliente,
                                            String mensagemCasoEstejaExcluido) {
        log.debug("Método de validação de cliente excluído acessado");
        if (cliente.getExclusao() != null) {
            log.debug("Cliente de id {}: Validação de cliente já excluído falhou. Não é possível realizar operações " +
                    "em um cliente que já foi excluído.", cliente.getId());
            throw new InvalidRequestException(mensagemCasoEstejaExcluido);
        }
        log.debug("O cliente de id {} não está excluído", cliente.getId());
    }

}
