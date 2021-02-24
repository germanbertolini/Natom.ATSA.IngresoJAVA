package ar.com.atsa.services.impl;

import java.io.File;
import java.io.InputStream;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.transaction.Transactional;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import ar.com.atsa.commons.dto.AfiliadoTO;
import ar.com.atsa.commons.dto.DownloadTO;
import ar.com.atsa.commons.dto.TramiteTO;
import ar.com.atsa.persistence.entities.DocumentoArchivado;
import ar.com.atsa.persistence.entities.Localidad;
import ar.com.atsa.persistence.entities.Persona;
import ar.com.atsa.persistence.entities.Tramite;
import ar.com.atsa.services.ServiceException;
import ar.com.atsa.services.logic.AfiliadosServiceLogic;
import ar.com.atsa.services.logic.TramitesServiceLogic;

import com.sun.jersey.multipart.FormDataParam;

@Component
@Path("/afiliadosservice")
public class AfiliadosService {
	
	private static Logger logger = Logger.getLogger(AfiliadosService.class);
	
	@Autowired
	AfiliadosServiceLogic afiliadosServiceLogic;
	
	@Autowired
	TramitesServiceLogic tramitesServiceLogic;
	
	
	@Path("/findAfiliadoByFamiliarId")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
	public Persona findAfiliadoByFamiliarId(@QueryParam("familiarId") final Long familiarId) {
		return this.afiliadosServiceLogic.findAfiliadoByFamiliarId(familiarId);
	}
	
	
	@Path("/findTramitesPageByTipo")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
	public List<Tramite> findTramitesPageByTipo(@QueryParam("tipo") final String tipo, @QueryParam("id") final Long id, @QueryParam("page") final Integer page) {
		return this.tramitesServiceLogic.findTramitesPageByTipo(tipo, id, page);
	}
	
	
	@Path("/findTramites")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
	public List<Tramite> findTramites(@QueryParam("id") final Long id) {
		return this.tramitesServiceLogic.findTramites(id);
	}
	
	
	@Path("/findTramitesAprobados")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
	public List<Tramite> findTramitesAprobados() {
		return this.tramitesServiceLogic.findTramitesAprobados();
	}
	
	
	@Path("/findTramitesAprobadosTramites")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
	public List<Tramite> findTramitesAprobadosTramites() {
		return this.tramitesServiceLogic.findTramitesAprobados(Arrays.asList(new Integer[] {0}));
	}
	
	
	@Path("/findTramitesAprobadosDocumentos")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
	public List<Tramite> findTramitesAprobadosDocumentos() {
		return this.tramitesServiceLogic.findTramitesAprobados(Arrays.asList(new Integer[] {1}));
	}
	
	
	@Path("/findTramitesRechazados")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
	public List<Tramite> findTramitesRechazados() {
		return this.tramitesServiceLogic.findTramitesRechazados();
	}
	
	
	@Path("/findTramitesRechazadosTramites")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
	public List<Tramite> findTramitesRechazadosTramites() {
		return this.tramitesServiceLogic.findTramitesRechazados(Arrays.asList(new Integer[] {0, 2}));
	}
	
	
	@Path("/findTramitesRechazadosDocumentos")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
	public List<Tramite> findTramitesRechazadosDocumentos() {
		return this.tramitesServiceLogic.findTramitesRechazados(Arrays.asList(new Integer[] {1}));
	}
	
	
	@Path("/findTramitesPorAprobar")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
	public List<Tramite> findTramitesPorAprobar() {
		return this.tramitesServiceLogic.findTramitesPorAprobar(Arrays.asList(new Integer[] {0, 2}));
	}
	
	
	@Path("/findTramitesPorAprobarDocumentos")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
	public List<Tramite> findTramitesPorAprobarDocumentos() {
		return this.tramitesServiceLogic.findTramitesPorAprobar(Arrays.asList(new Integer[] {1}));
	}
	
