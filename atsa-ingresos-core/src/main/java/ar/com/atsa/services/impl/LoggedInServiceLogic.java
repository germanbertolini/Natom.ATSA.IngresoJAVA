package ar.com.atsa.services.impl;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import ar.com.atsa.persistence.entities.Usuario;
import ar.com.atsa.security.AtsaUserDetailsService.AtsaUserDetails;

@Component
public class LoggedInServiceLogic {
	
	public Usuario getLoggedInUsuario() {
		return ((AtsaUserDetails) SecurityContextHolder
				.getContext().getAuthentication().getPrincipal()).getUsuario();
	}
}
