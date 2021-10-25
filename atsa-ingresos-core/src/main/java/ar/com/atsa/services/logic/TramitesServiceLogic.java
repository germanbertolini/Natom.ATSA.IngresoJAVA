package ar.com.atsa.services.logic;

import java.lang.reflect.InvocationTargetException;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.beanutils.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Component;

import ar.com.atsa.commons.dto.AfiliadoTO;
import ar.com.atsa.commons.dto.CambioEstablecimientoTO;
import ar.com.atsa.commons.dto.FamiliarTO;
import ar.com.atsa.commons.enums.EstadoCivil;
import ar.com.atsa.commons.enums.TipoPersona;
import ar.com.atsa.persistence.entities.Baja;
import ar.com.atsa.persistence.entities.Categoria;
import ar.com.atsa.persistence.entities.Convenio;
import ar.com.atsa.persistence.entities.DBEntity;
import ar.com.atsa.persistence.entities.DocumentoArchivado;
import ar.com.atsa.persistence.entities.Estado;
import ar.com.atsa.persistence.entities.EstadoTramite;
import ar.com.atsa.persistence.entities.Estudio;
import ar.com.atsa.persistence.entities.Familiar;
import ar.com.atsa.persistence.entities.Pais;
import ar.com.atsa.persistence.entities.Persona;
import ar.com.atsa.persistence.entities.Rol;
import ar.com.atsa.persistence.entities.TipoDocumentoArchivado;
import ar.com.atsa.persistence.entities.TipoTramite;
import ar.com.atsa.persistence.entities.Tramite;
import ar.com.atsa.persistence.entities.Usuario;
import ar.com.atsa.persistence.repositories.DocumentoArchivadoRepository;
import ar.com.atsa.persistence.repositories.EstadoRepository;
import ar.com.atsa.persistence.repositories.EstadoTramiteRepository;
import ar.com.atsa.persistence.repositories.EstudioRepository;
import ar.com.atsa.persistence.repositories.FamiliarRepository;
import ar.com.atsa.persistence.repositories.PersonaRepository;
import ar.com.atsa.persistence.repositories.TipoTramiteRepository;
import ar.com.atsa.persistence.repositories.TramiteRepository;
import ar.com.atsa.services.ServiceException;
import ar.com.atsa.services.impl.LoggedInServiceLogic;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.apache.commons.lang3.time.DateUtils;

@Component
public class TramitesServiceLogic {
	
	private static final Integer PAGE_SIZE = 10;
	
	private static final Integer HOME_PAGE_SIZE = 20;
	
	@Autowired
	LoggedInServiceLogic loggedInServiceLogic;
	
	@Autowired
	TramiteRepository tramiteRepository;
	
	@Autowired
	EstadoRepository estadoRepository;
	
	@Autowired
	EstadoTramiteRepository estadoTramiteRepository;
	
	@Autowired
	TipoTramiteRepository tipoTramiteRepository;
	
	@Autowired
	PersonaRepository personaRepository;
	
	@Autowired
	FamiliarRepository familiarRepository;
	
	@Autowired
	EstudioRepository estudioRepository;
	
	@Autowired
	DocumentoArchivadoRepository documentoArchivadoRepository;
	
	public List<Tramite> findTramitesPorAprobar(List<Integer> tramiteTipos) {
		Usuario usuario = this.loggedInServiceLogic.getLoggedInUsuario();
		
		Set<String> codigos = new HashSet<String>();
		for (Rol rol: usuario.getRoles()) {
			for (TipoTramite tipo: rol.getTramites()) {
				if (tramiteTipos.contains(tipo.getTipo())) {
					System.out.println("----------------->" + tipo.getCodigo());
					codigos.add(tipo.getCodigo());
				}
			}
		}
		
		if (codigos.size() > 0) {
			PageRequest request =  new PageRequest(0, HOME_PAGE_SIZE, Direction.DESC, "fechaSolicitud");
			List<Tramite> tramites = this.tramiteRepository.findByEstadoCodigoAndTipoCodigo("00", codigos, request);
		
			return tramites;
		} else {
			return new LinkedList<Tramite>();
		}
	}
	
	public List<Tramite> findTramitesAprobados(List<Integer> tramiteTipos) {
		Usuario usuario = this.loggedInServiceLogic.getLoggedInUsuario();
		
		Set<String> codigos = new HashSet<String>();
		Iterable<TipoTramite> tipoTramites = this.tipoTramiteRepository.findAll();
		for (TipoTramite tipo: tipoTramites) {
			if (tramiteTipos.contains(tipo.getTipo())) codigos.add(tipo.getCodigo());
		}
		
		PageRequest request =  new PageRequest(0, HOME_PAGE_SIZE, Direction.DESC, "fechaAprobacion");
		List<Tramite> tramites = this.tramiteRepository.findByEstadoCodigoAndUsuarioAndTipoCodigoIn("05", usuario, codigos, request);
		return tramites;
	}
	
