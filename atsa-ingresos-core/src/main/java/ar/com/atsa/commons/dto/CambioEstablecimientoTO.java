package ar.com.atsa.commons.dto;

import ar.com.atsa.persistence.entities.Establecimiento;

public class CambioEstablecimientoTO {
	
	Establecimiento original;
	Establecimiento destino;
	
	public Establecimiento getOriginal() {
		return original;
	}
	public void setOriginal(Establecimiento original) {
		this.original = original;
	}
	public Establecimiento getDestino() {
		return destino;
	}
	public void setDestino(Establecimiento destino) {
		this.destino = destino;
	}
}
