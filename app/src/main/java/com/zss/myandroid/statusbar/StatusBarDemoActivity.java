package com.zss.myandroid.statusbar;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.zss.myandroid.R;

public class StatusBarDemoActivity extends AppCompatActivity implements View.OnClickListener{
    private Button btn1;
    private Button btn2;
    private Button btn3;
    private Button btn4;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_status_bar_demo);

        btn1 = findViewById(R.id.btn_1);
        btn2 = findViewById(R.id.btn_2);
        btn3 = findViewById(R.id.btn_3);
        btn4 = findViewById(R.id.btn_4);
        btn1.setOnClickListener(this);
        btn2.setOnClickListener(this);
        btn3.setOnClickListener(this);
        btn4.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_1:
                startActivity(new Intent(StatusBarDemoActivity.this,StatusBar1Activity.class));
                break;
            case R.id.btn_2:
                startActivity(new Intent(StatusBarDemoActivity.this,StatusBar2Activity.class));
                break;
            case R.id.btn_3:
                startActivity(new Intent(StatusBarDemoActivity.this,StatusBar3Activity.class));
                break;
            case R.id.btn_4:
                startActivity(new Intent(StatusBarDemoActivity.this,StatusBar4Activity.class));
                break;
        }
    }
}
