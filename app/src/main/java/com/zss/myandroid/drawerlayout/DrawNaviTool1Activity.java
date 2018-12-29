package com.zss.myandroid.drawerlayout;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.zss.myandroid.R;

/**
 * DrawerLayout（侧滑布局） + NavigationView（作为侧滑控件） + Toolbar（作为ActionBar）
 *
 * 1.在顶层布局DrawerLayout中设置android:fitsSystemWindows="true"的效果。
 *   可以发现侧滑布局会布满整个window高度，被状态栏及标题栏遮盖。
 *   主内容布局则不会布满整个window高度，而是在状态栏及标题栏的下方。
 *
 * 2.Toolbar设置为系统ActionBar：
 *     （1） 代码设置：
 *          setSupportActionBar(toolbar);
 *     （2）设置activity的主题：
 *          <style name="AppTheme.NoActionBar">
 *              <item name="windowActionBar">false</item>
 *              <item name="windowNoTitle">true</item>
 *          </style>
 * 3.NavigationView
 *     （1）app:headerLayout属性，设置头布局
 *     （2）app:menu属性，设置item列表
 *     （3）监听item点击事件。
 * 4.对onBackPressed()重写，判断按Back键关闭DrawerLayout侧滑还是销毁Activity
 *
 * 【DrawerLayout和NavigationView使用详解】（https://www.jianshu.com/p/d2b1689a23bf）
 */
public class DrawNaviTool1Activity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_draw_navi_tool1);

        /**
         * setSupportActionBar(toolbar);
         * 这里设置Toolbar作为ActionBar，需要设置activity的theme为：
         *
         *  <style name="AppTheme.NoActionBar">
         *      <item name="windowActionBar">false</item>
         *      <item name="windowNoTitle">true</item>
         *  </style>
         *
         * 否则会报错
         * Caused by: java.lang.IllegalStateException:
         * This Activity already has an action bar supplied by the window decor.Do not request Window.
         * FEATURE_SUPPORT_ACTION_BAR and set windowActionBar to false in your theme to use a Toolbar instead.
         */
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //这里将DrawerLayout，ActionBarDrawerToggle，Toolbar配合使用，
        // 使得（滑动/点击ActionBarDrawerToggle）打开和关闭侧滑布局时，ToolBar上的 ActionBarDrawerToggle 呈现动画效果
        // 在Toolbar的左侧会出现一个图标，这个图标就是ActionBarDrawerToggle
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        //这个Home旋转开关按钮实际上是通过ActionBarDrawerToggle代码绑定到toolbar上的，
        // ActionBarDrawerToggle是和DrawerLayout搭配使用的，它可以改变android.R.id.home返回图标，监听drawer的显示和隐藏。
        // ActionBarDrawerToggle的syncState()方法会和Toolbar关联，将图标放入到Toolbar上。
        //
        //如果不想显示Home旋转开关按钮，有两种方式：
        //（1）注释上面的代码
        //（2）不传入toolbar
        //    ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
        //                this, drawer, R.string.navigation_drawer_open, R.string.navigation_drawer_close);


        //NavigationView作为DrawLayout的侧滑控件
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);//监听menu的点击事件
    }

    /**
     * NavigationView 中 menu 的点击事件
     * @param item
     * @return
     */
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
//        return false;
        int id = item.getItemId();
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);//这里统一处理：关闭侧滑
        return true;//实测:这里返回true,点击“单选item”后会显示当前列表点击位置；返回false则不会显示当前的列表位置。
    }

    /**
     * Back键回调
     * 判断DrawerLayout侧滑是否展开：
     * (1)若展开状态，则关闭
     * (2)若关闭状态，则退出Activity
     */
    @Override
    public void onBackPressed()
    {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

}
