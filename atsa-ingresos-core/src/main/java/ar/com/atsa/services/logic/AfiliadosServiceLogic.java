package ar.com.atsa.services.logic;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import javax.imageio.ImageIO;

import org.apache.commons.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import ar.com.atsa.commons.dto.AfiliadoTO;
import ar.com.atsa.commons.dto.DownloadTO;
import ar.com.atsa.commons.dto.FamiliarTO;
import ar.com.atsa.commons.dto.TramiteTO;
import ar.com.atsa.commons.enums.TipoPersona;
import ar.com.atsa.persistence.entities.Configuracion;
import ar.com.atsa.persistence.entities.DocumentoArchivado;
import ar.com.atsa.persistence.entities.Estado;
import ar.com.atsa.persistence.entities.Estudio;
import ar.com.atsa.persistence.entities.Familiar;
import ar.com.atsa.persistence.entities.Localidad;
import ar.com.atsa.persistence.entities.Persona;
import ar.com.atsa.persistence.entities.TipoDocumentoArchivado;
import ar.com.atsa.persistence.entities.TipoRelacion;
import ar.com.atsa.persistence.entities.Tramite;
import ar.com.atsa.persistence.repositories.ConfiguracionRepository;
import ar.com.atsa.persistence.repositories.DocumentoArchivadoRepository;
import ar.com.atsa.persistence.repositories.EstadoRepository;
import ar.com.atsa.persistence.repositories.EstudioRepository;
import ar.com.atsa.persistence.repositories.FamiliarRepository;
import ar.com.atsa.persistence.repositories.LocalidadRepository;
import ar.com.atsa.persistence.repositories.PersonaRepository;
import ar.com.atsa.persistence.repositories.TipoDocumentoArchivadoRepository;
import ar.com.atsa.persistence.repositories.TipoRelacionRepository;
import ar.com.atsa.persistence.repositories.TramiteRepository;
import ar.com.atsa.services.ServiceException;

@Component
public class AfiliadosServiceLogic {

	@Autowired
	PersonaRepository personaRepository;

	@Autowired
	EstudioRepository estudioRepository;

	@Autowired
	FamiliarRepository familiarRepository;

	@Autowired
	TramiteRepository tramiteRepository;

	@Autowired
	TipoRelacionRepository tipoRelacionRepository;

	@Autowired
	EstadoRepository estadoRepository;

	@Autowired
	DocumentoArchivadoRepository documentoArchivadoRepository;
	
	@Autowired
	TipoDocumentoArchivadoRepository tipoDocumentoArchivadoRepository;
	
	@Autowired
	ConfiguracionRepository configuracionRepository;
	
	@Autowired
	LocalidadRepository localidadRepository;
	
	@Autowired
	TramitesServiceLogic tramitesServiceLogic;
	
	@Autowired
	CarnetServiceLogic carnetServiceLogic;
	
	public Persona findAfiliadoByFamiliarId(Long familiarId) {
		return this.familiarRepository.findOneByFamiliarId(familiarId).getAfiliado();
	}
	
	public DownloadTO download(final Long id) {
		DocumentoArchivado documento = this.documentoArchivadoRepository.findOne(id);
		Configuracion pathConfig = this.configuracionRepository.findOneByNombre(Configuracion.KEY_DOCUMENTOS_PATH);
		String path = pathConfig.getValor();
		StringBuilder fileName = new StringBuilder();
		fileName.append(documento.getTipo().getCodigo())
			.append("_")
			.append(documento.getId())
			.append("_")
			.append(documento.getArchivo());
		DownloadTO to =  new DownloadTO();
		to.setFile(new File(path + fileName.toString()));
		to.setFileName(documento.getArchivo());
		return to;
	}
	
	public File getFoto(final Long id) {
		Configuracion pathConfig = this.configuracionRepository.findOneByNombre(Configuracion.KEY_FOTOS_PATH);
		String path = pathConfig.getValor();
		StringBuilder fileName = new StringBuilder();
		fileName.append(path).append(id).append(".jpeg");
		File file = new File(fileName.toString());
		
		if (!file.exists()) {
			file = new File(path + "persona.jpeg");
		}
		
		return file;
	}
	
