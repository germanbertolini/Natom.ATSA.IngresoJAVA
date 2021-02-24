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
@Table(name = "CONFIGURACION")
public class Configuracion implements Serializable, DBEntity{

	public final static String KEY_DOCUMENTOS_PATH = "KEY_DOCUMENTOS_PATH";
	public final static String KEY_FOTOS_PATH = "KEY_FOTOS_PATH";
	public final static String KEY_TEMPLATES_PATH = "KEY_TEMPLATES_PATH";
	public final static String KEY_TEMPLATE_FICHA_AFILIACION = "KEY_TEMPLATE_FICHA_AFILIACION";
	public final static String KEY_TEMPLATE_RECEPCION_CARNET = "KEY_TEMPLATE_RECEPCION_CARNET";
	public final static String KEY_TEMPLATE_DESAFILIACION = "KEY_TEMPLATE_DESAFILIACION";
	public final static String KEY_TEMPLATE_FORMULARIO_URL = "KEY_TEMPLATE_FORMULARIO_URL";
	public final static String KEY_TEMPLATE_FORMULARIO_FILE = "KEY_TEMPLATE_FORMULARIO_FILE";
	public final static String KEY_CARNET_SVG_FILE = "KEY_CARNET_SVG_FILE";
	public final static String KEY_CARNETS_PATH= "KEY_CARNETS_PATH";
	public final static String KEY_QR_URL_BASE = "KEY_QR_URL_BASE";
	public final static String KEY_AFILIADO_COUNT = "AFILIADO_COUNT";
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -3865007213157788630L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ID")
	private Long id;
	
	@Column(name = "NOMBRE", unique=true)
	private String nombre;
	
	@Basic
	@Column(name = "VALOR", nullable = true, insertable = true, updatable = true, length = 100, precision = 0)
	private String valor;


	@Override
	public boolean equals(final Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || this.getClass() != o.getClass()) {
			return false;
		}

		Configuracion configuracion = (Configuracion) o;

		if (this.id != configuracion.id) {
			return false;
		}
		if (this.nombre != null ? !this.nombre.equals(configuracion.nombre) : configuracion.nombre != null) {
			return false;
		}
		if (this.valor != null ? !this.valor.equals(configuracion.valor) : configuracion.valor != null) {
			return false;
		}

		return true;
	}
	@Override
	public int hashCode() {
		int result = this.id.intValue();
		result = 31 * result + (this.nombre != null ? this.nombre.hashCode() : 0);
		result = 31 * result + (this.valor != null ? this.valor.hashCode() : 0);
		return result;
	}

	public Long getId() {
		return this.id;
	}
	
	public String getNombre() {
		return nombre;
	}
	
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	
	public String getValor() {
		return valor;
	}
	
	public void setValor(String valor) {
		this.valor = valor;
	}
	
}
