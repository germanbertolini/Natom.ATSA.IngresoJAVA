package ar.com.atsa.security;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.codec.Hex;

import ar.com.atsa.persistence.entities.Usuario;
import ar.com.atsa.security.AtsaUserDetailsService.AtsaUserDetails;


public class TokenUtils {

	// TODO: Validar la clave de otro lado para evitar desencriptación
	public static final String MAGIC_KEY = "obfuscate";

	public static String createToken(AtsaUserDetails userDetails) {
		return createToken(userDetails.getUsername(), userDetails.getPassword());
	}
	
	public static String createToken(String userName, String password) {

		// TODO: Configurar la expiración de la sesion, pensar para celulares con RememberMe
		long expires = System.currentTimeMillis() + 1000L * 60 * 60;

		StringBuilder tokenBuilder = new StringBuilder();
		tokenBuilder.append(userName);
		tokenBuilder.append(":");
		tokenBuilder.append(expires);
		tokenBuilder.append(":");
		tokenBuilder.append(TokenUtils.computeSignature(userName, password, expires));
		tokenBuilder.append(":");
		//tokenBuilder.append(perfilId.toString());

		return tokenBuilder.toString();
	}


	public static String computeSignature(String userName, String password, long expires) {

		StringBuilder signatureBuilder = new StringBuilder();
		signatureBuilder.append(userName);
		signatureBuilder.append(":");
		signatureBuilder.append(expires);
		signatureBuilder.append(":");
		signatureBuilder.append(password);
		signatureBuilder.append(":");
		signatureBuilder.append(TokenUtils.MAGIC_KEY);

		MessageDigest digest;
		try {
			digest = MessageDigest.getInstance("MD5");
		} catch (NoSuchAlgorithmException e) {
			throw new IllegalStateException("No MD5 algorithm available!");
		}

		return new String(Hex.encode(digest.digest(signatureBuilder.toString().getBytes())));
	}


	public static String getUserNameFromToken(String authToken) {

		if (null == authToken) {
			return null;
		}

		String[] parts = authToken.split(":");
		return parts[0];
	}

	public static boolean validateToken(String authToken, Usuario usuario) {
		return validateToken(authToken, usuario.getEmail(), usuario.getClave());
	}

    public static boolean validateToken(String authToken, UserDetails userDetails){
        return validateToken(authToken, userDetails.getUsername(), userDetails.getPassword());
    }

    public static boolean validateToken(String authToken, String userName, String password) {

		String[] parts = authToken.split(":");
		long expires = Long.parseLong(parts[1]);
		String signature = parts[2];

		if (expires < System.currentTimeMillis()) {
			return false;
		}

		return signature.equals(TokenUtils.computeSignature(userName, password, expires));
	}
	
}
