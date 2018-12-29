package com.zss.myandroid.drawerlayout;

import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.zss.myandroid.R;

/**
 * DrawerLayout 在 Toolbar 下方 ，显示 ActionBarDrawerToggle
 *
 * 布局层级：
 * <LinearLayout ...>
 *
 *     <Toolbar .../>
 *
 *     <DrawerLayout>
 *
 *     </DrawerLayout>
 *
 * </LinearLayout>
 *
 * 【DrawerLayout和NavigationView使用详解】（https://www.jianshu.com/p/d2b1689a23bf）
 */
public class DrawAboveToolActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_draw_above_tool);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
    }
}
