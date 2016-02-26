package io.kodokojo.commons.utils;

/*
 * #%L
 * kodokojo-commons
 * %%
 * Copyright (C) 2016 Kodo-kojo
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-3.0.html>.
 * #L%
 */

import org.bouncycastle.util.io.pem.PemObject;
import org.bouncycastle.util.io.pem.PemWriter;

import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.*;
import java.security.*;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

import static org.apache.commons.lang.StringUtils.isBlank;

public class RSAUtils {

    private static final String SSH_RSA = "ssh-rsa";

    private static final String RSA = "RSA";

    private static final String SHA_1_PRNG = "SHA1PRNG";

    private static final int KEY_SIZE = 2048;

    private static final String PUBLIC_KEY_OUTPUT = "%s %s %s";

    private static final String AES = "AES";

    private static final String AES_ECB_NO_PADDING = "AES";

    private RSAUtils() {
        // Utility Class
    }

    public static KeyPair generateRsaKeyPair() throws NoSuchAlgorithmException {
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance(RSA);
        keyPairGenerator.initialize(KEY_SIZE, SecureRandom.getInstance(SHA_1_PRNG));
        return keyPairGenerator.generateKeyPair();
    }

    public static byte[] wrap(Key aesKey, Key keyToEncrypt) {
        if (aesKey == null) {
            throw new IllegalArgumentException("aesKey must be defined.");
        }
        if (!AES.equals(aesKey.getAlgorithm())) {
            throw new IllegalArgumentException("aesKey must be an AES key instead of " + aesKey.getAlgorithm() + ".");
        }
        if (keyToEncrypt == null) {
            throw new IllegalArgumentException("keyToEncrypt must be defined.");
        }
        try {
            Cipher cipher = Cipher.getInstance(AES_ECB_NO_PADDING);
            cipher.init(Cipher.WRAP_MODE, aesKey);
            return cipher.wrap(keyToEncrypt);
        } catch (NoSuchAlgorithmException | IllegalBlockSizeException | InvalidKeyException | NoSuchPaddingException e) {
            throw new RuntimeException("Unable to wrap key", e);
        }
    }

    public static Key unwrap(Key aesKey, byte[] envelop) {
        if (aesKey == null) {
            throw new IllegalArgumentException("aesKey must be defined.");
        }
        if (!AES.equals(aesKey.getAlgorithm())) {
            throw new IllegalArgumentException("aesKey must be an AES key.");
        }

        try {
            Cipher cipher = Cipher.getInstance(AES_ECB_NO_PADDING);
            cipher.init(Cipher.UNWRAP_MODE, aesKey);
            return cipher.unwrap(envelop, AES, Cipher.SECRET_KEY);
        } catch (NoSuchAlgorithmException | InvalidKeyException | NoSuchPaddingException e) {
            throw new RuntimeException("Unable to unwrap private key.", e);
        }
    }

    public static RSAPrivateKey unwrapPrivateRsaKey(Key aesKey, byte[] envelop) {
        if (aesKey == null) {
            throw new IllegalArgumentException("aesKey must be defined.");
        }
        if (!AES.equals(aesKey.getAlgorithm())) {
            throw new IllegalArgumentException("aesKey must be an AES key.");
        }
        Key unwrap = unwrap(aesKey, envelop);
        try {
            KeyFactory keyFactory = KeyFactory.getInstance(RSA);
            return (RSAPrivateKey) keyFactory.generatePrivate(new PKCS8EncodedKeySpec(unwrap.getEncoded()));
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            throw new RuntimeException("Unable to unwrap private key.", e);
        }
    }

    public static RSAPublicKey unwrapPublicRsaKey(Key aesKey, byte[] envelop) {
        if (aesKey == null) {
            throw new IllegalArgumentException("aesKey must be defined.");
        }
        if (!AES.equals(aesKey.getAlgorithm())) {
            throw new IllegalArgumentException("aesKey must be an AES key.");
        }
        Key unwrap = unwrap(aesKey, envelop);
        try {
            KeyFactory keyFactory = KeyFactory.getInstance(RSA);
            return (RSAPublicKey) keyFactory.generatePublic(new X509EncodedKeySpec(unwrap.getEncoded()));
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            throw new RuntimeException("Unable to unwrap public key.", e);
        }
    }

    public static void writeRsaPrivateKey(RSAPrivateKey privateKey, StringWriter sw) {
        PKCS8EncodedKeySpec pkcs8EncodedKeySpec = new PKCS8EncodedKeySpec(
                privateKey.getEncoded());

        try {
            PemWriter writer = new PemWriter(sw);
            writer.writeObject(new PemObject("RSA PRIVATE KEY", privateKey.getEncoded()));
            writer.flush();
            //outputStream.write(pkcs8EncodedKeySpec.getEncoded());

        } catch (IOException e) {
            throw new RuntimeException("Unable to write a private rsa key in output stream", e);
        }
    }

    public static String encodePublicKey(RSAPublicKey rsaPublicKey, String userEmail)  {
        if (rsaPublicKey == null) {
            throw new IllegalArgumentException("rsaPublicKey must be defined.");
        }
        if (isBlank(userEmail)) {
            throw new IllegalArgumentException("userEmail must be defined.");
        }
        ByteArrayOutputStream byteOs = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(byteOs);
        try {
            dos.writeInt(SSH_RSA.getBytes().length);
            dos.write(SSH_RSA.getBytes());
            dos.writeInt(rsaPublicKey.getPublicExponent().toByteArray().length);
            dos.write(rsaPublicKey.getPublicExponent().toByteArray());
            dos.writeInt(rsaPublicKey.getModulus().toByteArray().length);
            dos.write(rsaPublicKey.getModulus().toByteArray());
            String publicKeyEncoded = new String(Base64.getEncoder().encode(byteOs.toByteArray()));
            return String.format(PUBLIC_KEY_OUTPUT, SSH_RSA, publicKeyEncoded, userEmail);
        } catch (IOException e) {
            throw new RuntimeException("Unable to write un a memory DataOutputStream.", e);
        }
    }

}
