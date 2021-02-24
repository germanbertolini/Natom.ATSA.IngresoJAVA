package ar.com.atsa.persistence.entities;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PrePersist;
import javax.persistence.Table;

import ar.com.atsa.commons.utils.DateUtils;

@Entity
@Table(name = "TRAMITE")
public class Tramite implements Serializable, DBEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 497646767015020483L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
	private Long id;
	
	@ManyToOne
    @JoinColumn(name = "TIPO_TRAMITE_ID", referencedColumnName = "ID")
    private TipoTramite tipo;
	
	@ManyToOne
    @JoinColumn(name = "PERSONA_ID", referencedColumnName = "ID")
    private Persona persona;
	
	@ManyToOne
    @JoinColumn(name = "USUARIO_ID", referencedColumnName = "ID")
    private Usuario usuario;
	
	@ManyToOne
    @JoinColumn(name = "ESTADO_ID", referencedColumnName = "ID")
    private EstadoTramite estado;

	@Basic
	@Column(name = "FECHA_SOLICITUD", nullable = false, insertable = true, updatable = true, length = 10, precision = 0)
	private Date fechaSolicitud;

	@Basic
	@Column(name = "FECHA_APROBACION", nullable = true, insertable = true, updatable = true, length = 10, precision = 0)
	private Date fechaAprobacion;
	
	@ManyToOne
    @JoinColumn(name = "APROBADOR_ID", referencedColumnName = "ID", nullable = true)
    private Usuario aprobador;
	
	@ManyToOne
    @JoinColumn(name = "NOTA_ID", referencedColumnName = "ID", nullable = true)
    private Tramite nota;
	
	@Column(name = "DATA", columnDefinition = "TEXT")
	private String data;
	
	@Column(name = "MOTIVO_RECHAZO", length = 2040)
	private String motivoRechazo;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public TipoTramite getTipo() {
		return tipo;
	}

	public void setTipo(TipoTramite tipo) {
		this.tipo = tipo;
	}

	public Persona getPersona() {
		return persona;
	}

	public void setPersona(Persona persona) {
		this.persona = persona;
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	public EstadoTramite getEstado() {
		return estado;
	}

	public void setEstado(EstadoTramite estado) {
		this.estado = estado;
	}

	public Date getFechaSolicitud() {
		return fechaSolicitud;
	}

	public void setFechaSolicitud(Date fechaSolicitud) {
		this.fechaSolicitud = fechaSolicitud;
	}

	public String getFechaSolicitudString() {
		return (this.getFechaSolicitud() != null) ? DateUtils.getFormattedString(this.getFechaSolicitud(), "yyyy-MM-dd") : "";
	}

	public void setFechaSolicitudString(String fechaSolicitudString) {
		this.fechaSolicitud = DateUtils.getDateFromStringYyyyMMdd(fechaSolicitudString);
	}

	public Date getFechaAprobacion() {
		return fechaAprobacion;
	}

	public void setFechaAprobacion(Date fechaAprobacion) {
		this.fechaAprobacion = fechaAprobacion;
	}

	public String getFechaAprobacionString() {
		return (this.getFechaAprobacion() != null) ? DateUtils.getFormattedString(this.getFechaAprobacion(), "yyyy-MM-dd") : "";
	}

	public void setFechaAprobacionString(String fechaAprobacionString) {
		this.fechaAprobacion = DateUtils.getDateFromStringYyyyMMdd(fechaAprobacionString);
	}

	public Usuario getAprobador() {
		return aprobador;
	}

	public void setAprobador(Usuario aprobador) {
		this.aprobador = aprobador;
	}

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}
	
	public Tramite getNota() {
		return nota;
	}

	public void setNota(Tramite nota) {
		this.nota = nota;
	}
	
	public String getMotivoRechazo() {
		return motivoRechazo;
	}

	public void setMotivoRechazo(String motivoRechazo) {
		this.motivoRechazo = motivoRechazo;
	}

	@PrePersist
	void createdAt() {
		this.fechaSolicitud = new Date();
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null || getClass() != obj.getClass()) return false;
		
		Tramite tramite = (Tramite) obj;
		
		if (tramite.getId() == this.getId()) return false;
		if (this.getPersona() != null ? !this.getPersona().equals(tramite.getPersona()) : tramite.getPersona() != null)
            return false;
		if (this.getTipo() != null ? !this.getTipo().equals(tramite.getTipo()) : tramite.getTipo() != null)
            return false;
		if (this.getEstado() != null ? !this.getEstado().equals(tramite.getEstado()) : tramite.getEstado() != null)
            return false;
		if (this.getData() != null ? !this.getData().equals(tramite.getData()) : tramite.getData() != null)
            return false;
		
		return true;
	}

	@Override
	public int hashCode() {
		int result = this.getId().intValue();
		result = result + (this.getPersona() != null ? this.getPersona().hashCode() : 0);
		result = result + (this.getTipo() != null ? this.getTipo().hashCode() : 0);
		result = result + (this.getEstado() != null ? this.getEstado().hashCode() : 0);
		result = result + (this.getData() != null ? this.getData().hashCode() : 0);
		
		return result;
	}
}
