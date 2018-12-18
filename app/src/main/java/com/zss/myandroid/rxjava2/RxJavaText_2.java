package com.zss.myandroid.rxjava2;


import android.util.Log;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * 线程控制
 */
public class RxJavaText_2 {
    private static final String TAG = "RxJavaText_2";

    /**
     * 正常情况下, 上游和下游是工作在同一个线程中
     */
    public static void test1() {
        Observable<Integer> observable = Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(ObservableEmitter<Integer> emitter) throws Exception {
                Log.d(TAG, "Observable thread is : " + Thread.currentThread().getName());
                Log.d(TAG, "emit 1");
                emitter.onNext(1);
            }
        });

        Consumer<Integer> consumer = new Consumer<Integer>() {
            @Override
            public void accept(Integer integer) throws Exception {
                Log.d(TAG, "Observer thread is :" + Thread.currentThread().getName());
                Log.d(TAG, "onNext: " + integer);
            }
        };

        observable.subscribe(consumer);

        //在主线程中分别创建上游和下游, 然后将他们连接在一起, 同时分别打印出它们所在的线程, 运行结果为：
        // Observable thread is : main
        // emit 1
        // Observer thread is :main
        // onNext: 1
    }

    /**
     *  上下游默认是在同一个线程工作。
     *
     *  这样肯定是满足不了我们的需求的, 我们更多想要的是这么一种情况, 在子线程中做耗时的操作, 然后回到主线程中来操作UI。
     *  要达到这个目的, 我们需要先改变上游发送事件的线程, 让它去子线程中发送事件, 然后再改变下游的线程, 让它去主线程接收事件。
     *  通过RxJava内置的线程调度器可以很轻松的做到这一点。
     */

    /**
     * subscribeOn方法：
     * 指定上游发送事件的线程。
     *
     * observeOn方法：
     * 指定的是下游接收事件的线程.
     */
    public static void test2() {
        Observable<Integer> observable = Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(ObservableEmitter<Integer> emitter) throws Exception {
                Log.d(TAG, "Observable thread is : " + Thread.currentThread().getName());
                Log.d(TAG, "emit 1");
                emitter.onNext(1);
            }
        });

        Consumer<Integer> consumer = new Consumer<Integer>() {
            @Override
            public void accept(Integer integer) throws Exception {
                Log.d(TAG, "Observer thread is :" + Thread.currentThread().getName());
                Log.d(TAG, "onNext: " + integer);
            }
        };

        Disposable disposable = observable.subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(consumer);

        //还是刚才的例子, 只不过我们添加了一点东西, 先来看看运行结果:
        // Observable thread is : RxNewThreadScheduler-2
        // emit 1
        // Observer thread is :main
        // onNext: 1

        //可以看到, 上游发送事件的线程的确改变了, 是在一个叫  RxNewThreadScheduler-2的线程中发送的事件, 而下游仍然在主线程中接收事件。

        //接下来看看是如何做到的。和上一段代码相比,这段代码只不过是增加了两行代码:
        // .subscribeOn(Schedulers.newThread())
        // .observeOn(AndroidSchedulers.mainThread())
    }

    /**
     * subscribeOn方法：
     * 指定的是上游发送事件的线程，【多次指定上游的线程只有第一次指定的有效, 也就是说多次调用subscribeOn() 只有第一次的有效, 其余的会被忽略。】
     *
     * observeOn方法：
     * 指定的是下游接收事件的线程，【多次指定下游的线程是可以的, 也就是说每调用一次observeOn() , 下游的线程就会切换一次。】
     */
    public static void test3() {
        Observable<Integer> observable = Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(ObservableEmitter<Integer> emitter) throws Exception {
                // RxNewThreadScheduler-1
                Log.i(TAG, "Observable thread is: " + Thread.currentThread().getName());
                Log.i(TAG, "emit 1");
                emitter.onNext(1);
                emitter.onComplete();
            }
        });

        Consumer<Integer> consumer = new Consumer<Integer>() {
            @Override
            public void accept(Integer integer) throws Exception {
                // RxCachedThreadScheduler-2
                Log.i(TAG, "Observer thread is: " + Thread.currentThread().getName());
                Log.i(TAG, "onNext: " + integer);
            }
        };

        Disposable disposable = observable.subscribeOn(Schedulers.newThread()) // RxNewThreadScheduler-1
                .subscribeOn(Schedulers.io()) // 忽略
                .observeOn(AndroidSchedulers.mainThread()) // main
                .doOnNext(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer integer) throws Exception {
                        //main
                        Log.i(TAG, "After observeOn(mainThread), current thread is: " + Thread.currentThread().getName());
                    }
                })
                .observeOn(Schedulers.io()) // RxCachedThreadScheduler-2
                .doOnNext(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer integer) throws Exception {
                        Log.i(TAG, "After observeOn(io), current thread is : " + Thread.currentThread().getName());

                    }
                })
                .subscribe(consumer);

        // 输出：
        // Observable thread is : RxNewThreadScheduler-1
        // emit 1
        // After observeOn(mainThread), current thread is: main
        // After observeOn(io), current thread is : RxCachedThreadScheduler-2
        // Observer thread is :RxCachedThreadScheduler-2
        // onNext: 1
    }

    /**
     *  在RxJava中, 已经内置了很多线程选项供我们选择, 例如有
     * （1）Schedulers.io() 代表io操作的线程, 通常用于网络,读写文件等io密集型的操作
     * （2）Schedulers.computation() 代表CPU计算密集型的操作, 例如需要大量计算的操作
     * （3）Schedulers.newThread() 代表一个常规的新线程
     * （4）AndroidSchedulers.mainThread()  代表Android的主线程
     * 这些内置的Scheduler已经足够满足我们开发的需求, 因此我们应该使用内置的这些选项, 在RxJava内部使用的是线程池来维护这些线程, 所有效率也比较高。
     */

    /**
     * 对于我们Android开发人员来说, 经常会将一些耗时的操作放在后台,
     * 比如网络请求或者读写文件,操作数据库等等,等到操作完成之后回到主线程去更新UI,
     * 有了上面的这些基础, 那么现在我们就可以轻松的去做到这样一些操作.
     */

    /**
     * Retrofit 结合 RxJava 实现网络请求
     */
    public static void retrofit2_RxJava2() {
//        Api api = retrofit.create(Api.class);
//        api.login(request)
//                .subscribeOn(Schedulers.io())               //在IO线程进行网络请求
//                .observeOn(AndroidSchedulers.mainThread())  //回到主线程去处理请求结果
//                .subscribe(new Observer<LoginResponse>() {
//                    @Override
//                    public void onSubscribe(Disposable d) {}
//
//                    @Override
//                    public void onNext(LoginResponse value) {}
//
//                    @Override
//                    public void onError(Throwable e) {
//                        Toast.makeText(mContext, "登录失败", Toast.LENGTH_SHORT).show();
//                    }
//
//                    @Override
//                    public void onComplete() {
//                        Toast.makeText(mContext, "登录成功", Toast.LENGTH_SHORT).show();
//                    }
//                });

        /**
         * 看似很完美, 但我们忽略了一点, 【如果在请求的过程中Activity已经退出了,
         * 这个时候如果回到主线程去更新UI, 那么APP肯定就崩溃了, 怎么办呢？】
         *
         * 上一节我们说到了Disposable，说它是个开关，
         * 调用它的dispose()方法时就会切断水管，使得下游收不到事件，既然收不到事件，那么也就不会再去更新UI了。
         * 因此我们可以在Activity中将这个Disposable 保存起来，当Activity退出时，切断它即可。
         *
         * 【如果有多个Disposable 该怎么办呢，】
         * RxJava中已经内置了一个容器CompositeDisposable，
         * 每当我们得到一个Disposable时就调用CompositeDisposable.add()将它添加到容器中，
         * 在退出的时候，调用CompositeDisposable.clear() 即可切断所有的水管。
         */
    }

    /**
     * Retrofit 结合 RxJava 读写数据库
     *
     * 读写数据库也算一个耗时的操作, 因此我们也最好放在IO线程里去进行
     */
//    public static Observable<List<Record>> read_write_sqlite() {
//        return Observable.create(new ObservableOnSubscribe<List<Record>>() {
//            @Override
//            public void subscribe(ObservableEmitter<List<Record>> emitter) throws Exception {
//                Cursor cursor = null;
//                try {
//                    cursor = getReadableDatabase().rawQuery("select * from " + TABLE_NAME, new String[]{});
//                    List<Record> result = new ArrayList<>();
//                    while (cursor.moveToNext()) {
//                        result.add(Db.Record.read(cursor));
//                    }
//                    emitter.onNext(result);
//                    emitter.onComplete();
//                } finally {
//                    if (cursor != null) {
//                        cursor.close();
//                    }
//                }
//            }
//        }).subscribeOn(Schedulers.io())
//          .observeOn(AndroidSchedulers.mainThread());
//    }
}