	public List<Tramite> findTramitesRechazados(List<Integer> tramiteTipos) {
		Usuario usuario = this.loggedInServiceLogic.getLoggedInUsuario();

		Set<String> codigos = new HashSet<String>();
		Iterable<TipoTramite> tipoTramites = this.tipoTramiteRepository.findAll();
		for (TipoTramite tipo: tipoTramites) {
			if (tramiteTipos.contains(tipo.getTipo())) codigos.add(tipo.getCodigo());
		}
		
		PageRequest request =  new PageRequest(0, HOME_PAGE_SIZE, Direction.DESC, "fechaAprobacion");
		List<Tramite> tramites = this.tramiteRepository.findByEstadoCodigoAndUsuarioAndTipoCodigoIn("10", usuario, codigos, request);
		return tramites;
	}
	
	public List<Tramite> findTramitesPageByTipo(final String tipo, final Long id, final Integer page) {
		PageRequest pageRequest = new PageRequest(page, PAGE_SIZE);
		if (tipo.equalsIgnoreCase("Notas")) {
			return this.tramiteRepository.findByPersonaIdAndTipoTipoInAndEstadoCodigoOrderByIdDesc(id, new Integer[] {2}, "05", pageRequest);
		} else if (tipo.equals("Documentos")) {
			return this.tramiteRepository.findByPersonaIdAndTipoTipoInOrderByIdDesc(id, new Integer[]{1}, pageRequest);
		}
		return this.tramiteRepository.findByPersonaIdAndTipoTipoInOrderByIdDesc(id, new Integer[]{0}, pageRequest);
	}
	
	public List<Tramite> findTramites(Long id) {
		return this.tramiteRepository.findByPersonaId(id);
	}
	
	public List<Tramite> findTramitesAprobados() {
		Usuario usuario = this.loggedInServiceLogic.getLoggedInUsuario();
		
		PageRequest request =  new PageRequest(0, HOME_PAGE_SIZE, Direction.DESC, "fechaAprobacion");
		List<Tramite> tramites = this.tramiteRepository.findByEstadoCodigoAndUsuario("05", usuario, request);
		return tramites;
	}
	
	public List<Tramite> findTramitesRechazados() {
		Usuario usuario = this.loggedInServiceLogic.getLoggedInUsuario();
		
		PageRequest request =  new PageRequest(0, HOME_PAGE_SIZE, Direction.DESC, "fechaAprobacion");
		List<Tramite> tramites = this.tramiteRepository.findByEstadoCodigoAndUsuario("10", usuario, request);
		return tramites;
	}
	
	/**
	 * RECHAZA EL TRÁMITE
	 * 
	 * @param tramite
	 */
	public void rechazarTramite(final Tramite tramite, String nota) {
		Usuario usuario = this.loggedInServiceLogic.getLoggedInUsuario();
		EstadoTramite rechazado = this.estadoTramiteRepository.findOneByCodigo("10");
		tramite.setEstado(rechazado);
		tramite.setMotivoRechazo(nota);
		tramite.setAprobador(usuario);
		tramite.setFechaAprobacion(new Date());
		
		if (tramite.getTipo().getCodigo().equalsIgnoreCase("30")) {
			Familiar familiar = this.familiarRepository.findOne(new Long(tramite.getData()));
			
			familiar.setEstado("Rechazado");
			
			this.familiarRepository.save(familiar);
		} 
		/*if (tramite.getTipo().getCodigo().equalsIgnoreCase("60")) {
			List<Tramite> recepcionesCarnet = this.tramiteRepository.findByPersonaIdAndTipoCodigoAndEstadoCodigo(tramite.getPersona().getId(), "DA60", "00");
			for (Tramite recepcionCarnet: recepcionesCarnet) {
				recepcionCarnet.setEstado(rechazado);
				recepcionCarnet.setAprobador(usuario);
				recepcionCarnet.setFechaAprobacion(new Date());
				this.tramiteRepository.save(recepcionCarnet);
			}
		}*/
		
		this.tramiteRepository.save(tramite);
	}
	
