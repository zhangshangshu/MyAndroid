package com.zss.myandroid.coordinatorlayout;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.zss.myandroid.R;
import com.zss.myandroid.recyclerview.AAdapter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 *  CoordinatorLayout + AppBarLayout + ViewPager(RecyclerView) 可实现滑动时Toolbar隐藏的效果。
 *  CoordinatorLayout + AppBarLayout + ViewPager(ListView) 则不可以。
 *
 *  所以 CoordinatorLayout + AppBarLayout 需要配合 RecyclerView等控件才能实现滑动时Toolbar显示/隐藏。
 */
public class CoordinatorLayout2Activity extends AppCompatActivity {

    private TabLayout tabLayout;
    private ViewPager viewPager;

    private String[] mTitleArr = {"Android", "Java", "iOS", "C", "C++", "Swift", "Python"};
    List<View> viewList = new ArrayList<View>();
    private ListViewViewPager listViewViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coordinator_layout2);

        tabLayout = findViewById(R.id.tabs);
        viewPager = (ViewPager) findViewById(R.id.viewpager);

        initData();
    }

    private void initData() {
        int size = mTitleArr.length;
        viewList = new ArrayList<>(size);
        // 添加 Tab，初始化 View 列表
        for (int i = 0; i < size; i++) {

            tabLayout.addTab(tabLayout.newTab().setText(mTitleArr[i]));

//            View view = LayoutInflater.from(this).inflate(R.layout.list_view, null);
//            ListView lv = (ListView) view.findViewById(R.id.list_view);
//            //这里我们传入数据
//            String[] data = {"1","2","3","4","5","6","7","8","9","10","11","12","13"};
//            //android.R.layout.simple_list_item_1是android自带的一个布局，只有一个textview
//            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,data);
//            //为ListView设置适配器
//            lv.setAdapter(adapter);

            View view = LayoutInflater.from(this).inflate(R.layout.recycler_view, null);
            RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            String[] data = {"1","2","3","4","5","6","7","8","9","10","11","12","13","1","2","3","4","5","6","7","8","9","10","11","12","13"};
            recyclerView.setAdapter(new AAdapter(Arrays.asList(data),this));

            viewList.add(view);
        }

        listViewViewPager = new ListViewViewPager();

        // 填充 ViewPager
        viewPager.setAdapter(listViewViewPager);
        // 给ViewPager添加监听
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        // 设置setOnTabSelectedListener
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                // 切换到指定的 item
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    /**
     * ViewPager + ListView 的 Adapter
     * [如何在ViewPager里面加入ListView](https://blog.csdn.net/xuaho0907/article/details/78030155)
     */
    class ListViewViewPager extends PagerAdapter{

        //这个方法是返回总共有几个滑动的页面（）
        @Override
        public int getCount() {
            return viewList.size();
        }

        //该方法判断是否由该对象生成界面。
        @Override
        public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
            return view == object;
        }

        //这个方法返回一个对象，该对象表明PagerAapter选择哪个对象放在当前的ViewPager中。这里我们返回当前的页面
        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            viewPager.addView(viewList.get(position));
            return viewList.get(position);
        }

        //这个方法从viewPager中移动当前的view。（划过的时候）
        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            viewPager.removeView(viewList.get(position));
        }
    }
}
