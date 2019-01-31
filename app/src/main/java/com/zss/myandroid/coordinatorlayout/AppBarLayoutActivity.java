package com.zss.myandroid.coordinatorlayout;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import com.zss.myandroid.R;
import com.zss.myandroid.recyclerview.AAdapter;

import java.util.Arrays;

public class AppBarLayoutActivity extends AppCompatActivity {

    public Toolbar mToolbarTb;
    private RecyclerView mContentRv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_bar_layout);

        mToolbarTb = (Toolbar) findViewById(R.id.tb_toolbar);
        if (mToolbarTb!=null) {
            setSupportActionBar(mToolbarTb);

            getSupportActionBar().setHomeButtonEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        mContentRv = (RecyclerView) findViewById(R.id.rv_content);
        mContentRv.setLayoutManager(new LinearLayoutManager(this));
        String[] data = {"1","2","3","4","5","6","7","8","9","10","11","12","13","1","2","3","4","5","6","7","8","9","10","11","12","13"};
        mContentRv.setAdapter(new AAdapter(Arrays.asList(data),this));
    }
}
