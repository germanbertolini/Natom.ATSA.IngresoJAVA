package ar.com.atsa.persistence.entities;

import java.io.Serializable;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "ESTABLECIMIENTO")
public class Establecimiento implements Serializable, DBEntity {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -2879165335279281745L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ID")
	private Long id;

	@Basic
	@Column(name = "CUIT", nullable = true, insertable = true, updatable = true)
	private String cuit;

	@Basic
	@Column(name = "Codigo", nullable = true, insertable = true, updatable = true)
	private String codigo;

	@Basic
	@Column(name = "Nombre", nullable = true, insertable = true, updatable = true)
	private String nombre;

	@Basic
	@Column(name = "NUMERO_SUCURSAL", nullable = true, insertable = true, updatable = true)
	private Integer numeroSucursal;

	@Basic
	@Column(name = "CALLE", nullable = true, insertable = true, updatable = true)
	private String calle;

	@Basic
	@Column(name = "NUMERO", nullable = true, insertable = true, updatable = true)
	private Integer numero;

	@Basic
	@Column(name = "LOCALIDAD", nullable = true, insertable = true, updatable = true)
	private String localidad;

	@Basic
	@Column(name = "CODIGO_POSTAL", nullable = true, insertable = true, updatable = true)
	private String codigoPostal;

	@Basic
	@Column(name = "PARTIDO", nullable = true, insertable = true, updatable = true)
	private String partido;

	@Basic
	@Column(name = "TELEFONO", nullable = true, insertable = true, updatable = true)
	private String telefono;

	@Basic
	@Column(name = "NOTAS", nullable = true, insertable = true, updatable = true)
	private String notas;

	@Basic
	@Column(name = "VERIFICADO")
	private Boolean verificado = false;

	@Basic
	@Column(name = "ACTIVO")
	private Boolean activo = true;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getCuit() {
		return cuit;
	}

	public void setCuit(String cuit) {
		this.cuit = cuit;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public Integer getNumeroSucursal() {
		return numeroSucursal;
	}

	public void setNumeroSucursal(Integer numeroSucursal) {
		this.numeroSucursal = numeroSucursal;
	}

	public String getCalle() {
		return calle;
	}

	public void setCalle(String calle) {
		this.calle = calle;
	}

	public Integer getNumero() {
		return numero;
	}

	public void setNumero(Integer numero) {
		this.numero = numero;
	}

	public String getLocalidad() {
		return localidad;
	}

	public void setLocalidad(String localidad) {
		this.localidad = localidad;
	}

	public String getCodigoPostal() {
		return codigoPostal;
	}

	public void setCodigoPostal(String codigoPostal) {
		this.codigoPostal = codigoPostal;
	}

	public String getPartido() {
		return partido;
	}

	public void setPartido(String partido) {
		this.partido = partido;
	}

	public String getTelefono() {
		return telefono;
	}

	public void setTelefono(String telefono) {
		this.telefono = telefono;
	}

	public String getNotas() {
		return notas;
	}

	public void setNotas(String notas) {
		this.notas = notas;
	}
	
	public String getCodigo() {
		return codigo;
	}

	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}

	public Boolean getActivo() {
		return activo;
	}

	public void setActivo(Boolean activo) {
		this.activo = activo;
	}

	public Boolean getVerificado() {
		return verificado;
	}

	public void setVerificado(Boolean verificado) {
		this.verificado = verificado;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null || getClass() != obj.getClass()) return false;
		
		Establecimiento establecimiento = (Establecimiento) obj;
		
		if (establecimiento.getId() == this.getId()) return false;
		if (this.getNombre() != null ? !this.getNombre().equals(establecimiento.getNombre()) : establecimiento.getNombre() != null)
            return false;
		if (this.getNumeroSucursal() != null ? !this.getNumeroSucursal().equals(establecimiento.getNumeroSucursal()) : establecimiento.getNumeroSucursal() != null)
            return false;
		
		return true;
	}

	@Override
	public int hashCode() {
		int result = this.getId().intValue();
		result = result + (this.getNombre() != null ? this.getNombre().hashCode() : 0);
		result = result + (this.getNumeroSucursal() != null ? this.getNumeroSucursal().hashCode() : 0);
		
		return result;
	}
}
