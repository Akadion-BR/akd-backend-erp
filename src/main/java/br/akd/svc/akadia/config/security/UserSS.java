package br.akd.svc.akadia.config.security;

import br.akd.svc.akadia.modules.erp.colaboradores.colaborador.models.entity.colaborador.id.ColaboradorId;
import br.akd.svc.akadia.modules.erp.colaboradores.colaborador.models.enums.ModulosEnum;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

public class UserSS implements UserDetails {
    private static final long serialVersionUID = 1L;

    private final ColaboradorId colaboradorId;
    private final String matricula;
    private final String senha;
    private final Collection<? extends GrantedAuthority> authorities;

    public UserSS(ColaboradorId colaboradorId, String matricula, String senha, Set<ModulosEnum> perfis) {
        super();
        this.colaboradorId = colaboradorId;
        this.matricula = matricula;
        this.senha = senha;
        this.authorities = perfis.stream().map(x ->
                new SimpleGrantedAuthority("ROLE_" + x.getRole())).collect(Collectors.toSet());
    }

    public ColaboradorId getColaboradorId() {
        return colaboradorId;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return senha;
    }

    @Override
    public String getUsername() {
        return matricula;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

}
