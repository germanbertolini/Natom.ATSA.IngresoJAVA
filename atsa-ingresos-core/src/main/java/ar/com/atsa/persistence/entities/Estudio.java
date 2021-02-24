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

import ar.com.atsa.commons.enums.NivelEstudio;

@Entity
@Table(name = "ESTUDIO")
public class Estudio implements Serializable, DBEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = -9063001456563376266L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
	private Long id;

	@ManyToOne
    @JoinColumn(name = "PERSONA_ID", referencedColumnName = "ID")
    private Persona persona;

	@Basic
	@Column(name = "NIVEL", nullable = false, insertable = true, updatable = true, length = 1, precision = 0)
	private NivelEstudio nivel;
	
	@Basic
	@Column(name = "TERMINADO", nullable = true, insertable = true, updatable = true)
	private Boolean terminado;
	
	@Column(name = "TITULO")
	private String titulo;
	
	@Column(name = "ESTABLECIMIENTO")
	private String establecimiento;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Persona getPersona() {
		return persona;
	}

	public void setPersona(Persona persona) {
		this.persona = persona;
	}

	public NivelEstudio getNivel() {
		return nivel;
	}

	public void setNivel(NivelEstudio nivel) {
		this.nivel = nivel;
	}

	public Boolean getTerminado() {
		return terminado;
	}

	public void setTerminado(Boolean terminado) {
		this.terminado = terminado;
	}

	public String getTitulo() {
		return titulo;
	}

	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}

	public String getEstablecimiento() {
		return establecimiento;
	}

	public void setEstablecimiento(String establecimiento) {
		this.establecimiento = establecimiento;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null || getClass() != obj.getClass()) return false;
		
		Estudio estudio = (Estudio) obj;
		
		if (estudio.getId() == this.getId()) return false;
		if (this.getPersona() != null ? !this.getPersona().equals(estudio.getPersona()) : estudio.getPersona() != null)
            return false;
		if (this.getEstablecimiento() != null ? !this.getEstablecimiento().equals(estudio.getEstablecimiento()) : estudio.getEstablecimiento() != null)
            return false;
		
		return true;
	}

	@Override
	public int hashCode() {
		int result = this.getId().intValue();
		result = result + (this.getPersona() != null ? this.getPersona().hashCode() : 0);
		result = result + (this.getEstablecimiento() != null ? this.getEstablecimiento().hashCode() : 0);
		
		return result;
	}
}
