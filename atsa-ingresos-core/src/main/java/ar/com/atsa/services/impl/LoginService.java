package ar.com.atsa.services.impl;

import java.util.HashMap;
import java.util.Map;

import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.CredentialsExpiredException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import ar.com.atsa.commons.dto.UserData;
import ar.com.atsa.commons.dto.UserTransfer;
import ar.com.atsa.commons.enums.AuthenticationStatus;
import ar.com.atsa.persistence.repositories.UsuarioRepository;
import ar.com.atsa.security.TokenUtils;
import ar.com.atsa.security.AtsaUserDetailsService.AtsaUserDetails;

@Component
@Path("/loginservice")
public class LoginService {

    @Autowired
	private UsuarioRepository usuarioRepository;

    @Autowired
    private AuthenticationManager authManager;

    @Path("authenticate")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public UserTransfer authenticate(@HeaderParam("username") String username, @HeaderParam("password") String password) {

    	SecurityContextHolder.getContext().setAuthentication(null);
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(username, password);

        UserTransfer userTransfer = new UserTransfer(AuthenticationStatus.OK, null);
        
        try {
        	Authentication authentication = this.authManager.authenticate(authenticationToken);
        
        	Map<String, Boolean> roles = new HashMap<String, Boolean>();
        	
        	/*
        	 * Reload user as password of authentication principal will be null after authorization and
        	 * password is needed for token generation
        	 */
        	UserDetails userDetails = (UserDetails)authentication.getPrincipal();
        	for (GrantedAuthority authority : userDetails.getAuthorities()) {
        		roles.put(authority.toString(), Boolean.TRUE);
        	}

        	
        	this.setUserTransferObject(userTransfer, authentication, roles, userDetails);
        	SecurityContextHolder.getContext().setAuthentication(authentication);
        } catch(BadCredentialsException e){
        	userTransfer.setStatus(AuthenticationStatus.LOGIN_ERROR);
        	
        } catch(LockedException e){
        	userTransfer.setStatus(AuthenticationStatus.USER_BLOCKED);
        	
        } catch(CredentialsExpiredException e){
        	
        	if (e.getMessage().equals(AuthenticationStatus.CHANGE_PASSWORD_REQUIRED.name())){
        		userTransfer.setStatus(AuthenticationStatus.CHANGE_PASSWORD_REQUIRED);
        	} else {
        		userTransfer.setStatus(AuthenticationStatus.PASSWORD_EXPIRED);
        	}
        
        } catch (DisabledException e){
        	userTransfer.setStatus(AuthenticationStatus.MAX_TRIES_REACHED);
        
        } catch (AuthenticationServiceException e){
        	userTransfer.setStatus(AuthenticationStatus.LOGIN_ERROR);
        }
        
        return userTransfer;
    
    }

    /**
     * 
     * Lógica para crear un user transfer object
     * 
     * @param userTransfer
     * @param authentication
     * @param roles
     * @param userDetails
     */
	private void setUserTransferObject(UserTransfer userTransfer,
			Authentication authentication, Map<String, Boolean> roles,
			UserDetails userDetails) {
    	
		AtsaUserDetails atsaUserDetails = (AtsaUserDetails)authentication.getPrincipal();
		
		String lastName = atsaUserDetails.getUsuario().getApellidos();
		String firstName = atsaUserDetails.getUsuario().getNombres();
			
		UserData userData = new UserData(lastName, firstName, userDetails.getUsername(), roles, TokenUtils.createToken(atsaUserDetails));
		userTransfer.setUserData(userData);
	}

}
