package br.akd.svc.akadia.modules.erp.colaboradores.colaborador.services.utils;

import br.akd.svc.akadia.modules.erp.colaboradores.colaborador.repository.ColaboradorRepository;
import br.akd.svc.akadia.utils.ConversorDeDados;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

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

}
