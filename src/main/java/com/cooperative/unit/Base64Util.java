package com.cooperative.unit;

import java.util.Base64;

/**
 * @ClassName Base64Util
 * @Description TODO
 * @Author zhouliansheng
 * @Date 2020/6/23 16:21
 * @Version 1.0
 **/
public class Base64Util {
    public static byte[] decode(String encodedText){
        final Base64.Decoder decoder = Base64.getDecoder();
        return decoder.decode(encodedText);
    }

    public static String encode(byte[] data){
        final Base64.Encoder encoder = Base64.getEncoder();
        return encoder.encodeToString(data);
    }

}
