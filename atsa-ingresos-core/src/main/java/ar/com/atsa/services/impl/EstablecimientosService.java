package ar.com.atsa.services.impl;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import ar.com.atsa.persistence.entities.Establecimiento;
import ar.com.atsa.services.logic.EstablecimientosServiceLogic;

@Component
@Path("/establecimientosservice")
public class EstablecimientosService {
	
	private static Logger logger = Logger.getLogger(EstablecimientosService.class);
	
	@Autowired
	EstablecimientosServiceLogic establecimientosServiceLogic;
	
	@Path("/findAll")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
	public Iterable<Establecimiento> findAll(@QueryParam("page") final Integer page, @QueryParam("nombre") String nombre, @QueryParam("cuil") String cuil) {
		if (nombre != null && nombre.trim().length() < 1) nombre = null;
		if (cuil != null && cuil.trim().length() < 1) cuil = null;
		return this.establecimientosServiceLogic.findAll(page, nombre, cuil);
	}
	
	@Path("/findByNombreAndCuit")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
	public Iterable<Establecimiento> findByNombreAndCuit(@QueryParam("nombre") final String nombre, @QueryParam("cuit") final String cuit) {
		return this.establecimientosServiceLogic.findByNombreAndCuit(nombre, cuit);
	}
	
	@Path("/findById")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
	public Establecimiento findById(@QueryParam("id") final Long id) {
		return this.establecimientosServiceLogic.findById(id);
	}
	
	@Path("/eliminarEstablecimiento")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
	public Response eliminarEstablecimiento(@QueryParam("id") final Long id) {
		establecimientosServiceLogic.eliminarEstablecimiento(id);
		return Response.ok("Ok").build();
	}
	
	@Path("/saveEstablecimiento")
    @PUT
	@Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
	public Response saveEstablecimiento(final Establecimiento in) {
		try {
			Establecimiento out = this.establecimientosServiceLogic.saveEstablecimiento(in);
			return Response.ok(out).build();
		} catch (Exception e) {
			e.printStackTrace();
			EstablecimientosService.logger.error(e);
			return Response.serverError().entity(e.getMessage()).build();
		}
	}
}
