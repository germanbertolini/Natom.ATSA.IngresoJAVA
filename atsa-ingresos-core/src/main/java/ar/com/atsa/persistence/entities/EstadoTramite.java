package ar.com.atsa.persistence.entities;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "ESTADO_TRAMITE")
public class EstadoTramite implements Serializable, DBEntity {

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
	
	@Column(name = "CODIGO", unique=true)
	private String codigo;

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

	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null || getClass() != obj.getClass()) return false;
		
		EstadoTramite estadoTramite = (EstadoTramite) obj;
		
		if (estadoTramite.getId() == this.getId()) return false;
		if (this.getNombre() != null ? !this.getNombre().equals(estadoTramite.getNombre()) : estadoTramite.getNombre() != null)
            return false;
		if (this.getCodigo() != null ? !this.getCodigo().equals(estadoTramite.getCodigo()) : estadoTramite.getCodigo() != null)
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
