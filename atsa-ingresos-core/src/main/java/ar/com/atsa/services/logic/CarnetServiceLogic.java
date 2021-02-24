package ar.com.atsa.services.logic;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.util.Date;
import java.util.List;
import java.util.StringTokenizer;

import net.glxn.qrgen.QRCode;
import net.glxn.qrgen.image.ImageType;

import org.apache.batik.transcoder.TranscoderInput;
import org.apache.batik.transcoder.TranscoderOutput;
import org.apache.batik.transcoder.image.JPEGTranscoder;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.text.WordUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import ar.com.atsa.commons.enums.TipoPersona;
import ar.com.atsa.persistence.entities.Configuracion;
import ar.com.atsa.persistence.entities.Persona;
import ar.com.atsa.persistence.repositories.ConfiguracionRepository;
import ar.com.atsa.persistence.repositories.PersonaRepository;
import ar.com.atsa.services.ServiceException;

import com.google.zxing.EncodeHintType;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

@Component
public final class CarnetServiceLogic {
	
	static Logger logger = Logger.getLogger(CarnetServiceLogic.class);
	
	@Autowired
	ConfiguracionRepository configuracionRepository;
	
	@Autowired
	PersonaRepository personaRepository;
	
	public File getCarnetByKey(String key) throws ServiceException {
		Configuracion carnetsPathConf = this.configuracionRepository.findOneByNombre(Configuracion.KEY_CARNETS_PATH);
		String carnetsPath = carnetsPathConf.getValor();
		
		String outPath = carnetsPath + "Credencial_" + key + ".jpg";
		
		File file = new File(outPath);
		
		return file;
	}
	
	public File getCarnet(Long afiliadoId) throws ServiceException {
		Persona afiliado = this.personaRepository.findOne(afiliadoId);
		Configuracion carnetsPathConf = this.configuracionRepository.findOneByNombre(Configuracion.KEY_CARNETS_PATH);
		String carnetsPath = carnetsPathConf.getValor();
		
		String outPath = carnetsPath + "Credencial_" + afiliadoId + "_" + afiliado.getVersionCarnet() + ".jpg";
		logger.debug(outPath);
		
		File file = new File(outPath);
		if (!file.exists()) {
			String newPath = this.generarCarnet(afiliado);
			file = new File(newPath);
		}
		
		return file;
	}