	/**
	 * APRUEBA EL TRÁMITE SEGÚN EL TIPO
	 * 
	 * @param tramite
	 * @throws ServiceException 
	 */
	@SuppressWarnings("unchecked")
	public void aprobarTramite(final Tramite tramite) throws ServiceException {
		Usuario usuario = this.loggedInServiceLogic.getLoggedInUsuario();
		EstadoTramite aprobado = this.estadoTramiteRepository.findOneByCodigo("05");
		String data = tramite.getData();
		Gson gson = new Gson();
		
		Persona afiliado = tramite.getPersona();
		
		try {
			if (tramite.getTipo().getCodigo().equalsIgnoreCase("00")) { // AFILIACION
				Estado estadoAfiliado = this.estadoRepository.findOneByCodigo("05");
				afiliado.setEstado(estadoAfiliado);
				//afiliado.setFechaAfiliacion(new Date());
				this.personaRepository.save(afiliado);
			} else if (tramite.getTipo().getCodigo().equalsIgnoreCase("TE10")) { // TESTIMONIO
				List<Tramite> testimoniosPendientes = this.tramiteRepository.findOneByPersonaAndTipoCodigoAndEstadoCodigo(afiliado, "TE20", "00");
				for (Tramite testimonioPendiente: testimoniosPendientes) {
					testimonioPendiente.setEstado(aprobado);
					testimonioPendiente.setAprobador(usuario);
					testimonioPendiente.setFechaAprobacion(new Date());
					this.tramiteRepository.save(testimonioPendiente);
				}
				
				DocumentoArchivado documento = gson.fromJson(data, DocumentoArchivado.class);
				documento = this.documentoArchivadoRepository.findOne(documento.getId());
				if (tramite.getTipo().getCodigo().endsWith("5")) {
					documento.setActivo(false);
				} else {
					documento.setActivo(true);
				}
				this.documentoArchivadoRepository.save(documento);
				
			} else if (tramite.getTipo().getCodigo().equalsIgnoreCase("05")) { // CAMBIO DE DATOS REGISTRALES
				Map<String, Map<String, String>> cambios = new HashMap<String, Map<String, String>>();
				cambios = gson.fromJson(data, cambios.getClass());
				
				Set<String> keys = cambios.keySet();
				for (String key: keys) {
					String value = cambios.get(key).values().iterator().next();
					if (key.equalsIgnoreCase("estadoCivil")) {
						EstadoCivil estadoCivil = gson.fromJson(value, EstadoCivil.class);
						afiliado.setEstadoCivil(estadoCivil);
					} else if (key.equalsIgnoreCase("nacionalidad")) {
						Pais nacionalidad = gson.fromJson(value, Pais.class);
						afiliado.setNacionalidad(nacionalidad);
					} else if (key.equalsIgnoreCase("convenio")) {
						Convenio convenio = gson.fromJson(value, Convenio.class);
						afiliado.setConvenio(convenio);
					} else if (key.equalsIgnoreCase("categoria")) {
						Categoria categoria = gson.fromJson(value, Categoria.class);
						afiliado.setCategoria(categoria);
					} else {
						BeanUtils.copyProperty(afiliado, key, value);
					}
				}
				this.personaRepository.save(afiliado);
			} else if (tramite.getTipo().getCodigo().equalsIgnoreCase("10")) { // CAMBIO DE ESTABLECIMIENTO
				CambioEstablecimientoTO cambio = gson.fromJson(data, CambioEstablecimientoTO.class);
				
				afiliado.setEstablecimiento(cambio.getDestino());
				this.personaRepository.save(afiliado);
			} else if (tramite.getTipo().getCodigo().equalsIgnoreCase("15")) { // BAJA DE AFILIADO
                                Baja datosBaja = gson.fromJson(data, Baja.class);
				Estado estadoDesfiliado = this.estadoRepository.findOneByCodigo("10");
				afiliado.setEstado(estadoDesfiliado);
				afiliado.setTipoBaja(datosBaja.getTipoBaja());
				afiliado.setFechaBaja(datosBaja.getFechaBaja());
				this.personaRepository.save(afiliado);
			} else if (tramite.getTipo().getCodigo().equalsIgnoreCase("16")) { // REAFILIACION
				Estado estadoAfiliado = this.estadoRepository.findOneByCodigo("05");
				afiliado.setEstado(estadoAfiliado);
				this.personaRepository.save(afiliado);
			} else if (tramite.getTipo().getCodigo().equalsIgnoreCase("20")) { // AFILIADO PASIVO
				Estado estadoAfiliado = this.estadoRepository.findOneByCodigo("15");
				afiliado.setEstado(estadoAfiliado);
				afiliado.setTipoPersona(TipoPersona.AFILIADO_PASIVO);
				this.personaRepository.save(afiliado);
			} else if (tramite.getTipo().getCodigo().equalsIgnoreCase("21")) { // AFILIADO PASIVO --> ACTIVO
				Estado estadoAfiliado = this.estadoRepository.findOneByCodigo("05");
				afiliado.setEstado(estadoAfiliado);
				afiliado.setTipoPersona(TipoPersona.AFILIADO);
				this.personaRepository.save(afiliado);
			} else if (tramite.getTipo().getCodigo().equalsIgnoreCase("25")) { // NOTA MULTI PROPÓSITO
				// NOTHING TO DO
			} else if (tramite.getTipo().getCodigo().equalsIgnoreCase("30")) { // AGREGAR FAMILIAR/DEPENDIENTE
				Familiar familiar = this.familiarRepository.findOne(new Long(tramite.getData()));
				
				familiar.setEstado("Aprobado");
				
				EstadoTramite rechazado = this.estadoTramiteRepository.findOneByCodigo("10");
				List<Tramite> tramitesFamiliar = this.tramiteRepository.findByTipoCodigoAndEstadoCodigoAndData("35", "00", tramite.getData());
				for (Tramite tramiteFamiliar: tramitesFamiliar) {
					tramiteFamiliar.setEstado(rechazado);
					tramiteFamiliar.setMotivoRechazo("POR SISTEMA");
					tramiteFamiliar.setAprobador(usuario);
					tramiteFamiliar.setFechaAprobacion(new Date());
					this.tramiteRepository.save(tramiteFamiliar);
				}
				
				if (familiar.getFamiliar().getNumeroAfiliado() == null || familiar.getFamiliar().getNumeroAfiliado().trim().length() < 1) {
					familiar.getFamiliar().setNumeroAfiliado(
							familiar.getAfiliado().getNumeroAfiliado() 
							+ "-" + 
							familiar.getAfiliado().getFamiliaresCount().toString()
					);// nro de afiliado
					familiar.getAfiliado().setFamiliaresCount(familiar.getAfiliado().getFamiliaresCount() +1);
					this.personaRepository.save(familiar.getAfiliado());
				}
				this.personaRepository.save(familiar.getFamiliar());
				
				this.familiarRepository.save(familiar);
			} else if (tramite.getTipo().getCodigo().equalsIgnoreCase("35")) { // ELIMINAR FAMILIAR/DEPENDIENTE
				Familiar familiar = this.familiarRepository.findOne(new Long(tramite.getData()));
				
				EstadoTramite rechazado = this.estadoTramiteRepository.findOneByCodigo("10");
				List<Tramite> tramitesFamiliar = this.tramiteRepository.findByTipoCodigoAndEstadoCodigoAndData("30", "00", tramite.getData());
				for (Tramite tramiteFamiliar: tramitesFamiliar) {
					tramiteFamiliar.setEstado(rechazado);
					tramiteFamiliar.setMotivoRechazo("POR SISTEMA");
					tramiteFamiliar.setAprobador(usuario);
					tramiteFamiliar.setFechaAprobacion(new Date());
					this.tramiteRepository.save(tramiteFamiliar);
				}
				
				this.familiarRepository.delete(familiar);
				
			} else if (tramite.getTipo().getCodigo().equalsIgnoreCase("50")) { // AGREGAR ESTUDIO
				Estudio estudio = gson.fromJson(data, Estudio.class);
				
				Persona estudioPersona = this.personaRepository.findOne(estudio.getPersona().getId());
				estudio.setPersona(estudioPersona);
				
				this.estudioRepository.save(estudio);
			} else if (tramite.getTipo().getCodigo().equalsIgnoreCase("55")) { // ELIMINAR ESTUDIO
				Estudio estudio = gson.fromJson(data, Estudio.class);
				
				this.estudioRepository.delete(estudio);
				
			} else if (tramite.getTipo().getCodigo().startsWith("DA")) { // CARGA DE DOCUMENTO ARCHIVADO
				if (tramite.getTipo().getCodigo().equalsIgnoreCase("DA60")) { // RECEPCION DE CARNET
					
					/*List<Tramite> pendientesRecepcion = this.tramiteRepository.findByPersonaIdAndTipoCodigoAndEstadoCodigo(tramite.getPersona().getId(), "60", "00");
					for (Tramite pendienteRecepcion: pendientesRecepcion) {
						pendienteRecepcion.setEstado(aprobado);
						pendienteRecepcion.setAprobador(usuario);
						pendienteRecepcion.setFechaAprobacion(new Date());
						this.tramiteRepository.save(pendienteRecepcion);
					}*/
				}
				
				DocumentoArchivado documento = gson.fromJson(data, DocumentoArchivado.class);
				documento = this.documentoArchivadoRepository.findOne(documento.getId());
				if (tramite.getTipo().getCodigo().endsWith("5")) {
					documento.setActivo(false);
				} else {
					documento.setActivo(true);
				}
				this.documentoArchivadoRepository.save(documento);
			}
			
			tramite.setAprobador(usuario);
			tramite.setEstado(aprobado);
			tramite.setFechaAprobacion(new Date());
			this.tramiteRepository.save(tramite);
		} catch (Exception e) {
			e.printStackTrace();
			throw new ServiceException(e.getMessage(), e);
		}
	}
	
