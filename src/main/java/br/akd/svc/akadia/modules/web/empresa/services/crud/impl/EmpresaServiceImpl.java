package br.akd.svc.akadia.modules.web.empresa.services.crud.impl;

import br.akd.svc.akadia.modules.erp.colaboradores.colaborador.models.dto.colaborador.response.ColaboradorResponse;
import br.akd.svc.akadia.modules.erp.colaboradores.colaborador.models.entity.colaborador.ColaboradorEntity;
import br.akd.svc.akadia.modules.erp.colaboradores.colaborador.models.entity.configuracaoperfil.ConfiguracaoPerfilEntity;
import br.akd.svc.akadia.modules.erp.colaboradores.colaborador.models.enums.*;
import br.akd.svc.akadia.modules.erp.colaboradores.colaborador.repository.impl.ColaboradorRepositoryImpl;
import br.akd.svc.akadia.modules.erp.colaboradores.colaborador.services.crud.ColaboradorService;
import br.akd.svc.akadia.modules.global.acessosistema.entity.AcessoSistemaEntity;
import br.akd.svc.akadia.modules.global.exclusao.entity.ExclusaoEntity;
import br.akd.svc.akadia.modules.web.clientesistema.models.entity.ClienteSistemaEntity;
import br.akd.svc.akadia.modules.web.clientesistema.repository.impl.ClienteSistemaRepositoryImpl;
import br.akd.svc.akadia.modules.web.clientesistema.services.validator.ClienteSistemaValidationService;
import br.akd.svc.akadia.modules.web.empresa.models.dto.request.EmpresaRequest;
import br.akd.svc.akadia.modules.web.empresa.models.dto.response.CriaEmpresaResponse;
import br.akd.svc.akadia.modules.web.empresa.models.dto.response.EmpresaResponse;
import br.akd.svc.akadia.modules.web.empresa.models.entity.EmpresaEntity;
import br.akd.svc.akadia.modules.web.empresa.models.entity.id.EmpresaId;
import br.akd.svc.akadia.modules.web.empresa.repository.impl.EmpresaRepositoryImpl;
import br.akd.svc.akadia.modules.web.empresa.services.crud.EmpresaService;
import br.akd.svc.akadia.modules.web.empresa.services.validator.EmpresaValidationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Slf4j
@Service
public class EmpresaServiceImpl implements EmpresaService {
    @Autowired
    EmpresaRepositoryImpl empresaRepositoryImpl;

    @Autowired
    ClienteSistemaRepositoryImpl clienteSistemaRepositoryImpl;

    @Autowired
    ColaboradorRepositoryImpl colaboradorRepositoryImpl;

    @Autowired
    ColaboradorService colaboradorService;

    @Autowired
    EmpresaValidationService empresaValidationService;

    @Autowired
    ClienteSistemaValidationService clienteSistemaValidationService;

    public CriaEmpresaResponse criaNovaEmpresa(UUID idClienteSistemaSessao,
                                               EmpresaRequest empresaRequest) {

        log.debug("Método de serviço responsável pelo tratamento do objeto recebido e criação de nova empresa acessado");
        log.debug("Empresa recebida: {} | Id do cliente: {}", empresaRequest, idClienteSistemaSessao);

        log.debug("Iniciando acesso ao método de implementação de busca de cliente por id...");
        ClienteSistemaEntity clienteSistema = clienteSistemaRepositoryImpl
                .implementaBuscaPorId(idClienteSistemaSessao);

        log.debug("Iniciando acesso ao método de validação de quantidade limite de empresas por cliente...");
        clienteSistemaValidationService
                .validaSeCadastroDeNovaEmpresaIraExcederLimiteDeEmpresasPorCliente(clienteSistema);

        log.debug("Iniciando acesso ao método de validação de variáveis de chave única para empresa...");
        empresaValidationService.validacaoDeChaveUnicaParaNovaEmpresa(empresaRequest);

        log.debug("Iniciando construção do objeto EmpresaEntity...");
        EmpresaEntity empresaEntity = new EmpresaEntity().buildFromRequest(clienteSistema, empresaRequest);
        log.debug("Construção de objeto EmpresaEntity realizado com sucesso");

        log.debug("Adicionando a empresa à lista de empresas do cliente...");
        clienteSistema.addEmpresa(empresaEntity);
        log.info("Empresa adicionada à lista de empresas do cliente com sucesso");

        log.debug("Iniciando acesso ao método de implementação de persistência do cliente sistêmico...");
        ClienteSistemaEntity clienteSistemaEntity =
                clienteSistemaRepositoryImpl.implementaPersistencia(clienteSistema);
        log.info("Persistência do cliente sistêmico realizada com sucesso");

        log.debug("Obtendo empresa criada da lista das empresas do cliente persistido...");
        EmpresaEntity empresaCriada = clienteSistemaEntity
                .obtemEmpresaPorRazaoSocial(empresaRequest.getRazaoSocial());
        log.info("Empresa persistida obtida com sucesso");

        log.debug("Iniciando acesso ao método de criação de novo colaborador admin para empresa...");
        ColaboradorEntity colaborador = criaColaboradorAdminParaNovaEmpresa(empresaCriada);
        log.info("Colaborador criado com sucesso para nova empresa");

        log.info("Iniciando objeto do tipo CriaEmpresaResponse...");
        CriaEmpresaResponse criaEmpresaResponse = new CriaEmpresaResponse(
                clienteSistemaEntity.getId(),
                new ColaboradorResponse().buildFromEntity(colaborador),
                new EmpresaResponse().buildFromEntity(empresaCriada));
        log.info("Objeto criado com sucesso");

        log.info("Método responsável por realizar a criação da empresa executado com sucesso. Retornando response...");
        return criaEmpresaResponse;
    }

