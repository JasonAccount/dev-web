package common.encryptor;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.nio.charset.Charset;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.spec.InvalidKeySpecException;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

public class RSAEncryptor {
    private static final String RSA_ALGORITHM = "RSA";
    private static final String SHA1WITHRSA_SIGN_ALGORITHM = "SHA1WithRSA";
    private static final int MAX_ENCRYPT_BLOCK = 117;
    private static final int MAX_DECRYPT_BLOCK = 128;
    private static final int AES_KEY_LENGTH = 256;
    private Charset charset;
    private String encryptText_Key;
    private String decryptKey_key;


    public RSAEncryptor(){
        this(EncryptorTools.default_encryptText_key, EncryptorTools.default_decryptKey_key, EncryptorTools.default_charset);
    }

    public RSAEncryptor(String encryptText_Key, String decryptKey_key, Charset charset){
        this.charset = charset;
        this.encryptText_Key = encryptText_Key;
        this.decryptKey_key = decryptKey_key;
    }
    /**
     * RSA加密
     * @param plainText 待加密明文
     * @param cusPublicKey 客户公钥
     * @return
     */
    public Map<String, String> encrypt(String plainText, String cusPublicKey) {
        try{
            // AES加密明文，获得密文
            SecretKey secretKey = EncryptorTools.generateAesKey(AES_KEY_LENGTH);
            String aesEncodingKey = Base64.getEncoder().encodeToString(secretKey.getEncoded());
            AESEncryptor aesEncryptor = new AESEncryptor(aesEncodingKey);
            String encryptText = aesEncryptor.encrypt(plainText);

            // 使用客户公钥加密AES密钥
            PublicKey pubKey = EncryptorTools.getPublicKeyFromX509(RSA_ALGORITHM, new ByteArrayInputStream(cusPublicKey.getBytes(this.charset)));
            Cipher cipher = Cipher.getInstance(RSA_ALGORITHM);
            cipher.init(Cipher.ENCRYPT_MODE, pubKey);
            byte[] data = aesEncodingKey.getBytes(this.charset);
            int inputLen = data.length;
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            int offSet = 0;
            int i = 0;
            while((inputLen - offSet) > 0){
                byte[] cache;
                if((inputLen - offSet) > MAX_ENCRYPT_BLOCK){
                    cache = cipher.doFinal(data, offSet, MAX_ENCRYPT_BLOCK);
                }else{
                    cache = cipher.doFinal(data, offSet, (inputLen - offSet));
                }
                out.write(cache, 0, cache.length);
                i++;
                offSet = (i * MAX_ENCRYPT_BLOCK);
            }
            byte[] encryptedData = Base64.getEncoder().encode(out.toByteArray());
            out.close();

            Map<String, String> resultMap = new HashMap<>();
            resultMap.put(this.encryptText_Key, encryptText);
            resultMap.put(this.decryptKey_key, new String(encryptedData, this.charset));
            return resultMap;
        }catch (Exception e){
            throw new EncryptorRuntimeException(EncryptorRuntimeException.ex_07);
        }
    }
    /**
     * RSA解密
     * @param encryptContent 加密的数据
     * @param privateKey 私钥
     * @return
     */
    public String decrypt(Map<String, String> encryptContent, String privateKey) {
        try{
            String encryptText = encryptContent.get(this.encryptText_Key);
            String decryptKey = encryptContent.get(this.decryptKey_key);

            // 私钥解密decryptKey，获得AES密钥
            PrivateKey priKey = EncryptorTools.getPrivateKeyFromPKCS8(RSA_ALGORITHM, new ByteArrayInputStream(privateKey.getBytes(this.charset)));
            Cipher cipher = Cipher.getInstance(RSA_ALGORITHM);
            cipher.init(Cipher.DECRYPT_MODE, priKey);
            byte[] encryptedKey = Base64.getDecoder().decode(decryptKey.getBytes(this.charset));
            int inputLen = encryptedKey.length;
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            int offSet = 0;
            int i = 0;
            while(inputLen - offSet > 0){
                byte[] cache;
                if((inputLen - offSet) > MAX_DECRYPT_BLOCK){
                    cache = cipher.doFinal(encryptedKey, offSet, MAX_DECRYPT_BLOCK);
                }else{
                    cache = cipher.doFinal(encryptedKey, offSet, inputLen - offSet);
                }
                out.write(cache, 0, cache.length);
                i++;
                offSet = (i * MAX_DECRYPT_BLOCK);
            }
            byte[] decryptedData = out.toByteArray();
            out.close();
            String aesEncodingKey = new String(decryptedData, this.charset);

            //AES解密密文，获得明文
            AESEncryptor aesEncryptor = new AESEncryptor(aesEncodingKey);
            return aesEncryptor.decrypt(encryptText);
        }catch (Exception e){
            throw new EncryptorRuntimeException(EncryptorRuntimeException.ex_08);
        }
    }

    /**
     * 数字签名(私钥加密摘要，对方用公钥解密（验证数据来源）后比对摘要信息是否一致，从而验证数据的来源以及数据的完整性和一致性)
     * @param params 签名参数
     * @param privateKey 私钥
     * @return
     */
    public String sign(Map<String, String> params, String privateKey) {
        try{
            String signContent = EncryptorTools.getSignContent(params);
            PrivateKey priKey = EncryptorTools.getPrivateKeyFromPKCS8(RSA_ALGORITHM, new ByteArrayInputStream(privateKey.getBytes(this.charset)));
            Signature signature = Signature.getInstance(SHA1WITHRSA_SIGN_ALGORITHM);
            signature.initSign(priKey);
            signature.update(signContent.getBytes(this.charset));
            byte[] signed = signature.sign();
            return new String(Base64.getEncoder().encode(signed));
        }catch (InvalidKeySpecException ie){
            throw new EncryptorRuntimeException(EncryptorRuntimeException.ex_09);
        }catch (Exception e){
            throw new EncryptorRuntimeException(EncryptorRuntimeException.ex_10);
        }
    }

