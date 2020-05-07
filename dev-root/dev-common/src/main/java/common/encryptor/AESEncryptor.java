package common.encryptor;

import org.apache.commons.lang3.StringUtils;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.ByteArrayOutputStream;
import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.util.Arrays;
import java.util.Base64;

public class AESEncryptor {
    private static final int KEY_LENGTH = 43;
    private static final String AES_ALGORITHM = "AES";
    private static final String AES_CBC_PCK_ALG = "AES/CBC/NoPadding";
    private static final String SHA1_ALGORITHM = "SHA-1";
    private byte[] aesKey;
    private String token;
    private Charset charset;

    public AESEncryptor(String aesEncodingKey, String token) {
        this(aesEncodingKey, token, EncryptorTools.default_charset);
    }

    public AESEncryptor(String aesEncodingKey, String token, Charset charset) {
        if(StringUtils.isBlank(aesEncodingKey) ||
                StringUtils.isBlank(token) ||
                aesEncodingKey.length() != KEY_LENGTH){

            throw new EncryptorRuntimeException(EncryptorRuntimeException.ex_01);
        }
        this.aesKey = Base64.getDecoder().decode(aesEncodingKey +"=");
        this.token = token;
        this.charset = charset;
    }

    public AESEncryptor(String aesEncodingKey){
        this(aesEncodingKey, EncryptorTools.default_charset);
    }

    public AESEncryptor(String aesEncodingKey, Charset charset) {
        if(StringUtils.isBlank(aesEncodingKey)){
            throw new EncryptorRuntimeException(EncryptorRuntimeException.ex_02);
        }

        this.aesKey = Base64.getDecoder().decode(aesEncodingKey);
        this.charset = charset;
    }

    public String encrypt(String plainText) {
        try {
            byte[] plainTextBytes = plainText.getBytes(this.charset);
            byte[] lengthByte = EncryptorTools.int2Bytes(plainTextBytes.length);
            ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
            byteStream.write(lengthByte);
            byteStream.write(plainTextBytes);
            byte[] padBytes = PKCS7Padding.getPaddingBytes(byteStream.size());
            byteStream.write(padBytes);
            byte[] unencrypted = byteStream.toByteArray();
            byteStream.close();

            /*int blockSize = cipher.getBlockSize();
            byte[] plainTextBytes = plainText.getBytes(EncryptTools.CHARSET);
            int plainTextLength = plainTextBytes.length;
            int remainder = plainTextLength % blockSize;
            if (remainder != 0) {
                plainTextLength = plainTextLength + (blockSize - remainder);
            }
            byte[] encryptedBytes = new byte[plainTextLength];
            System.arraycopy(plainTextBytes, 0, encryptedBytes, 0, plainTextBytes.length);*/

            Cipher cipher = Cipher.getInstance(AES_CBC_PCK_ALG);
            SecretKeySpec keyspec = new SecretKeySpec(this.aesKey, AES_ALGORITHM);
            IvParameterSpec iv = new IvParameterSpec(Arrays.copyOfRange(this.aesKey, 0, 16));
            cipher.init(Cipher.ENCRYPT_MODE, keyspec, iv);
            byte[] encrypted = cipher.doFinal(unencrypted);
            return Base64.getEncoder().encodeToString(encrypted);
        } catch (Exception e) {
            throw new EncryptorRuntimeException(EncryptorRuntimeException.ex_03, e);
        }
    }

    public String decrypt(String encryptMsg) {
        try {
            Cipher cipher = Cipher.getInstance(AES_CBC_PCK_ALG);
            SecretKeySpec keyspec = new SecretKeySpec(this.aesKey, AES_ALGORITHM);
            IvParameterSpec iv = new IvParameterSpec(Arrays.copyOfRange(this.aesKey, 0, 16));
            cipher.init(Cipher.DECRYPT_MODE, keyspec, iv);
            byte[] encrypted = Base64.getDecoder().decode(encryptMsg);
            byte[] original = cipher.doFinal(encrypted);

            byte[] bytes = PKCS7Padding.removePaddingBytes(original);
            byte[] networkOrder = Arrays.copyOfRange(bytes, 0, 4);
            int plainTextLength = EncryptorTools.bytes2int(networkOrder);
            String plainText = new String(Arrays.copyOfRange(bytes, 4, 4 + plainTextLength), this.charset);
            return plainText;
        } catch (Exception e) {
            throw new EncryptorRuntimeException(EncryptorRuntimeException.ex_04, e);
        }
    }

    public String signature(String token, String timestamp, String nonce, String encrypMsg) {
        try {
            String[] array = {token, timestamp, nonce, encrypMsg};
            Arrays.sort(array);
            StringBuffer sb = new StringBuffer();
            for(int i=0; i<array.length; i++){
                sb.append(array[i]);
            }
            String str = sb.toString();
            MessageDigest md = MessageDigest.getInstance(SHA1_ALGORITHM);
            md.update(str.getBytes(this.charset));
            byte[] digest = md.digest();
            return EncryptorTools.byte2hex(digest);
        } catch (Exception e) {

            throw new EncryptorRuntimeException(EncryptorRuntimeException.ex_05);
        }
    }

    public String decryptAndSign(String msgSignature, String timestamp, String nonce, String encryptMsg) {
        String signature = signature(this.token, timestamp, nonce, encryptMsg);
        if(StringUtils.isBlank(msgSignature)
                || !msgSignature.equals(signature)){

            throw new EncryptorRuntimeException(EncryptorRuntimeException.ex_06);
        }
        return decrypt(encryptMsg);
    }

    public static void main(String[] args) {
        AESEncryptor aesEncryptor = new AESEncryptor("aesdefadfeaesdefadfeaesdefadfeaesdefadfewer", "token");

        String encrypt = aesEncryptor.encrypt("123zhangjie张杰");
        System.out.println(encrypt);

        String decrypt = aesEncryptor.decrypt(encrypt);
        System.out.println(decrypt);
    }
}
