package ar.com.atsa.commons.dto;

import java.util.Map;

import com.fasterxml.jackson.annotation.JsonRootName;

@JsonRootName("UserData")
public class UserData {

	private String userName = null;

	private Map<String, Boolean> roles = null;

	private String token = null;

	private String firstName;
	
	private String lastName;

    public UserData() {
    }

    public UserData(String lastName, String firstName, String userName, Map<String, Boolean> roles, String token) {
		super();
		this.lastName = lastName;
		this.firstName = firstName;
		this.userName = userName;
		this.roles = roles;
		this.token = token;
	}

    public String getUserName() {
		return userName;
	}

    public String getFirstName() {
    	return firstName;
    }
    
    public String getLastName() {
    	return lastName;
    }

    public Map<String, Boolean> getRoles() {
		return roles;
	}

	public String getToken() {
		return token;
	}

}
