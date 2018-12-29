package com.zss.myandroid.drawerlayout;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.zss.myandroid.R;

public class DrawerDemoActivity extends AppCompatActivity implements View.OnClickListener{
    private Button btnDraw1;
    private Button btnDraw2;
    private Button btnDraw3;
    private Button btnDraw4;
    private Button btnDraw5;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drawer_demo);

        btnDraw1 = findViewById(R.id.btn_drawer1);
        btnDraw2 = findViewById(R.id.btn_drawer2);
        btnDraw3 = findViewById(R.id.btn_drawer3);
        btnDraw4 = findViewById(R.id.btn_drawer4);
        btnDraw5 = findViewById(R.id.btn_drawer5);

        btnDraw1.setOnClickListener(this);
        btnDraw2.setOnClickListener(this);
        btnDraw3.setOnClickListener(this);
        btnDraw5.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_drawer1:
                startActivity(new Intent(DrawerDemoActivity.this, DrawerLayoutActivity.class));
                break;
            case R.id.btn_drawer2:
                startActivity(new Intent(DrawerDemoActivity.this, DrawNaviTool1Activity.class));
                break;
            case R.id.btn_drawer3:
                startActivity(new Intent(DrawerDemoActivity.this, DrawAboveToolActivity.class));
                break;
            case R.id.btn_drawer4:
                startActivity(new Intent(DrawerDemoActivity.this, DrawNaviTool2Activity.class));
                break;
            case R.id.btn_drawer5:
                startActivity(new Intent(DrawerDemoActivity.this, NavigationViewActivity.class));
                break;
        }
    }
}
