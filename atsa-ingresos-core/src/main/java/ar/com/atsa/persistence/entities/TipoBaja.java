package ar.com.atsa.persistence.entities;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "TIPO_BAJA")
public class TipoBaja implements Serializable, DBEntity {

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
		
		TipoBaja estado = (TipoBaja) obj;
		
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
