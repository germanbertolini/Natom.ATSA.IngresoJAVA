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
@Table(name = "TIPO_TRAMITE")
public class TipoTramite implements Serializable, DBEntity {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 9004804491337656731L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
	private Long id;
	
	@Column(name = "CODIGO", unique=true, length = 4, precision = 0)
	private String codigo;
	
	@Column(name = "NOMBRE", unique=true)
	private String nombre;
	
	@Column(name = "TIPO")
	private Integer tipo;
	
	@Column(name = "AUTOAPROBADO")
	private Boolean isAutoaprobado;
	
	@ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "tipo_tramite_tipo_documento", joinColumns = { @JoinColumn(name = "TIPO_TRAMITE_ID", referencedColumnName = "ID") }, inverseJoinColumns = { @JoinColumn(name = "TIPO_DOCUMENTO_ID", referencedColumnName = "ID") }, uniqueConstraints = @UniqueConstraint(name = "UK_TIPO_TRAMITE_TIPO_DOCUMENTO", columnNames = {
                    "TIPO_TRAMITE_ID", "TIPO_DOCUMENTO_ID" }))
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
	
	public String getCodigo() {
		return codigo;
	}

	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}

	public List<TipoDocumentoArchivado> getDocumentosObligatorios() {
		return documentosObligatorios;
	}

	public void setDocumentosObligatorios(
			List<TipoDocumentoArchivado> documentosObligatorios) {
		this.documentosObligatorios = documentosObligatorios;
	}

	public Integer getTipo() {
		return tipo;
	}

	public void setTipo(Integer tipo) {
		this.tipo = tipo;
	}

	public Boolean getIsAutoaprobado() {
		return isAutoaprobado;
	}

	public void setIsAutoaprobado(Boolean isAutoaprobado) {
		this.isAutoaprobado = isAutoaprobado;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null || getClass() != obj.getClass()) return false;
		
		TipoTramite tipoTramite = (TipoTramite) obj;
		
		if (tipoTramite.getId() == this.getId()) return false;
		if (this.getNombre() != null ? !this.getNombre().equals(tipoTramite.getNombre()) : tipoTramite.getNombre() != null)
            return false;
		if (this.getCodigo() != null ? !this.getCodigo().equals(tipoTramite.getCodigo()) : tipoTramite.getCodigo() != null)
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
