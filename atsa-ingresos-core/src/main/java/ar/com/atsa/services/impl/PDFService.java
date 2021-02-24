package ar.com.atsa.services.impl;

import java.io.File;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import ar.com.atsa.services.logic.PDFServiceLogic;

@Component
@Path("/pdfservice")
public class PDFService {
	
	@Autowired
	PDFServiceLogic pdfServiceLogic;
	
	@Path("/formulario")
	@Produces(MediaType.APPLICATION_OCTET_STREAM)
    @GET
	public Response formulario(@QueryParam("id") final Long id) {
		try {
			File file = this.pdfServiceLogic.documento(id);
			ResponseBuilder response = Response.ok((Object) file);
	        response.header("Content-Disposition", "attachment; filename=\"formulario.pdf\"");
	        return response.build();
		} catch (Exception e) {
			e.printStackTrace();
			return Response.serverError().entity(e.getMessage()).build();
		}
	}
	
	@Path("/ficha")
	@Produces(MediaType.APPLICATION_OCTET_STREAM)
    @GET
	public Response ficha(@QueryParam("id") final Long id) {
		try {
			File file = this.pdfServiceLogic.ficha(id);
			ResponseBuilder response = Response.ok((Object) file);
	        response.header("Content-Disposition", "attachment; filename=\"ficha.pdf\"");
	        return response.build();
		} catch (Exception e) {
			e.printStackTrace();
			return Response.serverError().entity(e.getMessage()).build();
		}
	}
	
	@Path("/desafiliacion")
	@Produces(MediaType.APPLICATION_OCTET_STREAM)
    @GET
	public Response desafiliacion(@QueryParam("id") final Long id) {
		try {
			File file = this.pdfServiceLogic.desafiliacion(id);
			ResponseBuilder response = Response.ok((Object) file);
	        response.header("Content-Disposition", "attachment; filename=\"desafiliacion.pdf\"");
	        return response.build();
		} catch (Exception e) {
			e.printStackTrace();
			return Response.serverError().entity(e.getMessage()).build();
		}
	}
	
	@Path("/recepcionCarnet")
	@Produces(MediaType.APPLICATION_OCTET_STREAM)
    @GET
	public Response recepcionCarnet(@QueryParam("id") final Long id) {
		try {
			File file = this.pdfServiceLogic.recepcionCarnet(id);
			ResponseBuilder response = Response.ok((Object) file);
	        response.header("Content-Disposition", "attachment; filename=\"recepcion_carnet.pdf\"");
	        return response.build();
		} catch (Exception e) {
			e.printStackTrace();
			return Response.serverError().entity(e.getMessage()).build();
		}
	}
}
