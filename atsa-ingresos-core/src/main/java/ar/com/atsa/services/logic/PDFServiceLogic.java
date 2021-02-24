package ar.com.atsa.services.logic;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.w3c.dom.Document;
import org.xhtmlrenderer.pdf.ITextRenderer;

import ar.com.atsa.persistence.entities.Configuracion;
import ar.com.atsa.persistence.entities.Estudio;
import ar.com.atsa.persistence.entities.Familiar;
import ar.com.atsa.persistence.entities.Persona;
import ar.com.atsa.persistence.repositories.ConfiguracionRepository;
import ar.com.atsa.persistence.repositories.EstudioRepository;
import ar.com.atsa.persistence.repositories.FamiliarRepository;
import ar.com.atsa.persistence.repositories.PersonaRepository;
import ar.com.atsa.persistence.repositories.TramiteRepository;

@Component
public class PDFServiceLogic {
	
	@Autowired
	PersonaRepository personaRepository;
	
	@Autowired
	TramiteRepository tramiteRepository;
	

	@Autowired
	ConfiguracionRepository configuracionRepository;

	@Autowired
	EstudioRepository estudioRepository;

	@Autowired
	FamiliarRepository familiarRepository;
	
	private final String familiarTemplate = "<TABLE width=\"100%\" style=\"text-size:10px\">"
			+ "<tr>"
			+ "<td width=\"25%\"></td>"
			+ "<td width=\"25%\"></td>"
			+ "<td width=\"25%\"></td>"
			+ "<td width=\"25%\"></td>"
			+ "</tr>"
			+ "<tr>"
			+ "<td><strong>Apellido y nombre</strong></td>"
			+ "<td colspan=\"3\">XXNOMBRESXX</td>"
			+ "</tr>"
			+ "<tr>"
			+ "<td><strong>Fecha de nacim.</strong></td>"
			+ "<td>XXFECHANACXX</td>"
			+ "<td><strong>Documento:</strong></td>"
			+ "<td>XXDOCUMENTOXX</td>"
			+ "</tr>"
			+ "</TABLE><br/>";

	public File documento(final Long id) throws Exception {
		Persona afiliado = this.personaRepository.findOne(id);
		
		Configuracion pathConfig = this.configuracionRepository.findOneByNombre(Configuracion.KEY_TEMPLATES_PATH);
		String path = pathConfig.getValor();
        String ficheroPDF = path + id + ".pdf";
		
		Configuracion templateConfig = this.configuracionRepository.findOneByNombre(Configuracion.KEY_TEMPLATE_FORMULARIO_FILE);
		String templateFile = templateConfig.getValor();
		
		
		String content = FileUtils.readFileToString(new File(templateFile), "utf-8");
		content = content.replaceAll("XXNOMBREXX", afiliado.getNombres() + " " + afiliado.getApellidos());
		content = content.replaceAll("XXCUILXX", afiliado.getCuil());
		content = content.replaceAll("XXAFILIADOXX", afiliado.getNumeroAfiliado());
		content = content.replaceAll("XXESTNOMBREXX", (afiliado.getEstablecimiento() != null ? afiliado.getEstablecimiento().getNombre() : ""));
		
		
		DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
	    Document doc = builder.parse(new ByteArrayInputStream(content.getBytes("utf-8")));

	    ITextRenderer renderer = new ITextRenderer();
	    renderer.setDocument(doc, null);

	    OutputStream os = new FileOutputStream(ficheroPDF);
	    renderer.layout();
	    renderer.createPDF(os);
	    os.close();

		
		return new File(ficheroPDF);
	}

