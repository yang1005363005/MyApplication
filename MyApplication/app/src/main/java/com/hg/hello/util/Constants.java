package com.hg.hello.util;

import android.util.Log;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2019/4/17 0017.
 */

public class Constants {
    public static final String  ip = "http://192.168.1.106";
    public static String mallJSON=ip+":8080/suzhouSys/";
    public static String httpaddr=mallJSON+"appController/test.do";


    public static  String httpsString(String serviceCode, List<Map<String, String>> parammap) {
        String Str = "data="+parammap+"&orgNo=&imei=1232143214&serviceCode="+serviceCode+"&source=sz&target=&userNo=";
        return Str;
    }
}
