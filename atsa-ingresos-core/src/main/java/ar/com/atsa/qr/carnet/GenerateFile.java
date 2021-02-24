package ar.com.atsa.qr.carnet;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import com.google.zxing.EncodeHintType;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

import net.glxn.qrgen.QRCode;
import net.glxn.qrgen.image.ImageType;

public class GenerateFile {

	public static void main(String[] args) {
		//Genera el QR con la URL (la misma deberia ser un parametro de configuración)
		ByteArrayOutputStream file = QRCode.from("http://tomcatapps.scotsteam.com.ar/atsa-ingresos-ui/#!/ver-afiliado/2")
                .withSize(250, 250)
                .withCharset("UTF-8")
                .to(ImageType.PNG)
                .withErrorCorrection(ErrorCorrectionLevel.M)
                .withHint(EncodeHintType.MARGIN, 1)
                .stream();
		
		try {
			FileOutputStream fop = new FileOutputStream("C:/Users/Marcos/devel/qrcode.png");
			file.writeTo(fop);
			fop.flush();
			fop.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
