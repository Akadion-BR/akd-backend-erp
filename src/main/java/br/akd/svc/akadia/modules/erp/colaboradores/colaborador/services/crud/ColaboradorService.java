package br.akd.svc.akadia.modules.erp.colaboradores.colaborador.services.crud;

import br.akd.svc.akadia.modules.erp.colaboradores.colaborador.models.dto.colaborador.response.ColaboradorResponse;
import br.akd.svc.akadia.modules.erp.colaboradores.colaborador.models.dto.colaborador.response.page.ColaboradorPageResponse;
import br.akd.svc.akadia.modules.erp.colaboradores.colaborador.models.entity.colaborador.id.ColaboradorId;
import br.akd.svc.akadia.modules.global.imagem.response.ImagemResponse;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

public interface ColaboradorService {

    @Transactional
    String criaNovoColaborador(ColaboradorId idColaboradorSessao,
                               MultipartFile contratoColaborador,
                               String colaboradorEmJson) throws IOException;

    ColaboradorPageResponse realizaBuscaPaginadaPorColaboradores(ColaboradorId idColaboradorSessao,
                                                                 Pageable pageable,
                                                                 String campoBusca);

    ImagemResponse obtemImagemPerfilColaborador(ColaboradorId idColaboradorSessao,
                                                UUID idColaborador);

    List<String> obtemTodasOcupacoesEmpresa(ColaboradorId idColaboradorSessao);

    ColaboradorResponse realizaBuscaDeColaboradorPorId(ColaboradorId idColaboradorSessao,
                                                       UUID idColaborador);

    @Transactional
    ColaboradorResponse atualizaColaborador(ColaboradorId idColaboradorSessao,
                                            UUID idColaborador,
                                            MultipartFile contratoColaborador,
                                            String colaboradorEmJson) throws IOException;

    @Transactional
    ColaboradorResponse atualizaImagemPerfilColaborador(ColaboradorId idColaboradorSessao,
                                                        UUID idColaborador,
                                                        MultipartFile fotoPerfil) throws IOException;

    @Transactional
    ColaboradorResponse removeColaborador(ColaboradorId idColaboradorSessao,
                                          UUID idColaborador);

    @Transactional
    void removeColaboradoresEmMassa(ColaboradorId idColaboradorSessao,
                                    List<UUID> uuidsColaborador);

}