	@Transactional
	public void upload(InputStream is, String inputFileName, String tipoDocumento, Long afiliadoId) throws ServiceException {
		TipoDocumentoArchivado tipoDocumentoArchivado = this.tipoDocumentoArchivadoRepository.findOneByCodigo(tipoDocumento);
		Persona afiliado = this.personaRepository.findOne(afiliadoId);
		
		DocumentoArchivado documentoArchivado = new DocumentoArchivado();
		documentoArchivado.setActivo(false);
		documentoArchivado.setActual(true);
		documentoArchivado.setPersona(afiliado);
		documentoArchivado.setTipo(tipoDocumentoArchivado);
		documentoArchivado.setArchivo(inputFileName);
		
		documentoArchivado = this.documentoArchivadoRepository.save(documentoArchivado);
		
		Tramite tramite = this.tramitesServiceLogic.inferirTramitePorDocumento(afiliado, tipoDocumentoArchivado, documentoArchivado);
		if (tramite!= null) {
			documentoArchivado.setTramite(tramite);
			documentoArchivado = this.documentoArchivadoRepository.save(documentoArchivado);
			
			if (afiliado.getEstado().getCodigo().equalsIgnoreCase("00") || tramite.getTipo().getIsAutoaprobado()) {
				this.tramitesServiceLogic.aprobarTramite(tramite);
			}
		}
		
		Configuracion pathConfig = this.configuracionRepository.findOneByNombre(Configuracion.KEY_DOCUMENTOS_PATH);
		String path = pathConfig.getValor();
		StringBuilder fileName = new StringBuilder();
		fileName.append(documentoArchivado.getTipo().getCodigo())
			.append("_")
			.append(documentoArchivado.getId())
			.append("_")
			.append(documentoArchivado.getArchivo());
		
		try {
			this.writeToFile(is, path, fileName.toString());
		} catch (IOException e) {
			e.printStackTrace();
			throw new ServiceException("Error guardando el archivo", e);
		}
	}
	
	@Transactional
	public void uploadFamiliar(InputStream is, String inputFileName, String tipoDocumento, Long familiarId) throws ServiceException {
		TipoDocumentoArchivado tipoDocumentoArchivado = this.tipoDocumentoArchivadoRepository.findOneByCodigo(tipoDocumento);
		Familiar familiar = this.familiarRepository.findOne(familiarId);
		
		DocumentoArchivado documentoArchivado = new DocumentoArchivado();
		documentoArchivado.setActivo(true);
		documentoArchivado.setActual(true);
		documentoArchivado.setPersona(familiar.getFamiliar());
		documentoArchivado.setTipo(tipoDocumentoArchivado);
		documentoArchivado.setArchivo(inputFileName);
		
		documentoArchivado = this.documentoArchivadoRepository.save(documentoArchivado);
		
		Tramite tramite = this.tramitesServiceLogic.familiarNuevoDocumento(familiar);
		documentoArchivado.setTramite(tramite);
		documentoArchivado = this.documentoArchivadoRepository.save(documentoArchivado);
		
		Configuracion pathConfig = this.configuracionRepository.findOneByNombre(Configuracion.KEY_DOCUMENTOS_PATH);
		String path = pathConfig.getValor();
		StringBuilder fileName = new StringBuilder();
		fileName.append(documentoArchivado.getTipo().getCodigo())
			.append("_")
			.append(documentoArchivado.getId())
			.append("_")
			.append(documentoArchivado.getArchivo());
		
		try {
			this.writeToFile(is, path, fileName.toString());
		} catch (IOException e) {
			e.printStackTrace();
			throw new ServiceException("Error guardando el archivo", e);
		}
	}
	
	public List<DocumentoArchivado> findFamiliarDocumentos(final Long id) {
		Familiar familiar = this.familiarRepository.findOne(id);
		return this.documentoArchivadoRepository.findByPersonaAndActualAndActivo(familiar.getFamiliar(), true, true);
	}
	
	private void writeToFile(InputStream uploadedInputStream, String filePath, String fileName) throws IOException {
		OutputStream out = new FileOutputStream(new File(filePath + fileName));
		int read = 0;
		byte[] bytes = new byte[1024];
 
		while ((read = uploadedInputStream.read(bytes)) != -1) {
				out.write(bytes, 0, read);
		}
		out.flush();
		out.close();
	}
	