	public String generarCarnet(Persona afiliado) throws ServiceException {
		afiliado.setVersionCarnet(afiliado.getVersionCarnet() + 1);
		this.personaRepository.save(afiliado);
		
		
		Configuracion svgFileConf = this.configuracionRepository.findOneByNombre(Configuracion.KEY_CARNET_SVG_FILE);
		Configuracion carnetsPathConf = this.configuracionRepository.findOneByNombre(Configuracion.KEY_CARNETS_PATH);
		Configuracion qrUrlBaseConf = this.configuracionRepository.findOneByNombre(Configuracion.KEY_QR_URL_BASE);
		
		String svgFilePath = svgFileConf.getValor();
		String carnetsPath = carnetsPathConf.getValor();
		String qrUrlBase = qrUrlBaseConf.getValor();
		String outPath = carnetsPath + "Credencial_" + afiliado.getId() + "_" + afiliado.getVersionCarnet() + ".jpg";
		
		String svg = "";

        try {
            // Lee la plantilla de diseño
            List<String> lines = Files.readAllLines(Paths.get(svgFilePath), Charset.forName("UTF-8"));

            //Genera el QR con la URL (la misma deberia ser un parametro de configuración)
            ByteArrayOutputStream out = QRCode.from(qrUrlBase + afiliado.getId() + "/" + afiliado.getVersionCarnet())
                    .withSize(200, 200)
                    //.withCharset("UTF-8")
                    .to(ImageType.PNG)
                    .withErrorCorrection(ErrorCorrectionLevel.M)
                    .withHint(EncodeHintType.MARGIN, 1)
                    .stream();
            byte imageData[] = out.toByteArray();
            String img = Base64.encodeBase64String(imageData);

            StringBuilder sb = new StringBuilder();
            String sep = "";
            for (String s : lines) {
                sb.append(sep).append(s);
                sep = "";
            }
            svg = sb.toString();
            
            
            String tipoDocumento = "CUIL";
            String numeroDocumento = "";
            String tipoPersona = "TITULAR";
            
            if (afiliado.getTipoPersona() == TipoPersona.AFILIADO_PASIVO) {
            	tipoPersona = "JUBILADO VITALICIO";
            	numeroDocumento = afiliado.getDocumento();
            	tipoDocumento = afiliado.getDocumentoTipo().toString();
            } else if (afiliado.getTipoPersona() == TipoPersona.FAMILIAR) {
            	tipoPersona = "GRUPO FAMILIAR";
            	numeroDocumento = afiliado.getDocumento();
            	tipoDocumento = afiliado.getDocumentoTipo().toString();
            } else {
            	numeroDocumento = afiliado.getCuil();
            }
        	
            
            String establecimiento = "-";
            String[] numAfilTit = afiliado.getNumeroAfiliado().toString().split("-");
        	Persona titular =this.personaRepository.findByNumeroAfiliadoAndTipoPersonaIn(numAfilTit[0],new TipoPersona[] {TipoPersona.AFILIADO, TipoPersona.AFILIADO_PASIVO}).get(0);
        	
            if (afiliado.getTipoPersona() == TipoPersona.AFILIADO && afiliado.getEstablecimiento() != null) {
            	establecimiento = afiliado.getEstablecimiento().getNombre();
            } else if (afiliado.getTipoPersona() == TipoPersona.FAMILIAR && titular.getEstablecimiento() != null) {
            	establecimiento = titular.getEstablecimiento().getNombre();
            }
            Date fechaIngreso = afiliado.getFechaAfiliacion();
            if (afiliado.getTipoPersona() == TipoPersona.FAMILIAR) {
            	fechaIngreso = titular.getFechaAfiliacion();
            }
            String numeroAfiliado=afiliado.getNumeroAfiliado();
            if (afiliado.getTipoPersona() == TipoPersona.FAMILIAR) {
            	numeroAfiliado=numAfilTit[0];
            }
            
            //Reemplaza los datos en la plantilla.
            svg = svg.replace("IMAGETAGQRATSA", img);
            svg = svg.replace("NUMEROATSA", numeroAfiliado);
            svg = svg.replace("NOMBREATSA", this.getTrimmedNombre(afiliado));
            svg = svg.replace("TIPO_PERSONA", tipoPersona);
            svg = svg.replace("ESTABLECIMIENTO", WordUtils.capitalizeFully(establecimiento.substring(0, Math.min(32, establecimiento.length()))) );
            svg = svg.replace("FECHA_INGRESO", DateFormat.getDateInstance().format(fechaIngreso));
            svg = svg.replace("VERSION", afiliado.getVersionCarnet().toString());
            svg = svg.replace("DOCUMENTONUMERO", numeroDocumento);
            svg = svg.replace("DOCUMENTOTIPO", tipoDocumento);

            InputStream is = new ByteArrayInputStream(svg.getBytes(Charset.forName("UTF-8")));
            TranscoderInput input_svg = new TranscoderInput(is);
            //Genera el JPEG
            OutputStream pdf_ostream = new FileOutputStream(outPath);
            TranscoderOutput output_pdf_image = new TranscoderOutput(pdf_ostream);
            JPEGTranscoder my_converter = new JPEGTranscoder();
            // set the transcoding hints
            my_converter.addTranscodingHint(JPEGTranscoder.KEY_QUALITY,
                    new Float(1));

            /*Dimensions CR-80
             3.375" x 2.125" (85.6 mm x 54 mm)
             3.375 x 300 dpi
             1012 pixels
             2.125 x 300 dpi
             638 pixels*/
            my_converter.addTranscodingHint(JPEGTranscoder.KEY_WIDTH,
                    new Float(1012));
            
            my_converter.transcode(input_svg, output_pdf_image);

            pdf_ostream.flush();
            pdf_ostream.close();
        
        } catch (Exception ex) {
        	ex.printStackTrace();
            logger.error(ex);
            throw new ServiceException(ex.getMessage(), ex);
        }
        return outPath;
	}
	
	private String getTrimmedNombre(Persona afiliado) {
		StringBuilder sb = new StringBuilder();
		
		StringTokenizer nombreTokenizer = new StringTokenizer(afiliado.getNombres());
		sb.append(nombreTokenizer.nextToken()).append(" ");
		if (nombreTokenizer.hasMoreTokens()) {
			sb.append(nombreTokenizer.nextToken().substring(0, 1)).append(". ");
		}
		
		StringTokenizer apellidoTokenizer = new StringTokenizer(afiliado.getApellidos());
		sb.append(apellidoTokenizer.nextToken()).append(" ");
		if (apellidoTokenizer.hasMoreTokens()) {
			sb.append(apellidoTokenizer.nextToken());//.substring(0, 1)).append(".");
		}
		
		return sb.toString();
	}
}
