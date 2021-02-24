package ar.com.atsa.services.impl;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import ar.com.atsa.persistence.entities.Convenio;
import ar.com.atsa.persistence.entities.Establecimiento;
import ar.com.atsa.persistence.entities.Estado;
import ar.com.atsa.persistence.entities.Pais;
import ar.com.atsa.persistence.entities.Rol;
import ar.com.atsa.persistence.entities.TipoBaja;
import ar.com.atsa.persistence.entities.TipoDocumentoArchivado;
import ar.com.atsa.persistence.entities.TipoRelacion;
import ar.com.atsa.persistence.entities.TipoTramite;
import ar.com.atsa.services.logic.ParametrosServiceLogic;

@Component
@Path("/parametrosservice")
public class ParametrosService {
	
	@Autowired
	ParametrosServiceLogic parametrosServiceLogic;
	
	@Path("/getAllConvenios")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
	public Iterable<Convenio> getAllConvenios() {
		return this.parametrosServiceLogic.getAllConvenios();
	}
	
	@Path("/getAllTiposBaja")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
	public Iterable<TipoBaja> getAllTiposBaja() {
		return this.parametrosServiceLogic.getAllTiposBaja();
	}
	
	@Path("/getAllEstablecimientos")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
	public Iterable<Establecimiento> getAllEstablecimientos() {
		return this.parametrosServiceLogic.getAllEstablecimientos();
	}
	
	@Path("/getAllEstados")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
	public Iterable<Estado> getAllEstados() {
		return this.parametrosServiceLogic.getAllEstados();
	}
	
	@Path("/getAllPaises")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
	public Iterable<Pais> getAllPaises() {
		return this.parametrosServiceLogic.getAllPaises();
	}
	
	@Path("/getAllRoles")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
	public Iterable<Rol> getAllRoles() {
		return this.parametrosServiceLogic.getAllRoles();
	}
	
	@Path("/getAllTipoDocumentosArchivados")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
	public Iterable<TipoDocumentoArchivado> getAllTipoDocumentosArchivados() {
		return this.parametrosServiceLogic.getAllTipoDocumentosArchivados();
	}
	
	@Path("/getAllTipoRelaciones")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
	public Iterable<TipoRelacion> getAllTipoRelaciones() {
		return this.parametrosServiceLogic.getAllTipoRelaciones();
	}
	
	@Path("/getAllTipoTramites")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
	public Iterable<TipoTramite> getAllTipoTramites() {
		return this.parametrosServiceLogic.getAllTipoTramites();
	}
}
