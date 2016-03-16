package com.dg.app.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by lenovo on 2015/11/16.
 */
public class MD5 {
    public static String getMD5(String s) {
        char hexDigits[] = {

                '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd',

                'e', 'f'};

        try {

            byte[] strTemp = s.getBytes();

            MessageDigest mdTemp = MessageDigest.getInstance("MD5");

            mdTemp.update(strTemp);

            byte[] md = mdTemp.digest();

            int j = md.length;

            char str[] = new char[j * 2];

            int k = 0;

            for (int i = 0; i < j; i++) {

                byte byte0 = md[i];

                str[k++] = hexDigits[byte0 >>> 4 & 0xf];

                str[k++] = hexDigits[byte0 & 0xf];

            }

            return new String(str);

        }

        catch (Exception e){

            return null;

        }

    }
    public static String getString(byte[] b)
    {
        StringBuffer sb=new StringBuffer();
        for(int i=0;i<b.length;i++)
        {
            sb.append(b[i]);
        }
        return  sb.toString();
    }
}
