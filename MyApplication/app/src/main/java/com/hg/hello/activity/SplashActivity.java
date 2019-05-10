package com.hg.hello.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.hg.hello.util.DownloadUtil;
import com.hg.hello.util.MyAsyncTask;
import com.hg.hello.myapplication.R;
import com.hg.hello.util.StringUtil;
import com.hg.hello.util.Utils;

import org.json.JSONObject;

import java.io.File;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import static com.hg.hello.myapplication.R.id.progressBar;

/**
 * Created by Administrator on 2019/4/23 0023.
 */

public class SplashActivity extends AppCompatActivity {
    private String URL = "http://192.168.1.106:8080/Hello/json/urlJson.json";
    private TextView tv, tv1;
    private Context mContext;
    private long time = 0;
    private long waitTime = 1000;
    private ProgressBar viewById;
    private MyAsyncTask myAsyncTask;
    private LinearLayout line;
    private String name = "";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getApplicationContext();
        setContentView(R.layout.splash_layout);
        tv = (TextView) findViewById(R.id.textView);
        tv1 = (TextView) findViewById(R.id.tv1);
        line = (LinearLayout) findViewById(R.id.line);
        this.viewById = (ProgressBar) findViewById(progressBar);
        tv.setText("版本号：" + StringUtil.getVersionName(mContext));

        //checkVerson();
        new MyASyncTask().execute(URL);
    }

    private Handler myHandle = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    try {
                        if (System.currentTimeMillis() - time < waitTime)
                        Thread.sleep((waitTime - (System.currentTimeMillis() - time)));
                        enterHome(msg.getData().getString("obj"));
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    break;
                case 1://进度条更新
                    viewById.setProgress((Integer) msg.obj);
                    tv1.setText(msg.obj + "%");
                    break;
                case 2://下载完成

                    break;
                default:
                    break;
            }
        }
    };

    /**
     * ASyncTask 异步请求
     */
    private class MyASyncTask extends AsyncTask<String, Void, String> {

        @Override   //json解析
        protected void onPostExecute(String result) {
            if (!"".equals(result)) {
                try {
                    JSONObject js = new JSONObject(result);
                    int vCode = js.getInt("versionCode");//版本号
                    String vName = js.getString("versionName");//版本名
                    String dPtion = js.getString("description");//版本信息
                    String dUrl = js.getString("downloadUrl");//下载地址
                    name = js.getString("name");//下载地址
                    if (vCode > StringUtil.getVersionCode(mContext)) {
                        //服务器Code大于本地Code更新版本
                        showUpdateDailog(vName, dPtion, dUrl);//升级对话框
                    } else {
                        Message message = new Message();
                        message.what = 0;
                        Bundle bundle = new Bundle();
                        bundle.putString("obj","");  //往Bundle中存放数据
                        message.setData(bundle);//mes利用Bundle传递数
                        myHandle.sendMessage(message);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            super.onPostExecute(result);
        }

        @Override//网络请求
        protected String doInBackground(String... params) {
            time = System.currentTimeMillis();
            String result = "";
            try {
                URL url = new URL("http://192.168.1.106:8080/Hello/json/urlJson.json");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");//请求方法
                conn.setConnectTimeout(5000);//网络超时
                conn.setReadTimeout(5000);//返回超时
                conn.connect();
                int responseCode = conn.getResponseCode();
                if (responseCode == 200) {
                    InputStream inputStream = conn.getInputStream();
                    result = StringUtil.retFromString(inputStream);
                }
            } catch (Exception e) {
                Message message = new Message();
                message.what = 0;
                Bundle bundle = new Bundle();
                bundle.putString("obj","网络异常！");  //往Bundle中存放数据
                message.setData(bundle);//mes利用Bundle传递数
                myHandle.sendMessage(message);
                e.printStackTrace();
            }
            return result;
        }
    }

    /**
     * 升级对话框
     */
    private void showUpdateDailog(String vName, String dPtion, final String dUrl) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("最新版本信息：" + vName);
        builder.setMessage(dPtion);
        builder.setPositiveButton("立即更新", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                line.setVisibility(View.VISIBLE);
                download(dUrl);
            }
        });
        builder.setNegativeButton("以后再说", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                enterHome("");
            }
        });
        builder.show();
    }

    /**
     * 跳转主页面
     */
    public void enterHome(String data) {
        startActivity(new Intent(SplashActivity.this, HomeActivity.class));
        finish();
        if (!"".equals(data)) Toast.makeText(this, data, Toast.LENGTH_LONG).show();
    }

    private void download(String url) {
        DownloadUtil.get().download(url, "download", new DownloadUtil.OnDownloadListener() {
            @Override
            public void onDownloadSuccess(String str) {
                openFile(str);
                Log.e("TAG", "onDownloadFailed: 下载完成");
            }

            @Override
            public void onDownloading(int progress) {
                Message message = new Message();
                message.what = 1;
                message.obj = progress;
                myHandle.sendMessage(message);
                Log.e("TAG", "onDownloadFailed:" + progress);
            }

            @Override
            public void onDownloadFailed() {
                Log.e("TAG", "onDownloadFailed: 下载失败");
            }
        });
    }

    private void openFile(String filePath) {
        Log.e("TAG",filePath+"/"+name );
        File file = new File(filePath+"/"+name);
        Intent intent = new Intent();
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setAction(android.content.Intent.ACTION_VIEW);
        /* 调用getMIMEType()来取得MimeType */
        String type = "application/vnd.android.package-archive";
        /* 设置intent的file与MimeType */
        intent.setDataAndType(Uri.fromFile(file), type);
        startActivity(intent);
    }

}
