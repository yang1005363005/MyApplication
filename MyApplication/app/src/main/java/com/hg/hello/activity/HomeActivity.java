package com.hg.hello.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.hg.hello.myapplication.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * 主页面
 * Created by Administrator on 2019/4/28 0028.
 */

public class HomeActivity extends AppCompatActivity {
    private GridView gView;
    private String[] mItems = new String[] {"手机防盗","通讯卫士","软件管理","进程管理","流量统计","手机杀毒","缓存清理","高级工具","设置中心"};
    private int[] mPic = new int[] {R.drawable.pic1,R.drawable.pic2,R.drawable.pic3,R.drawable.pic4,R.drawable.pic5,R.drawable.pic6,R.drawable.pic7,R.drawable.pic8,R.drawable.pic9};
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_layout);
        gView = (GridView) findViewById(R.id.gView);
        initView();
    }

    private void initView() {
        String[] g_text = {"img","tv"};
        int[] g_img = {R.id.h_g_img,R.id.h_g_tv};
        List<Map<String, Object>> dList = new ArrayList<Map<String, Object>>();
        for (int i = 0;i<mItems.length;i++ ){
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("img",mPic[i]);
            map.put("tv",mItems[i]);
            dList.add(map);
        }

        gView.setAdapter(new SimpleAdapter(this, dList, R.layout.home_gridview, g_text, g_img));

        gView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position){
                    case 0:
                        startActivity(new Intent(HomeActivity.this,TheftPreventionAvtivity.class));
                        break;
                    default:
                        Toast.makeText(HomeActivity.this,position+"----"+mItems[position],Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        });
    }

}