	public File ficha(final Long id) throws Exception {
		Persona afiliado = this.personaRepository.findOne(id);
		
		Configuracion pathConfig = this.configuracionRepository.findOneByNombre(Configuracion.KEY_TEMPLATES_PATH);
		String path = pathConfig.getValor();
        String ficheroPDF = path + id + ".pdf";
		
		Configuracion templateConfig = this.configuracionRepository.findOneByNombre(Configuracion.KEY_TEMPLATE_FICHA_AFILIACION);
		String templateFile = templateConfig.getValor();
		
		SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
		
		
		List<Familiar> familiares = this.familiarRepository.findByAfiliadoId(id);
		List<Estudio> estudios = this.estudioRepository.findByPersonaId(id);
		
		String content = FileUtils.readFileToString(new File(templateFile), "utf-8");
		content = content.replaceAll("XXNOMBREXX", afiliado.getNombres() + " " + afiliado.getApellidos());
		content = content.replaceAll("XXCUILXX", afiliado.getCuil());
		content = content.replaceAll("XXAFILIADOXX", afiliado.getNumeroAfiliado());
		content = content.replaceAll("XXFECHAAFILXX", format.format(afiliado.getFechaAfiliacion()));
		content = content.replaceAll("XXDOMICILIOXX", afiliado.getDomicilio());
		content = content.replaceAll("XXLOCALIDADXX", afiliado.getLocalidad());
		content = content.replaceAll("XXCPXX", afiliado.getCodigoPostal());
		content = content.replaceAll("XXTELEFONOXX", afiliado.getTelefono());
		content = content.replaceAll("XXFECHANACXX", format.format(afiliado.getFechaNacimiento()));
		content = content.replaceAll("XXESTADOCIVILXX", afiliado.getEstadoCivil().name());
		content = content.replaceAll("XXNACIONALIDADXX", afiliado.getNacionalidad().getDescripcion());
		content = content.replaceAll("XXDOCTIPOXX", afiliado.getDocumentoTipo().name());
		content = content.replaceAll("XXDOCUMENTOXX", afiliado.getDocumento());
		content = content.replaceAll("XXPROFESIONXX", afiliado.getProfesion());
		content = content.replaceAll("XXESTNOMBREXX", (afiliado.getEstablecimiento() != null ? afiliado.getEstablecimiento().getNombre() : ""));
		content = content.replaceAll("XXESTVERIFICADOXX", (afiliado.getEstablecimiento() != null ? (afiliado.getEstablecimiento().getVerificado() ? "": "(NO VERIFICADO)") : ""));
		content = content.replaceAll("XXESTCUITXX", (afiliado.getEstablecimiento() != null ? afiliado.getEstablecimiento().getCuit() : ""));
		content = content.replaceAll("XXTITULOXX", "-");
		content = content.replaceAll("XXESTDOMICILIOXX", (afiliado.getEstablecimiento() != null ? afiliado.getEstablecimiento().getCalle() + afiliado.getEstablecimiento().getNumero() : ""));
		content = content.replaceAll("XXESTLOCALIDADXX", (afiliado.getEstablecimiento() != null ? afiliado.getEstablecimiento().getLocalidad() : ""));
		content = content.replaceAll("XXESTPARTIDOXX", (afiliado.getEstablecimiento() != null ? afiliado.getEstablecimiento().getPartido() : ""));
		content = content.replaceAll("XXESTTELEFONOXX", (afiliado.getEstablecimiento() != null ? afiliado.getEstablecimiento().getTelefono() : ""));
		content = content.replaceAll("XXFECHAINGRESOXX", format.format(afiliado.getFechaIngreso()));
		content = content.replaceAll("XXCONYUGEXX", this.getConyuge(familiares));
		content = content.replaceAll("XXHIJOSXX", this.getHijos(familiares));
		content = content.replaceAll("XXOTROS_FAMILIARESXX", this.getOtrosFamiliares(familiares));
		content = content.replaceAll("XXESTUDIOSXX", this.getEstudios(estudios));

		
		DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
	    Document doc = builder.parse(new ByteArrayInputStream(content.getBytes("utf-8")));

	    ITextRenderer renderer = new ITextRenderer();
	    renderer.setDocument(doc, null);

	    OutputStream os = new FileOutputStream(ficheroPDF);
	    renderer.layout();
	    renderer.createPDF(os);
	    os.close();

		
		return new File(ficheroPDF);
	}

