package ar.com.atsa.security;

import java.util.Collection;
import java.util.HashSet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import ar.com.atsa.commons.enums.AuthenticationStatus;
import ar.com.atsa.persistence.entities.Rol;
import ar.com.atsa.persistence.entities.TipoTramite;
import ar.com.atsa.persistence.entities.Usuario;
import ar.com.atsa.persistence.repositories.UsuarioRepository;
import ar.com.atsa.security.AtsaUserDetailsService.AtsaUserDetails;

/**
 * Created by tomas.colombo on 25/02/14.
 */
@Component
public class AtsaAuthenticationProvider implements AuthenticationProvider {

	private static final Logger LOG = LoggerFactory
			.getLogger(AtsaAuthenticationProvider.class);

	@Autowired
	private UsuarioRepository repo;

	@Autowired
	private AtsaUserDetailsService userService;

	@Override
	public Authentication authenticate(Authentication authentication)
			throws AuthenticationException {

		// Thread.dumpStack();
		String userName = authentication.getName();
		String password = authentication.getCredentials().toString();

		String message = String.format("Username: '%s' Password: '%s'",
				userName, password);
		Usuario usuario = repo.findByEmail(userName);

		LOG.debug(message);

		if (usuario == null) {
			throw new UsernameNotFoundException(
					AuthenticationStatus.LOGIN_ERROR.name());
		}

		if (this.checkBlockedUser(usuario)) {
			throw new LockedException(AuthenticationStatus.USER_BLOCKED.name());
		}

		if (!checkUserPassMatch(userName, password, usuario)) {
			throw new BadCredentialsException(
					AuthenticationStatus.LOGIN_ERROR.name());
		}

		Collection<GrantedAuthority> grantedAuths = new HashSet<GrantedAuthority>();
		for (Rol rol: usuario.getRoles()) {
			grantedAuths.add(new SimpleGrantedAuthority(rol.getNombre()));
			for (TipoTramite tipoTramite: rol.getTramites()) {
				grantedAuths.add(new SimpleGrantedAuthority("Tramite." + tipoTramite.getCodigo()));
			}
		}

		AtsaUserDetails userDetails = (AtsaUserDetails)this.userService.loadUserByUsername(usuario
				.getEmail());

		return new UsernamePasswordAuthenticationToken(userDetails,
				authentication, grantedAuths);
	}

	private Boolean checkUserPassMatch(String userName, String password,
			Usuario usuario) {
		return (usuario != null && password.equals(usuario.getClave()));
	}

	private Boolean checkBlockedUser(Usuario usuario) {
		return !usuario.getActivo();
	}

	@Override
	public boolean supports(Class<?> authentication) {
		return authentication.equals(UsernamePasswordAuthenticationToken.class);
	}
}