    //TODO DESLOCAR PARA MÓDULO DE COLABORADORES
    public ColaboradorEntity criaColaboradorAdminParaNovaEmpresa(EmpresaEntity empresaEntity) {

        log.debug("Método de criação de colaborador ADMIN DEFAULT para nova empresa acessado");

        log.debug("Iniciando acesso ao método de geração de senha aleatória...");
        String senha = geraSenhaAleatoriaParaNovoLogin(empresaEntity);
        log.debug("Senha criada com sucesso");

        log.debug("Criando objeto para definir permissões do usuário...");
        Set<ModulosEnum> modulosLiberadosUsuario = new HashSet<>();

        log.debug("Iniciando acesso ao método para realizar carga no objeto de permissões do usuário...");
        habilitaTodosModulosColaborador(modulosLiberadosUsuario);

        log.debug("Construindo objeto e iniciando método de implementação de persistência de colaborador...");
        return colaboradorRepositoryImpl.implementaPersistencia(ColaboradorEntity.builder()
                        .dataCadastro(LocalDate.now().toString())
                        .horaCadastro(LocalTime.now().toString())
//                .matricula(colaboradorService.geraMatriculaUnica()) //TODO INCLUIR MÉTODO DE GERAR MATRÍCULA UNICA
                        .fotoPerfil(null)
                        .nome("admin")
                        .dataNascimento(null)
                        .email(null)
                        .cpfCnpj(null)
                        .salario(0.0)
                        .entradaEmpresa(null)
                        .saidaEmpresa(null)
                        .contratoContratacao(null)
                        .ocupacao("Administrador do sistema")
                        .tipoOcupacaoEnum(TipoOcupacaoEnum.ADMINISTRADOR)
                        .modeloTrabalhoEnum(ModeloTrabalhoEnum.PRESENCIAL)
                        .modeloContratacaoEnum(ModeloContratacaoEnum.CLT)
                        .statusColaboradorEnum(StatusColaboradorEnum.ATIVO)
                        .acessoSistema(AcessoSistemaEntity.builder()
                                .acessoSistemaAtivo(true)
                                .senha(senha)
                                .senhaCriptografada(new BCryptPasswordEncoder().encode(senha))
                                .permissaoEnum(PermissaoEnum.LEITURA_AVANCADA_ALTERACAO)
                                .privilegios(modulosLiberadosUsuario)
                                .build())
                        .configuracaoPerfil(ConfiguracaoPerfilEntity.builder()
                                .dataUltimaAtualizacao(LocalDate.now().toString())
                                .horaUltimaAtualizacao(LocalTime.now().toString())
                                .temaTelaEnum(TemaTelaEnum.TELA_CLARA)
                                .build())
                        .exclusao(null)
                        .endereco(null)
                        .telefone(null)
                        .expediente(null)
                        .dispensa(null)
                        .pontos(new ArrayList<>())
                        .historicoFerias(new ArrayList<>())
                        .advertencias(new ArrayList<>())
                        .acessos(new ArrayList<>())
                        .acoes(new ArrayList<>())
                        .empresa(empresaEntity)
                        .build()
        );
    }

    //TODO DESLOCAR PARA UTILITÁRIO MÓDULO DE COLABORADORES
    public void habilitaTodosModulosColaborador(Set<ModulosEnum> privilegios) {
        log.debug("Método de setagem de todos os privilégios à lista de privilégios do colaborador acessado");
        log.debug("Iniciando setagem de privilégios...");
        privilegios.add(ModulosEnum.HOME);
        privilegios.add(ModulosEnum.CLIENTES);
        privilegios.add(ModulosEnum.VENDAS);
        privilegios.add(ModulosEnum.PDV);
        privilegios.add(ModulosEnum.ESTOQUE);
        privilegios.add(ModulosEnum.DESPESAS);
        privilegios.add(ModulosEnum.FECHAMENTOS);
        privilegios.add(ModulosEnum.PATRIMONIOS);
        privilegios.add(ModulosEnum.FORNECEDORES);
        privilegios.add(ModulosEnum.COMPRAS);
        privilegios.add(ModulosEnum.COLABORADORES);
        privilegios.add(ModulosEnum.PRECOS);
        log.debug("Lista de privilégios preenchida com sucesso");
    }

