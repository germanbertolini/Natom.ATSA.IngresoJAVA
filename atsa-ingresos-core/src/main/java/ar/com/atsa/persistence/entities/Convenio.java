package ar.com.atsa.persistence.entities;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "CONVENIO")
public class Convenio implements Serializable, DBEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3028626172517992334L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ID")
	private Long id;
	@Basic
	@Column(name = "NOMBRE", nullable = true, insertable = true, updatable = true)
	private String nombre;
	@OneToMany()
	@JoinColumn(name="CONVENIO_ID")
	private List<Categoria> categorias = new LinkedList<Categoria>();
	
	
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
	public List<Categoria> getCategorias() {
		return categorias;
	}
	public void setCategorias(List<Categoria> categorias) {
		this.categorias = categorias;
	}

	
	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null || getClass() != obj.getClass()) return false;
		
		Convenio c = (Convenio) obj;
		
		if (c.getId() == this.getId()) return false;
		if (this.getNombre() != null ? !this.getNombre().equals(c.getNombre()) : c.getNombre() != null)
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
