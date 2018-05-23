package com.beanu.l3_common.util;

import android.text.TextUtils;
import android.util.Base64;

/**
 * base64
 * Created by Beanu on 2017/12/8.
 */

public class Base64Util {

    public static String makeStringToBase64(String str) {
        String enUid = new String(Base64.encode(str.getBytes(), Base64.DEFAULT));
        return enUid;
    }

    public static String getStringFromBase64(String base64Id) {
        String result = "";
        if (!TextUtils.isEmpty(base64Id)) {
            if (!TextUtils.isEmpty(base64Id)) {
                result = new String(Base64.decode(base64Id.getBytes(), Base64.DEFAULT));
            }
        }
        return result;
    }
}
