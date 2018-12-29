package com.zss.myandroid.drawerlayout;

import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.zss.myandroid.R;

import java.util.ArrayList;
import java.util.List;

/**
 * DrawerLayout 基本使用
 * （1）依赖库：com.android.support:appcompat-v7:24.2.1
 * （2）DrawerLayout分为侧边菜单和主内容区两部分，侧边菜单可以根据手势展开与隐藏，主内容区的部分可以随着菜单的点击而变化。
 * （3）DrawerLayout内部一般有两个子布局（或控件），先写的是内容(主布局)，后写的是侧滑布局（android:layout_gravity="left"）。
 *     至多可以有两个侧滑布局（一左一右）。
 * （5）方法：
 *      openDrawer(Gravity.LEFT):打开侧滑，只能传入LEFT或Right
 *      closeDrawer(Gravity.LEFT):关闭侧滑，只能传入LEFT或Right
 *      isDrawerOpen(Gravity.LEFT):判断是否打开侧滑，只能传入LEFT或Right
 *
 * 参考：
 * https://www.cnblogs.com/zhangqie/p/6410749.html
 * https://blog.csdn.net/ytfunnysite/article/details/56484251
 * https://blog.csdn.net/u012702547/article/details/49562747
 */
public class DrawerLayoutActivity extends AppCompatActivity {
    private ListView listView;

    private DrawerLayout drawerLayout;

    private TextView textView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drawer_layout);
        initView();
    }
    private void initView() {
        listView=(ListView) findViewById(R.id.v4_listview);
        drawerLayout=(DrawerLayout) findViewById(R.id.v4_drawerlayout);
        textView=(TextView) findViewById(R.id.v4_text);
        initDate();
    }

    private void initDate(){
        final List<String> list = new ArrayList<String>();
        list.add("网易");
        list.add("腾讯");
        list.add("新浪");
        list.add("搜狐");
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, list);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                textView.setText(list.get(position));
                showDrawerLayout();
            }
        });
        drawerLayout.openDrawer(Gravity.LEFT);//侧滑打开  不设置则不会默认打开
    }

    private void showDrawerLayout() {
        if (!drawerLayout.isDrawerOpen(Gravity.LEFT)) {//判断未展开
            drawerLayout.openDrawer(Gravity.LEFT);//打开Drawer
        } else {//判断已展开
            drawerLayout.closeDrawer(Gravity.LEFT);//关闭Drawer
        }
    }
}
