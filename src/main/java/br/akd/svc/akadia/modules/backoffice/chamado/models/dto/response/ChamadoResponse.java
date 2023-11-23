package br.akd.svc.akadia.modules.backoffice.chamado.models.dto.response;

import br.akd.svc.akadia.modules.backoffice.chamado.avaliacao.dto.response.AvaliacaoResponse;
import br.akd.svc.akadia.modules.backoffice.chamado.mensagem.dto.response.MensagemResponse;
import br.akd.svc.akadia.modules.backoffice.chamado.mensagem.entity.MensagemEntity;
import br.akd.svc.akadia.modules.backoffice.chamado.models.entity.ChamadoEntity;
import lombok.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ChamadoResponse {
    private UUID id;
    private String dataCriacao;
    private String horaCriacao;
    private Long ticket;
    private String titulo;
    private String descricao;
    private String dataBaixa;
    private String horaBaixa;
    private String categoriaChamadoEnum;
    private String statusChamadoEnum;
    private String nomeAtendenteResponsavel;
    private AvaliacaoResponse avaliacao;
    private List<MensagemResponse> mensagens = new ArrayList<>();

    public ChamadoResponse buildFromEntity(ChamadoEntity chamado) {
        return chamado != null
                ? ChamadoResponse.builder()
                .id(chamado.getId())
                .dataCriacao(chamado.getDataCadastro())
                .horaCriacao(chamado.getHoraCadastro())
                .ticket(chamado.getTicket())
                .titulo(chamado.getTitulo())
                .descricao(chamado.getDescricao())
                .dataBaixa(chamado.getDataBaixa())
                .horaBaixa(chamado.getHoraBaixa())
                .categoriaChamadoEnum(chamado.getCategoriaChamadoEnum().getDesc())
                .statusChamadoEnum(chamado.getStatusChamadoEnum().getDesc())
                .nomeAtendenteResponsavel(chamado.getAtendenteResponsavel().getNome())
                .avaliacao(new AvaliacaoResponse().buildFromEntity(chamado.getAvaliacao()))
                .mensagens(new MensagemResponse().buildListFromEntity(chamado.getMensagens()))
                .build()
                : null;
    }

    public List<ChamadoResponse> buildListFromEntity(List<ChamadoEntity> chamadoEntities) {
        List<ChamadoResponse> chamadoResponses = new ArrayList<>();
        chamadoEntities.forEach(chamado -> chamadoResponses.add(buildFromEntity(chamado)));
        return chamadoResponses;
    }
}
