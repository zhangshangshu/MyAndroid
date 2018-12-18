package com.zss.myandroid.rxjava2;

import android.util.Log;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.BiFunction;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * Backpressure
 */
public class RxJavaText_5 {
    private static final String TAG = "RxJavaText_5";

    /**
     * 上一节中我们说到Zip可以将多个上游发送的事件组合起来发送给下游。
     *
     * 如果其中一个水管A发送事件特别快, 而另一个水管B 发送事件特别慢,
     * 那就可能出现这种情况, 发得快的水管A 已经发送了1000个事件了, 而发的慢的水管B 才发一个出来,
     * 组合了一个之后水管A 还剩999个事件, 这些事件需要继续等待水管B 发送事件出来组合, 那么这么多的事件是放在哪里的呢?
     * 总有一个地方保存吧?  没错, Zip给我们的每一根水管都弄了一个水缸 , 用来保存这些事件
     *
     * 【zip给我们的水缸! 它将每根水管发出的事件保存起来, 等两个水缸都有事件了之后就分别从水缸中取出一个事件来组合,
     *  当其中一个水缸是空的时候就处于等待的状态.】
     *
     * 题外话: 大家来分析一下这个水缸有什么特点呢? 它是按顺序保存的, 先进来的事件先取出来, 这个特点是不是很熟悉呀?
     * 没错, 这就是我们熟知的队列, 【这个水缸在Zip内部的实现就是用的队列。】
     */

    /**
     * 【这个水缸有大小限制吗? 要是一直往里存会怎样?】
     * 我们来看个例子:
     */
    public static void test1(){
        Observable<Integer> observable1 = Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(ObservableEmitter<Integer> emitter) throws Exception {
                for (int i = 0; ; i++) {   //无限循环发事件
                    emitter.onNext(i);
                }
            }
        }).subscribeOn(Schedulers.io());

        Observable<String> observable2 = Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> emitter) throws Exception {
                emitter.onNext("A");
            }
        }).subscribeOn(Schedulers.io());

        Disposable disposable = Observable.zip(observable1, observable2, new BiFunction<Integer, String, String>() {
            @Override
            public String apply(Integer integer, String s) throws Exception {
                return integer + s;
            }
        }).observeOn(AndroidSchedulers.mainThread()).subscribe(new Consumer<String>() {
            @Override
            public void accept(String s) throws Exception {
                Log.d(TAG, s);
            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) throws Exception {
                Log.w(TAG, throwable);
            }
        });

        //在这个例子中, 我们分别创建了两根水管, 第一根水管用机器指令的执行速度来无限循环发送事件, 第二根水管随便发送点什么,
        //【由于我们没有发送Complete事件, 因此第一根水管会一直发事件到它对应的水缸里去。】

        //内存占用以斜率为1的直线迅速上涨, 几秒钟就300多M , 最终报出了OOM
    }

    /**
     * 出现这种情况肯定是我们不想看见的, 这里就可以引出我们的Backpressure了, 所谓的Backpressure其实就是为了控制流量, 水缸存储的能力毕竟有限
     *
     * 因此我们还得从源头去解决问题, 既然你发那么快, 数据量那么大, 那我就想办法不让你发那么快呗.
     *
     * 【那么这个源头到底在哪里, 究竟什么时候会出现这种情况,】【这里只是说的Zip这一个例子, 其他的地方会出现吗?】 带着这个问题我们来探究一下.
     */

    /**
     * 我们让事情变得简单一点, 【从一个单一的Observable说起.】
     *
     * 这段代码很简单, 【上游同样无限循环的发送事件, 在下游每次接收事件前延时2秒. 上下游工作在同一个线程里,】 来看下运行结果:
     */
    public static void test2() {
        Disposable disposable = Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(ObservableEmitter<Integer> emitter) throws Exception {
                for (int i = 0; ; i++) {   //无限循环发事件
                    emitter.onNext(i);
                }
            }
        }).subscribe(new Consumer<Integer>() {
            @Override
            public void accept(Integer integer) throws Exception {
                Thread.sleep(2000);
                Log.d(TAG, "" + integer);
            }
        });

        //【内存如此平静，为什么呢, 因为上下游工作在同一个线程】
        //这个时候上游每次调用 emitter.onNext(i) 其实就相当于直接调用了Consumer中的 Thread.sleep(2000);
        //所以这个时候其实就是上游每延时2秒发送一次.
    }

    /**
     * 【那我们加个线程呢】, 改成这样:
     */
    public static void test3(){
        Disposable disposable = Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(ObservableEmitter<Integer> emitter) throws Exception {
                for (int i = 0; ; i++) {    //无限循环发事件
                    emitter.onNext(i);
                }
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer integer) throws Exception {
                        Thread.sleep(2000);
                        Log.d(TAG, "" + integer);
                    }
                });

        //这个时候把【上游切换到了IO线程中去, 下游到主线程去接收, 】来看看运行结果呢:
        //给上游加了个线程之后, 它就像脱缰的野马一样, 【内存又爆掉了.】

        //【为什么不加线程和加上线程区别这么大呢, 这就涉及了同步和异步的知识了.】
        //
        //【当上下游工作在同一个线程中时, 这时候是一个同步的订阅关系,】
        //也就是说【上游每发送一个事件必须等到下游接收处理完了以后才能接着发送下一个事件.】
        //
        //【当上下游工作在不同的线程中时, 这时候是一个异步的订阅关系,】
        //这个时候【上游发送数据不需要等待下游接收,】
        //为什么呢, 因为两个线程并不能直接进行通信, 因此上游发送的事件并不能直接到下游里去,
        //这个时候就需要一个田螺姑娘来帮助它们俩, 这个田螺姑娘就是我们刚才说的水缸 !
        //上游把事件发送到水缸里去, 下游从水缸里取出事件来处理,
        //因此, 当上游发事件的速度太快, 下游取事件的速度太慢, 水缸就会迅速装满, 然后溢出来, 最后就OOM了

        //【同步和异步的区别仅仅在于是否有水缸.】
        //【源头找到了, 只要有水缸, 就会出现上下游发送事件速度不平衡的情况】
        //因此当我们以后遇到这种情况时, 仔细思考一下水缸在哪里, 找到水缸, 你就找到了解决问题的办法.
    }
}
