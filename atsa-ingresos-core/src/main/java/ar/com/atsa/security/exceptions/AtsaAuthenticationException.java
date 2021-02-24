package ar.com.atsa.security.exceptions;

import org.springframework.security.core.AuthenticationException;

import ar.com.atsa.commons.enums.AuthenticationStatus;

/**
 * 
 * @author diegap
 *
 */
public class AtsaAuthenticationException extends AuthenticationException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4798474518236237900L;

	private AuthenticationStatus status;
	
	public AtsaAuthenticationException(String msg, AuthenticationStatus status) {
		super(msg);
		this.setStatus(status);
	}
	
	public AtsaAuthenticationException(String msg, AuthenticationStatus status, Throwable t) {
		super(msg, t);
		this.setStatus(status);
	}

	public AuthenticationStatus getStatus() {
		return status;
	}

	public void setStatus(AuthenticationStatus status) {
		this.status = status;
	}

}
