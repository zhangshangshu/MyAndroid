package com.zss.myandroid;

import android.app.Application;
import android.content.Context;
import android.text.TextUtils;
import android.view.Gravity;

import com.tencent.bugly.crashreport.CrashReport;
import com.zss.myandroid.util.ToastUtils;
import com.zss.myandroid.util.Utils;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Context context = getApplicationContext();
        // 获取当前包名
        String packageName = context.getPackageName();
        // 获取当前进程名
        String processName = getProcessName(android.os.Process.myPid());
        // 设置是否为上报进程
        CrashReport.UserStrategy strategy = new CrashReport.UserStrategy(context);
        strategy.setUploadProcess(processName == null || processName.equals(packageName));
        //是否调试模式
        boolean isDebug = true;
        // 初始化Bugly
        CrashReport.initCrashReport(context, "87ca6e0a2e", isDebug, strategy);


        Utils.init(this);
        //配置ToastUtils的相关的属性
        ToastUtils.setGravity(Gravity.TOP,0, (int) (80 * Utils.getApp().getResources().getDisplayMetrics().density + 0.5));
        ToastUtils.setBgColor(getResources().getColor(R.color.colorWhite));
        ToastUtils.setMsgColor(getResources().getColor(R.color.colorAccent));
    }

    /**
     * 获取进程号对应的进程名(其中获取进程名的方法“getProcessName”有多种实现方法，推荐方法如下)
     *
     * @param pid 进程号
     * @return 进程名
     */
    private static String getProcessName(int pid) {
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader("/proc/" + pid + "/cmdline"));
            String processName = reader.readLine();
            if (!TextUtils.isEmpty(processName)) {
                processName = processName.trim();
            }
            return processName;
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        } finally {
            try {
                if (reader != null) {
                    reader.close();
                }
            } catch (IOException exception) {
                exception.printStackTrace();
            }
        }
        return null;
    }
}
