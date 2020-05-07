package common.encryptor;

import java.util.Arrays;

public class PKCS7Padding {
    private static final int BLOCK_SIZE = 32;

    public static byte[] getPaddingBytes(int count) {
        int amountToPad = BLOCK_SIZE - count % BLOCK_SIZE;
        if (amountToPad == 0) {
            amountToPad = BLOCK_SIZE;
        }
        char padChr = chr(amountToPad);
        String tmp = new String();
        for (int index = 0; index < amountToPad; index++) {
            tmp = tmp + padChr;
        }
        return tmp.getBytes(EncryptorTools.default_charset);
    }

    public static byte[] removePaddingBytes(byte[] decrypted) {
        int pad = decrypted[(decrypted.length - 1)];
        if ((pad < 1) || (pad > BLOCK_SIZE)) {
            pad = 0;
        }
        return Arrays.copyOfRange(decrypted, 0, decrypted.length - pad);
    }

    private static char chr(int a) {
        byte target = (byte)(a & 0xFF);
        return (char)target;
    }
}
