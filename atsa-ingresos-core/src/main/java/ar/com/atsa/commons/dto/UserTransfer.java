package ar.com.atsa.commons.dto;

import ar.com.atsa.commons.enums.AuthenticationStatus;

import com.fasterxml.jackson.annotation.JsonRootName;

@JsonRootName("authenticationData")
public class UserTransfer {

	private AuthenticationStatus status;
	
	private UserData userData;

    public UserTransfer() {
    }

    public UserTransfer(AuthenticationStatus status, UserData userData) {
		super();
		this.status = status;
		this.userData = userData;
	}

	public AuthenticationStatus getStatus() {
		return status;
	}

	public void setStatus(AuthenticationStatus status) {
		this.status = status;
	}

	public UserData getUserData() {
		return userData;
	}

	public void setUserData(UserData userData) {
		this.userData = userData;
	}
}
