package ar.com.atsa.persistence.entities;

import java.io.Serializable;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "DOCUMENTO_ARCHIVADO")
public class DocumentoArchivado implements Serializable, DBEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5908462683589028384L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ID")
	private Long id;
	
	@ManyToOne
    @JoinColumn(name = "TIPO_DOCUMENTO_ID", referencedColumnName = "ID")
    private TipoDocumentoArchivado tipo;
	
	@ManyToOne
    @JoinColumn(name = "PERSONA_ID", referencedColumnName = "ID")
    private Persona persona;
	
	@ManyToOne
    @JoinColumn(name = "TRAMITE_ID", referencedColumnName = "ID")
    private Tramite tramite;
	
	@Basic
	@Column(name = "ACTUAL", columnDefinition = "BIT", length = 1)
	private Boolean actual;
	
	@Basic
	@Column(name = "ACTIVO", columnDefinition = "BIT", length = 1)
	private Boolean activo;
	
	@Column(name = "NOTA")
	private String nota;
	
	@Column(name = "ARCHIVO")
	private String archivo;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public TipoDocumentoArchivado getTipo() {
		return tipo;
	}

	public void setTipo(TipoDocumentoArchivado tipo) {
		this.tipo = tipo;
	}

	public Persona getPersona() {
		return persona;
	}

	public void setPersona(Persona persona) {
		this.persona = persona;
	}

	public Tramite getTramite() {
		return tramite;
	}

	public void setTramite(Tramite tramite) {
		this.tramite = tramite;
	}

	public Boolean getActual() {
		return actual;
	}

	public void setActual(Boolean actual) {
		this.actual = actual;
	}

	public String getNota() {
		return nota;
	}

	public void setNota(String nota) {
		this.nota = nota;
	}

	public String getArchivo() {
		return archivo;
	}

	public void setArchivo(String archivo) {
		this.archivo = archivo;
	}
	
	public Boolean getActivo() {
		return activo;
	}

	public void setActivo(Boolean activo) {
		this.activo = activo;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null || getClass() != obj.getClass()) return false;
		
		DocumentoArchivado documentoArchivado = (DocumentoArchivado) obj;
		
		if (documentoArchivado.getId() == this.getId()) return false;
		if (this.getTipo() != null ? !this.getTipo().equals(documentoArchivado.getTipo()) : documentoArchivado.getTipo() != null)
            return false;
		if (this.getPersona() != null ? !this.getPersona().equals(documentoArchivado.getPersona()) : documentoArchivado.getPersona() != null)
            return false;
		if (this.getTramite() != null ? !this.getTramite().equals(documentoArchivado.getTramite()) : documentoArchivado.getTramite() != null)
            return false;
		if (this.getActual() != null ? !this.getActual() && documentoArchivado.getActual() : documentoArchivado.getActual() != null)
            return false;
		if (this.getArchivo() != null ? !this.getArchivo().equals(documentoArchivado.getArchivo()) : documentoArchivado.getArchivo() != null)
            return false;
		
		return true;
	}

	@Override
	public int hashCode() {
		int result = this.getId().intValue();
		result = result + (this.getTipo() != null ? this.getTipo().hashCode() : 0);
		result = result + (this.getPersona() != null ? this.getPersona().hashCode() : 0);
		result = result + (this.getTramite() != null ? this.getTramite().hashCode() : 0);
		result = result + (this.getActual() != null ? this.getActual().hashCode() : 0);
		result = result + (this.getArchivo() != null ? this.getArchivo().hashCode() : 0);
		
		return result;
	}
}
