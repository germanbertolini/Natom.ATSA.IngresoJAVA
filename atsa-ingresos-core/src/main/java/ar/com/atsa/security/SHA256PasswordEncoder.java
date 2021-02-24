package ar.com.atsa.security;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.springframework.security.crypto.codec.Hex;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

/**
 * Created by tomas.colombo on 24/02/14.
 */
@Component
public class SHA256PasswordEncoder implements PasswordEncoder {
    private static final String DEFAULT_SALT = "conedu";

    private final String salt;

    private final MessageDigest digest;

    public SHA256PasswordEncoder() throws NoSuchAlgorithmException{
        this.salt = SHA256PasswordEncoder.DEFAULT_SALT;
        this.digest = MessageDigest.getInstance("SHA-256");
    }

    public SHA256PasswordEncoder(String salt) throws NoSuchAlgorithmException {

        this.salt = salt;
        this.digest = MessageDigest.getInstance("SHA-256");
    }


    @Override
    public String encode(CharSequence rawPassword) {

        String saltedPassword = rawPassword + this.salt;
        try {
            return new String(Hex.encode(this.digest.digest(saltedPassword.getBytes("UTF-8"))));
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("UTF-8 not supported");
        }
    }


    @Override
    public boolean matches(CharSequence rawPassword, String encodedPassword) {

        return this.encode(rawPassword).equals(encodedPassword);
    }
}