    /**
     * 验证数字签名
     * @param params
     * @param cusPublicKey
     * @return
     */
    public boolean checkSign(Map<String, String> params, String cusPublicKey) {
        try{
            String sign = params.get("sign");
            String content = EncryptorTools.getSignCheckContent(params);
            PublicKey pubKey = EncryptorTools.getPublicKeyFromX509(RSA_ALGORITHM, new ByteArrayInputStream(cusPublicKey.getBytes(this.charset)));
            Signature signature = Signature.getInstance(SHA1WITHRSA_SIGN_ALGORITHM);
            signature.initVerify(pubKey);
            signature.update(content.getBytes(this.charset));
            return signature.verify(Base64.getDecoder().decode(sign.getBytes(this.charset)));
        }catch (Exception e){

            throw new EncryptorRuntimeException(EncryptorRuntimeException.ex_11);
        }
    }


    public static void main(String[] args) {
        String private_key = "MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBAK3TZZT" +
                "AgFz5xfzbpnGCtINPByprcvne+sSr5JhCHxMhhB8KDfpiH8LIz1rqaIhp/Y11mvNkwTmEp0Bfue54aFoWiS" +
                "86YDpV1NamYjTw/OOVlzz4bH+Js9Q3B4mR7zB4InwPVW1HymmiaRlq8UqfeUW7qmuJQVt1Ld0Basj11LC7AgM" +
                "BAAECgYALqDCYwCwbkBiJ84T++b/3SlZ1rs0AGscAZ3KmD1EcLFQ67KaxUuFEY8kcFLo747fAQ9HHXstiexXoSEMkzehZfajR" +
                "Tve3pfZD7C6JZWPpDmgUAr4cmRiW5kp1NmCr+Qu43Gpl9UIUYfXdHXQwC9Gndm384g0d3TXpFfLVx0sXeQJBAPV+2IUeumrqRWYEUYD" +
                "CS7WKIdEqBUAGN0FcvP1JxBi2JCZcysvzmoKJU+MrdLPWYL/3P5oN8aRUgkFuEOzqJ10CQQC1Q3tbyRCuwEk7GQFwvDoxp" +
                "hlqllXBE19mAbvuacHH4TG/5Cr6laaF9/Csc49wokuraGvUhfSo9lhA5Qcgzi73AkEAiPYqsCITmOB4tVDFc9NyT6bv9PS/lxegr" +
                "iuiGKGV8L91bZ2pWp3e3uLk1UGCS27X7WFwr28GGEs5nG1erf541QJAbn0v2Ib7EEpNWSSo6BsULeddXsc8hnBUmk08TNtSL5BpTtq" +
                "6B/zogIjdv4DBUprbHSSNdgIhXEytBwGUEr8bi" +
                "QJBAOR4dnMiYGcTEEluZLNSBAcHubBqrf8GBe7Ya6JsF3kr0DtY8cwJIj45mhnbgn4yLzINjKAijR3bak1aCGiKxtQ=";

        String public_key = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCt02WUwIBc+cX826ZxgrS" +
                "DTwcqa3L53vrEq+SYQh8TIYQfCg36Yh/CyM9a6miIaf2NdZrzZME5hKdAX7nueGhaFokvOmA6" +
                "VdTWpmI08PzjlZc8+Gx/ibPUNweJke8weCJ8D1VtR8ppomkZavFKn3lFu6priUFbdS3dAWrI9dSwuwIDAQAB";

        /*Map<String, String> params = new HashMap<>();
        params.put("param1", "param2");
        System.out.println(rsaSign(params, private_key));*/

        /*String sign = "dzJ6PZoOEske7/nb6sCSIVA/WVpQ38w+xTpnFLuEuCe6Y1Yggau3JfNrePxv" +
                "Ygo78hSuDQshPeXrCTOcwZglXoXh4NKTdf1qLBVYYhNd+jwLog9i" +
                "/C3dfjSq/DnSupyf5PHLmKQ9QKFVrWixTUegjXwnyEfQ0SbRUj/6yANtUjk=";
        Map<String, String> params = new HashMap<>();
        params.put("param1", "param2");
        params.put("sign", sign);
        System.out.println(rsaCheck(params, public_key));*/

        String plainText = "jason=zhangjie;andy=liudehua";


        System.out.println("starttime: "+ System.currentTimeMillis());
        RSAEncryptor rsaEncryptor = new RSAEncryptor();
        Map<String, String> encrypt = rsaEncryptor.encrypt(plainText, public_key);
        System.out.println("encryptText="+ encrypt.get(rsaEncryptor.encryptText_Key) +"; decryptKey="+ encrypt.get(rsaEncryptor.decryptKey_key));
        System.out.println("endtime: "+ System.currentTimeMillis());

        System.out.println("######################################################");
        System.out.println("starttime: "+ System.currentTimeMillis());
        System.out.println(rsaEncryptor.decrypt(encrypt, private_key));
        System.out.println("endtime: "+ System.currentTimeMillis());
    }

}
