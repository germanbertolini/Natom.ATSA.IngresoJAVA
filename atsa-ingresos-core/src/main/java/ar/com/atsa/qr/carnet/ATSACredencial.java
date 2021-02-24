/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ar.com.atsa.qr.carnet;

/**
 *
 * @author admin
 */
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import net.glxn.qrgen.QRCode;
import net.glxn.qrgen.image.ImageType;

import org.apache.batik.transcoder.TranscoderInput;
import org.apache.batik.transcoder.TranscoderOutput;
import org.apache.batik.transcoder.image.JPEGTranscoder;
import org.apache.commons.codec.binary.Base64;

import ar.com.atsa.persistence.entities.Persona;

import com.google.zxing.EncodeHintType;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

public class ATSACredencial {

    public static void createCredencialFor(Persona afiliado) {

        String svg = "";

        try {
            // Lee la plantilla de diseño
            List<String> lines = Files.readAllLines(Paths.get("D:\\Dropbox\\Clientes\\ATSA\\ATSA- AFILIADOS\\credencial.svg"), Charset.forName("UTF-8"));

            //Genera el QR con la URL (la misma deberia ser un parametro de configuración)
            ByteArrayOutputStream out = QRCode.from("http://tomcatapps.scotsteam.com.ar/atsa-ingresos-ui/#!/ver-afiliado/2")
                    .withSize(250, 250)
                    .withCharset("UTF-8")
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
            //Reemplaza los datos en la plantilla.
            svg = svg.replace("IMAGETAGQRATSA", img);
            svg = svg.replace("NOMBREATSA", "DAMIAN SANCHEZ");
            svg = svg.replace("NUMEROATSA", "00000000002");

            InputStream is = new ByteArrayInputStream(svg.getBytes());
            TranscoderInput input_svg = new TranscoderInput(is);
            //Genera el JPEG
            OutputStream pdf_ostream = new FileOutputStream("c:\\credencial.jpg");
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
            Logger.getLogger(ATSACredencial.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
}