	public File desafiliacion(final Long id) throws Exception {
		Persona afiliado = this.personaRepository.findOne(id);
		
		Configuracion pathConfig = this.configuracionRepository.findOneByNombre(Configuracion.KEY_TEMPLATES_PATH);
		String path = pathConfig.getValor();
        String ficheroPDF = path + id + ".pdf";
		
		Configuracion templateConfig = this.configuracionRepository.findOneByNombre(Configuracion.KEY_TEMPLATE_DESAFILIACION);
		String templateFile = templateConfig.getValor();
		
		SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
		List<Familiar> familiares = this.familiarRepository.findByAfiliadoId(id);
		List<Estudio> estudios = this.estudioRepository.findByPersonaId(id);
		;
		
		String content = FileUtils.readFileToString(new File(templateFile), "utf-8");
		content = content.replaceAll("XXFECHADESAFILXX", format.format(this.tramiteRepository.findOneByPersonaAndTipoCodigo(afiliado, "15").getFechaAprobacion()));
		content = content.replaceAll("XXNOMBREXX", afiliado.getNombres() + " " + afiliado.getApellidos());
		content = content.replaceAll("XXCUILXX", afiliado.getCuil());
		content = content.replaceAll("XXAFILIADOXX", afiliado.getNumeroAfiliado());
		content = content.replaceAll("XXFECHAAFILXX", format.format(afiliado.getFechaAfiliacion()));
		content = content.replaceAll("XXDOMICILIOXX", afiliado.getDomicilio());
		content = content.replaceAll("XXLOCALIDADXX", afiliado.getLocalidad());
		content = content.replaceAll("XXCPXX", afiliado.getCodigoPostal());
		content = content.replaceAll("XXTELEFONOXX", afiliado.getTelefono());
		content = content.replaceAll("XXFECHANACXX", format.format(afiliado.getFechaNacimiento()));
		content = content.replaceAll("XXESTADOCIVILXX", afiliado.getEstadoCivil().name());
		content = content.replaceAll("XXNACIONALIDADXX", afiliado.getNacionalidad().getDescripcion());
		content = content.replaceAll("XXDOCTIPOXX", afiliado.getDocumentoTipo().name());
		content = content.replaceAll("XXDOCUMENTOXX", afiliado.getDocumento());
		content = content.replaceAll("XXPROFESIONXX", afiliado.getProfesion());
		content = content.replaceAll("XXESTNOMBREXX", (afiliado.getEstablecimiento() != null ? afiliado.getEstablecimiento().getNombre() : ""));
		content = content.replaceAll("XXESTVERIFICADOXX", (afiliado.getEstablecimiento() != null ? (afiliado.getEstablecimiento().getVerificado() ? "": "(NO VERIFICADO)") : ""));
		content = content.replaceAll("XXESTCUITXX", (afiliado.getEstablecimiento() != null ? afiliado.getEstablecimiento().getCuit() : ""));
		content = content.replaceAll("XXTITULOXX", "-");
		content = content.replaceAll("XXESTDOMICILIOXX", (afiliado.getEstablecimiento() != null ? afiliado.getEstablecimiento().getCalle() + afiliado.getEstablecimiento().getNumero() : ""));
		content = content.replaceAll("XXESTLOCALIDADXX", (afiliado.getEstablecimiento() != null ? afiliado.getEstablecimiento().getLocalidad() : ""));
		content = content.replaceAll("XXESTPARTIDOXX", (afiliado.getEstablecimiento() != null ? afiliado.getEstablecimiento().getPartido() : ""));
		content = content.replaceAll("XXESTTELEFONOXX", (afiliado.getEstablecimiento() != null ? afiliado.getEstablecimiento().getTelefono() : ""));
		content = content.replaceAll("XXFECHAINGRESOXX", format.format(afiliado.getFechaIngreso()));
		content = content.replaceAll("XXCONYUGEXX", this.getConyuge(familiares));
		content = content.replaceAll("XXHIJOSXX", this.getHijos(familiares));
		content = content.replaceAll("XXOTROS_FAMILIARESXX", this.getOtrosFamiliares(familiares));
		content = content.replaceAll("XXESTUDIOSXX", this.getEstudios(estudios));
		
		
		DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
	    Document doc = builder.parse(new ByteArrayInputStream(content.getBytes("utf-8")));

	    ITextRenderer renderer = new ITextRenderer();
	    renderer.setDocument(doc, null);

	    OutputStream os = new FileOutputStream(ficheroPDF);
	    renderer.layout();
	    renderer.createPDF(os);
	    os.close();

		
		return new File(ficheroPDF);
	}

