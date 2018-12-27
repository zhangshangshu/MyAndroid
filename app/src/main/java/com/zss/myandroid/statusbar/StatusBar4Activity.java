package com.zss.myandroid.statusbar;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.TextView;

import com.zss.myandroid.R;

import java.util.ArrayList;

public class StatusBar4Activity extends AppCompatActivity {
    public Toolbar mToolbar;
    public TextView mTitle;
    private ArrayList<BaseNoStatusBarFragment> mFragments;
    private FragmentManager mSupportFragmentManager;
    private FragmentTransaction mFragmentTransaction;
    private Fragment mLastFragment;
    protected View mStatusBarView;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_status_bar4);

        mToolbar = (Toolbar) findViewById(R.id.base_toolbar);
        mTitle = (TextView) findViewById(R.id.base_title);

        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        mFragments = new ArrayList<>();
        HaveToolBarFragment haveToolBarFragment = new HaveToolBarFragment();
        NoToolBarStatusFragment noToolBarStatusFragment = new NoToolBarStatusFragment();
        ToolBarStatusDifColorFragment toolBarStatusDifColorFragment = new ToolBarStatusDifColorFragment();
        OnlyBannerFragment onlyBannerFragment = new OnlyBannerFragment();

        mFragments.add(haveToolBarFragment);
        mFragments.add(noToolBarStatusFragment);
        mFragments.add(toolBarStatusDifColorFragment);
        mFragments.add(onlyBannerFragment);

        mSupportFragmentManager = getSupportFragmentManager();

        findViewById(R.id.bt_have_tool_bar).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectFragment(0);
            }
        });

        findViewById(R.id.bt_no_have_toolbar).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectFragment(1);
            }
        });

        findViewById(R.id.bt_have_toolbar_change_color).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectFragment(2);
            }
        });

        findViewById(R.id.bt_only_banner).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectFragment(3);
            }
        });

        addStatusBar();
        selectFragment(0);
    }

    private void addStatusBar() {
        //条件状态栏透明，要不然不会起作用
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        if (mStatusBarView == null) {
            mStatusBarView = new View(StatusBar4Activity.this);
            int screenWidth = getResources().getDisplayMetrics().widthPixels;
            int statusBarHeight = getStatusBarHeight();
            ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(screenWidth, statusBarHeight);
            mStatusBarView.setLayoutParams(params);
            mStatusBarView.requestLayout();

            //获取根布局
            ViewGroup systemContent = (ViewGroup) findViewById(android.R.id.content);
            ViewGroup userContent = (ViewGroup) systemContent.getChildAt(0);
            userContent.setFitsSystemWindows(false);
            userContent.addView(mStatusBarView, 0);
        }
    }

    public int getStatusBarHeight() {
        int statusBarHeight1 = -1;
        //获取status_bar_height资源的ID
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            //根据资源ID获取响应的尺寸值
            statusBarHeight1 = getResources().getDimensionPixelSize(resourceId);
        }
        return statusBarHeight1;
    }

    @Override
    public void setTitle(CharSequence title) {
        mTitle.setText(title);
    }

    /**
     * Fragment 的 hide、show、add 操作
     * @param index
     */
    private void selectFragment(int index) {
        if (mFragments != null && mFragments.size() > 0) {
            mFragmentTransaction = mSupportFragmentManager.beginTransaction();
            Fragment baseFragment = mFragments.get(index);
            if (mLastFragment != null) {
                // hide fragment
                mFragmentTransaction.hide(mLastFragment);
            }
            if (mFragmentTransaction != null) {
                // 判断fragment是否已添加
                if (baseFragment.isAdded()) {
                    // show fragment
                    mFragmentTransaction.show(baseFragment);
                } else {
                    // add fragment
                    mFragmentTransaction.add(R.id.base_container, baseFragment);
                }
            }
            mFragmentTransaction.commitAllowingStateLoss();
            mLastFragment = baseFragment;
        }
    }

    /**
     * 有ToolBar ， 有 StatusBar
     */
    public static class HaveToolBarFragment extends BaseNoStatusBarFragment {

        private StatusBar4Activity mActivity;

        @Override
        protected void configFragmentView(View view) {
            mActivity = ((StatusBar4Activity) getActivity());
            mActivity.mToolbar.setVisibility(View.VISIBLE);
            mActivity.mToolbar.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
            mActivity.mStatusBarView.setBackgroundColor(getResources().getColor(android.R.color.holo_blue_bright));
            mActivity.mStatusBarView.setVisibility(View.VISIBLE);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
                mActivity.getWindow().getDecorView().setSystemUiVisibility(View.VISIBLE);
        }

        @Override
        public void onHiddenChanged(boolean hidden) {
            super.onHiddenChanged(hidden);
            mActivity.mToolbar.setVisibility(View.VISIBLE);//设置ToolBar显示
            mActivity.mToolbar.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
            mActivity.mStatusBarView.setBackgroundColor(getResources().getColor(android.R.color.holo_blue_bright));
            mActivity.mStatusBarView.setVisibility(View.VISIBLE);
            //恢复默认statusBar文字颜色
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
                mActivity.getWindow().getDecorView().setSystemUiVisibility(View.VISIBLE);
        }

        @Override
        public int getFragmentView() {
            return R.layout.fragment_have_havetool;
        }

        @Override
        public String getFragmentTitle() {
            return "yes";
        }
    }

    /**
     * 无ToolBar ， 有StatusBar
     */
    public static class NoToolBarStatusFragment extends BaseNoStatusBarFragment {
        private StatusBar4Activity mActivity;


        @Override
        protected void configFragmentView(View view) {
            mActivity = ((StatusBar4Activity) getActivity());
            mActivity.mToolbar.setVisibility(View.GONE);//Toolbar隐藏
            mActivity.mToolbar.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
            mActivity.mStatusBarView.setBackgroundColor(getResources().getColor(android.R.color.holo_orange_light));
            mActivity.mStatusBarView.setVisibility(View.VISIBLE);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
                mActivity.getWindow().getDecorView().setSystemUiVisibility(View.VISIBLE);
        }

        @Override
        public void onHiddenChanged(boolean hidden) {
            super.onHiddenChanged(hidden);
            mActivity.mToolbar.setVisibility(View.GONE);//设置ToolBar消失
            mActivity.mToolbar.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
            mActivity.mStatusBarView.setBackgroundColor(getResources().getColor(android.R.color.holo_orange_light));
            mActivity.mStatusBarView.setVisibility(View.VISIBLE);
            //恢复默认statusBar文字颜色
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
                mActivity.getWindow().getDecorView().setSystemUiVisibility(View.VISIBLE);
        }

        @Override
        public int getFragmentView() {
            return R.layout.fragment_no_have_tool;
        }

        @Override
        public String getFragmentTitle() {
            return "no";
        }


    }

    /**
     * 有ToolBar ， 有StatusBar ， 设置 StatusBar 字体颜色
     */
    public static class ToolBarStatusDifColorFragment extends BaseNoStatusBarFragment {
        private StatusBar4Activity mActivity;


        @Override
        protected void configFragmentView(View view) {
            mActivity = ((StatusBar4Activity) getActivity());
            mActivity.mToolbar.setVisibility(View.VISIBLE);
            mActivity.mToolbar.setBackgroundColor(getResources().getColor(R.color.colorAccent));
            mActivity.mStatusBarView.setVisibility(View.VISIBLE);
            mActivity.mStatusBarView.setBackgroundColor(getResources().getColor(R.color.colorAccent));
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
                mActivity.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }

        @Override
        public void onHiddenChanged(boolean hidden) {
            super.onHiddenChanged(hidden);
            mActivity.mToolbar.setVisibility(View.VISIBLE);
            mActivity.mToolbar.setBackgroundColor(getResources().getColor(R.color.colorAccent));
            mActivity.mStatusBarView.setBackgroundColor(getResources().getColor(R.color.colorAccent));
            mActivity.mStatusBarView.setVisibility(View.VISIBLE);
            //设置 statusBar 字体颜色（目前只有android原生6.0以上支持修改状态栏字体。API 23（Android 6.0））
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
                mActivity.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }

        @Override
        public int getFragmentView() {
            return R.layout.fragment_no_have_tool;
        }

        @Override
        public String getFragmentTitle() {
            return "no";
        }


    }

    public static class OnlyBannerFragment extends BaseNoStatusBarFragment {
        private StatusBar4Activity mActivity;


        @Override
        protected void configFragmentView(View view) {
            mActivity = ((StatusBar4Activity) getActivity());
            mActivity.mToolbar.setVisibility(View.GONE);
            mActivity.mStatusBarView.setVisibility(View.GONE);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
                mActivity.getWindow().getDecorView().setSystemUiVisibility(View.VISIBLE);
        }

        @Override
        public void onHiddenChanged(boolean hidden) {
            super.onHiddenChanged(hidden);
            mActivity.mToolbar.setVisibility(View.GONE);
            mActivity.mStatusBarView.setVisibility(View.GONE);
            //恢复默认statusBar文字颜色
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
                mActivity.getWindow().getDecorView().setSystemUiVisibility(View.VISIBLE);
                mActivity.mStatusBarView.setVisibility(View.GONE);
        }

        @Override
        public int getFragmentView() {
            return R.layout.fragment_no_have_tool;
        }

        @Override
        public String getFragmentTitle() {
            return "no";
        }
    }
}
