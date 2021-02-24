package ar.com.atsa.commons.enums;

import com.fasterxml.jackson.annotation.JsonRootName;

/**
 * 
 * @author diegap
 *
 */
@JsonRootName("status")
public enum AuthenticationStatus {

	OK,
	LOGIN_ERROR,
	USER_BLOCKED,
	CHANGE_PASSWORD_REQUIRED,
	PASSWORD_EXPIRED,
	MAX_TRIES_REACHED,
	MUST_SELECT_PROFILE;
}
