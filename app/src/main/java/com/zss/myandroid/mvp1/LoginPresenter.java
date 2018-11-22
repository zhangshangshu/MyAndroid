package com.zss.myandroid.mvp1;

public class LoginPresenter extends BasePresenter<LoginView> {
    public void login() {
        mView.loginSuccess();
    }
}