	public File recepcionCarnet(final Long id) throws Exception {
		Persona afiliado = this.personaRepository.findOne(id);
		
		Configuracion pathConfig = this.configuracionRepository.findOneByNombre(Configuracion.KEY_TEMPLATES_PATH);
		String path = pathConfig.getValor();
        String ficheroPDF = path + id + ".pdf";
		
		Configuracion templateConfig = this.configuracionRepository.findOneByNombre(Configuracion.KEY_TEMPLATE_RECEPCION_CARNET);
		String templateFile = templateConfig.getValor();
		
		
		SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
		List<Familiar> familiares = this.familiarRepository.findByAfiliadoId(id);
		List<Estudio> estudios = this.estudioRepository.findByPersonaId(id);
		
		String content = FileUtils.readFileToString(new File(templateFile), "utf-8");
		content = content.replaceAll("XXNOMBREXX", afiliado.getNombres() + " " + afiliado.getApellidos());
		content = content.replaceAll("XXCUILXX", afiliado.getCuil());
		content = content.replaceAll("XXAFILIADOXX", afiliado.getNumeroAfiliado());
		content = content.replaceAll("XXFECHAAFILXX", format.format(afiliado.getFechaAfiliacion()));
		content = content.replaceAll("XXDOMICILIOXX", afiliado.getDomicilio());
		content = content.replaceAll("XXLOCALIDADXX", afiliado.getLocalidad());
		content = content.replaceAll("XXCPXX", afiliado.getCodigoPostal());
		content = content.replaceAll("XXTELEFONOXX", afiliado.getTelefono());
		content = content.replaceAll("XXFECHANACXX", format.format(afiliado.getFechaNacimiento()));
		content = content.replaceAll("XXESTADOCIVILXX", afiliado.getEstadoCivil().name());
		content = content.replaceAll("XXNACIONALIDADXX", afiliado.getNacionalidad().getDescripcion());
		content = content.replaceAll("XXDOCTIPOXX", afiliado.getDocumentoTipo().name());
		content = content.replaceAll("XXDOCUMENTOXX", afiliado.getDocumento());
		content = content.replaceAll("XXPROFESIONXX", afiliado.getProfesion());
		content = content.replaceAll("XXESTNOMBREXX", (afiliado.getEstablecimiento() != null ? afiliado.getEstablecimiento().getNombre() : ""));
		content = content.replaceAll("XXESTVERIFICADOXX", (afiliado.getEstablecimiento() != null ? (afiliado.getEstablecimiento().getVerificado() ? "": "(NO VERIFICADO)") : ""));
		content = content.replaceAll("XXESTCUITXX", (afiliado.getEstablecimiento() != null ? afiliado.getEstablecimiento().getCuit() : ""));
		content = content.replaceAll("XXTITULOXX", "-");
		content = content.replaceAll("XXESTDOMICILIOXX", (afiliado.getEstablecimiento() != null ? afiliado.getEstablecimiento().getCalle() + afiliado.getEstablecimiento().getNumero() : ""));
		content = content.replaceAll("XXESTLOCALIDADXX", (afiliado.getEstablecimiento() != null ? afiliado.getEstablecimiento().getLocalidad() : ""));
		content = content.replaceAll("XXESTPARTIDOXX", (afiliado.getEstablecimiento() != null ? afiliado.getEstablecimiento().getPartido() : ""));
		content = content.replaceAll("XXESTTELEFONOXX", (afiliado.getEstablecimiento() != null ? afiliado.getEstablecimiento().getTelefono() : ""));
		content = content.replaceAll("XXFECHAINGRESOXX", format.format(afiliado.getFechaIngreso()));
		content = content.replaceAll("XXCONYUGEXX", this.getConyuge(familiares));
		content = content.replaceAll("XXHIJOSXX", this.getHijos(familiares));
		content = content.replaceAll("XXOTROS_FAMILIARESXX", this.getOtrosFamiliares(familiares));
		content = content.replaceAll("XXESTUDIOSXX", this.getEstudios(estudios));
		
		
		DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
	    Document doc = builder.parse(new ByteArrayInputStream(content.getBytes("utf-8")));

	    ITextRenderer renderer = new ITextRenderer();
	    renderer.setDocument(doc, null);

	    OutputStream os = new FileOutputStream(ficheroPDF);
	    renderer.layout();
	    renderer.createPDF(os);
	    os.close();

		
		return new File(ficheroPDF);
	}
	
