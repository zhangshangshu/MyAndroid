package com.zss.myandroid.drawerlayout;

import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;

import com.zss.myandroid.R;
import com.zss.myandroid.util.ToastUtils;

public class NavigationViewActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation_view);

        /**
         * 1.图标设置颜色：
         */
        //方式一：可以设置图片显示本身的颜色，否则会显示灰色（这个更合理）
        NavigationView navigationView1 = (NavigationView) findViewById(R.id.nav_view1);
        navigationView1.setItemIconTintList(null);

        //方式二：在xml中通过 app:itemIconTint="@color/blue"属性 ，设置所有图标统一颜色
        NavigationView navigationView2 = (NavigationView) findViewById(R.id.nav_view2);

        /**
         * 2.头部点击事件
         */
        //获取头布局文件
        View headerView = navigationView1.getHeaderView(0);
        //然后通过调用headerView中的findViewById方法来查找到头部的控件，设置点击事件即可。
        headerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ToastUtils.showShort("头部控件点击事件");
            }
        });

        /**
         * 3.item点击事件
         */
        navigationView1.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                int id = item.getItemId();
                String content = item.getTitle().toString();
                ToastUtils.showShort(content);

                //在这里处理item的点击事件
                return true;//实测:这里返回true,点击“单选item”后会显示当前列表点击位置；返回false则不会显示当前的列表位置。
            }
        });

        /**
         * 4.NavigationView的 xml属性（详情见 layout文件）
         * 5.NavigationView的 menu菜单文件的书写（详情见 menu文件）
         */
    }
}