	public void guardarTramiteImprimirCarnet(Persona afiliado) {
		Usuario usuario = this.loggedInServiceLogic.getLoggedInUsuario();
		EstadoTramite aprobado = this.estadoTramiteRepository.findOneByCodigo("05");
		TipoTramite tipo = this.tipoTramiteRepository.findOneByCodigo("98");
		
		Tramite tramite = new Tramite();
		tramite.setUsuario(usuario);
		tramite.setAprobador(usuario);
		tramite.setEstado(aprobado);
		tramite.setPersona(afiliado);
		tramite.setTipo(tipo);
		tramite.setData(afiliado.getId() + "_" + afiliado.getVersionCarnet());
		
		this.tramiteRepository.save(tramite);
		
		EstadoTramite pendiente = this.estadoTramiteRepository.findOneByCodigo("00");
		tipo = this.tipoTramiteRepository.findOneByCodigo("60");
		
		tramite = new Tramite();
		tramite.setUsuario(usuario);
		if (tipo.getIsAutoaprobado()) {
			tramite.setEstado(aprobado);
		} else {
			tramite.setEstado(pendiente);
		}
		tramite.setPersona(afiliado);
		tramite.setTipo(tipo);
		tramite.setData(afiliado.getId() + "_" + afiliado.getVersionCarnet());
		
		this.tramiteRepository.save(tramite);
	}
	
	public void guardarTramiteCambioDeFoto(Persona afiliado, String foto) {
		Usuario usuario = this.loggedInServiceLogic.getLoggedInUsuario();
		EstadoTramite aprobado = this.estadoTramiteRepository.findOneByCodigo("05");
		TipoTramite tipo = this.tipoTramiteRepository.findOneByCodigo("99");
		
		Tramite tramite = new Tramite();
		tramite.setUsuario(usuario);
		tramite.setAprobador(usuario);
		tramite.setEstado(aprobado);
		tramite.setPersona(afiliado);
		tramite.setTipo(tipo);
		tramite.setData(foto);
		
		this.tramiteRepository.save(tramite);
	}
	
