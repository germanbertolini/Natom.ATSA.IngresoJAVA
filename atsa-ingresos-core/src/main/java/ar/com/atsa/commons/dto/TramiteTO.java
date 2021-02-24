package ar.com.atsa.commons.dto;

import ar.com.atsa.persistence.entities.Tramite;

public class TramiteTO {
	private Tramite tramite;
	private String nota;
	
	public Tramite getTramite() {
		return tramite;
	}
	public void setTramite(Tramite tramite) {
		this.tramite = tramite;
	}
	public String getNota() {
		return nota;
	}
	public void setNota(String nota) {
		this.nota = nota;
	}
}
