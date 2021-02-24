package ar.com.atsa.persistence.entities;

import javax.persistence.*;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 
 * @author diegap
 *
 */
@Entity
@Table(name = "USUARIO")
public class Usuario implements Serializable, DBEntity {

	private static final long serialVersionUID = 3417416080885574187L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
	private Long id;

	@Column(name = "EMAIL", unique=true)
	private String email;

    @Column(name = "CLAVE")
    private String clave;

	@Column(name = "NOMBRES")
	private String nombres;

	@Column(name = "APELLIDOS")
	private String apellidos;
	
	@Basic
	@Column(name = "ACTIVO", columnDefinition = "BIT", length = 1)
	private Boolean activo;
	
	@ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "USUARIO_ROL", joinColumns = { @JoinColumn(name = "USUARIO_ID", referencedColumnName = "ID") }, inverseJoinColumns = { @JoinColumn(name = "ROL_ID", referencedColumnName = "ID") }, uniqueConstraints = @UniqueConstraint(name = "UK_USUARIO_ROL", columnNames = {
                    "USUARIO_ID", "ROL_ID" }))
    private List<Rol> roles;


	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getClave() {
		return clave;
	}

	public void setClave(String clave) {
		this.clave = clave;
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

	public Boolean getActivo() {
		return activo;
	}

	public void setActivo(Boolean activo) {
		this.activo = activo;
	}

	public List<Rol> getRoles() {
		return roles;
	}

	public void setRoles(List<Rol> roles) {
		this.roles = roles;
	}
}