	public AfiliadoTO darPorAfiliado(Long personaId) throws ServiceException {
		Persona afiliado = this.personaRepository.findOne(personaId);
		Tramite tramite = this.tramiteRepository.findOneByPersonaAndTipoCodigo(afiliado, "00");
		if (tramite!= null) {
			TramiteTO to = new TramiteTO();
			to.setTramite(tramite);
			return this.aprobarTramite(to);
		} else {
			Estado estado = this.estadoRepository.findOneByCodigo("05");
			afiliado.setEstado(estado);
			this.personaRepository.save(afiliado);
			return this.getAfiliado(personaId);
		}
	}
	
	/**
	 * APROBAR TRÁMITE
	 * 
	 * @param tramite
	 * @return
	 * @throws ServiceException
	 */
	public AfiliadoTO aprobarTramite(final TramiteTO tramite) throws ServiceException {
		this.tramitesServiceLogic.aprobarTramite(tramite.getTramite());
		
		return this.getAfiliado(tramite.getTramite().getPersona().getId());
	}
	
	/**
	 * RECHAZAR TRÁMITE
	 * 
	 * @param tramite
	 * @return
	 * @throws ServiceException
	 */
	public AfiliadoTO rechazarTramite(final TramiteTO tramite) throws ServiceException {
		this.tramitesServiceLogic.rechazarTramite(tramite.getTramite(), tramite.getNota());
		
		return this.getAfiliado(tramite.getTramite().getPersona().getId());
	}
	
	/**
	 * BÚSQUEDA DE AFILIADOS POR CUIL
	 * 
	 * @param cuil
	 * @return
	 */
	public List<Persona> findByCUIL(final String cuil) {
		return this.personaRepository.findByCuilAndTipoPersona(cuil, TipoPersona.AFILIADO);
	}
	
	/**
	 * BÚSQUEDA DE AFILIADOS POR DOCUMENTO
	 * 
	 * @param documento
	 * @return
	 */
	public List<Persona> findByDocumento(final String documento) {
		return this.personaRepository.findByDocumento(documento);
	}
	
	/**
	 * BÚSQUEDA DE AFILIADOS POR NÚMERO DE AFILIADO
	 * 
	 * @param numeroAfiliado
	 * @return
	 */
	public List<Persona> findByNumeroAfiliado(final String numeroAfiliado) {
		return this.personaRepository.findByNumeroAfiliadoAndTipoPersonaIn(numeroAfiliado, new TipoPersona[] {TipoPersona.AFILIADO, TipoPersona.FAMILIAR, TipoPersona.AFILIADO_PASIVO});
	}
	
	/**
	 * BÚSQUEDA DE PERSONAS POR DOCUMENTO
	 * 
	 * @param documento
	 * @return
	 */
	public List<Persona> findPersonaByDocumento(final String documento) {
		return this.personaRepository.findByDocumento(documento);
	}
	
	/**
	 * OBTENCIÓN DE DETALLES DE AFILIADO
	 * 
	 * @param afiliadoId
	 * @return
	 */
	public AfiliadoTO getAfiliado(final Long afiliadoId) {
		AfiliadoTO to =  new AfiliadoTO();
		Persona afiliado = this.personaRepository.findOne(afiliadoId);
		List<Estudio> estudios = this.estudioRepository.findByPersonaId(afiliadoId);
		List<Familiar> familiares = this.familiarRepository.findByAfiliadoId(afiliadoId);
		//List<Tramite> tramites = this.tramiteRepository.findByPersonaId(afiliadoId);
		List<DocumentoArchivado> documentos = this.documentoArchivadoRepository.findByPersonaAndActualAndActivo(afiliado, true, true);
		
		List<FamiliarTO> familiaresTO =  new ArrayList<FamiliarTO>();
		for (Familiar familiar: familiares) {
			FamiliarTO familiarTO = new FamiliarTO(familiar);
			List<DocumentoArchivado> familiarDocumentos = this.documentoArchivadoRepository.findByPersonaAndActualAndActivo(familiar.getFamiliar(), true, true);
			familiarTO.setDocumentos(familiarDocumentos);
			familiaresTO.add(familiarTO);
		}
		
		to.setAfiliado(afiliado);
		to.setEstudios(estudios);
		to.setFamiliares(familiaresTO);
		to.setTramites(new LinkedList<Tramite>());
		to.setDocumentos(documentos);
		
		return to;
	}
	
