package br.akd.svc.akadia.modules.web.clientesistema.proxy.models.request;

import br.akd.svc.akadia.modules.global.endereco.dto.request.EnderecoRequest;
import br.akd.svc.akadia.modules.web.clientesistema.models.dto.request.atualizacao.AtualizaClienteSistemaRequest;
import br.akd.svc.akadia.modules.web.clientesistema.models.dto.request.criacao.ClienteSistemaRequest;
import br.akd.svc.akadia.modules.web.clientesistema.proxy.utils.ClienteAsaasProxyUtils;
import lombok.*;

@Builder
@ToString
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ClienteAsaasRequest {

    private String name;
    private String cpfCnpj;
    private String email;
    private String phone;
    private String mobilePhone;
    private String address;
    private String addressNumber;
    private String complement;
    private String province;
    private String postalCode;
    private String externalReference;
    private Boolean notificationDisabled;
    private String additionalEmails;
    private String municipalInscription;
    private String stateInscription;
    private String groupName;

    public ClienteAsaasRequest constroiObjetoCriaClienteAsaasRequest(ClienteSistemaRequest clienteSistemaRequest) {
        EnderecoRequest endereco = clienteSistemaRequest.getEndereco();
        return ClienteAsaasRequest.builder()
                .name(clienteSistemaRequest.getNome())
                .cpfCnpj(clienteSistemaRequest.getCpf())
                .email(clienteSistemaRequest.getEmail())
                .phone(ClienteAsaasProxyUtils.obtemTelefoneFixoCliente(clienteSistemaRequest.getTelefone()))
                .mobilePhone(ClienteAsaasProxyUtils.obtemTelefoneCelularCliente(clienteSistemaRequest.getTelefone()))
                .address(endereco != null ? endereco.getLogradouro() : null)
                .addressNumber(endereco != null ? endereco.getNumero().toString() : null)
                .complement(endereco != null ? endereco.getComplemento() : null)
                .province(endereco != null ? endereco.getBairro() : null)
                .postalCode(endereco != null ? endereco.getCodigoPostal() : null)
                .externalReference(clienteSistemaRequest.getCpf())
                .notificationDisabled(true)
                .additionalEmails(null)
                .municipalInscription(null)
                .stateInscription(null)
                .groupName("AKD")
                .build();
    }

    public ClienteAsaasRequest constroiObjetoCriaClienteAsaasRequestParaAtualizacao(
            AtualizaClienteSistemaRequest atualizaClienteSistemaRequest) {
        EnderecoRequest endereco = atualizaClienteSistemaRequest.getEndereco();
        return ClienteAsaasRequest.builder()
                .name(atualizaClienteSistemaRequest.getNome())
                .cpfCnpj(atualizaClienteSistemaRequest.getCpf())
                .email(atualizaClienteSistemaRequest.getEmail())
                .phone(ClienteAsaasProxyUtils.obtemTelefoneFixoCliente(atualizaClienteSistemaRequest.getTelefone()))
                .mobilePhone(ClienteAsaasProxyUtils.obtemTelefoneCelularCliente(atualizaClienteSistemaRequest.getTelefone()))
                .address(endereco != null ? endereco.getLogradouro() : null)
                .addressNumber(endereco != null ? endereco.getNumero().toString() : null)
                .complement(endereco != null ? endereco.getComplemento() : null)
                .province(endereco != null ? endereco.getBairro() : null)
                .postalCode(endereco != null ? endereco.getCodigoPostal() : null)
                .externalReference(atualizaClienteSistemaRequest.getCpf())
                .notificationDisabled(true)
                .additionalEmails(null)
                .municipalInscription(null)
                .stateInscription(null)
                .groupName("AKD")
                .build();
    }
}
