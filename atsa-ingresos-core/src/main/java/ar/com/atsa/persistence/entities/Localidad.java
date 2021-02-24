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
@Table(name = "LOCALIDAD")
public class Localidad implements Serializable, DBEntity{

	/**
	 * 
	 */
	private static final long serialVersionUID = -3755657653582001824L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ID")
	private Long id;
	@Basic
	@Column(name = "NOMBRE", nullable = true, insertable = true, updatable = true, length = 100, precision = 0)
	private String nombre;
	@Basic
	@Column(name = "CODIGO_POSTAL", nullable = true, insertable = true, updatable = true, length = 100, precision = 0)
	private String codigoPostal;
	
	
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
	public String getCodigoPostal() {
		return codigoPostal;
	}
	public void setCodigoPostal(String codigoPostal) {
		this.codigoPostal = codigoPostal;
	}
	
	
}
