package com.zss.myandroid.rxjava2;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.ObservableSource;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;

/**
 * map、
 */
public class RxJavaText_3 {

    private static final String TAG = "RxJavaText_3";

    /**
     * 一个新用户,必须先注册, 等注册成功之后再自动登录该怎么做呢.
     * 很明显, 这是一个嵌套的网络请求, 首先需要去请求注册, 待注册成功回调了再去请求登录的接口.
     * 我们当然可以想当然的写成这样:
     */
//    private void login() {
//        api.login(new LoginRequest())
//                .subscribeOn(Schedulers.io())               //在IO线程进行网络请求
//                .observeOn(AndroidSchedulers.mainThread())  //回到主线程去处理请求结果
//                .subscribe(new Consumer<LoginResponse>() {
//                    @Override
//                    public void accept(LoginResponse loginResponse) throws Exception {
//                        Toast.makeText(MainActivity.this, "登录成功", Toast.LENGTH_SHORT).show();
//                    }
//                }, new Consumer<Throwable>() {
//                    @Override
//                    public void accept(Throwable throwable) throws Exception {
//                        Toast.makeText(MainActivity.this, "登录失败", Toast.LENGTH_SHORT).show();
//                    }
//                });
//    }
//
//    private void register() {
//        api.register(new RegisterRequest())
//                .subscribeOn(Schedulers.io())               //在IO线程进行网络请求
//                .observeOn(AndroidSchedulers.mainThread())  //回到主线程去处理请求结果
//                .subscribe(new Consumer<RegisterResponse>() {
//                    @Override
//                    public void accept(RegisterResponse registerResponse) throws Exception {
//                        Toast.makeText(MainActivity.this, "注册成功", Toast.LENGTH_SHORT).show();
//                        login();   //注册成功, 调用登录的方法
//                    }
//                }, new Consumer<Throwable>() {
//                    @Override
//                    public void accept(Throwable throwable) throws Exception {
//                        Toast.makeText(MainActivity.this, "注册失败", Toast.LENGTH_SHORT).show();
//                    }
//                });
//    }

    /**
     * 这样的代码能够工作, 但不够优雅, 通过本节的学习, 可以让我们用一种更优雅的方式来解决这个问题.
     */

