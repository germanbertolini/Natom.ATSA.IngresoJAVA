package ar.com.atsa.persistence.entities;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

@Entity
@Table(name = "TIPO_RELACION")
public class TipoRelacion implements Serializable, DBEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = -554241928141820962L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
	private Long id;
	
	@Column(name = "NOMBRE", unique=true)
	private String nombre;
	
	@ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "TIPO_RELACION_DOCUMENTOS_OBLIGATORIOS", joinColumns = { @JoinColumn(name = "TIPO_RELACION_ID", referencedColumnName = "ID") }, inverseJoinColumns = { @JoinColumn(name = "TIPO_DOCUMENTO_ID", referencedColumnName = "ID") }, uniqueConstraints = @UniqueConstraint(name = "UK_TIPO_RELACION_DOCUMENTOS_OBLIGATORIOS", columnNames = {
                    "TIPO_RELACION_ID", "TIPO_DOCUMENTO_ID" }))
    private List<TipoDocumentoArchivado> documentosObligatorios;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	
	public List<TipoDocumentoArchivado> getDocumentosObligatorios() {
		return documentosObligatorios;
	}

	public void setDocumentosObligatorios(
			List<TipoDocumentoArchivado> documentosObligatorios) {
		this.documentosObligatorios = documentosObligatorios;
	}
	
	public String toString() {
		return "{id: " + this.getId() + ", nombre: " + this.getNombre() + "}";
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null || getClass() != obj.getClass()) return false;
		
		TipoRelacion tipoTramite = (TipoRelacion) obj;
		
		if (tipoTramite.getId() == this.getId()) return false;
		if (this.getNombre() != null ? !this.getNombre().equals(tipoTramite.getNombre()) : tipoTramite.getNombre() != null)
            return false;
		
		return true;
	}

	@Override
	public int hashCode() {
		int result = this.getId().intValue();
		result = result + (this.getNombre() != null ? this.getNombre().hashCode() : 0);
		
		return result;
	}
}
