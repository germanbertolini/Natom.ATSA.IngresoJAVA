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
import javax.persistence.Table;

import ar.com.atsa.commons.enums.EstadoCivil;
import ar.com.atsa.commons.enums.Sexo;
import ar.com.atsa.commons.enums.TipoDocumento;
import ar.com.atsa.commons.enums.TipoPersona;
import ar.com.atsa.commons.utils.DateUtils;

@Entity
@Table(name = "PERSONA")
public class Persona implements Serializable, DBEntity {
	
	private static final long serialVersionUID = 3344361175626947854L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ID")
	private Long id;

	@Basic
	@Column(name = "TIPO_PERSONA")
	private TipoPersona tipoPersona = TipoPersona.AFILIADO;

	@Basic
	@Column(name = "NOMBRES")
	private String nombres;

	@Basic
	@Column(name = "APELLIDOS")
	private String apellidos;

	@Basic
	@Column(name = "ACTIVO", columnDefinition = "BIT", length = 1)
	private boolean activo = true;

	@Basic
	@Column(name = "TIENE_FOTO", columnDefinition = "BIT", length = 1)
	private boolean tieneFoto = false;

	@Basic
	@Column(name = "NUMERO_AFILIADO")
	private String numeroAfiliado;

	@Basic
	@Column(name = "DOCUMENTO_TIPO")
	private TipoDocumento documentoTipo = TipoDocumento.DNI;

	@Basic
	@Column(name = "DOCUMENTO")
	private String documento;

	@Basic
	@Column(name = "SEXO", nullable = false, insertable = true, updatable = true, length = 1, precision = 0)
	private Sexo sexo;

	@Basic
	@Column(name = "FECHA_NACIMIENTO")
	private Date fechaNacimiento;

	@ManyToOne
	@JoinColumn(name = "NACIONALIDAD", referencedColumnName = "ID")
	private Pais nacionalidad;

	@Basic
	@Column(name = "PROFESION", length = 100, precision = 0)
	private String profesion;

	@Basic
	@Column(name = "DOMICILIO")
	private String domicilio;

	@Basic
	@Column(name = "LOCALIDAD")
	private String localidad;

	@Basic
	@Column(name = "CODIGO_POSTAL")
	private String codigoPostal;

	@Basic
	@Column(name = "TELEFONO")
	private String telefono;

	@Basic
	@Column(name = "CUIL")
	private String cuil;

	@Basic
	@Column(name = "FECHA_INGRESO", nullable = true)
	private Date fechaIngreso;

	@Basic
	@Column(name = "FECHA_AFILIACION", nullable = true)
	private Date fechaAfiliacion;
        
        @Basic
	@Column(name = "EMAIL", nullable = true)
	private String email;

	@ManyToOne
    @JoinColumn(name = "ESTADO_ID", referencedColumnName = "ID")
    private Estado estado;

	@ManyToOne
    @JoinColumn(name = "ESTABLECIMIENTO_ID", referencedColumnName = "ID")
    private Establecimiento establecimiento;

	@ManyToOne
    @JoinColumn(name = "CONVENIO_ID", referencedColumnName = "ID")
    private Convenio convenio;

	@ManyToOne
    @JoinColumn(name = "CATEGORIA_ID", referencedColumnName = "ID")
    private Categoria categoria;
	
	@Basic
	@Column(name = "ESTADO_CIVIL", insertable = true, updatable = true, length = 1, precision = 0)
	private EstadoCivil estadoCivil;
	
	@Basic
	@Column(name = "VERSION_CARNET")
	private Integer versionCarnet = 0;
	
	@ManyToOne
    @JoinColumn(name = "TIPO_BAJA_ID", referencedColumnName = "ID")
    private TipoBaja tipoBaja;

	@Basic
	@Column(name = "FECHA_BAJA")
	private Date fechaBaja;
	
	@Basic
	@Column(name = "FAMILIARES_COUNT")
	private Integer familiaresCount = 0;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public EstadoCivil getEstadoCivil() {
		return estadoCivil;
	}

	public void setEstadoCivil(EstadoCivil estadoCivil) {
		this.estadoCivil = estadoCivil;
	}

	public TipoPersona getTipoPersona() {
		return tipoPersona;
	}

	public void setTipoPersona(TipoPersona tipoPersona) {
		this.tipoPersona = tipoPersona;
	}

	public String getNombres() {
		return nombres;
	}

	public void setNombres(String nombres) {
		this.nombres = nombres;
	}

	public String getApellidos() {
		return apellidos;
	}

	public void setApellidos(String apellidos) {
		this.apellidos = apellidos;
	}

	public boolean isActivo() {
		return activo;
	}

	public void setActivo(boolean activo) {
		this.activo = activo;
	}

	public TipoDocumento getDocumentoTipo() {
		return documentoTipo;
	}

	public void setDocumentoTipo(TipoDocumento documentoTipo) {
		this.documentoTipo = documentoTipo;
	}
	
	public String getDocumento() {
		return documento;
	}

	public void setDocumento(String documento) {
		this.documento = documento;
	}

	public Sexo getSexo() {
		return sexo;
	}

	public void setSexo(Sexo sexo) {
		this.sexo = sexo;
	}

	public Date getFechaNacimiento() {
		return fechaNacimiento;
	}

	public void setFechaNacimiento(Date fechaNacimiento) {
		this.fechaNacimiento = fechaNacimiento;
	}