	public Tramite familiarNuevoDocumento(Familiar familiar) {
		List<Tramite> tramites = this.tramiteRepository.findByPersonaIdAndEstadoCodigoAndData(familiar.getAfiliado().getId(), "00", familiar.getId().toString());
		Tramite ret = null;
		
		for (Tramite tramite: tramites) {
			if (tramite.getTipo().getCodigo().equalsIgnoreCase("30")) {
				ret = tramite;
			} else {
				this.rechazarTramite(tramite, "POR SISTEMA");
			}
		}
		
		if (ret == null) {
			Usuario usuario = this.loggedInServiceLogic.getLoggedInUsuario();
			//EstadoTramite pendiente = this.estadoTramiteRepository.findOneByCodigo("00");
			EstadoTramite aprobado = this.estadoTramiteRepository.findOneByCodigo("05");
			TipoTramite tipo = this.tipoTramiteRepository.findOneByCodigo("30");
			
			Tramite tramite = new Tramite();
			tramite.setEstado(aprobado);
			tramite.setTipo(tipo);
			tramite.setData(familiar.getId().toString());
			tramite.setUsuario(usuario);
			tramite.setFechaSolicitud(new Date());
			tramite.setPersona(familiar.getAfiliado());
			tramite.setAprobador(usuario);
			tramite.setFechaAprobacion(new Date());
			
			tramite = this.tramiteRepository.save(tramite);
			ret = tramite;
		}
		
		return ret;
	}
	
	public Tramite inferirTramitePorDocumento(Persona afiliado, TipoDocumentoArchivado tipoDocumentoArchivado, DocumentoArchivado documento) {
		if (tipoDocumentoArchivado.getTipoTramiteAlta() != null) {
			Usuario usuario = this.loggedInServiceLogic.getLoggedInUsuario();
			EstadoTramite pendiente = this.estadoTramiteRepository.findOneByCodigo("00");
			
			Tramite documentoTramite = new Tramite();
			documentoTramite.setUsuario(usuario);
			documentoTramite.setEstado(pendiente);
			documentoTramite.setPersona(afiliado);
			documentoTramite.setTipo(tipoDocumentoArchivado.getTipoTramiteAlta());
			
			Gson gson = new Gson();
			String data = gson.toJson(documento);
			documentoTramite.setData(data);
			
			documentoTramite = this.tramiteRepository.save(documentoTramite);
			
			return documentoTramite;
		} else {
			return null;
		}
	}
	
	/**
	 * INFERIR TRÁMITES A PARTIR DE LOS CAMBIOS
	 * 
	 * @param in
	 * @param original
	 * @param newAfiliado
	 * @return
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 * @throws NoSuchMethodException
	 */
	List<Tramite> inferirTramites(final AfiliadoTO in, final AfiliadoTO original, boolean newAfiliado, int accionAfiliacion) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
		List<Tramite> tramites = new LinkedList<Tramite>();
		
		Usuario usuario = this.loggedInServiceLogic.getLoggedInUsuario();
		
		EstadoTramite pendiente = this.estadoTramiteRepository.findOneByCodigo("00");
		EstadoTramite aprobado = this.estadoTramiteRepository.findOneByCodigo("05");
		
