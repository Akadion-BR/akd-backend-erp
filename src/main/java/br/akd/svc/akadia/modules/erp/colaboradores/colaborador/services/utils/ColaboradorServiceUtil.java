package br.akd.svc.akadia.modules.erp.colaboradores.colaborador.services.utils;

import br.akd.svc.akadia.modules.erp.colaboradores.colaborador.models.enums.ModulosEnum;
import br.akd.svc.akadia.modules.erp.colaboradores.colaborador.repository.ColaboradorRepository;
import br.akd.svc.akadia.utils.ConversorDeDados;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

@Slf4j
@Component
public class ColaboradorServiceUtil {

    @Autowired
    ColaboradorRepository colaboradorRepository;

    public String geraMatriculaUnica() {

        log.debug("Método responsável por gerar matrícula do colaborador acessado");
        long min = 1000000L;
        long max = 9999999L;
        while (true) {
            log.debug("Gerando matrícula...");

            long matriculaAleatoria =
                    (long) (ConversorDeDados.RANDOM.nextFloat() * (max - min) + min);

            String matricula = Long.toString(matriculaAleatoria);

            log.debug("Matrícula gerada: {}", matricula);
            if (Boolean.FALSE.equals(colaboradorRepository.existsByMatricula(matricula)))
                return matricula;

            else log.debug("A matrícula gerada já existe. Tentando novamente...");
        }
    }

    public Set<ModulosEnum> habilitaTodosModulosColaborador() {
        log.info("Método de setagem de todos os privilégios à lista de privilégios do colaborador acessado");
        log.info("Iniciando setagem de privilégios...");

        Set<ModulosEnum> privilegios = new HashSet<>();
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
        log.info("Lista de privilégios preenchida com sucesso");
        return privilegios;
    }

    public String geraSenhaAleatoriaParaNovoLogin() {
        log.info("Método de criação de senha aleatória acessado");
//        return "@" + empresaEntity.getNome().replace(" ", "").substring(0, 2).toUpperCase() +
//                empresaEntity.getCnpj()
//                        .replace("-", ".")
//                        .replace(".", "")
//                        .replace("/", "")
//                        .substring(0, 2) +
//                empresaEntity.getDataCadastro().substring(0, 2) +
//                empresaEntity.getSegmentoEmpresaEnum().getDesc().substring(0, 2).toUpperCase(); //TODO ALTERADO
        return "senhaTeste"; //TODO ALTERADO
    }

}
