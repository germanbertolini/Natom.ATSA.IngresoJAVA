package ar.com.atsa.persistence.entities;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "FAMILIAR")
public class Familiar implements Serializable, DBEntity {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 7557591240975815035L;
	
	public Familiar() {
		
	}
	
	public Familiar(Familiar otro) {
		this.setAfiliado(otro.getAfiliado());
		this.setEstado(otro.getEstado());
		this.setFamiliar(otro.getFamiliar());
		this.setId(otro.getId());
		this.setRelacion(otro.getRelacion());
	}
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
	private Long id;
	
	@ManyToOne
    @JoinColumn(name = "AFILIADO_ID", referencedColumnName = "ID")
    private Persona afiliado;
	
	@ManyToOne
    @JoinColumn(name = "FAMILIAR_ID", referencedColumnName = "ID")
    private Persona familiar;
	
	@ManyToOne
    @JoinColumn(name = "TIPO_RELACION_ID", referencedColumnName = "ID")
    private TipoRelacion relacion;
	
	@Column(name = "ESTADO")
	private String estado;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Persona getAfiliado() {
		return afiliado;
	}

	public void setAfiliado(Persona afiliado) {
		this.afiliado = afiliado;
	}

	public Persona getFamiliar() {
		return familiar;
	}

	public void setFamiliar(Persona familiar) {
		this.familiar = familiar;
	}

	public TipoRelacion getRelacion() {
		return relacion;
	}

	public void setRelacion(TipoRelacion relacion) {
		this.relacion = relacion;
	}
	
	public String getEstado() {
		return estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}
	
	public String toString() {
		StringBuilder builder = new StringBuilder("-----------------------AFILIADO TO START------------------------------\n");
		
		builder.append("\t id --> ").append(this.getId())
			.append("\t propiedad --> ").append(this.getEstado()).append("\n")
			.append("\t propiedad --> ").append(this.getAfiliado()).append("\n")
			.append("\t propiedad --> ").append(this.getFamiliar()).append("\n")
			.append("\t propiedad --> ").append(this.getRelacion()).append("\n");
		
		builder.append("-----------------------AFILIADO TO START------------------------------\n");
		return builder.toString();
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null || getClass() != obj.getClass()) return false;
		
		Familiar familiar = (Familiar) obj;
		
		if (familiar.getId() == this.getId()) return false;
		if (this.getAfiliado() != null ? !this.getAfiliado().equals(familiar.getAfiliado()) : familiar.getAfiliado() != null)
            return false;
		if (this.getFamiliar() != null ? !this.getFamiliar().equals(familiar.getFamiliar()) : familiar.getFamiliar() != null)
            return false;
		if (this.getRelacion() != null ? !this.getRelacion().equals(familiar.getRelacion()) : familiar.getRelacion() != null)
            return false;
		
		return true;
	}

	@Override
	public int hashCode() {
		int result = this.getId().intValue();
		result = result + (this.getAfiliado() != null ? this.getAfiliado().hashCode() : 0);
		result = result + (this.getFamiliar() != null ? this.getFamiliar().hashCode() : 0);
		result = result + (this.getRelacion() != null ? this.getRelacion().hashCode() : 0);
		
		return result;
	}
}
