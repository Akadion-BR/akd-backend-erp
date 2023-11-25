package br.akd.svc.akadia;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients
@SpringBootApplication
public class AkadiaApplication {
    //TODO REMOVER LOG.DEBUG E INSERIR LOG.INFO NO LUGAR
    //TODO REFINAR LOGS DAS CLASSES DE SERVIÇOS
    //TODO REVISAR TODOS OS CAMPOS OBRIGATÓRIOS E COMPARAR COM O FRONT SE ESTÁ BATENDO COM AS CONFS DE DATABASE
    //TODO COMPARAR TAMANHOS DOS CAMPOS DO FRONT COM AS REGRAS DO DATABASE
    //TODO CRIAR WEBHOOK FISCAL
    //TODO IMPLEMENTAR VALIDAÇÕES PARA TODOS OS CPFS E CNPJS INSERIDOS
    //TODO MELHORAR TRATAMENTO DE ERROS PARA TIPAGENS INCORRETAS, ENTRADAS INCORRETAS, ETC
    public static void main(String[] args) {
        SpringApplication.run(AkadiaApplication.class, args);
    }

}
