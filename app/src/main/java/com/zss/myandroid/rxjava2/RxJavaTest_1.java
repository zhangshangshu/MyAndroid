package com.zss.myandroid.rxjava2;

import android.util.Log;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

public class RxJavaTest_1 {
    private static final String TAG = "RxJavaTest_1";

    /**
     * 最简单的使用
     */
    public static void test1() {
        Observable observable = Observable.create(new ObservableOnSubscribe() {
            @Override
            public void subscribe(ObservableEmitter emitter) throws Exception {
                emitter.onNext(1);
                emitter.onNext(2);
                emitter.onNext(3);
                emitter.onComplete();
            }
        });

        Observer observer = new Observer() {
            @Override
            public void onSubscribe(Disposable d) {
                Log.i(TAG, "onSubscribe: ");
            }

            @Override
            public void onNext(Object o) {
                Log.i(TAG, "" + o);
            }

            @Override
            public void onError(Throwable e) {
                Log.i(TAG, "onError: ");
            }

            @Override
            public void onComplete() {
                Log.i(TAG, "onComplete: ");
            }
        };

        //建立连接
        observable.subscribe(observer);

        //运行结果：
        // subscribe
        // 1
        // 2
        // 3
        // complete
    }

    /**
     * 注意: 只有当上游和下游建立连接之后, 上游才会开始发送事件. 也就是调用了subscribe() 方法之后才开始发送事件.
     */

    /**
     * 链式调用（把上面的代码连起来写就成了RxJava引以为傲的链式操作）
     */
    public static void test2() {
        Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(ObservableEmitter<Integer> emitter) throws Exception {
                emitter.onNext(1);
                emitter.onNext(2);
                emitter.onNext(3);
                emitter.onComplete();
            }
        }).subscribe(new Observer<Integer>() {
            @Override
            public void onSubscribe(Disposable d) {
                Log.i(TAG, "onSubscribe: ");
            }

            @Override
            public void onNext(Integer integer) {
                Log.i(TAG, "" + integer);
            }

            @Override
            public void onError(Throwable e) {
                Log.i(TAG, "onError: ");
            }

            @Override
            public void onComplete() {
                Log.i(TAG, "onComplete: ");
            }
        });
    }

    /**
     * ObservableEmitter 和 Disposable
     */
    public static void test3() {
        Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(ObservableEmitter<Integer> emitter) throws Exception {
                // ObservableEmitter：
                // ObservableEmitterEmitter是发射器的意思，它可以发出三种类型的事件，
                // 通过调用emitter的onNext(T value)、onComplete()和onError(Throwable error)
                // 就可以分别发出next事件、complete事件和error事件。

                Log.d(TAG, "emit 1");
                emitter.onNext(1);
                Log.d(TAG, "emit 2");
                emitter.onNext(2);
                Log.d(TAG, "emit 3");
                emitter.onNext(3);
                Log.d(TAG, "emit complete");
                emitter.onComplete();
                Log.d(TAG, "emit 4");
                emitter.onNext(4);

                //并不意味着你可以随意乱七八糟发射事件，满足一定的规则：
                //(1)上游可以发送无限个onNext, 下游也可以接收无限个onNext.
                //(2)当上游发送了一个onComplete后, 上游onComplete之后的事件将会继续发送, 而下游收到onComplete事件之后将不再继续接收事件.
                //(3)当上游发送了一个onError后,  上游onError之后的事件将继续发送, 而下游收到onError事件之后将不再继续接收事件.
                //(4)上游可以不发送onComplete或onError.
                //(5)最为关键的是onComplete和onError必须唯一并且互斥, 即不能发多个onComplete, 也不能发多个onError,
                //   也不能先发一个onComplete, 然后再发一个onError, 反之亦然
                //
                //如果你的代码逻辑中违背了这个规则, **并不一定会导致程序崩溃. **
                //比如发送多个onComplete是可以正常运行的, 依然是收到第一个onComplete就不再接收了,
                //但若是发送多个onError, 则收到第二个onError事件会导致程序会崩溃。
            }
        }).subscribe(new Observer<Integer>() {
            private Disposable disposable;
            private int i;

            @Override
            public void onSubscribe(Disposable d) {
                Log.i(TAG, "onSubscribe: ");
                //Disposable：
                // 可以把它理解成两根管道之间的一个机关，当调用它的dispose()方法时, 它就会将两根管道切断, 从而导致下游收不到事件.
                disposable = d;
            }

            @Override
            public void onNext(Integer integer) {
                Log.i(TAG, "onNext: " + integer);
                i++;
                if (i == 2) {
                    Log.i(TAG, "disposable");
                    //Disposable：
                    // 注意: 调用dispose()并不会导致上游不再继续发送事件, 上游会继续发送剩余的事件.
                    disposable.dispose();
                    Log.i(TAG, "isDisposed : " + disposable.isDisposed());
                }
            }

            @Override
            public void onError(Throwable e) {
                Log.i(TAG, "onError: ");
            }

            @Override
            public void onComplete() {
                Log.i(TAG, "onComplete: ");
            }
        });

        //运行结果：
        // subscribe
        // emit 1
        // onNext: 1
        // emit 2
        // onNext: 2
        // dispose
        // isDisposed : true
        // emit 3
        // emit complete
        // emit 4

        //从运行结果我们看到, 在收到onNext 2这个事件后, 切断了水管,
        // 但是上游仍然发送了3, complete, 4这几个事件, 而且上游并没有因为发送了onComplete而停止.
        // 同时可以看到下游的onSubscribe()方法是最先调用的.
    }

    /**
     * subscribe()有多个重载
     */
    public static void test4() {
        Disposable disposable = Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(ObservableEmitter<Integer> emitter) throws Exception {

                Log.d(TAG, "emit 1");
                emitter.onNext(1);
                Log.d(TAG, "emit 2");
                emitter.onNext(2);
                Log.d(TAG, "emit 3");
                emitter.onNext(3);
                Log.d(TAG, "emit complete");
                emitter.onComplete();
                Log.d(TAG, "emit 4");
                emitter.onNext(4);

            }
        }).subscribe(new Consumer<Integer>() {
            @Override
            public void accept(Integer integer) throws Exception {
                Log.i(TAG, "accept: " + integer);
            }
        });

        /**
         * subscribe()有多个重载：
         * public final Disposable subscribe() {}
         * public final Disposable subscribe(Consumer<? super T> onNext) {}
         * public final Disposable subscribe(Consumer<? super T> onNext, Consumer<? super Throwable> onError) {}
         * public final Disposable subscribe(Consumer<? super T> onNext, Consumer<? super Throwable> onError, Action onComplete) {}
         * public final Disposable subscribe(Consumer<? super T> onNext, Consumer<? super Throwable> onError, Action onComplete, Consumer<? super Disposable> onSubscribe) {}
         * public final void subscribe(Observer<? super T> observer) {}
         *
         * 不带任何参数的subscribe() 表示下游不关心任何事件,你上游尽管发你的数据去吧, 老子可不管你发什么。
         * 带有一个Consumer参数的方法表示下游只关心onNext事件, 其他的事件我假装没看见。
         * 其他几个方法同理。
         */
    }
}
