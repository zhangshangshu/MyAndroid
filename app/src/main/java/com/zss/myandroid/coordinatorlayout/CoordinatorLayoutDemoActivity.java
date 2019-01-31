package com.zss.myandroid.coordinatorlayout;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.zss.myandroid.R;

public class CoordinatorLayoutDemoActivity extends AppCompatActivity implements View.OnClickListener{
    private Button btn1;
    private Button btn2;
    private Button btn3;
    private Button btn4;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coordinator_layout_demo);

        btn1 = findViewById(R.id.btn_coordinator1);
        btn2 = findViewById(R.id.btn_coordinator2);
        btn3 = findViewById(R.id.btn_coordinator3);
        btn4 = findViewById(R.id.btn_coordinator4);

        btn1.setOnClickListener(this);
        btn2.setOnClickListener(this);
        btn3.setOnClickListener(this);
        btn4.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_coordinator1:
                startActivity(new Intent(CoordinatorLayoutDemoActivity.this,CoordinatorLayout1Activity.class));
                break;
            case R.id.btn_coordinator2:
                startActivity(new Intent(CoordinatorLayoutDemoActivity.this,CoordinatorLayout2Activity.class));
                break;
            case R.id.btn_coordinator3:
                startActivity(new Intent(CoordinatorLayoutDemoActivity.this,AppBarLayoutActivity.class));
                break;
            case R.id.btn_coordinator4:
                startActivity(new Intent(CoordinatorLayoutDemoActivity.this,ImageViewActivity.class));
                break;
        }
    }
}
