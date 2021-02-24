package ar.com.atsa.persistence.entities;

import java.io.Serializable;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Created by tomas.colombo on 02/03/14.
 */
@Entity
@Table(name = "PAIS")
public class Pais implements Serializable, DBEntity{

	/**
	 * 
	 */
	private static final long serialVersionUID = 2908753066577041510L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ID")
	private Long id;
	@Basic
	@Column(name = "DESCRIPCION", nullable = true, insertable = true, updatable = true, length = 100, precision = 0)
	private String descripcion;


	@Override
	public boolean equals(final Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || this.getClass() != o.getClass()) {
			return false;
		}

		Pais pais = (Pais) o;

		if (this.id != pais.id) {
			return false;
		}
		if (this.descripcion != null ? !this.descripcion.equals(pais.descripcion) : pais.descripcion != null) {
			return false;
		}

		return true;
	}
	public String getDescripcion() {
		return this.descripcion;
	}

	public Long getId() {
		return this.id;
	}
	@Override
	public int hashCode() {
		int result = this.id.intValue();
		result = 31 * result + (this.descripcion != null ? this.descripcion.hashCode() : 0);
		return result;
	}

	public void setDescripcion(final String descripcion) {
		this.descripcion = descripcion;
	}

	public void setId(final Long id) {
		this.id = id;
	}
}
