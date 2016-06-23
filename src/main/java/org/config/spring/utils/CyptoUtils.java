package org.config.spring.utils;




import com.google.common.base.Strings;

import javax.crypto.Cipher;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.IvParameterSpec;
import java.security.Key;
import java.security.spec.AlgorithmParameterSpec;

/**
 * Created by yangyu on 2016/6/23.
 */
public class CyptoUtils {

    public static final String ALGORITHM_DES = "DES/CBC/PKCS5Padding";
    public static final String DEFAULT_KEY = "aRQKMLhUW6YVtJPV3yqdGfPV";

    /**
     * DES�㷨������
     *
     * @param data �������ַ���
     * @param key  ����˽Կ�����Ȳ��ܹ�С��8λ
     * @return ���ܺ���ֽ����飬һ����Base64����ʹ��
     * @throws Exception
     */
    public static String encode(String key, String data) {
        if (data == null) {
            return null;
        }
        if (Strings.isNullOrEmpty(key)) {
            key = DEFAULT_KEY;
        }
        try {
            DESKeySpec dks = new DESKeySpec(key.getBytes());
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
            //key�ĳ��Ȳ��ܹ�С��8λ�ֽ�
            Key secretKey = keyFactory.generateSecret(dks);
            Cipher cipher = Cipher.getInstance(ALGORITHM_DES);
            IvParameterSpec iv = new IvParameterSpec("12345678".getBytes());
            AlgorithmParameterSpec paramSpec = iv;
            cipher.init(Cipher.ENCRYPT_MODE, secretKey, paramSpec);
            byte[] bytes = cipher.doFinal(data.getBytes());
            return byte2hex(bytes);
        } catch (Exception e) {
            e.printStackTrace();
            return data;
        }
    }

    /**
     * DES�㷨������
     *
     * @param data �������ַ���
     * @param key  ����˽Կ�����Ȳ��ܹ�С��8λ
     * @return ���ܺ���ֽ�����
     * @throws Exception �쳣
     */
    public static String decode(String key, String data) {
        if (data == null) {
            return null;
        }
        if (Strings.isNullOrEmpty(key)) {
            key = DEFAULT_KEY;
        }
        try {
            DESKeySpec dks = new DESKeySpec(key.getBytes());
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
            //key�ĳ��Ȳ��ܹ�С��8λ�ֽ�
            Key secretKey = keyFactory.generateSecret(dks);
            Cipher cipher = Cipher.getInstance(ALGORITHM_DES);
            IvParameterSpec iv = new IvParameterSpec("12345678".getBytes());
            AlgorithmParameterSpec paramSpec = iv;
            cipher.init(Cipher.DECRYPT_MODE, secretKey, paramSpec);
            return new String(cipher.doFinal(hex2byte(data.getBytes())));
        } catch (Exception e) {
            e.printStackTrace();
            return data;
        }
    }

    /**
     * ������ת�ַ���
     *
     * @param b
     * @return
     */
    private static String byte2hex(byte[] b) {
        StringBuilder hs = new StringBuilder();
        String stmp;
        for (int n = 0; b != null && n < b.length; n++) {
            stmp = Integer.toHexString(b[n] & 0XFF);
            if (stmp.length() == 1)
                hs.append('0');
            hs.append(stmp);
        }
        return hs.toString().toUpperCase();
    }

    private static byte[] hex2byte(byte[] b) {
        if ((b.length % 2) != 0)
            throw new IllegalArgumentException();
        byte[] b2 = new byte[b.length / 2];
        for (int n = 0; n < b.length; n += 2) {
            String item = new String(b, n, 2);
            b2[n / 2] = (byte) Integer.parseInt(item, 16);
        }
        return b2;
    }


    public static void main(String[] args) {
//        System.out.println(encode("aRQKMLhUW6YVtJPV3yqdGfPV", "aaa:1,bbb:2"));
//        System.out.println(decode("aRQKMLhUW6YVtJPV3yqdGfPV", "5B658A2BCAF410D6E1508EB85F956CA9"));
          String s =
                  "jdbc.driverClassName$com.mysql.jdbc.Driver$"+
       "jdbc.url$xxx$"+
        "jdbc.username$xxx$"+
        "jdbc.password$xxx$"+
        "jdbc.initialPoolSize$10$"+
        "jdbc.minPoolSize$2$"+
        "jdbc.maxPoolSize$30$";
        String encodeS = CyptoUtils.encode(null, s);
        System.out.println(encodeS);
        String decodeS = CyptoUtils.decode(null, encodeS);
        if(s.equals(decodeS))
            System.out.println(true);

    }
}

