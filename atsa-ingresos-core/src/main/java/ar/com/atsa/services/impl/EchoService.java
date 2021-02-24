package ar.com.atsa.services.impl;

import javax.ws.rs.GET;
import javax.ws.rs.Path;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;

@Component
@Path("/echoservice")
public class EchoService {

	@GET
	@Path("/ping")
    @PreAuthorize("hasAnyRole('ROLE_USER')")
	public String ping(){
		return "Respondiendo desde EchoService";
	}
	
}
