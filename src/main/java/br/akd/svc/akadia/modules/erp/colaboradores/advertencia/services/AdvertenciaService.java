package br.akd.svc.akadia.modules.erp.colaboradores.advertencia.services;

import br.akd.svc.akadia.modules.erp.colaboradores.advertencia.models.dto.response.AdvertenciaPageResponse;
import br.akd.svc.akadia.modules.erp.colaboradores.advertencia.models.enums.StatusAdvertenciaEnum;
import br.akd.svc.akadia.modules.erp.colaboradores.colaborador.models.entity.colaborador.id.ColaboradorId;
import br.akd.svc.akadia.modules.global.objects.arquivo.entity.ArquivoEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.UUID;

public interface AdvertenciaService {

    @Transactional
    void geraAdvertenciaColaborador(ColaboradorId idColaboradorSessao,
                                    HttpServletResponse res,
                                    UUID idColaboradorAlvo,
                                    MultipartFile contratoAdvertencia,
                                    String advertenciaEmJson) throws IOException;

    AdvertenciaPageResponse obtemAdvertenciasPaginadasColaborador(Pageable pageable,
                                                                  ColaboradorId idColaboradorSessao,
                                                                  UUID idColaborador);

    void geraPdfPadraoAdvertencia(ColaboradorId idColaboradorSessao,
                                  HttpServletResponse res,
                                  UUID idColaborador,
                                  UUID idAdvertencia) throws IOException;

    ArquivoEntity obtemAnexoAdvertencia(ColaboradorId idColaboradorSessao,
                                        UUID idColaborador,
                                        UUID idAdvertencia);

    @Transactional
    void alteraStatusAdvertencia(ColaboradorId idColaboradorSessao,
                                 StatusAdvertenciaEnum statusAdvertenciaEnum,
                                 UUID idColaborador,
                                 UUID idAdvertencia);

    @Transactional
    void anexaArquivoAdvertencia(ColaboradorId idColaboradorSessao,
                                 MultipartFile anexo,
                                 UUID idColaborador,
                                 UUID idAdvertencia) throws IOException;

    @Transactional
    void removerAdvertencia(ColaboradorId idColaboradorSessao,
                            UUID idColaborador,
                            UUID idAdvertencia);
}