		if (newAfiliado) {
			TipoTramite tipo = this.tipoTramiteRepository.findOneByCodigo("00");
			
			Tramite afiliacion = new Tramite();
			afiliacion.setUsuario(usuario);
			afiliacion.setEstado(pendiente);
			afiliacion.setPersona(in.getAfiliado());
			afiliacion.setTipo(tipo);
			afiliacion.setData(in.getAfiliado().getNombres() + " " + in.getAfiliado().getApellidos());
			
			tramites.add(afiliacion);
			

			tipo = this.tipoTramiteRepository.findOneByCodigo("TE20");
			
			Tramite testimonioPendiente = new Tramite();
			testimonioPendiente.setUsuario(usuario);
			testimonioPendiente.setEstado(pendiente);
			testimonioPendiente.setPersona(in.getAfiliado());
			testimonioPendiente.setTipo(tipo);
			testimonioPendiente.setData("");
			
			tramites.add(testimonioPendiente);
		} else {
			// DATOS REGISTRALES
			Map<String, Map<String, String>> cambiosRegistrales = this.inferirDatosRegistrales(in.getAfiliado(), original.getAfiliado());
			if (cambiosRegistrales.size() > 0) {
				Gson gson = new Gson();
				String data = gson.toJson(cambiosRegistrales);
				
				TipoTramite tipo = this.tipoTramiteRepository.findOneByCodigo("05");
				
				Tramite registral = new Tramite();
				registral.setUsuario(usuario);
				registral.setEstado(pendiente);
				registral.setPersona(in.getAfiliado());
				registral.setTipo(tipo);
				registral.setData(data);
				
				tramites.add(registral);
			}
			
			// CAMBIO DE ESTABLECIMIENTO
			/*if (in.getAfiliado().getEstablecimiento() == null) {
				System.out.println("in.getAfiliado().getEstablecimiento() IS NULL");
			} else {
				System.out.println("in.getAfiliado().getEstablecimiento() IS NOT NULL");
				System.out.println("id is " + in.getAfiliado().getEstablecimiento().getId());
			}
			if (original.getAfiliado().getEstablecimiento() != null) {
				System.out.println("original.getAfiliado().getEstablecimiento() IS NOT NULL");
				System.out.println("id is " + original.getAfiliado().getEstablecimiento().getId());
			} else {
				System.out.println("original.getAfiliado().getEstablecimiento() IS NULL");
			}*/
			
			if (in.getAfiliado().getEstablecimiento() != null && 
					(original.getAfiliado().getEstablecimiento() == null || 
						(!original.getAfiliado().getEstablecimiento().getId().equals(in.getAfiliado().getEstablecimiento().getId()))
					)
				) {
				System.out.println("---------------------------------------------->NUEVO ESTABLECIMIENTO");
				TipoTramite tipo = this.tipoTramiteRepository.findOneByCodigo("10");
				
				CambioEstablecimientoTO cambio = new CambioEstablecimientoTO();
				cambio.setOriginal(original.getAfiliado().getEstablecimiento() == null ? null : original.getAfiliado().getEstablecimiento());
				cambio.setDestino(in.getAfiliado().getEstablecimiento());
				
				Gson gson = new Gson();
				String data = gson.toJson(cambio);
				
				Tramite cambioEstablecimiento = new Tramite();
				cambioEstablecimiento.setUsuario(usuario);
				cambioEstablecimiento.setEstado(pendiente);
				cambioEstablecimiento.setPersona(in.getAfiliado());
				cambioEstablecimiento.setTipo(tipo);
				cambioEstablecimiento.setData(data);
				
				tramites.add(cambioEstablecimiento);
				
				
				tipo = this.tipoTramiteRepository.findOneByCodigo("TE20");
				
				Tramite testimonioPendiente = new Tramite();
				testimonioPendiente.setUsuario(usuario);
				testimonioPendiente.setEstado(pendiente);
				testimonioPendiente.setPersona(in.getAfiliado());
				testimonioPendiente.setTipo(tipo);
				testimonioPendiente.setData("");
				
				tramites.add(testimonioPendiente);
			}
			
			// NUEVOS FAMILIARES
			for (FamiliarTO inFamiliar: in.getFamiliares()) {
				boolean isNew = true;
				FamiliarTO familiarOriginal = null;
				for (FamiliarTO orFamiliar: original.getFamiliares()) {
					if (orFamiliar.getId().equals(inFamiliar.getId())) {
						familiarOriginal = orFamiliar;
						isNew = false;
					}
				}
				if (isNew) {
					TipoTramite tipo = this.tipoTramiteRepository.findOneByCodigo("30");
					
					Persona familiarPersona = inFamiliar.getFamiliar();
					
					if (familiarPersona.getId() == null) {
						familiarPersona.setTipoPersona(TipoPersona.FAMILIAR);
						familiarPersona = this.personaRepository.save(familiarPersona);
					} else {
						familiarPersona = this.personaRepository.findOne(familiarPersona.getId());
					}
					this.personaRepository.save(familiarPersona);
					
					inFamiliar.setAfiliado(in.getAfiliado());
					inFamiliar.setFamiliar(familiarPersona);
					inFamiliar.setEstado("Pendiente");
					
					Familiar familiarEntity = new Familiar(inFamiliar);
					familiarEntity = this.familiarRepository.save(familiarEntity);
					inFamiliar.setId(familiarEntity.getId());
					
					Tramite nuevoFamiliar = new Tramite();
					nuevoFamiliar.setUsuario(usuario);
					nuevoFamiliar.setEstado(pendiente);
					nuevoFamiliar.setPersona(in.getAfiliado());
					nuevoFamiliar.setTipo(tipo);
					nuevoFamiliar.setData(familiarEntity.getId().toString());
					
					tramites.add(nuevoFamiliar);
				} else {
					if (familiarOriginal != null) {
						boolean deleted = false;
						for (DocumentoArchivado documentoOriginal: familiarOriginal.getDocumentos()) {
							boolean found = false;
							for (DocumentoArchivado documentoIn: inFamiliar.getDocumentos()) {
								if (documentoOriginal.getId().equals(documentoIn.getId())) {
									found = true;
								}
							}
							if (!found) {
								documentoOriginal.setActivo(false);
								this.documentoArchivadoRepository.save(documentoOriginal);
								deleted = true;
							}
						}
						
						if (deleted || !familiarOriginal.getFamiliar().getNombres().equalsIgnoreCase(inFamiliar.getFamiliar().getNombres()) || 
								!familiarOriginal.getFamiliar().getApellidos().equalsIgnoreCase(inFamiliar.getFamiliar().getApellidos()) || 
								!(familiarOriginal.getFamiliar().getSexo() == inFamiliar.getFamiliar().getSexo()) || 
								!familiarOriginal.getFamiliar().getDocumento().equalsIgnoreCase(inFamiliar.getFamiliar().getDocumento())|| 
								!familiarOriginal.getRelacion().getNombre().equalsIgnoreCase(inFamiliar.getRelacion().getNombre())) {
							List<Tramite> tramitesFamiliar = this.tramiteRepository.findByTipoCodigoAndEstadoCodigoAndData("30", "00", inFamiliar.getId().toString());
							if (tramitesFamiliar == null || tramitesFamiliar.size() <= 0) {
								TipoTramite tipo = this.tipoTramiteRepository.findOneByCodigo("30");
								Tramite nuevoFamiliar = new Tramite();
								nuevoFamiliar.setUsuario(usuario);
								nuevoFamiliar.setEstado(pendiente);
								nuevoFamiliar.setPersona(in.getAfiliado());
								nuevoFamiliar.setTipo(tipo);
								nuevoFamiliar.setData(inFamiliar.getId().toString());
								
								tramites.add(nuevoFamiliar);
							}
						}
					}
				}
			}
			
			// FAMILIARES ELIMINADOS
			for (Familiar orFamiliar: original.getFamiliares()) {
				boolean isDeleted = true;
				for (Familiar inFamiliar: in.getFamiliares()) {
					if (orFamiliar.getId().equals(inFamiliar.getId())) {
						isDeleted = false;
					}
				}
				if (isDeleted) {
					TipoTramite tipo = this.tipoTramiteRepository.findOneByCodigo("35");
					
					Tramite familiarEliminado = new Tramite();
					familiarEliminado.setUsuario(usuario);
					familiarEliminado.setEstado(pendiente);
					familiarEliminado.setPersona(in.getAfiliado());
					familiarEliminado.setTipo(tipo);
					familiarEliminado.setData(orFamiliar.getId().toString());
					
					tramites.add(familiarEliminado);
				}
			}
			
			// NUEVOS ESTUDIOS
			for (Estudio inEstudio: in.getEstudios()) {
				boolean isNew = true;
				for (Estudio orEstudio: original.getEstudios()) {
					if (orEstudio.getId().equals(inEstudio.getId())) {
						isNew = false;
					}
				}
				if (isNew) {
					TipoTramite tipo = this.tipoTramiteRepository.findOneByCodigo("50");
					inEstudio.setPersona(in.getAfiliado());
					
					Gson gson = new Gson();
					String data = gson.toJson(inEstudio);
					System.out.println(data);
					
					Tramite nuevoEstudio = new Tramite();
					nuevoEstudio.setUsuario(usuario);
					nuevoEstudio.setEstado(pendiente);
					nuevoEstudio.setPersona(in.getAfiliado());
					nuevoEstudio.setTipo(tipo);
					nuevoEstudio.setData(data);
					
					tramites.add(nuevoEstudio);
				}
			}
			
			// ESTUDIOS ELIMINADOS
			for (Estudio orEstudio: original.getEstudios()) {
				boolean isDeleted = true;
				for (Estudio inEstudio: in.getEstudios()) {
					if (orEstudio.getId().equals(inEstudio.getId())) {
						isDeleted = false;
					}
				}
				if (isDeleted) {
					TipoTramite tipo = this.tipoTramiteRepository.findOneByCodigo("55");
					
					Gson gson = new Gson();
					String data = gson.toJson(orEstudio);
					
					
					Tramite estudioEliminado = new Tramite();
					estudioEliminado.setUsuario(usuario);
					estudioEliminado.setEstado(pendiente);
					estudioEliminado.setPersona(in.getAfiliado());
					estudioEliminado.setTipo(tipo);
					estudioEliminado.setData(data);
					
					tramites.add(estudioEliminado);
				}
			}
			
			// DOCUMENTOS ELIMINADOS
			for (DocumentoArchivado orDocumento: original.getDocumentos()) {
				boolean isDeleted = true;
				for (DocumentoArchivado inDocumento: in.getDocumentos()) {
					if (inDocumento.getId().longValue() == orDocumento.getId().longValue()) {
						isDeleted = false;
					}
				}
				if (isDeleted) {
					TipoTramite tipo = orDocumento.getTipo().getTipoTramiteBaja();
					
					if (tipo != null) {
						Gson gson = new GsonBuilder().setExclusionStrategies(new ExclusionStrategy() {
							
							@Override
							public boolean shouldSkipField(FieldAttributes f) {
								return f.getName().equalsIgnoreCase("tramite");
							}
							
							@Override
							public boolean shouldSkipClass(Class<?> clazz) {
								return clazz.equals(Tramite.class);
							}
						}).create();
						String data = gson.toJson(orDocumento);
						System.out.println(data);
						
						Tramite documentoEliminado = new Tramite();
						documentoEliminado.setUsuario(usuario);
						documentoEliminado.setEstado(pendiente);
						documentoEliminado.setPersona(in.getAfiliado());
						documentoEliminado.setTipo(tipo);
						documentoEliminado.setData(data);
						
						tramites.add(documentoEliminado);
					}
				}
			}
		}
		
