package ar.com.atsa.commons.dto;

import java.util.ArrayList;
import java.util.List;

import ar.com.atsa.persistence.entities.DocumentoArchivado;
import ar.com.atsa.persistence.entities.Familiar;

public class FamiliarTO extends Familiar {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2021480879510640693L;
	
	private List<DocumentoArchivado> documentos = new ArrayList<DocumentoArchivado>();
	
	public FamiliarTO() {
		
	}
	
	public FamiliarTO(Familiar familiar) {
		this.setAfiliado(familiar.getAfiliado());
		this.setEstado(familiar.getEstado());
		this.setFamiliar(familiar.getFamiliar());
		this.setId(familiar.getId());
		this.setRelacion(familiar.getRelacion());
	}

	public List<DocumentoArchivado> getDocumentos() {
		return documentos;
	}

	public void setDocumentos(List<DocumentoArchivado> documentos) {
		this.documentos = documentos;
	}
}
