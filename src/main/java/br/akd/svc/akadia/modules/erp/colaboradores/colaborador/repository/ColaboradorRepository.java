package br.akd.svc.akadia.modules.erp.colaboradores.colaborador.repository;

import br.akd.svc.akadia.modules.erp.colaboradores.acao.models.entity.AcaoEntity;
import br.akd.svc.akadia.modules.erp.colaboradores.acesso.models.entity.AcessoEntity;
import br.akd.svc.akadia.modules.erp.colaboradores.advertencia.models.entity.AdvertenciaEntity;
import br.akd.svc.akadia.modules.erp.colaboradores.colaborador.models.entity.colaborador.ColaboradorEntity;
import br.akd.svc.akadia.modules.erp.colaboradores.colaborador.models.entity.colaborador.id.ColaboradorId;
import br.akd.svc.akadia.modules.global.objects.imagem.entity.ImagemEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ColaboradorRepository extends JpaRepository<ColaboradorEntity, ColaboradorId> {

    Boolean existsByMatricula(String matricula);

    Optional<ColaboradorEntity> findByMatricula(String matricula);

    @Query("SELECT c FROM ColaboradorEntity c " +
            "WHERE c.empresa.id = ?1 " +
            "AND c.exclusao IS NULL")
    List<ColaboradorEntity> buscaTodos(UUID uuidColaborador);

    @Query("SELECT c FROM ColaboradorEntity c " +
            "WHERE c.matricula = ?2 " +
            "AND c.empresa.id = ?1")
    Optional<ColaboradorEntity> buscaPorMatricula(UUID idEmpresa,
                                                  String matricula);

    @Query("SELECT c.fotoPerfil FROM ColaboradorEntity c " +
            "WHERE c.id=?1 " +
            "AND c.empresa.id = ?2 " +
            "AND c.exclusao IS NULL")
    Optional<ImagemEntity> buscaImagemPerfilPorId(UUID uuidEmpresa,
                                                  UUID uuidColaborador);

    @Query("SELECT c FROM ColaboradorEntity c " +
            "WHERE c.id=?1 " +
            "AND c.empresa.id = ?2 " +
            "AND c.exclusao IS NULL")
    Optional<ColaboradorEntity> buscaPorId(UUID uuidEmpresa,
                                           UUID uuidColaborador);

    @Query("SELECT a FROM ColaboradorEntity c JOIN c.acoes a " +
            "WHERE c.id=?1 " +
            "AND c.empresa.id = ?2 " +
            "AND c.exclusao IS NULL")
    Page<AcaoEntity> buscaAcoesPorIdColaborador(Pageable pageable,
                                                UUID uuidEmpresa,
                                                UUID uuidColaborador);

    @Query("SELECT a FROM ColaboradorEntity c " +
            "JOIN c.advertencias a " +
            "WHERE c.id = ?1 " +
            "AND c.empresa.id = ?2 " +
            "AND c.exclusao IS NULL")
    Page<AdvertenciaEntity> buscaAdvertenciasPorIdColaborador(Pageable pageable,
                                                              UUID uuidEmpresa,
                                                              UUID uuidColaborador);

    @Query("SELECT a FROM ColaboradorEntity c join c.acessos a " +
            "WHERE c.id=?2 " +
            "AND c.empresa.id = ?1 " +
            "AND c.exclusao IS NULL")
    Page<AcessoEntity> buscaAcessosPorIdColaborador(Pageable pageable,
                                                    UUID idEmpresa,
                                                    UUID uuidColaborador);

    @Query("SELECT upper(c.ocupacao) FROM ColaboradorEntity c " +
            "WHERE c.empresa.id = ?1 " +
            "AND c.ocupacao IS NOT NULL")
    List<String> buscaTodasOcupacoesDaEmpresa(UUID uuidEmpresa);

    @Query("SELECT c FROM ColaboradorEntity c " +
            "WHERE c.empresa.id = ?1 " +
            "AND (?2 IS NULL OR (upper(c.nome) LIKE ?2% and c.exclusao IS NULL " +
            "OR upper(c.email) LIKE ?2% and c.exclusao IS NULL " +
            "OR upper(c.cpfCnpj) LIKE ?2% and c.exclusao IS NULL))")
    Page<ColaboradorEntity> buscaPaginadaPorColaboradores(Pageable pageable,
                                                          UUID uuidEmpresa,
                                                          String busca);
}
