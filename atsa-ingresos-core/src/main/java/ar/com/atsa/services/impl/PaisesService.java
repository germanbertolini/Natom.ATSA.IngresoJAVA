package ar.com.atsa.services.impl;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import ar.com.atsa.commons.dto.PaisTO;
import ar.com.atsa.persistence.entities.Pais;
import ar.com.atsa.services.logic.PaisesServiceLogic;

@Component
@Path("/paisesservice")
public class PaisesService {
	
	private static Logger logger = Logger.getLogger(PaisesService.class);
	
	@Autowired
	PaisesServiceLogic paisesServiceLogic;
	
	@Path("/findAll")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
	public List<Pais> findAll() {
		return this.paisesServiceLogic.findAll();
	}
	
	@Path("/savePais")
    @PUT
	@Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
	public Response savePais(final PaisTO in) {
		try {
			PaisTO out = this.paisesServiceLogic.savePais(in);
			return Response.ok(out).build();
		} catch (Exception e) {
			PaisesService.logger.error(e);
			return Response.serverError().entity(e.getMessage()).build();
		}
	}
}
