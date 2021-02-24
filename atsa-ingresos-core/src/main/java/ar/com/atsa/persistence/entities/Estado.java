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
@Table(name = "ESTADO")
public class Estado implements Serializable, DBEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6501015089339233209L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
	private Long id;
	
	@Column(name = "NOMBRE", unique=true)
	private String nombre;
	
	@Column(name = "CODIGO", unique=true)
	private String codigo;
	
	@ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "ESTADO_TIPO_TRAMITE", joinColumns = { @JoinColumn(name = "ESTADO_ID", referencedColumnName = "ID") }, inverseJoinColumns = { @JoinColumn(name = "TIPO_TRAMITE_ID", referencedColumnName = "ID") }, uniqueConstraints = @UniqueConstraint(name = "UK_ESTADO_TIPO_TRAMITE", columnNames = {
                    "ESTADO_ID", "TIPO_TRAMITE_ID" }))
    private List<TipoTramite> tramites;

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
	
	public List<TipoTramite> getTramites() {
		return tramites;
	}

	public void setTramites(List<TipoTramite> tramites) {
		this.tramites = tramites;
	}

	public String getCodigo() {
		return codigo;
	}

	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null || getClass() != obj.getClass()) return false;
		
		Estado estado = (Estado) obj;
		
		if (estado.getId() == this.getId()) return false;
		if (this.getNombre() != null ? !this.getNombre().equals(estado.getNombre()) : estado.getNombre() != null)
            return false;
		if (this.getCodigo() != null ? !this.getCodigo().equals(estado.getCodigo()) : estado.getCodigo() != null)
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
