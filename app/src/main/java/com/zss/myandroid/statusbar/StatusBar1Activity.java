package com.zss.myandroid.statusbar;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.WindowManager;

import com.zss.myandroid.R;

/**
 * 需求一、全屏，不保留状态栏文字(Splash页面，欢迎页面)
 */
public class StatusBar1Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_status_bar1);

        //方式一
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

        //方式二
//        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN);

        //方式三（在style.xml中设置）
        //<style name="fullScreen" parent="Theme.AppCompat.DayNight.NoActionBar">
        //        <item name="android:windowFullscreen">true</item>
        //</style>
    }
}
