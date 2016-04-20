package Encripcion;

import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import javax.crypto.BadPaddingException;

import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import javax.swing.JOptionPane;
import org.apache.commons.codec.binary.Base64;

public class Encriptar {

    private static final byte[] sharedvector = {0x01, 0x02, 0x03, 0x05, 0x07, 0x0B, 0x0D, 0x11};

    public static String encriptaEnMD5(String RawText) {
        String EncText = "";
        byte[] keyArray = new byte[24];
        byte[] temporaryKey;
        String key = "developersnotedotcom";
        byte[] toEncryptArray = null;

        try {

            toEncryptArray = RawText.getBytes("UTF-8");
            MessageDigest m = MessageDigest.getInstance("MD5");
            temporaryKey = m.digest(key.getBytes("UTF-8"));

            if (temporaryKey.length < 24) // DESede require 24 byte length key
            {
                int index = 0;
                for (int i = temporaryKey.length; i < 24; i++) {
                    keyArray[i] = temporaryKey[index];
                }
            }

            Cipher c = Cipher.getInstance("DESede/CBC/PKCS5Padding");
            c.init(Cipher.ENCRYPT_MODE, new SecretKeySpec(keyArray, "DESede"), new IvParameterSpec(sharedvector));
            byte[] encrypted = c.doFinal(toEncryptArray);
            EncText = Base64.encodeBase64String(encrypted);

        } catch (NoSuchAlgorithmException | UnsupportedEncodingException | NoSuchPaddingException | InvalidKeyException | InvalidAlgorithmParameterException | IllegalBlockSizeException | BadPaddingException NoEx) {
            JOptionPane.showMessageDialog(null, NoEx);
        }

        return EncText;
    }

    public static String DesencriptaEnMD5(String EncText) throws InvalidAlgorithmParameterException {

        String RawText = "";
        byte[] keyArray = new byte[24];
        byte[] temporaryKey;
        String key = "developersnotedotcom";
        byte[] toEncryptArray = null;

        try {
            MessageDigest m = MessageDigest.getInstance("MD5");
            temporaryKey = m.digest(key.getBytes("UTF-8"));

            if (temporaryKey.length < 24) // DESede require 24 byte length key
            {
                int index = 0;
                for (int i = temporaryKey.length; i < 24; i++) {
                    keyArray[i] = temporaryKey[index];
                }
            }

            Cipher c = Cipher.getInstance("DESede/CBC/PKCS5Padding");
            c.init(Cipher.DECRYPT_MODE, new SecretKeySpec(keyArray, "DESede"), new IvParameterSpec(sharedvector));
            byte[] decrypted = c.doFinal(Base64.decodeBase64(EncText));

            RawText = new String(decrypted, "UTF-8");
        } catch (NoSuchAlgorithmException | UnsupportedEncodingException | NoSuchPaddingException | InvalidKeyException | InvalidAlgorithmParameterException | IllegalBlockSizeException | BadPaddingException NoEx) {
            JOptionPane.showMessageDialog(null, NoEx);
        }

        return RawText;

    }

    public static void main(String[] args) throws Exception {
        System.out.println("\n\nEncriptacion en MD5 de 123: '" + encriptaEnMD5("123") + "'");
        System.out.println("Encriptacion en MD5 de hola: '" + encriptaEnMD5("Hola") + "'");
        String encriptado = encriptaEnMD5("ca008c38-a27c-4e86-ae76-c378cc8fd9fe");
        System.out.println(encriptado.length());
        System.out.println("Encriptacion en MD5 de c6d1fa87-f6fc-40b8-803d-f9be02016c76: '" + encriptaEnMD5("cristian") + "'");
        System.out.println(DesencriptaEnMD5("6BOAobT1mkeMZYni44T0BA=="));

    }
}
