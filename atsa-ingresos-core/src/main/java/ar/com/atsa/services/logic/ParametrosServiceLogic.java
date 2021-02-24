package ar.com.atsa.services.logic;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import ar.com.atsa.persistence.entities.Convenio;
import ar.com.atsa.persistence.entities.Establecimiento;
import ar.com.atsa.persistence.entities.Estado;
import ar.com.atsa.persistence.entities.Pais;
import ar.com.atsa.persistence.entities.Rol;
import ar.com.atsa.persistence.entities.TipoBaja;
import ar.com.atsa.persistence.entities.TipoDocumentoArchivado;
import ar.com.atsa.persistence.entities.TipoRelacion;
import ar.com.atsa.persistence.entities.TipoTramite;
import ar.com.atsa.persistence.repositories.ConvenioRepository;
import ar.com.atsa.persistence.repositories.EstablecimientoRepository;
import ar.com.atsa.persistence.repositories.EstadoRepository;
import ar.com.atsa.persistence.repositories.PaisRepository;
import ar.com.atsa.persistence.repositories.RolRepository;
import ar.com.atsa.persistence.repositories.TipoBajaRepository;
import ar.com.atsa.persistence.repositories.TipoDocumentoArchivadoRepository;
import ar.com.atsa.persistence.repositories.TipoRelacionRepository;
import ar.com.atsa.persistence.repositories.TipoTramiteRepository;

@Component
public class ParametrosServiceLogic {
	
	@Autowired
	private TipoTramiteRepository tipoTramiteRepository;
	
	@Autowired
	private TipoRelacionRepository tipoRelacionRepository;
	
	@Autowired
	private TipoDocumentoArchivadoRepository tipoDocumentoArchivadoRepository;
	
	@Autowired
	private RolRepository rolRepository;
	
	@Autowired
	private PaisRepository paisRepository;
	
	@Autowired
	private EstadoRepository estadoRepository;
	
	@Autowired
	private EstablecimientoRepository establecimientoRepository;
	
	@Autowired
	private ConvenioRepository convenioRepository;
	
	@Autowired
	private TipoBajaRepository tipoBajaRepository;
	
	public Iterable<TipoBaja> getAllTiposBaja() {
		return this.tipoBajaRepository.findAll();
	}
	
	public Iterable<Convenio> getAllConvenios() {
		return this.convenioRepository.findAll();
	}
	
	public Iterable<Establecimiento> getAllEstablecimientos() {
		return this.establecimientoRepository.findAll();
	}
	
	public Iterable<Estado> getAllEstados() {
		return this.estadoRepository.findAll();
	}
	
	public Iterable<Pais> getAllPaises() {
		return this.paisRepository.findAll();
	}
	
	public Iterable<Rol> getAllRoles() {
		return this.rolRepository.findAll();
	}
	
	public Iterable<TipoDocumentoArchivado> getAllTipoDocumentosArchivados() {
		return this.tipoDocumentoArchivadoRepository.findAll();
	}
	
	public Iterable<TipoRelacion> getAllTipoRelaciones() {
		return this.tipoRelacionRepository.findAll();
	}
	
	public Iterable<TipoTramite> getAllTipoTramites() {
		return this.tipoTramiteRepository.findAll();
	}
}
