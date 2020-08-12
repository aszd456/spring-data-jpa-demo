package com.cooperative.unit;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

/**
 * @ClassName AESUtil
 * @Description TODO
 * @Author zhouliansheng
 * @Date 2020/6/23 16:19
 * @Version 1.0
 **/
public class AESUtil {
    /**
     * 密钥算法
     */
    private static final String ALGORITHM = "AES";
    /**
     * 加解密算法/工作模式/填充方式
     */
    private static final String ALGORITHM_MODE_PADDING = "AES/ECB/PKCS5Padding";


    /**
     * AES加密
     *
     * @param data
     * @return
     * @throws Exception
     */
    public static String encryptData(String data, String key) throws Exception {
        // 创建密码器
        Cipher cipher = Cipher.getInstance(ALGORITHM_MODE_PADDING);
        // 初始化
        SecretKeySpec secretKeySpec =
                new SecretKeySpec(MD5.MD5Encode(key).toLowerCase().getBytes(), ALGORITHM);
        cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec);
        return Base64Util.encode(cipher.doFinal(data.getBytes()));
    }

    /**
     * AES解密
     *
     * @param base64Data
     * @return
     * @throws Exception
     */
    public static String decryptData(String base64Data, String key) throws Exception {
        Cipher cipher = Cipher.getInstance(ALGORITHM_MODE_PADDING);
        SecretKeySpec secretKeySpec =
                new SecretKeySpec(MD5.MD5Encode(key).toLowerCase().getBytes(), ALGORITHM);
        cipher.init(Cipher.DECRYPT_MODE, secretKeySpec);
        return new String(cipher.doFinal(Base64Util.decode(base64Data)));
    }

}
