package common.encryptor;

import org.apache.commons.lang3.StringUtils;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.nio.charset.Charset;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.*;

public class EncryptorTools {
    public static String default_encryptText_key = "encryptText";
    public static String default_decryptKey_key = "decryptKey";
    public static final Charset default_charset = Charset.forName("utf-8");


    /**
     * 获得签名字符串
     * @param sortedParams
     * @return
     */
    public static String getSignContent(Map<String, String> sortedParams){
        StringBuffer content = new StringBuffer();
        List<String> keys = new ArrayList(sortedParams.keySet());
        Collections.sort(keys);
        int index = 0;
        for(int i=0; i<keys.size(); i++){
            String key = keys.get(i);
            String value = sortedParams.get(key);
            if(StringUtils.isNotBlank(key) && StringUtils.isNotBlank(value)){
                content.append((index == 0 ? "" : "&")+ key +"="+ value);
                index++;
            }
        }
        return content.toString();
    }

    /**
     * 获得验签字符串
     * @param params
     * @return
     */
    public static String getSignCheckContent(Map<String, String> params){
        params.remove("sign");
        StringBuffer content = new StringBuffer();
        List<String> keys = new ArrayList(params.keySet());
        Collections.sort(keys);
        for(int i = 0; i < keys.size(); i++){
            String key = keys.get(i);
            String value = params.get(key);
            content.append((i == 0 ? "" : "&") + key + "=" + value);
        }
        return content.toString();
    }

    /**
     * 获得PKCS8格式私钥
     * @param algorithm
     * @param ins
     * @return
     * @throws Exception
     */
    public static PrivateKey getPrivateKeyFromPKCS8(String algorithm, InputStream ins) throws Exception{
        KeyFactory keyFactory = KeyFactory.getInstance(algorithm);
        byte[] encodedKey = StreamTools.readText(ins).getBytes();
        encodedKey = Base64.getDecoder().decode(encodedKey);
        return keyFactory.generatePrivate(new PKCS8EncodedKeySpec(encodedKey));
    }

    /**
     * 获得X509格式公钥
     * @param algorithm
     * @param ins
     * @return
     * @throws Exception
     */
    public static PublicKey getPublicKeyFromX509(String algorithm, InputStream ins) throws Exception{
        KeyFactory keyFactory = KeyFactory.getInstance(algorithm);
        StringWriter writer = new StringWriter();
        StreamTools.io(new InputStreamReader(ins), writer);
        byte[] encodedKey = writer.toString().getBytes();
        encodedKey = Base64.getDecoder().decode(encodedKey);
        return keyFactory.generatePublic(new X509EncodedKeySpec(encodedKey));
    }

    public static byte[] int2Bytes(int arg) {
        byte[] result = new byte[4];
        result[3] = ((byte)(arg & 0xFF));
        result[2] = ((byte)(arg >> 8 & 0xFF));
        result[1] = ((byte)(arg >> 16 & 0xFF));
        result[0] = ((byte)(arg >> 24 & 0xFF));
        return result;
    }

    public static int bytes2int(byte[] bytes){
        int result = 0;
        for (int i = 0; i < 4; i++){
            result <<= 8;
            result |= bytes[i] & 0xFF;
        }
        return result;
    }

    public static String byte2hex(byte[] bytes){
        StringBuffer hexstr = new StringBuffer();
        String shaHex = "";
        for(int i=0; i<bytes.length; i++){
            shaHex = Integer.toHexString(bytes[i] & 0xFF);
            if(shaHex.length() < 2){
                hexstr.append(0);
            }
            hexstr.append(shaHex);
        }
        return hexstr.toString();
    }

    /**
     * 生成指定长度的AES密钥
     * @param length
     * @return
     * @throws Exception
     */
    public static SecretKey generateAesKey(int length) throws Exception {
        KeyGenerator kgen = KeyGenerator.getInstance("AES");
        kgen.init(length);
        SecretKey skey = kgen.generateKey();
        return skey;
    }

    /**
     * 初始化IV向量
     * @param fullAlg
     * @return
     */
    public static byte[] initIv(String fullAlg){
        try{
            Cipher cipher = Cipher.getInstance(fullAlg);
            int blockSize = cipher.getBlockSize();
            byte[] iv = new byte[blockSize];
            for(int i = 0; i < blockSize; i++){
                iv[i] = 0;
            }
            return iv;
        }catch(Exception e){
            int blockSize = 16;
            byte[] iv = new byte[blockSize];
            for(int i = 0; i < blockSize; i++){
                iv[i] = 0;
            }
            return iv;
        }
    }
}
