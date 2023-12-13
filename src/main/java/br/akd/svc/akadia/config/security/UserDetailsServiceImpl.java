package br.akd.svc.akadia.config.security;

import br.akd.svc.akadia.modules.erp.colaboradores.colaborador.models.entity.colaborador.ColaboradorEntity;
import br.akd.svc.akadia.modules.erp.colaboradores.colaborador.models.entity.colaborador.id.ColaboradorId;
import br.akd.svc.akadia.modules.erp.colaboradores.colaborador.repository.ColaboradorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private ColaboradorRepository colaboradorRepository;

    @Override
    public UserDetails loadUserByUsername(String matricula) throws UsernameNotFoundException {
        Optional<ColaboradorEntity> colaborador = colaboradorRepository.findByMatricula(matricula);
        if (colaborador.isPresent()) {
            return new UserSS(
                    new ColaboradorId(
                            colaborador.get().getIdClienteSistema(),
                            colaborador.get().getIdEmpresa(),
                            colaborador.get().getId()),
                    colaborador.get().getMatricula(),
                    colaborador.get().getAcessoSistema().getSenhaCriptografada(),
                    colaborador.get().getAcessoSistema().getPrivilegios());
        }
        throw new UsernameNotFoundException("Usuário não encontrado");
    }
}
