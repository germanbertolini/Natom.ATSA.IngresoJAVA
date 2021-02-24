package ar.com.atsa.services.impl;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import ar.com.atsa.persistence.entities.Persona;
import ar.com.atsa.persistence.repositories.PersonaRepository;
import ar.com.atsa.services.ServiceException;
import ar.com.atsa.services.logic.CarnetServiceLogic;

@Component
@Path("/carnetservice")
public class CarnetService {
	
	private static Logger logger = Logger.getLogger(CarnetService.class);
	
	@Autowired
	CarnetServiceLogic carnetServiceLogic;

	@Autowired
	PersonaRepository personaRepository;
	
	@Path("/getCarnet")
	@Produces(MediaType.APPLICATION_OCTET_STREAM)
    @GET
	public Response getCarnet(@QueryParam("id") final Long id) {
		try {
			File file = this.carnetServiceLogic.getCarnet(id);
			
			ResponseBuilder response = Response.ok((Object) file);
			response.header("Content-Disposition", "attachment; filename=\"" + file.getName() + "\"");
			return response.build();
		} catch (Exception e) {
			ResponseBuilder response = Response.serverError().entity(e);
			return response.build();
		}
	}
	
	@Path("/getCarnetByKey")
	@Produces(MediaType.APPLICATION_OCTET_STREAM)
    @GET
	public Response getCarnetByKey(@QueryParam("key") final String key) {
		try {
			File file = this.carnetServiceLogic.getCarnetByKey(key);
			
			ResponseBuilder response = Response.ok((Object) file);
			response.header("Content-Disposition", "attachment; filename=\"" + file.getName() + "\"");
			return response.build();
		} catch (Exception e) {
			ResponseBuilder response = Response.serverError().entity(e);
			return response.build();
		}
	}
	
	@Path("/generarCarnet")
	@Produces(MediaType.APPLICATION_OCTET_STREAM)
    @GET
	public Response generarCarnet(@QueryParam("id") final Long id) {
		Persona persona = this.personaRepository.findOne(id);
		try {
			String path = this.carnetServiceLogic.generarCarnet(persona);
			
			
			File file = new File(path);
			
			ResponseBuilder response = Response.ok((Object) file);
			response.header("Content-Disposition", "attachment; filename=\"" + file.getName() + "\"");
			return response.build();
		} catch (Exception e) {
			ResponseBuilder response = Response.serverError().entity(e);
			return response.build();
		}
	}
	
	@Path("/generarCarnetSilent")
	@Produces(MediaType.APPLICATION_OCTET_STREAM)
    @GET
	public Response generarCarnetSilent(@QueryParam("id") final Long id) {
		Persona persona = this.personaRepository.findOne(id);
		try {
			this.carnetServiceLogic.generarCarnet(persona);
			ResponseBuilder response = Response.ok();
			return response.build();
		} catch (Exception e) {
			ResponseBuilder response = Response.serverError().entity(e);
			return response.build();
		}
	}
	
	@Path("/testCarnet")
	@GET
	public Response testCarnet() {
		Persona persona = new Persona();
		
		persona.setId(111l);
		persona.setNombres("Matias");
		persona.setApellidos("Sanchez");
		persona.setNumeroAfiliado("001");
		
		Map<String, String> map = new HashMap<String, String>();
		try {
			String path = carnetServiceLogic.generarCarnet(persona);
			map.put("path", path);
		} catch (ServiceException e) {
			e.printStackTrace();
			map.put("error", e.getMessage());
		}
		return Response.ok(map).build();
	}
}
