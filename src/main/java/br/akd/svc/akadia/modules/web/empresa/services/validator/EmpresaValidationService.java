package br.akd.svc.akadia.modules.web.empresa.services.validator;

import br.akd.svc.akadia.exceptions.InvalidRequestException;
import br.akd.svc.akadia.modules.web.empresa.models.dto.request.EmpresaRequest;
import br.akd.svc.akadia.modules.web.empresa.models.entity.EmpresaEntity;
import br.akd.svc.akadia.modules.web.empresa.repository.impl.EmpresaRepositoryImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

@Slf4j
@Service
public class EmpresaValidationService {

    @Autowired
    EmpresaRepositoryImpl empresaRepositoryImpl;

    public void validaSeEmpresaJaEstaExcluida(EmpresaEntity empresa,
                                              String mensagemErro) {
        if (empresa.getExclusao() != null) {
            log.warn("{}. Empresa: {}", mensagemErro, empresa);
            throw new InvalidRequestException(mensagemErro);
        }
    }

    public void validacaoDeChaveUnicaParaNovaEmpresa(EmpresaRequest empresaRequest) {
        log.debug("Método de validação de chave única para criação de nova empresa acessado...");
        //TODO VALIDAR SE OBJECTUTILS FUNCIONAM
        if (ObjectUtils.isEmpty(empresaRequest.getCnpj()))
            validaSeCnpjJaExiste(empresaRequest.getCnpj());

        if (ObjectUtils.isEmpty(empresaRequest.getRazaoSocial()))
            validaSeRazaoSocialJaExiste(empresaRequest.getRazaoSocial());

        if (ObjectUtils.isEmpty(empresaRequest.getEndpoint()))
            validaSeEndpointJaExiste(empresaRequest.getEndpoint());

        if (ObjectUtils.isEmpty(empresaRequest.getInscricaoEstadual()))
            validaSeInscricaoEstadualJaExiste(empresaRequest.getInscricaoEstadual());

        if (ObjectUtils.isEmpty(empresaRequest.getInscricaoMunicipal()))
            validaSeInscricaoEstadualJaExiste(empresaRequest.getInscricaoMunicipal());
    }

    public void validacaoDeChaveUnicaParaAtualizacaoDeEmpresa(EmpresaRequest empresaRequest,
                                                              EmpresaEntity empresaEditada) {
        log.debug("Método de validação de chave única para atualização de empresa acessado...");
        if (ObjectUtils.isEmpty(empresaRequest.getCnpj())
                && !empresaEditada.getCnpj().equals(empresaRequest.getCnpj()))
            validaSeCnpjJaExiste(empresaRequest.getCnpj());

        if (ObjectUtils.isEmpty(empresaRequest.getEndpoint())
                && !empresaEditada.getEndpoint().equalsIgnoreCase(empresaRequest.getEndpoint()))
            validaSeEndpointJaExiste(empresaRequest.getEndpoint());

        if (ObjectUtils.isEmpty(empresaRequest.getRazaoSocial())
                && !empresaEditada.getRazaoSocial().equalsIgnoreCase(empresaRequest.getRazaoSocial()))
            validaSeRazaoSocialJaExiste(empresaRequest.getRazaoSocial());

        if (ObjectUtils.isEmpty(empresaRequest.getInscricaoEstadual())
                && !empresaEditada.getInscricaoEstadual().equalsIgnoreCase(empresaRequest.getInscricaoEstadual()))
            validaSeInscricaoEstadualJaExiste(empresaRequest.getInscricaoEstadual());

        if (ObjectUtils.isEmpty(empresaRequest.getInscricaoMunicipal())
                && !empresaEditada.getInscricaoMunicipal().equalsIgnoreCase(empresaRequest.getInscricaoMunicipal()))
            validaSeInscricaoMunicipalJaExiste(empresaRequest.getInscricaoMunicipal());
    }

    public void validaSeCnpjJaExiste(String cnpj) {
        log.debug("Método de validação de chave única de CNPJ acessado");
        if (empresaRepositoryImpl.implementaBuscaPorCnpj(cnpj).isPresent()) {
            log.warn("O cnpj informado já existe");
            throw new InvalidRequestException("O cnpj informado já existe");
        }
        log.debug("Validação de chave única de CNPJ... OK");
    }

    public void validaSeEndpointJaExiste(String endpoint) {
        log.debug("Método de validação de chave única de endpoint acessado");
        if (empresaRepositoryImpl.implementaBuscaPorEndpoint(endpoint).isPresent()) {
            log.warn("O endpoint informado já existe");
            throw new InvalidRequestException("O endpoint informado já existe");
        }
        log.debug("Validação de chave única de endpoint... OK");
    }

    public void validaSeRazaoSocialJaExiste(String razaoSocial) {
        log.debug("Método de validação de chave única de Razão Social acessado");
        if (empresaRepositoryImpl.implementaBuscaPorRazaoSocial(razaoSocial).isPresent()) {
            log.warn("A razão social informada já existe");
            throw new InvalidRequestException("A razão social informada já existe");
        }
        log.debug("Validação de chave única de Razão social... OK");
    }

    public void validaSeInscricaoEstadualJaExiste(String inscricaoEstadual) {
        log.debug("Método de validação de chave única de Inscrição estadual acessado");
        if (empresaRepositoryImpl.implementaBuscaPorInscricaoEstadual(inscricaoEstadual).isPresent()) {
            log.warn("A inscrição estadual informada já existe");
            throw new InvalidRequestException("A inscrição estadual informada já existe");
        }
        log.debug("Validação de chave única de inscrição estadual... OK");
    }

    public void validaSeInscricaoMunicipalJaExiste(String inscricaoMunicipal) {
        log.debug("Método de validação de chave única de Inscrição municipal acessado");
        if (empresaRepositoryImpl.implementaBuscaPorInscricaoMunicipal(inscricaoMunicipal).isPresent()) {
            log.warn("A inscrição municipal informada já existe");
            throw new InvalidRequestException("A inscrição municipal informada já existe");
        }
        log.debug("Validação de chave única de inscrição municipal... OK");
    }

}
