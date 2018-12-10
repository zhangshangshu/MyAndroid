package com.zss.myandroid;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.tencent.bugly.crashreport.CrashReport;
import com.zss.myandroid.gson.GsonTest3;
import com.zss.myandroid.webview.WebViewActivity;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    private Button btnBugly;
    private Button btnAnimSet;
    private Button btnWebView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnBugly = findViewById(R.id.btn_bugly);
        btnAnimSet = findViewById(R.id.btn_animation_set);
        btnWebView = findViewById(R.id.btn_webview);

        btnBugly.setOnClickListener(this);
        btnAnimSet.setOnClickListener(this);
        btnWebView.setOnClickListener(this);

//        GsonTest1.test1();
//        GsonTest1.test2();

        GsonTest3.test3();
        GsonTest3.test4();
        GsonTest3.test5();
//        test1();
//        test();
//        Add();
//        System.out.println();
//        System.out.println();
//        Sub();
    }

    int i = 0;
    private void test(){
        for(int j=0;j<=99;j++) {
            if (i == 50) {
                System.out.println(i);
            } else {
                System.out.println(i);
            }
            i++;
        }
    }

    private void Add(){
        for(int i=0;i<=99;i++){
            System.out.println(i);
        }
    }

    private void Sub(){
        for(int i = 99;i>=0;i--){
            System.out.println(i);
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_bugly:
                CrashReport.testJavaCrash();
                break;
            case R.id.btn_animation_set:
                startActivity(new Intent(MainActivity.this,AnimationSetActivity.class));
                break;
            case R.id.btn_webview:
                startActivity(new Intent(MainActivity.this,WebViewActivity.class));
                break;
        }
    }

    //测试
}
