package com.zss.myandroid.recyclerview;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.zss.myandroid.R;
import com.zss.myandroid.util.ToastUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 【Android RecyclerView 使用完全解析 体验艺术般的控件】 https://blog.csdn.net/lmj623565791/article/details/45059587
 */
public class RecyclerViewActivity extends AppCompatActivity {

    private List<String> mDatas;
    private RecyclerView mRecyclerView;
    private AAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recycler_view);

        initData();

        mRecyclerView = (RecyclerView) findViewById(R.id.id_recyclerview);
        //设置布局管理器
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        // mRecyclerView.setLayoutManager(new GridLayoutManager(this,4)); //GridView效果（4列，竖直滑动）

        //可实现瀑布流效果，需要在Adapter的onBindViewHolder方法中为我们的item设置个随机的高度。
        // mRecyclerView.setLayoutManager(new StaggeredGridLayoutManager(4,StaggeredGridLayoutManager.VERTICAL)); //4列，竖直滑动
        // mRecyclerView.setLayoutManager(new StaggeredGridLayoutManager(4,StaggeredGridLayoutManager.HORIZONTAL)); //4行，水平滑动

        //设置适配器
        mRecyclerView.setAdapter(mAdapter = new AAdapter(mDatas,this));
        //添加分割线
        mRecyclerView.addItemDecoration(new DividerItemDecoration(this,
                DividerItemDecoration.VERTICAL_LIST));
        //设置item动画
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());

        //设置Item事件监听
        mAdapter.setOnItemClickLitener(new AAdapter.OnItemClickLitener() {
            @Override
            public void onItemClick(View view, int position) {
                //点击事件
                ToastUtils.showShort("click：" +  position);
            }

            @Override
            public void onItemLongClick(View view, int position) {
                //长按事件
                ToastUtils.showShort("long click：" +  position);
            }
        });
    }

    protected void initData()
    {
        mDatas = new ArrayList<String>();
        for (int i = 'A'; i < 'z'; i++)
        {
            mDatas.add("" + (char) i);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
            case R.id.id_action_add:
                mAdapter.addData(1);
                break;
            case R.id.id_action_delete:
                mAdapter.removeData(1);
                break;
        }
        return true;
    }

    /**
     * 通过简单改变下LayoutManager，就可以产生不同的效果，那么我们可以根据手机屏幕的宽度去动态设置LayoutManager，
     * 屏幕宽度一般的，显示为ListView；
     * 宽度稍大的显示两列的GridView或者瀑布流（或者横纵屏幕切换时变化，有点意思~）；显示的列数和宽度成正比。
     * 甚至某些特殊屏幕，让其横向滑动~~再选择一个nice的动画效果，相信这种插件式的编码体验一定会让你迅速爱上RecyclerView。
     */
}

