package ar.com.atsa.persistence.entities;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "TIPO_DOCUMENTO_ARCHIVADO")
public class TipoDocumentoArchivado implements Serializable, DBEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1105137870927849940L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
	private Long id;
	
	@Column(name = "NOMBRE", unique=true)
	private String nombre;
	
	@Column(name = "CODIGO", unique=true)
	private String codigo;
	
	@ManyToOne
	@JoinColumn(name = "ALTA_TIPO_TRAMITE_ID", referencedColumnName = "ID")
	private TipoTramite tipoTramiteAlta;
	
	@ManyToOne
	@JoinColumn(name = "BAJA_TIPO_TRAMITE_ID", referencedColumnName = "ID")
	private TipoTramite tipoTramiteBaja;

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

	public String getCodigo() {
		return codigo;
	}

	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}

	public TipoTramite getTipoTramiteAlta() {
		return tipoTramiteAlta;
	}

	public void setTipoTramiteAlta(TipoTramite tipoTramiteAlta) {
		this.tipoTramiteAlta = tipoTramiteAlta;
	}

	public TipoTramite getTipoTramiteBaja() {
		return tipoTramiteBaja;
	}

	public void setTipoTramiteBaja(TipoTramite tipoTramiteBaja) {
		this.tipoTramiteBaja = tipoTramiteBaja;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null || getClass() != obj.getClass()) return false;
		
		TipoDocumentoArchivado tipoDocumentoArchivado = (TipoDocumentoArchivado) obj;
		
		if (tipoDocumentoArchivado.getId() == this.getId()) return false;
		if (this.getNombre() != null ? !this.getNombre().equals(tipoDocumentoArchivado.getNombre()) : tipoDocumentoArchivado.getNombre() != null)
            return false;
		if (this.getCodigo() != null ? !this.getNombre().equals(tipoDocumentoArchivado.getCodigo()) : tipoDocumentoArchivado.getCodigo() != null)
            return false;
		
		return true;
	}

	@Override
	public int hashCode() {
		int result = this.getId().intValue();
		result = result + (this.getNombre() != null ? this.getNombre().hashCode() : 0);
		result = result + (this.getCodigo() != null ? this.getCodigo().hashCode() : 0);
		
		return result;
	}
}
