package com.hg.hello.util;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.support.v7.app.AppCompatActivity;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by Administrator on 2019/4/24 0024.
 */

public class StringUtil {

    /**
     * 将输入流转成String类型返回
     * @param in
     * @return
     * @throws IOException
     */
    public static String retFromString(InputStream in) throws IOException {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        int len = 0;
        byte[] bytes = new byte[1024];
        while ((len = in.read(bytes)) != -1){
            stream.write(bytes,0,len);
        }
        String result = stream.toString();
        in.close();
        stream.close();
        return result;
    }

    /**
     * 获取版本信息
     */
    public static String getVersionName(Context context) {
        PackageManager packageManager =context.getPackageManager();
        try {
            PackageInfo archiveInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
            int versionCode = archiveInfo.versionCode;
            String versionName = archiveInfo.versionName;
            return versionName;
        }catch (Exception e){
            e.printStackTrace();
        }
        return "";
    }

    /**
     * 获取版本信息
     */
    public static int getVersionCode(Context context) {
        PackageManager packageManager = context.getPackageManager();
        try {
            PackageInfo archiveInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
            int versionCode = archiveInfo.versionCode;
            String versionName = archiveInfo.versionName;
            return versionCode;
        }catch (Exception e){
            e.printStackTrace();
        }
        return 0;
    }

}