	private String getEstudios(List<Estudio> estudios) {
		String html = "";
		
		if (estudios != null && estudios.size() > 0) {
			html += "<TABLE width=\"100%\" style=\"text-size:10px\">";
			
			for (Estudio estudio: estudios) {
				html += "<tr>";
				html += "<td width=\"60%\"><input type=\"checkbox\" checked=\"true\"/> " + estudio.getNivel().name() + "</td>";
				html += "<td width=\"40%\"><input type=\"checkbox\" " + (estudio.getTerminado() ? "" : "checked=\"true\"") + "/> Incompletos</td>";
				html += "</tr>";
			}
			
			html += "</TABLE>";
		} else {
			html = "No tiene";
		}
		
		return html;
	}
	
	private String getConyuge(List<Familiar> familiares) {
		String html = null;
		if (familiares!=null) {
			for(Familiar familiar: familiares) {
				if (familiar.getRelacion().getNombre().equalsIgnoreCase("Conyuge")) {
					html = familiarTemplate.replaceAll("XXNOMBRESXX", familiar.getFamiliar().getNombres() + " " + familiar.getFamiliar().getApellidos())
							.replaceAll("XXFECHANACXX", familiar.getFamiliar().getFechaNacimientoString())
							.replaceAll("XXDOCUMENTOXX", familiar.getFamiliar().getDocumentoTipo() + " " + familiar.getFamiliar().getDocumento());
				}
			}
		}
		
		if (html == null) html = "No tiene";
		
		return html;
	}
	
	private String getHijos(List<Familiar> familiares) {
		String html = "";
		if (familiares!=null) {
			for(Familiar familiar: familiares) {
				if (familiar.getRelacion().getNombre().toLowerCase().indexOf("hijo") > -1) {
					html += familiarTemplate.replaceAll("XXNOMBRESXX", familiar.getFamiliar().getNombres() + " " + familiar.getFamiliar().getApellidos())
							.replaceAll("XXFECHANACXX", familiar.getFamiliar().getFechaNacimientoString())
							.replaceAll("XXDOCUMENTOXX", familiar.getFamiliar().getDocumentoTipo() + " " + familiar.getFamiliar().getDocumento());
				}
			}
		}
		
		if (html.length() < 1) html = "No tiene";
		
		return html;
	}
	
	private String getOtrosFamiliares(List<Familiar> familiares) {
		String html = "";
		if (familiares!=null) {
			for(Familiar familiar: familiares) {
				if (familiar.getRelacion().getNombre().toLowerCase().indexOf("hijo") < 0 &&
						!familiar.getRelacion().getNombre().equalsIgnoreCase("Conyuge")) {
					html += familiarTemplate.replaceAll("XXNOMBRESXX", familiar.getFamiliar().getNombres() + " " + familiar.getFamiliar().getApellidos())
							.replaceAll("XXFECHANACXX", familiar.getFamiliar().getFechaNacimientoString())
							.replaceAll("XXDOCUMENTOXX", familiar.getFamiliar().getDocumentoTipo() + " " + familiar.getFamiliar().getDocumento());
				}
			}
		}
		
		if (html.length() < 1) html = "No tiene";
		
		return html;
	}
}
