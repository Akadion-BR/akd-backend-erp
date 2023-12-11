package br.akd.svc.akadia.modules.erp.colaboradores.colaborador.models.dto.colaborador.request;

import br.akd.svc.akadia.modules.erp.colaboradores.colaborador.models.dto.expediente.request.ExpedienteRequest;
import br.akd.svc.akadia.modules.erp.colaboradores.colaborador.models.enums.ModeloContratacaoEnum;
import br.akd.svc.akadia.modules.erp.colaboradores.colaborador.models.enums.ModeloTrabalhoEnum;
import br.akd.svc.akadia.modules.erp.colaboradores.colaborador.models.enums.StatusColaboradorEnum;
import br.akd.svc.akadia.modules.erp.colaboradores.colaborador.models.enums.TipoOcupacaoEnum;
import br.akd.svc.akadia.modules.global.objects.acessosistema.dto.request.AcessoSistemaRequest;
import br.akd.svc.akadia.modules.global.objects.endereco.dto.request.EnderecoRequest;
import br.akd.svc.akadia.modules.global.objects.telefone.request.TelefoneRequest;
import lombok.*;

@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ColaboradorRequest {
    //TODO JAVAX VALIDATOR
    private String nome;
    private String dataNascimento;
    private String email;
    private String cpfCnpj;
    private Double salario;
    private String entradaEmpresa;
    private String saidaEmpresa;
    private String ocupacao;
    private TipoOcupacaoEnum tipoOcupacaoEnum;
    private ModeloContratacaoEnum modeloContratacaoEnum;
    private ModeloTrabalhoEnum modeloTrabalhoEnum;
    private StatusColaboradorEnum statusColaboradorEnum;
    private AcessoSistemaRequest acessoSistema;
    private EnderecoRequest endereco;
    private TelefoneRequest telefone;
    private ExpedienteRequest expediente;
}
