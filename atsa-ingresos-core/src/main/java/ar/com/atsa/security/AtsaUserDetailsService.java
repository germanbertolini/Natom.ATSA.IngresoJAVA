package ar.com.atsa.security;

import java.util.Collection;
import java.util.HashSet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import ar.com.atsa.persistence.entities.Rol;
import ar.com.atsa.persistence.entities.TipoTramite;
import ar.com.atsa.persistence.entities.Usuario;
import ar.com.atsa.persistence.repositories.UsuarioRepository;

/**
 * Created by tomas.colombo on 28/02/14.
 */
@Service
public class AtsaUserDetailsService implements UserDetailsService {

    private UsuarioRepository usuarioRepository;

    @Autowired
    public void setUsuarioRepository(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws
            UsernameNotFoundException {
    	System.out.println("---------------xxx--------------->" + email + "<");
        Usuario user = usuarioRepository.findByEmail(email);

        Iterable<Usuario> users = usuarioRepository.findAll();
        for (Usuario u: users) {
        	System.out.println("------------------------------>" + u.getEmail() + "<");
        }
        
        return new AtsaUserDetails(user);
    }
    
   @SuppressWarnings("serial")
    public static class AtsaUserDetails implements UserDetails {

        public static final String SCOPE_READ = "read";

        public static final String SCOPE_WRITE = "write";

        public static final String ROLE_USER = "ROLE_USER";

        private Collection<GrantedAuthority> grantedAuthorities = new HashSet<GrantedAuthority>();

        private Usuario usuario;

        public AtsaUserDetails(Usuario usuario) {
            Assert.notNull(usuario, "El usuario no puede ser nulo");
            this.usuario = usuario;
            
    		for (Rol rol: usuario.getRoles()) {
				this.grantedAuthorities.add((new SimpleGrantedAuthority(rol.getNombre())));
				for (TipoTramite tipoTramite: rol.getTramites()) {
					this.grantedAuthorities.add((new SimpleGrantedAuthority("Tramite." + tipoTramite.getCodigo())));
				}
    		}
        }

        @Override
        public Collection<? extends GrantedAuthority> getAuthorities() {
            return this.grantedAuthorities;
        }

        @Override
        public String getPassword() {
            return usuario.getClave();
        }

        @Override
        public String getUsername() {
            return usuario.getEmail();
        }

        @Override
        public boolean isAccountNonExpired() {
            return isEnabled();
        }

        @Override
        public boolean isAccountNonLocked() {
            return isEnabled();
        }

        @Override
        public boolean isCredentialsNonExpired() {
            return isEnabled();
        }

        @Override
        public boolean isEnabled() {
            return usuario.getActivo();
        }

        public Usuario getUsuario() {
            return this.usuario;
        }

    }
}