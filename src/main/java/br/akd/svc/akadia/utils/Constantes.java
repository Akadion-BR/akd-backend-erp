package br.akd.svc.akadia.utils;

public class Constantes {

    Constantes() {
    }

    public static final String INICIANDO_SALVAMENTO_HISTORICO_COLABORADOR =
            "Iniciando acesso ao método de salvamento de histórico de ações do colaborador... ";

    public static final String INICIANDO_SALVAMENTO_HISTORICO_DESPESA =
            "Iniciando acesso ao método de salvamento de histórico de ações da despesa... ";

    public static final String INICIANDO_SALVAMENTO_HISTORICO_PATRIMONIO =
            "Iniciando acesso ao método de salvamento de histórico de ações do patrimônio... ";

    public static final String INICIANDO_SALVAMENTO_HISTORICO_PRODUTO =
            "Iniciando acesso ao método de salvamento de histórico de ações do produto... ";

    public static final String VERIFICANDO_SE_COLABORADOR_PODE_ALTERAR_DADOS =
            "Iniciando acesso ao método de verificação se colaborador logado possui nível de permissão " +
                    "suficiente para realizar alterações";

    public static final String INICIANDO_IMPL_PERSISTENCIA_CLIENTE =
            "Iniciando acesso ao método de implementação de persistência do cliente...";

    public static final String INICIANDO_IMPL_PERSISTENCIA_COLABORADOR =
            "Iniciando acesso ao método de implementação de persistência do colaborador...";
    public static final String OBJETO_PAGAMENTO_CRIADO = "Objeto pagamento criado: {}";

    public static final String OBJETO_EXCLUSAO_DESPESA_SETADO_COM_SUCESSO =
            "Objeto Exclusao da despesa de id {} setado com sucesso";
    public static final String ATUALIZANDO_EXCLUSAO_DESPESA =
            "Atualizando objeto Exclusao da despesa com dados referentes à sua exclusão...";

    public static final String DESPESA_JA_EXCLUIDA = "A despesa selecionada já foi excluída";
    public static final String PERSISTINDO_DESPESA_EXCLUIDA =
            "Persistindo despesa excluída no banco de dados...";

    public static final String RETORNO_INTEGRADORA_NULO = "O retorno da integradora é nulo";
    public static final String TOKEN_ASAAS = "TOKEN_ASAAS";
    public static final String FALHA_COMUNICACAO_ASAAS =
            "Ocorreu uma falha na comunicação com a integradora de pagamentos: ";

    public static final String ERRO_INTERNO = "Ocorreu um erro interno no servidor da aplicação. Favor " +
            "entrar com contato com o suporte";

    public static final String CONVERSAO_SUCESSO = "Conversão de tipagem realizada com sucesso";

    public static final String REGEX_CARACTERES_ESPECIAIS_CPFCNPJ = "(\\.?-?/?)";
}