	/**
	 * GUARDAR AFILIADO / GENERAR TRÁMITES
	 * 
	 * @param in
	 * @return
	 * @throws ServiceException 
	 */
	public Long saveAfiliado(final AfiliadoTO in, int accionAfiliacion) throws ServiceException {
		
		Persona afiliadoOriginal = null;
		List<Estudio> estudiosOriginales = new LinkedList<Estudio>();
		List<FamiliarTO> familiaresOriginales = new LinkedList<FamiliarTO>();
		List<DocumentoArchivado> documentosOriginales = new LinkedList<DocumentoArchivado>();
		
		Persona afiliado = in.getAfiliado();
		List<Estudio> estudios = in.getEstudios();
		List<FamiliarTO> familiares = in.getFamiliares();

		boolean newAfiliado = in.getAfiliado().getId() == null;
		
		if (!newAfiliado) { // EDITANDO UN AFILIADO EXISTENTE
			afiliadoOriginal = this.personaRepository.findOne(in.getAfiliado().getId());
			estudiosOriginales = this.estudioRepository.findByPersonaId(in.getAfiliado().getId());
			List<Familiar> familiaresDB = this.familiarRepository.findByAfiliadoId(in.getAfiliado().getId());
			documentosOriginales = this.documentoArchivadoRepository.findByPersonaAndActualAndActivo(in.getAfiliado(), true, true);
			
			
			for (Familiar familiar: familiaresDB) {
				FamiliarTO familiarTO = new FamiliarTO(familiar);
				List<DocumentoArchivado> familiarDocumentos = this.documentoArchivadoRepository.findByPersonaAndActualAndActivo(familiar.getFamiliar(), true, true);
				familiarTO.setDocumentos(familiarDocumentos);
				familiaresOriginales.add(familiarTO);
			}
		} else { // AFILIACION DE UNA NUEVA PERSONA
			Estado estado = this.estadoRepository.findOneByCodigo("00");
			afiliado.setEstado(estado);
			afiliado.setFechaAfiliacion(new Date());
			
			afiliado = this.personaRepository.save(afiliado); // SE GUARDAN LOS DATOS DEL NUEVO AFILIADO
			afiliado.setNumeroAfiliado(this.getNumeroAfiliado());
			afiliado = this.personaRepository.save(afiliado); 
			in.setAfiliado(afiliado);
			
			for (Estudio estudio: estudios) {
				estudio.setPersona(afiliado);
			}
			
			int familiaresCount = 0;
			List<Familiar> familiaresEntities = new LinkedList<Familiar>();
			for (Familiar familiar: familiares) {
				Familiar familiarEntity = new Familiar(familiar);
				Persona familiarFamiliar = familiarEntity.getFamiliar();
				familiarFamiliar.setNumeroAfiliado(
						afiliado.getNumeroAfiliado() 
						+ "-" + 
						familiaresCount);
				familiarFamiliar = this.personaRepository.save(familiarFamiliar);
				familiarFamiliar.setTipoPersona(TipoPersona.FAMILIAR);
				
				familiarEntity.setAfiliado(afiliado);
				familiarEntity.setFamiliar(familiarFamiliar);
				//System.out.println(familiarEntity.toString());
				familiaresEntities.add(familiarEntity);
				
				familiaresCount ++;
			}
			afiliado.setFamiliaresCount(familiaresCount);
			afiliado = this.personaRepository.save(afiliado);
			in.setAfiliado(afiliado);

			this.estudioRepository.save(estudios);
			this.familiarRepository.save(familiaresEntities);
		}
		
		// GENERAR TRAMITES SEGÚN LO MODIFICADO
		AfiliadoTO original = new AfiliadoTO();
		original.setAfiliado(afiliadoOriginal);
		original.setEstudios(estudiosOriginales);
		original.setFamiliares(familiaresOriginales);
		original.setDocumentos(documentosOriginales);
		
		try {
			List<Tramite> tramites = this.tramitesServiceLogic.inferirTramites(in, original, newAfiliado, accionAfiliacion);
			this.tramiteRepository.save(tramites);
			afiliado = this.personaRepository.findOne(afiliado.getId());
			
			
			for (Tramite tramite: tramites) {
				if ((afiliado.getEstado().getCodigo().equalsIgnoreCase("00") && 
						!tramite.getTipo().getCodigo().equalsIgnoreCase("00") && 
						!tramite.getTipo().getCodigo().startsWith("TE")) ||
						tramite.getTipo().getIsAutoaprobado()) {
						/*(afiliado.getEstado().getCodigo().equalsIgnoreCase("00") || tramite.getTipo().getCodigo().equalsIgnoreCase("30")) &&
						!tramite.getTipo().getCodigo().equalsIgnoreCase("00") && 
						!tramite.getTipo().getCodigo().startsWith("TE") && 
						tramite.getEstado().getCodigo().equalsIgnoreCase("00")) {*/
					this.tramitesServiceLogic.aprobarTramite(tramite);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new ServiceException(e.getMessage(), e);
		}
		
		// Guardar foto si es el caso
		try {
			if (in.getFoto() != null && in.getFoto().trim().length() > 0) {
				Configuracion pathConfig = this.configuracionRepository.findOneByNombre(Configuracion.KEY_FOTOS_PATH);
				String path = pathConfig.getValor();
				
				String foto = in.getFoto().substring(in.getFoto().indexOf(",") + 1 );
				InputStream stream = new ByteArrayInputStream(Base64.decodeBase64(foto));
				
				BufferedImage image =  ImageIO.read(stream);
				ImageIO.write(image, "jpeg", new File(path + afiliado.getId() + ".jpeg"));
				
				afiliado.setTieneFoto(true);
				this.personaRepository.save(afiliado);
				
				this.tramitesServiceLogic.guardarTramiteCambioDeFoto(afiliado, in.getFoto());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		// imprimir carnet si es necesario
		try {
			if (in.getImprimirCarnet()) {
				this.carnetServiceLogic.generarCarnet(afiliado);
				
				this.tramitesServiceLogic.guardarTramiteImprimirCarnet(afiliado);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return afiliado.getId();
	}
	
	private String getNumeroAfiliado() {
		Configuracion lastNumberStr = this.configuracionRepository.findOneByNombre(Configuracion.KEY_AFILIADO_COUNT);
		Integer lastNumber = new Integer(lastNumberStr.getValor()) + 1;
		
		lastNumberStr.setValor(lastNumber.toString());
		this.configuracionRepository.save(lastNumberStr);
		
		return lastNumber.toString();
	}
	
	public List<String> findLocalidades() {
		List<Localidad> localidades = this.localidadRepository.findAll();
		
		List<String> nombres = new LinkedList<String>();
		for (Localidad localidad: localidades) {
			nombres.add(localidad.getNombre());
		}
		
		return nombres;
	}
	
	public Localidad findLocalidadByNombre(String nombre) {
		return this.localidadRepository.findOneByNombre(nombre);
	}
	
	/**
	 * BÚSQUEDA POR DOCUMENTO
	 * 
	 * @param documento
	 * @return
	 */
	public Persona findOneByDocumento(final String documento) {
		return this.personaRepository.findOneByDocumento(documento);
	}
	
	public void scheduledHijosMayores() {
		TipoRelacion hijoMayor = this.tipoRelacionRepository.findOneByNombre("Hijo mayor");
		
		Calendar cal = Calendar.getInstance();
		cal.setTime(new Date());
		cal.add(Calendar.YEAR, -25);
		Date dateLess25 = cal.getTime();
		
		
		List<Familiar> hijosEstudiantes = this.familiarRepository.findByFamiliarFechaNacimientoLessThanAndRelacionNombre(dateLess25, "Hijo estudiante");
		for(Familiar familiar: hijosEstudiantes) {
			familiar.setRelacion(hijoMayor);
			this.familiarRepository.save(familiar);
		}
		
		cal = Calendar.getInstance();
		cal.setTime(new Date());
		cal.add(Calendar.YEAR, -21);
		Date dateLess21 = cal.getTime();
		List<Familiar> hijos = this.familiarRepository.findByFamiliarFechaNacimientoLessThanAndRelacionNombre(dateLess21, "Hijo");
		for(Familiar familiar: hijos) {
			familiar.setRelacion(hijoMayor);
			this.familiarRepository.save(familiar);
		}
	}
}
