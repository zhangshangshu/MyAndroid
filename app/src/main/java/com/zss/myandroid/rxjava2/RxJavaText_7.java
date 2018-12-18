package com.zss.myandroid.rxjava2;

import android.util.Log;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.FlowableEmitter;
import io.reactivex.FlowableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class RxJavaText_7 {
    private static final String TAG = "RxJavaText_7";
    /**
     * 上一节里我们学习了只使用Observable如何去解决上下游流不均衡的问题，
     * 之所以学习这个是因为Observable还是有很多它使用的场景。
     *
     * 【有些朋友自从听说了Flowable之后就觉得Flowable能解决任何问题,
     * 甚至有抛弃Observable这种想法, 这是万万不可的, 它们都有各自的优势和不足.】
     *
     * 在这一节里我们先来学习如何使用Flowable, 它东西比较多, 也比较繁琐
     */

    /**
     * 之前我们说的【上游和下游分别是Observable和Observer,】
     * 这次不一样的是【上游变成了Flowable, 下游变成了Subscriber,】
     * 但是水管之间的连接还是通过subscribe(), 我们来看看最基本的用法吧:
     */
    public static void test1(){
        Flowable<Integer> upstream = Flowable.create(new FlowableOnSubscribe<Integer>() {
            @Override
            public void subscribe(FlowableEmitter<Integer> emitter) throws Exception {
                Log.d(TAG, "emit 1");
                emitter.onNext(1);
                Log.d(TAG, "emit 2");
                emitter.onNext(2);
                Log.d(TAG, "emit 3");
                emitter.onNext(3);
                Log.d(TAG, "emit complete");
                emitter.onComplete();
            }
        }, BackpressureStrategy.ERROR);//增加了一个参数

        Subscriber<Integer> downstream = new Subscriber<Integer>() {
            @Override
            public void onSubscribe(Subscription s) {
                Log.i(TAG, "onSubscribe");
                s.request(Long.MAX_VALUE);//注意这句代码
            }

            @Override
            public void onNext(Integer integer) {
                Log.i(TAG, "onNext: ");
            }

            @Override
            public void onError(Throwable t) {
                Log.i(TAG, "onError: ");
            }

            @Override
            public void onComplete() {
                Log.i(TAG, "onComplete: ");
            }
        };

        upstream.subscribe(downstream);

        //这段代码中,【分别创建了一个上游Flowable和下游Subscriber, 上下游工作在同一个线程中,】
        //和之前的Observable的使用方式只有一点点的区别, 先来看看运行结果吧:
        // onSubscribe
        // emit 1
        // onNext: 1
        // emit 2
        // onNext: 2
        // emit 3
        // onNext: 3
        // emit complete
        // onComplete
        //结果也和我们预期的是一样的.
    }

    /**
     * 我们注意到【这次和Observable有些不同.】
     *
     * 首先是【创建Flowable的时候增加了一个参数, 这个参数是用来选择背压,也就是出现上下游流速不均衡的时候应该怎么处理的办法,】
     * 这里我们直接用【BackpressureStrategy.ERROR】这种方式,
     * 这种方式会在【出现上下游流速不均衡的时候直接抛出一个异常,】
     * 这个异常就是著名的【MissingBackpressureException.】
     * 其余的策略后面再来讲解.
     *
     * 另外的一个区别是在【下游的onSubscribe方法中传给我们的不再是Disposable了, 而是Subscription,】
     * 它俩有什么区别呢,
     * 首先【它们都是上下游中间的一个开关, 之前我们说调用Disposable.dispose()方法可以切断水管,】
     * 同样的【调用Subscription.cancel()也可以切断水管,】
     * 【不同的地方在于Subscription增加了一个void request(long n)方法,】 这个方法有什么用呢, 在上面的代码中也有这么一句代码:
     *  s.request(Long.MAX_VALUE);
     */

    /**
     * s.request(Long.MAX_VALUE);
     * 这句代码有什么用呢, 不要它可以吗? 我们来试试:
     */
    public static void test2(){
        Flowable.create(new FlowableOnSubscribe<Integer>() {
            @Override
            public void subscribe(FlowableEmitter<Integer> emitter) throws Exception {
                Log.d(TAG, "emit 1");
                emitter.onNext(1);
                Log.d(TAG, "emit 2");
                emitter.onNext(2);
                Log.d(TAG, "emit 3");
                emitter.onNext(3);
                Log.d(TAG, "emit complete");
                emitter.onComplete();
            }
        }, BackpressureStrategy.ERROR).subscribe(new Subscriber<Integer>() {

            @Override
            public void onSubscribe(Subscription s) {
                Log.d(TAG, "onSubscribe");
            }

            @Override
            public void onNext(Integer integer) {
                Log.d(TAG, "onNext: " + integer);

            }

            @Override
            public void onError(Throwable t) {
                Log.w(TAG, "onError: ", t);
            }

            @Override
            public void onComplete() {
                Log.d(TAG, "onComplete");
            }
        });

        //这次我们【取消掉了request这句代码,】 来看看运行结果:
        // onSubscribe
        // emit 1
        // onError:io.reactivex.exceptions.MissingBackpressureException: create: could not emit value due to lack of requests

        //从运行结果中可以看到,【 在上游发送第一个事件之后, 下游就抛出了一个著名的MissingBackpressureException异常, 并且下游没有收到任何其余的事件】
        //可是【这是一个同步的订阅呀, 上下游工作在同一个线程,】
        //上游每发送一个事件应该会等待下游处理完了才会继续发事件啊,
        //不可能出现上下游流速不均衡的问题呀.
    }

    /**
     * 带着这个疑问, 我们再来看看异步的情况:
     */
    private Subscription mSubscription;
    public void test3(){
        Flowable.create(new FlowableOnSubscribe<Integer>() {
            @Override
            public void subscribe(FlowableEmitter<Integer> emitter) throws Exception {
                Log.d(TAG, "emit 1");
                emitter.onNext(1);
                Log.d(TAG, "emit 2");
                emitter.onNext(2);
                Log.d(TAG, "emit 3");
                emitter.onNext(3);
                Log.d(TAG, "emit complete");
                emitter.onComplete();
            }
        }, BackpressureStrategy.ERROR).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Integer>() {

                    @Override
                    public void onSubscribe(Subscription s) {
                        Log.d(TAG, "onSubscribe");
                        mSubscription = s;
                    }

                    @Override
                    public void onNext(Integer integer) {
                        Log.d(TAG, "onNext: " + integer);
                    }

                    @Override
                    public void onError(Throwable t) {
                        Log.w(TAG, "onError: ", t);
                    }

                    @Override
                    public void onComplete() {
                        Log.d(TAG, "onComplete");
                    }
                });

        //这次我们同样【去掉了request这句代码, 但是让上下游工作在不同的线程,】 来看看运行结果:
        // onSubscribe
        // emit 1
        // emit 2
        // emit 3
        // emit complete

        //【这次上游正确的发送了所有的事件, 但是下游一个事件也没有收到,】 这是因为什么呢?

        //这是因为【Flowable在设计的时候采用了一种新的思路也就是响应式拉取的方式来更好的解决上下游流速不均衡的问题,】
        // 与我们之前所讲的控制数量和控制速度不太一样,
        // 这种方式用通俗易懂的话来说就好比是叶问打鬼子, 我们把上游看成小日本, 把下游当作叶问,
        // 当调用Subscription.request(1)时, 叶问就说我要打一个! 然后小日本就拿出一个鬼子给叶问, 让他打,
        // 等叶问打死这个鬼子之后, 再次调用request(10), 叶问就又说我要打十个!  然后小日本又派出十个鬼子给叶问,
        // 然后就在边上看热闹, 看叶问能不能打死十个鬼子, 等叶问打死十个鬼子后再继续要鬼子接着打...

        //所以我们【把request当做是一种能力, 当成下游处理事件的能力, 下游能处理几个就告诉上游我要几个,】
        // 【这样只要上游根据下游的处理能力来决定发送多少事件, 就不会造成一窝蜂的发出一堆事件来, 从而导致OOM.】
        // 【这也就完美的解决之前我们所学到的两种方式的缺陷, 过滤事件会导致事件丢失, 减速又可能导致性能损失.】
        // 而这种方式既解决了事件丢失的问题, 又解决了速度的问题, 完美 !

        //但是太完美的东西也就意味着陷阱也会很多, 你可能只是被它的外表所迷惑, 失去了理智, 如果你滥用或者不遵守规则, 一样会吃到苦头.

        //比如这里需要注意的是, 【只有当上游正确的实现了如何根据下游的处理能力来发送事件的时候, 才能达到这种效果,】
        // 如果上游根本不管下游的处理能力, 一股脑的瞎他妈发事件, 仍然会产生上下游流速不均衡的问题,
        // 这就好比小日本管他叶问要打几个, 老子直接拿出1万个鬼子, 这尼玛有种打死给我看看?
        // 【那么如何正确的去实现上游呢,】 这里先卖个关子, 之后我们再来讲解.
    }

    /**
     * 学习了request, 我们就可以解释上面的两段代码了.
     *
     * 首先第一个同步的代码, 为什么上游发送第一个事件后下游就抛出了MissingBackpressureException异常,
     * 这是【因为下游没有调用request, 上游就认为下游没有处理事件的能力,】
     * 而【这又是一个同步的订阅, 既然下游处理不了, 那上游不可能一直等待吧,
     * 如果是这样, 万一这两根水管工作在主线程里, 界面不就卡死了吗, 因此只能抛个异常来提醒我们.】
     * 那如何解决这种情况呢,
     * 很简单啦, 下游直接调用request(Long.MAX_VALUE)就行了,
     * 或者根据上游发送事件的数量来request就行了, 比如这里request(3)就可以了.
     *
     * 然后我们再来看看第二段代码, 【为什么上下游没有工作在同一个线程时, 上游却正确的发送了所有的事件呢?】
     * 这是因为在【Flowable里默认有一个大小为128的水缸,】 【当上下游工作在不同的线程中时, 上游就会先把事件发送到这个水缸中,】
     * 因此, 下游虽然没有调用request, 但是上游在水缸中保存着这些事件, 【只有当下游调用request时, 才从水缸里取出事件发给下游.】
     *
     * 是不是这样呢, 我们来验证一下:
     */
    public void request(long n) {
        mSubscription.request(n); //在外部调用request请求上游
    }
    public void test4(){
        Flowable.create(new FlowableOnSubscribe<Integer>() {
            @Override
            public void subscribe(FlowableEmitter<Integer> emitter) throws Exception {
                Log.d(TAG, "emit 1");
                emitter.onNext(1);
                Log.d(TAG, "emit 2");
                emitter.onNext(2);
                Log.d(TAG, "emit 3");
                emitter.onNext(3);
                Log.d(TAG, "emit complete");
                emitter.onComplete();
            }
        }, BackpressureStrategy.ERROR).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Integer>() {

                    @Override
                    public void onSubscribe(Subscription s) {
                        Log.d(TAG, "onSubscribe");
                        mSubscription = s;  //把Subscription保存起来
                    }

                    @Override
                    public void onNext(Integer integer) {
                        Log.d(TAG, "onNext: " + integer);
                    }

                    @Override
                    public void onError(Throwable t) {
                        Log.w(TAG, "onError: ", t);
                    }

                    @Override
                    public void onComplete() {
                        Log.d(TAG, "onComplete");
                    }
                });

        //这里我们【把Subscription保存起来, 在界面上增加了一个按钮, 点击一次就调用Subscription.request(1),】 来看看运行结果:
        //结果似乎像那么回事, 【上游发送了四个事件保存到了水缸里,】 【下游每request一个, 就接收一个进行处理.】
    }

    /**
     * 刚刚我们有说到【水缸的大小为128,】 有朋友就问了, 你说128就128吗, 又不是唯品会周年庆, 我不信. 那就来【验证】一下:
     */
    public void test5(){
        Flowable.create(new FlowableOnSubscribe<Integer>() {
            @Override
            public void subscribe(FlowableEmitter<Integer> emitter) throws Exception {
                for (int i = 0; i < 128; i++) {
                    Log.d(TAG, "emit " + i);
                    emitter.onNext(i);
                }
            }
        }, BackpressureStrategy.ERROR).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Integer>() {

                    @Override
                    public void onSubscribe(Subscription s) {
                        Log.d(TAG, "onSubscribe");
                        mSubscription = s;
                    }

                    @Override
                    public void onNext(Integer integer) {
                        Log.d(TAG, "onNext: " + integer);
                    }

                    @Override
                    public void onError(Throwable t) {
                        Log.w(TAG, "onError: ", t);
                    }

                    @Override
                    public void onComplete() {
                        Log.d(TAG, "onComplete");
                    }
                });

        //这里我们让【上游一次性发送了128个事件, 下游一个也不接收, 】来看看运行结果:
        // onSubscribe
        // emit 0
        //  ...
        // emit 126
        // emit 127

        //这段代码的运行结果很正常, 没有任何错误和异常, 【上游仅仅是发送了128个事件.】

        //那来试试129个呢, 【把上面代码中的128改成129试试:】
        // onSubscribe
        // emit 0
        //  ...
        // emit 126
        // emit 127
        // emit 128  //这是第129个事件
        // onError:io.reactivex.exceptions.MissingBackpressureException: create: could not emit value due to lack of requests

        //这次可以看到, 【在上游发送了第129个事件的时候, 就抛出了MissingBackpressureException异常, 】提醒我们发洪水啦

        //当然了, 【这个128也不是我凭空捏造出来的, Flowable的源码中就有这个buffersize的大小定义】

        //注意这里我们是【把上游发送的事件全部都存进了水缸里, 下游一个也没有消费, 所以就溢出了,】
        // 【如果下游去消费了事件, 可能就不会导致水缸溢出来了】
    }

    // https://www.jianshu.com/u/c50b715ccaeb
}