                //DESAFILIACION
		if (accionAfiliacion == 1) {
			TipoTramite tipoDesafiliacion = this.tipoTramiteRepository.findOneByCodigo("15");
			TipoTramite tipoReafiliacion = this.tipoTramiteRepository.findOneByCodigo("16");
			
			Tramite desafiliacion = new Tramite();
			desafiliacion.setUsuario(usuario);
			desafiliacion.setEstado(pendiente);
			desafiliacion.setPersona(in.getAfiliado());
			if (in.getAfiliado().getEstado().getCodigo().equalsIgnoreCase("05")) {
				desafiliacion.setTipo(tipoDesafiliacion);
			} else {
				desafiliacion.setTipo(tipoReafiliacion);
			}
                        
                        //AGREGO 3 HORAS POR EL UTC -3:00
                        Date fechaBajaUTC = DateUtils.addMinutes(in.getAfiliado().getFechaBaja(), 180);
                        
                        Gson gson =  new Gson();
                        Baja datosBaja = new Baja();
                        datosBaja.setTipoBaja(in.getAfiliado().getTipoBaja());
                        datosBaja.setFechaBaja(fechaBajaUTC);
			String data = gson.toJson(datosBaja);
			System.out.println(data);
			
			desafiliacion.setData(data);
			
			tramites.add(desafiliacion);
		}
                
