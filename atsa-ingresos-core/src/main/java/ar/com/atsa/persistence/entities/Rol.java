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
@Table(name = "ROL")
public class Rol implements Serializable, DBEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1475781760992316193L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
	private Long id;
	
	@Column(name = "NOMBRE", unique=true)
	private String nombre;
	
	@ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "ROL_TIPO_TRAMITE", joinColumns = { @JoinColumn(name = "ROL_ID", referencedColumnName = "ID") }, inverseJoinColumns = { @JoinColumn(name = "TIPO_TRAMITE_ID", referencedColumnName = "ID") }, uniqueConstraints = @UniqueConstraint(name = "UK_ROL_TIPO_TRAMITE", columnNames = {
                    "ROL_ID", "TIPO_TRAMITE_ID" }))
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

	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null || getClass() != obj.getClass()) return false;
		
		Rol rol = (Rol) obj;
		
		if (rol.getId() == this.getId()) return false;
		if (this.getNombre() != null ? !this.getNombre().equals(rol.getNombre()) : rol.getNombre() != null)
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
