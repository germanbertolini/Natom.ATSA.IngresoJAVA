package ar.com.atsa.commons.dto;

import java.util.LinkedList;
import java.util.List;

import ar.com.atsa.persistence.entities.DocumentoArchivado;
import ar.com.atsa.persistence.entities.Estudio;
import ar.com.atsa.persistence.entities.Familiar;
import ar.com.atsa.persistence.entities.Persona;
import ar.com.atsa.persistence.entities.Tramite;

public class AfiliadoTO {
	
	private Persona afiliado;
	private List<Estudio> estudios = new LinkedList<Estudio>();
	private List<FamiliarTO> familiares = new LinkedList<FamiliarTO>();
	private List<Tramite> tramites = new LinkedList<Tramite>();
	private List<DocumentoArchivado> documentos = new LinkedList<DocumentoArchivado>();
	private String notas;
	private Boolean imprimirCarnet = false;

	private String foto;
	
	public Persona getAfiliado() {
		return afiliado;
	}
	public void setAfiliado(Persona afiliado) {
		this.afiliado = afiliado;
	}
	public List<Estudio> getEstudios() {
		return estudios;
	}
	public void setEstudios(List<Estudio> estudios) {
		this.estudios = estudios;
	}
	public List<FamiliarTO> getFamiliares() {
		return familiares;
	}
	public void setFamiliares(List<FamiliarTO> familiares) {
		this.familiares = familiares;
	}
	public List<Tramite> getTramites() {
		return tramites;
	}
	public void setTramites(List<Tramite> tramites) {
		this.tramites = tramites;
	}
	public String getNotas() {
		return notas;
	}
	public void setNotas(String notas) {
		this.notas = notas;
	}
	public List<DocumentoArchivado> getDocumentos() {
		return documentos;
	}
	public void setDocumentos(List<DocumentoArchivado> documentos) {
		this.documentos = documentos;
	}
	public String getFoto() {
		return foto;
	}
	public void setFoto(String foto) {
		this.foto = foto;
	}
	public Boolean getImprimirCarnet() {
		return imprimirCarnet;
	}
	public void setImprimirCarnet(Boolean imprimirCarnet) {
		this.imprimirCarnet = imprimirCarnet;
	}
	
}