                //PASIVACION
                else if (accionAfiliacion == 2) {
			TipoTramite tipoPasivacion = this.tipoTramiteRepository.findOneByCodigo("20");
			TipoTramite tipoReactivacion = this.tipoTramiteRepository.findOneByCodigo("21");
			
			Tramite pasivacion = new Tramite();
			pasivacion.setUsuario(usuario);
			pasivacion.setEstado(pendiente);
			pasivacion.setPersona(in.getAfiliado());
			if (in.getAfiliado().getEstado().getCodigo().equalsIgnoreCase("05")) {
				pasivacion.setTipo(tipoPasivacion);
			} else {
				pasivacion.setTipo(tipoReactivacion);
			}
			pasivacion.setData("");
			
			tramites.add(pasivacion);
		}
		
		// NOTAS
		if (in.getNotas() != null && in.getNotas().trim().length() > 0) {
			TipoTramite tipo;
			if (tramites.size() > 0) {
				tipo = this.tipoTramiteRepository.findOneByCodigo("26"); // NOTA DE TRÁMITES
			} else {
				tipo = this.tipoTramiteRepository.findOneByCodigo("25"); // NOTA MULTIPROPÓSITO
			}
			
			String data = in.getNotas().trim();
			
			Tramite nota = new Tramite();
			nota.setUsuario(usuario);
			
			nota.setEstado(pendiente);
			if (tipo.getCodigo().equalsIgnoreCase("25")) nota.setEstado(aprobado);
			
			nota.setPersona(in.getAfiliado());
			nota.setTipo(tipo);
			nota.setData(data);
			
			for (Tramite tramite: tramites) {
				tramite.setNota(nota);
			}
			
			tramites.add(nota);
		}
		
		return tramites;
	}
	
	/**
	 * INFERIR DATOS CATASTRALES
	 * 
	 * @param in
	 * @param original
	 * @return
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 * @throws NoSuchMethodException
	 */
	@SuppressWarnings("unchecked")
	private Map<String, Map<String, String>> inferirDatosRegistrales(final Persona in, final Persona original) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
		Map<String, Map<String, String>> cambios = new HashMap<String, Map<String, String>>();
		String[] propiedadesRegistrales = new String[]{"fechaNacimientoString", "fechaIngresoString", "profesion", "domicilio", "localidad", "codigoPostal", "telefono",
				"nombres", "apellidos", "documento", "sexo", "fechaNacimientoString", "cuil", "email"};
		
		Map<String, String> inProps = BeanUtils.describe(in);
		Map<String, String> orProps = BeanUtils.describe(original);
		
		if (original.getEstadoCivil() == null || in.getEstadoCivil() != original.getEstadoCivil()) {
			Map<String, String> cambio =  new HashMap<String, String>();
			Gson gson =  new Gson();
			String estadoCivilOriginal = gson.toJson(original.getEstadoCivil());
			String estadoCivilDestino = gson.toJson(in.getEstadoCivil());
			
			cambio.put(estadoCivilOriginal, estadoCivilDestino);
			cambios.put("estadoCivil", cambio);
		}
		if (childChanged(in.getConvenio(), original.getConvenio())) {
			Map<String, String> cambio =  new HashMap<String, String>();
			Gson gson =  new Gson();
			String convenioOriginal = gson.toJson(original.getConvenio());
			String convenioDestino = gson.toJson(in.getConvenio());
			
			cambio.put(convenioOriginal, convenioDestino);
			cambios.put("convenio", cambio);
		}
		if (childChanged(in.getCategoria(), original.getCategoria())) {
			Map<String, String> cambio =  new HashMap<String, String>();
			Gson gson =  new Gson();
			String categoriaOriginal = gson.toJson(original.getCategoria());
			String categoriaDestino = gson.toJson(in.getCategoria());
			
			cambio.put(categoriaOriginal, categoriaDestino);
			cambios.put("categoria", cambio);
		}
		if (childChanged(in.getNacionalidad(), original.getNacionalidad())) {
			Map<String, String> cambio =  new HashMap<String, String>();
			Gson gson =  new Gson();
			String paisCivilOriginal = gson.toJson(original.getNacionalidad());
			String paisCivilDestino = gson.toJson(in.getNacionalidad());
			
			cambio.put(paisCivilOriginal, paisCivilDestino);
			cambios.put("nacionalidad", cambio);
		}
		for (String propiedadRegistral: propiedadesRegistrales) {
			String inProp = inProps.get(propiedadRegistral);
			String orProp = orProps.get(propiedadRegistral);
			if ((inProp == null && orProp != null) || (inProp != null && !inProp.equalsIgnoreCase(orProp))) {
				Map<String, String> cambio =  new HashMap<String, String>();
				cambio.put(orProp, inProp);
				cambios.put(propiedadRegistral, cambio);
			}
		}
		
		return cambios;
	}
	
	private boolean childChanged(DBEntity myChild, DBEntity parentChild) {
		boolean ret = false;
		
		if (myChild != null) {
			if (parentChild == null) {
				ret = true;
			} else {
				if (myChild.getId() == null) {
					ret = true;
				} else {
					if (myChild.getId().longValue() != parentChild.getId().longValue()) {
						ret = true;
					}
				}
			}
		}
		
		return ret;
	}
}
