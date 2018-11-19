package com.zss.myandroid.mvp;

public interface BaseView {

    void showLoading();

    void hideLoading();

    void onError(Throwable throwable);
}