    //TODO DESLOCAR PARA UTILITÁRIO DO MÓDULO DE COLABORADORES
    public String geraSenhaAleatoriaParaNovoLogin(EmpresaEntity empresaEntity) {
        log.debug("Método de criação de senha aleatória acessado");
        return "@" + empresaEntity.getNome().replace(" ", "").substring(0, 2).toUpperCase() +
                empresaEntity.getCnpj()
                        .replace("-", ".")
                        .replace(".", "")
                        .replace("/", "")
                        .substring(0, 2) +
                empresaEntity.getDataCadastro().substring(0, 2) +
                empresaEntity.getSegmentoEmpresaEnum().getDesc().substring(0, 2).toUpperCase();
    }

    public EmpresaResponse atualizaEmpresa(UUID idClienteSistemaSessao,
                                           UUID uuidEmpresa,
                                           EmpresaRequest empresaRequest) {

        log.debug("Método de serviço de atualização de empresa acessado");

        log.debug("Iniciando acesso ao método de implementação de busca de empresa por id...");
        EmpresaEntity empresaPreAtualizacao = empresaRepositoryImpl
                .implementaBuscaPorId(new EmpresaId(idClienteSistemaSessao, uuidEmpresa));

        log.debug("Iniciando validação se empresa a ser alterada foi deletada anteriormente...");
        empresaValidationService.validaSeEmpresaJaEstaExcluida(empresaPreAtualizacao,
                "A empresa selecionada não pode ser alterada, pois foi excluída");

        log.debug("Iniciando acesso ao método de validação de chaves únicas para a atualização da empresa...");
        empresaValidationService.validacaoDeChaveUnicaParaAtualizacaoDeEmpresa(empresaRequest, empresaPreAtualizacao);

        log.debug("Iniciando setagem de atributos atualizados da empresa...");
        EmpresaEntity empresaAtualizada = new EmpresaEntity()
                .updateFromRequest(empresaPreAtualizacao, empresaRequest);
        log.debug("Setagem de atributos finalizada com sucesso");

        log.debug("Iniciando acesso ao método de implementação de persistência de empresa...");
        EmpresaEntity empresaPersistida = empresaRepositoryImpl.implementaPersistencia(empresaAtualizada);
        log.info("Empresa atualizada com sucesso");

        log.info("Iniciando conversão da empresa persistida para objeto do tipo response...");
        EmpresaResponse empresaResponse = new EmpresaResponse().buildFromEntity(empresaPersistida);
        log.info("Conversão para objeto do tipo EmpresaResponse realizada com sucesso. Retornando objeto...");
        return empresaResponse;
    }

    public EmpresaResponse removeEmpresa(UUID idClienteSistemaSessao,
                                         UUID uuidEmpresa) {

        log.debug("Método de serviço de remoção de empresa acessado");

        log.debug("Iniciando acesso ao método de implementação de busca de empresa por id...");
        EmpresaEntity empresaEncontrada = empresaRepositoryImpl
                .implementaBuscaPorId(new EmpresaId(idClienteSistemaSessao, uuidEmpresa));

        log.debug("Iniciando acesso ao método de validação de exclusão de empresa que já foi excluída...");
        empresaValidationService.validaSeEmpresaJaEstaExcluida(
                empresaEncontrada, "A empresa selecionada já foi excluída");

        log.debug("Atualizando objeto Exclusao da empresa com dados referentes à sua exclusão...");
        ExclusaoEntity exclusaoEntity = new ExclusaoEntity().constroiObjetoExclusao();

        empresaEncontrada.setExclusao(exclusaoEntity);
        log.debug("Objeto Exclusao da empresa de id {} setado com sucesso", uuidEmpresa);

        log.debug("Persistindo empresa excluída no banco de dados...");
        EmpresaEntity empresaExcluida = empresaRepositoryImpl.implementaPersistencia(empresaEncontrada);
        log.info("Remoção da empresa realizada com sucesso");

        log.info("Iniciando conversão da empresa excluída para objeto do tipo EmpresaResponse...");
        EmpresaResponse empresaResponse = new EmpresaResponse().buildFromEntity(empresaExcluida);

        log.info("Objeto EmpresaResponse criado com sucesso. Retornando objeto...");
        return empresaResponse;
    }
}