    /**
     * map操作符
     *
     * map是RxJava中最简单的一个变换操作符了，
     * 它的作用就是对上游发送的每一个事件应用一个函数，使得每一个事件都按照指定的函数去变化。
     */
    public static void test1(){
        Disposable disposable = Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(ObservableEmitter<Integer> emitter) throws Exception {
                //在上游我们发送的是数字类型
                emitter.onNext(1);
                emitter.onNext(2);
                emitter.onNext(3);
            }
        }).map(new Function<Integer, String>() {//中间起转换作用的就是map操作符
            @Override
            public String apply(Integer integer) throws Exception {
                return "This is result " + integer;
            }
        }).subscribe(new Consumer<String>() {
            @Override
            public void accept(String s) throws Exception {
                //在下游我们接收的是String类型
                Log.i(TAG, s);
            }
        });

        // 输出：
        // This is result 1
        // This is result 2
        // This is result 3

        //通过Map, 可以将上游发来的事件转换为任意的类型, 可以是一个Object, 也可以是一个集合
    }

    /**
     * flatMap操作符
     */
    public static void test2(){
        Disposable disposable = Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(ObservableEmitter<Integer> emitter) throws Exception {
                emitter.onNext(1);
                emitter.onNext(2);
                emitter.onNext(3);
            }
            //在flatMap中将上游发来的每个事件转换为一个新的发送三个String事件的水管
        }).flatMap(new Function<Integer, ObservableSource<String>>() {
            @Override
            public ObservableSource<String> apply(Integer integer) throws Exception {
                List<String> list = new ArrayList<>();
                for(int i=0;i<3;i++){
                    list.add("I am value " + integer);
                }
                //为了看到flatMap结果是无序的,所以加了10毫秒的延时
                return Observable.fromIterable(list).delay(10, TimeUnit.MILLISECONDS);
            }
        }).subscribe(new Consumer<String>() {
            @Override
            public void accept(String s) throws Exception {
                Log.i(TAG, s);
            }
        });

        // 输出：
        // I am value 1
        // I am value 1
        // I am value 1
        // I am value 3
        // I am value 3
        // I am value 3
        // I am value 2
        // I am value 2
        // I am value 2

        // 中间flatMap的作用是将圆形的事件转换为一个发送矩形事件和三角形事件的新的上游Observable。
        // 上游每发送一个事件，flatMap都将创建一个新的水管，然后发送转换之后的新的事件，下游接收到的就是这些新的水管发送的数据。
        // 【这里需要注意的是, flatMap并不保证事件的顺序。】
        //
        // 【如果需要保证顺序则需要使用concatMap。】
    }


    /**
     * concatMap操作符
     */
    public static void test3(){
        Disposable disposable = Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(ObservableEmitter<Integer> emitter) throws Exception {
                emitter.onNext(1);
                emitter.onNext(2);
                emitter.onNext(3);
            }
        }).concatMap(new Function<Integer, ObservableSource<String>>() {
            @Override
            public ObservableSource<String> apply(Integer integer) throws Exception {
                final List<String> list = new ArrayList<>();
                for (int i = 0; i < 3; i++) {
                    list.add("I am value " + integer);
                }
                return Observable.fromIterable(list).delay(10,TimeUnit.MILLISECONDS);
            }
        }).subscribe(new Consumer<String>() {
            @Override
            public void accept(String s) throws Exception {
                Log.d(TAG, s);
            }
        });

        // 只是将之前的flatMap改为了concatMap, 其余原封不动, 运行结果如下:
        // I am value 1
        // I am value 1
        // I am value 1
        // I am value 2
        // I am value 2
        // I am value 2
        // I am value 3
        // I am value 3
        // I am value 3
        // 可以看到, 结果仍然是有序的.
    }

    /**
     * 好了关于RxJava的操作符最基本的使用就讲解到这里了, RxJava中内置了许许多多的操作符,
     * 这里通过讲解map和flatMap只是起到一个抛砖引玉的作用,
     * 关于其他的操作符只要大家按照本文的思路去理解, 再仔细阅读文档, 应该是没有问题的了
     */

    /**
     * （实践）
     * 注册 -> 登录
     *
     * 如何优雅的解决嵌套请求, 只需要用flatMap转换一下就行了.
     *
     *  public interface Api {
     *      @GET
     *      Observable<LoginResponse> login(@Body LoginRequest request);
     *
     *      @GET
     *      Observable<RegisterResponse> register(@Body RegisterRequest request);
     *  }
     * 可以看到登录和注册返回的都是一个上游Observable, 而我们的flatMap操作符的作用就是把一个Observable转换为另一个Observable
     */
    public static void test4(){
//        api.register(new RegisterRequest())            //发起注册请求
//                .subscribeOn(Schedulers.io())               //在IO线程进行网络请求
//                .observeOn(AndroidSchedulers.mainThread())  //回到主线程去处理请求注册结果
//                .doOnNext(new Consumer<RegisterResponse>() {
//                    @Override
//                    public void accept(RegisterResponse registerResponse) throws Exception {
//                        //先根据注册的响应结果去做一些操作
//                    }
//                })
//                .observeOn(Schedulers.io())                 //回到IO线程去发起登录请求
//                .flatMap(new Function<RegisterResponse, ObservableSource<LoginResponse>>() {
//                    @Override
//                    public ObservableSource<LoginResponse> apply(RegisterResponse registerResponse) throws Exception {
//                        return api.login(new LoginRequest());
//                    }
//                })
//                .observeOn(AndroidSchedulers.mainThread())  //回到主线程去处理请求登录的结果
//                .subscribe(new Consumer<LoginResponse>() {
//                    @Override
//                    public void accept(LoginResponse loginResponse) throws Exception {
//                        Toast.makeText(MainActivity.this, "登录成功", Toast.LENGTH_SHORT).show();
//                    }
//                }, new Consumer<Throwable>() {
//                    @Override
//                    public void accept(Throwable throwable) throws Exception {
//                        Toast.makeText(MainActivity.this, "登录失败", Toast.LENGTH_SHORT).show();
//                    }
//                });

        //从这个例子也可以看到我们切换线程是多么简单.
    }
}
