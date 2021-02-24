package ar.com.atsa.security;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.GenericFilterBean;

import ar.com.atsa.security.AtsaUserDetailsService.AtsaUserDetails;

@Component
public class AuthenticationTokenProcessingFilter extends GenericFilterBean {

    @Autowired
    private AtsaUserDetailsService userService;
    
    
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException,
            ServletException {

        if (!(request instanceof HttpServletRequest)) {
            throw new RuntimeException("Expecting a HTTP request");
        }

        /*
         * Recupera la validación del Token
         */
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        String authToken = httpRequest.getHeader("X-Auth-Token");
        String userName = TokenUtils.getUserNameFromToken(authToken);
        //Long perfilId = TokenUtils.getPerfilIdFromToken(authToken);
        
        /*
         * Si tiene token valida que sea originado por la app.
         * Si no tiene token no hace nada si no tiene session en Spring daría error de autorización para zonas seguras
         * y libre acceso a zonas sin seguridad
         */
        if (userName != null) {
        	
        	UserDetails userDetails = null;
        	
        	/*
        	 * Si ya tiene un contexto de seguridad validado en Spring Security 
        	 * valido que corresponsa el mismo usuario o lo recreo del Token si no lo tiene.
        	 */
        	if (SecurityContextHolder.getContext().getAuthentication() != null && SecurityContextHolder.getContext().getAuthentication().getPrincipal() != null) {
            	/*
            	 * Si se esta utilizando el contexto ya validado de spring 
            	 * comprueba que sea el mismo usuario y con un token valido
            	 */
        		userDetails = (UserDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            	if (!userName.equals(userDetails.getUsername()) || !TokenUtils.validateToken(authToken, userDetails)) {
            		SecurityContextHolder.getContext().setAuthentication(null);	
            	}
        	} else {
        		/*
            	 * Si pasa por Token en cookie y no por login recrea la seguridad
            	 */
            	userDetails = this.userService.loadUserByUsername(userName);            		
        		if(TokenUtils.validateToken(authToken, userDetails)) {
            		UsernamePasswordAuthenticationToken authentication =
                            new UsernamePasswordAuthenticationToken(userDetails, userDetails.getPassword() , userDetails.getAuthorities());
	                //Perfil perfil = this.perfilRepository.findById(perfilId);  
	                //((AtsaUserDetails)authentication.getPrincipal()).setPerfil(perfil);
	                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails((HttpServletRequest) request));
	                SecurityContextHolder.getContext().setAuthentication(authentication);
        		}
            }
        }

        chain.doFilter(request, response);
    }
}