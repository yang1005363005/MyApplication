package com.hg.hello.util;

import android.content.Context;
import android.widget.Toast;

import com.hg.hello.activity.SplashActivity;

/**
 * Created by Administrator on 2019/5/5 0005.
 */
public class Utils {
    public static void showToast(Context context, String str) {
        Toast.makeText(context,str,Toast.LENGTH_SHORT).show();
    }
}
