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

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "CATEGORIA")
public class Categoria implements Serializable, DBEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3119239001911346862L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ID")
	private Long id;
	@Basic
	@Column(name = "NOMBRE", nullable = true, insertable = true, updatable = true)
	private String nombre;
//	@ManyToOne
//    @JoinColumn(name = "CONVENIO_ID", referencedColumnName = "ID")
//	@JsonIgnore
//    private Convenio convenio;
	
	
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
//	public Convenio getConvenio() {
//		return convenio;
//	}
//	public void setConvenio(Convenio convenio) {
//		this.convenio = convenio;
//	}

	
	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null || getClass() != obj.getClass()) return false;
		
		Categoria c = (Categoria) obj;
		
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