	@Path("/upload")
    @POST
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)
	public Response upload(@FormDataParam("file") InputStream is, @FormDataParam("flowFilename") String fileName, @FormDataParam("tipoDocumento") String tipoDocumento, @FormDataParam("afiliadoId") Long afiliadoId) {
		try {
			this.afiliadosServiceLogic.upload(is, fileName, tipoDocumento, afiliadoId);
			return Response.ok().build();
		} catch (ServiceException e) {
			e.printStackTrace();
			return Response.serverError().entity(e.getMessage()).build();
		}
	}
	
	@Path("/uploadFamiliar")
    @POST
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)
	public Response uploadFamiliar(@FormDataParam("file") InputStream is, @FormDataParam("flowFilename") String fileName, @FormDataParam("tipoDocumento") String tipoDocumento, @FormDataParam("familiarId") Long afiliadoId) {
		try {
			this.afiliadosServiceLogic.uploadFamiliar(is, fileName, tipoDocumento, afiliadoId);
			return Response.ok().build();
		} catch (ServiceException e) {
			e.printStackTrace();
			return Response.serverError().entity(e.getMessage()).build();
		}
	}
	
	@Path("/findFamiliarDocumentos")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
	public List<DocumentoArchivado> findFamiliarDocumentos(@QueryParam("id") final Long id) {
		return this.afiliadosServiceLogic.findFamiliarDocumentos(id);
	}
	
	@Path("/download")
    @GET
	public Response download(@QueryParam("id") final Long id) {
		DownloadTO to = this.afiliadosServiceLogic.download(id);
		File file = to.getFile();
        
        ResponseBuilder response = Response.ok((Object) file);
        response.header("Content-Disposition", "attachment; filename=\"" + to.getFileName() + "\"");
        return response.build();
	}
	
	@Path("/foto")
    @GET
	public Response foto(@QueryParam("id") final Long id) {
		File file = this.afiliadosServiceLogic.getFoto(id);
        
        ResponseBuilder response = Response.ok((Object) file);
        response.header("Content-Disposition", "attachment; filename=\"" + file.getName() + "\"");
        return response.build();
	}
	
	
	@Path("/findByCUIL")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
	public List<Persona> findByCUIL(@QueryParam("cuil") final String cuil) {
		return this.afiliadosServiceLogic.findByCUIL(cuil);
	}
	
	@Path("/findByDocumento")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
	public List<Persona> findByDocumento(@QueryParam("documento") final String documento) {
		return this.afiliadosServiceLogic.findByDocumento(documento);
	}
	
	@Path("/findByNumeroAfiliado")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
	public List<Persona> findByNumeroAfiliado(@QueryParam("numeroAfiliado") final String numeroAfiliado) {
		return this.afiliadosServiceLogic.findByNumeroAfiliado(numeroAfiliado);
	}
	
	@Path("/getAfiliado")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
	public AfiliadoTO getAfiliado(@QueryParam("afiliadoId") final Long afiliadoId) {
		return this.afiliadosServiceLogic.getAfiliado(afiliadoId);
	}
	
	@Path("/saveAfiliado")
    @PUT
	@Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
	@Transactional
	public Response saveAfiliado(final AfiliadoTO in) {
		try {
			Long id = this.afiliadosServiceLogic.saveAfiliado(in, 0);
			AfiliadoTO out = this.afiliadosServiceLogic.getAfiliado(id);
			return Response.ok(out).build();
		} catch (Exception e) {
			e.printStackTrace();
			AfiliadosService.logger.error(e);
			return Response.serverError().entity(e.getMessage()).build();
		}
	}
	
	@Path("/darPorAfiliado")
    @PUT
	@Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
	@Transactional
	public Response darPorAfiliado(final AfiliadoTO in)  {
		try {
			Long id = this.afiliadosServiceLogic.saveAfiliado(in, 0);
			AfiliadoTO out = this.afiliadosServiceLogic.darPorAfiliado(id);
			return Response.ok(out).build();
		} catch (Exception e) {
			e.printStackTrace();
			AfiliadosService.logger.error(e);
			return Response.serverError().entity(e.getMessage()).build();
		}
	}
	
	@Path("/desafiliarAfiliado")
    @PUT
	@Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
	@Transactional
	public Response desafiliarAfiliado(final AfiliadoTO in) {
		try {
			Long id = this.afiliadosServiceLogic.saveAfiliado(in, 1);
			AfiliadoTO out = this.afiliadosServiceLogic.getAfiliado(id);
			return Response.ok(out).build();
		} catch (Exception e) {
			e.printStackTrace();
			AfiliadosService.logger.error(e);
			return Response.serverError().entity(e.getMessage()).build();
		}
	}
	
	@Path("/reafiliarAfiliado")
    @PUT
	@Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
	@Transactional
	public Response reafiliarAfiliado(final AfiliadoTO in) {
		try {
			Long id = this.afiliadosServiceLogic.saveAfiliado(in, 1);
			AfiliadoTO out = this.afiliadosServiceLogic.getAfiliado(id);
			return Response.ok(out).build();
		} catch (Exception e) {
			e.printStackTrace();
			AfiliadosService.logger.error(e);
			return Response.serverError().entity(e.getMessage()).build();
		}
	}
	
	@Path("/pasivarAfiliado")
    @PUT
	@Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
	@Transactional
	public Response pasivarAfiliado(final AfiliadoTO in) {
		try {
			Long id = this.afiliadosServiceLogic.saveAfiliado(in, 2);
			AfiliadoTO out = this.afiliadosServiceLogic.getAfiliado(id);
			return Response.ok(out).build();
		} catch (Exception e) {
			e.printStackTrace();
			AfiliadosService.logger.error(e);
			return Response.serverError().entity(e.getMessage()).build();
		}
	}
	
	@Path("/aprobarTramite")
    @PUT
	@Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
	public Response aprobarTramite(final TramiteTO tramite) {
		try {
			AfiliadoTO out = this.afiliadosServiceLogic.aprobarTramite(tramite);
			return Response.ok(out).build();
		} catch (Exception e) {
			AfiliadosService.logger.error(e);
			return Response.serverError().entity(e.getMessage()).build();
		}
	}
	
	@Path("/rechazarTramite")
    @PUT
	@Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
	public Response rechazarTramite(final TramiteTO tramite) {
		try {
			AfiliadoTO out = this.afiliadosServiceLogic.rechazarTramite(tramite);
			return Response.ok(out).build();
		} catch (Exception e) {
			e.printStackTrace();
			AfiliadosService.logger.error(e);
			return Response.serverError().entity(e.getMessage()).build();
		}
	}
	
	@Path("/findFamiliarByDocumento")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
	public Persona findFamiliarByDocumento(@QueryParam("documento") final String documento) {
		return this.afiliadosServiceLogic.findOneByDocumento(documento);
	}
	
	@Path("/findLocalidades")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
	public Map<String, List<String>> findLocalidades() {
		List<String> localidades = this.afiliadosServiceLogic.findLocalidades();
		Map<String, List<String>> ret =  new HashMap<String, List<String>>();
		ret.put("data", localidades);
		return ret;
	}
	
	@Path("/findLocalidadByNombre")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
	public Localidad findLocalidadByNombre(@QueryParam("nombre") final String nombre) {
		return this.afiliadosServiceLogic.findLocalidadByNombre(nombre);
	}
	
	@Scheduled(cron ="0 15 1 ? * *")
	public void scheduledTask() {
		this.afiliadosServiceLogic.scheduledHijosMayores();
	}
}