	public String getFechaNacimientoString() {
		return (this.getFechaNacimiento() != null) ? DateUtils.getFormattedString(this.getFechaNacimiento(), "yyyy-MM-dd") : "";
	}

	public void setFechaNacimientoString(String fechaNacimientoString) {
		this.fechaNacimiento = DateUtils.getDateFromStringYyyyMMdd(fechaNacimientoString);
	}

	public Pais getNacionalidad() {
		return nacionalidad;
	}

	public void setNacionalidad(Pais nacionalidad) {
		this.nacionalidad = nacionalidad;
	}

	public String getProfesion() {
		return profesion;
	}

	public void setProfesion(String profesion) {
		this.profesion = profesion;
	}

	public String getDomicilio() {
		return domicilio;
	}

	public void setDomicilio(String domicilio) {
		this.domicilio = domicilio;
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

	public String getTelefono() {
		return telefono;
	}

	public void setTelefono(String telefono) {
		this.telefono = telefono;
	}

	public String getCuil() {
		return cuil;
	}

	public void setCuil(String cuil) {
		this.cuil = cuil;
	}

	public Date getFechaIngreso() {
		return fechaIngreso;
	}

	public void setFechaIngreso(Date fechaIngreso) {
		this.fechaIngreso = fechaIngreso;
	}

	public String getFechaIngresoString() {
		return (this.getFechaIngreso() != null) ? DateUtils.getFormattedString(this.getFechaIngreso(), "yyyy-MM-dd") : "";
	}

	public void setFechaIngresoString(String fechaIngresoString) {
		this.fechaIngreso = DateUtils.getDateFromStringYyyyMMdd(fechaIngresoString);
	}

	public Date getFechaAfiliacion() {
		return fechaAfiliacion;
	}

	public void setFechaAfiliacion(Date fechaAfiliacion) {
		this.fechaAfiliacion = fechaAfiliacion;
	}

	public String getFechaAfiliacionString() {
		return (this.getFechaAfiliacion() != null) ? DateUtils.getFormattedString(this.getFechaAfiliacion(), "yyyy-MM-dd") : "";
	}

	public void setFechaAfiliacionString(String fechaAfiliacionString) {
		this.fechaAfiliacion = DateUtils.getDateFromStringYyyyMMdd(fechaAfiliacionString);
	}
        
        public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Estado getEstado() {
		return estado;
	}

	public void setEstado(Estado estado) {
		this.estado = estado;
	}

	public Establecimiento getEstablecimiento() {
		return establecimiento;
	}

	public void setEstablecimiento(Establecimiento establecimiento) {
		this.establecimiento = establecimiento;
	}

	public String getNumeroAfiliado() {
		return numeroAfiliado;
	}

	public void setNumeroAfiliado(String numeroAfiliado) {
		this.numeroAfiliado = numeroAfiliado;
	}
	
	public boolean getTieneFoto() {
		return tieneFoto;
	}

	public void setTieneFoto(boolean tieneFoto) {
		this.tieneFoto = tieneFoto;
	}
	
	public Convenio getConvenio() {
		return convenio;
	}

	public void setConvenio(Convenio convenio) {
		this.convenio = convenio;
	}

	public Categoria getCategoria() {
		return categoria;
	}

	public void setCategoria(Categoria categoria) {
		this.categoria = categoria;
	}

	public Integer getVersionCarnet() {
		return versionCarnet;
	}

	public void setVersionCarnet(Integer versionCarnet) {
		this.versionCarnet = versionCarnet;
	}

	public Integer getFamiliaresCount() {
		return familiaresCount;
	}

	public void setFamiliaresCount(Integer familiaresCount) {
		this.familiaresCount = familiaresCount;
	}
	
	public TipoBaja getTipoBaja() {
		return tipoBaja;
	}

	public void setTipoBaja(TipoBaja tipoBaja) {
		this.tipoBaja = tipoBaja;
	}

	public Date getFechaBaja() {
		return fechaBaja;
	}

	public void setFechaBaja(Date fechaBaja) {
		this.fechaBaja = fechaBaja;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;

		Persona persona = (Persona) o;

		if (activo != persona.activo)
			return false;
		if (documentoTipo != persona.documentoTipo)
			return false;
		if (id != persona.id)
			return false;
		if (nacionalidad != persona.nacionalidad)
			return false;
		if (apellidos != null ? !apellidos.equals(persona.apellidos)
				: persona.apellidos != null)
			return false;
		if (nombres != null ? !nombres.equals(persona.nombres)
				: persona.nombres != null)
			return false;
		if (sexo != null ? !sexo.equals(persona.sexo) : persona.sexo != null)
			return false;
		if (numeroAfiliado != null ? !numeroAfiliado.equals(persona.numeroAfiliado) : persona.numeroAfiliado != null)
			return false;

		return true;
	}

	@Override
	public int hashCode() {
		int result = id.intValue();
		result = 31 * result + (nombres != null ? nombres.hashCode() : 0);
		result = 31 * result + (apellidos != null ? apellidos.hashCode() : 0);
		result = 31 * result + (activo ? 1 : 0);
		result = 31 * result + documentoTipo.hashCode();
		result = 31 * result + (documento != null ? documento.hashCode() : 0);
		result = 31 * result + (sexo != null ? sexo.hashCode() : 0);
		result = 31 * result + (numeroAfiliado != null ? numeroAfiliado.hashCode() : 0);
		
		return result;
	}
}
