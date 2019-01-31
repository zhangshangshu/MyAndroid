package com.zss.myandroid.coordinatorlayout;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.zss.myandroid.R;

/**
 * CoordinatorLayout + FloatingActionButton + Snackbar
 * 
 * FloatingActionButton是最简单的使用CoordinatorLayout的例子，FloatingActionButton默认使用 FloatingActionButton.Behavior。
 */
public class CoordinatorLayout1Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coordinator_layout1);

        findViewById(R.id.fab).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Snackbar.make(view,"Hello,I'm Snackbar!",Snackbar.LENGTH_LONG)
                        .setAction("cancel", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                //这里的单击事件代表点击消除Action后的响应事件

                            }
                        })
                        .show();
            }
        });
    }
}
