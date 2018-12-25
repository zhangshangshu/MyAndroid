package com.zss.myandroid.rxjava;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import com.zss.myandroid.R;

import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * RxJava 1.0 的学习
 */
public class StudyRxJava {

    private static final String TAG = "StudyRxJava";

    /**
     * Observer 即观察者，它决定事件触发的时候将有怎样的行为。
     */
    public void guanCaZhe() {
        //第一种方式
        Observer<String> observer = new Observer<String>() {
            @Override
            public void onNext(String s) {
                Log.d(TAG, "Item: " + s);
            }

            @Override
            public void onCompleted() {
                Log.d(TAG, "Completed!");
            }

            @Override
            public void onError(Throwable e) {
                Log.d(TAG, "Error!");
            }
        };

        /**
         * 第二种方式
         *
         * Subscriber：一个实现了 Observer 的抽象类
         * Subscriber 对 Observer 接口进行了一些扩展，但他们的基本使用方式是完全一样的。
         *
         * 它们的区别对于使用者来说主要有两点：
         * （1）onStart(): 这是 Subscriber 增加的方法。
         *                它会在 subscribe 刚开始，而事件还未发送之前被调用，可以用于做一些准备工作，
         *                例如数据的清零或重置。这是一个可选方法，默认情况下它的实现为空。需要注意的是，
         *                如果对准备工作的线程有要求（例如弹出一个显示进度的对话框，这必须在主线程执行），
         *                onStart() 就不适用了，因为它总是在 subscribe 所发生的线程被调用，而不能指定线程。
         *               要在指定的线程来做准备工作，可以使用 doOnSubscribe() 方法，具体可以在后面的文中看到。
         * （2）unsubscribe(): 这是 Subscriber 所实现的另一个接口 Subscription 的方法，用于取消订阅。
         *                    在这个方法被调用后，Subscriber 将不再接收事件。一般在这个方法调用前，可以使用 isUnsubscribed() 先判断一下状态。
         *                    unsubscribe() 这个方法很重要，因为在 subscribe() 之后， Observable 会持有 Subscriber 的引用，这个引用如果不能及时被释放，将有内存泄露的风险。
         *                    所以最好保持一个原则：要在不再使用的时候尽快在合适的地方（例如 onPause() onStop() 等方法中）调用 unsubscribe() 来解除引用关系，以避免内存泄露的发生。
         */
        Subscriber<String> subscriber = new Subscriber<String>() {
            @Override
            public void onNext(String s) {
                Log.d(TAG, "Item: " + s);
            }

            @Override
            public void onCompleted() {
                Log.d(TAG, "Completed!");
            }

            @Override
            public void onError(Throwable e) {
                Log.d(TAG, "Error!");
            }
        };
    }


    /**
     * Observable 即被观察者，它决定什么时候触发事件以及触发怎样的事件。
     */
    public void beiGuanCaZhe() {
        /**
         * create() 方法是 RxJava 最基本的创造事件序列的方法。
         * 使用 create() 方法来创建一个 Observable ，并为它定义事件触发规则。
         *
         * 这里传入了一个 OnSubscribe 对象作为参数。OnSubscribe 会被存储在返回的 Observable 对象中，它的作用相当于一个计划表，
         * 当 Observable 被订阅的时候，OnSubscribe 的 call() 方法会自动被调用，
         * 事件序列就会依照设定依次触发（对于上面的代码，就是观察者Subscriber 将会被调用三次 onNext() 和一次 onCompleted()）。
         * 这样，由被观察者调用了观察者的回调方法，就实现了由被观察者向观察者的事件传递，即观察者模式。
         */
        Observable observable1 = Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                subscriber.onNext("Hello");
                subscriber.onNext("Hi");
                subscriber.onNext("Aloha");
                subscriber.onCompleted();
            }
        });

        /**
         * RxJava 还提供了一些方法用来快捷创建事件队列 如 just(T...) 和 from(T[]) / from(Iterable<? extends T>)
         *
         * just(T...): 将传入的参数依次发送出来
         */
        Observable observable2 = Observable.just("Hello", "Hi", "Aloha");

        /**
         * from(T[]) / from(Iterable<? extends T>) : 将传入的数组或 Iterable 拆分成具体对象后，依次发送出来。
         */
        String[] words = {"Hello", "Hi", "Aloha"};
        Observable observable3 = Observable.from(words);
    }

    public void buWanZhengDingYiHuiDiao() {
        /**
         * 除了 subscribe(Observer) 和 subscribe(Subscriber) ，subscribe() 还支持不完整定义的回调
         */
        Action1<String> onNextAction = new Action1<String>() {
            // onNext()
            @Override
            public void call(String s) {
                Log.d(TAG, s);
            }
        };
        Action1<Throwable> onErrorAction = new Action1<Throwable>() {
            // onError()
            @Override
            public void call(Throwable throwable) {
                // Error handling
            }
        };
        Action0 onCompletedAction = new Action0() {
            // onCompleted()
            @Override
            public void call() {
                Log.d(TAG, "completed");
            }
        };

        // 自动创建 Subscriber ，并使用 onNextAction 来定义 onNext()
        //observable.subscribe(onNextAction);

        // 自动创建 Subscriber ，并使用 onNextAction 和 onErrorAction 来定义 onNext() 和 onError()
        //observable.subscribe(onNextAction, onErrorAction);

        // 自动创建 Subscriber ，并使用 onNextAction、 onErrorAction 和 onCompletedAction 来定义 onNext()、 onError() 和 onCompleted()
        //observable.subscribe(onNextAction, onErrorAction, onCompletedAction);
    }

    public void test() {
        Observable o = Observable.create(new Observable.OnSubscribe<String>() {

            @Override
            public void call(Subscriber<? super String> subscriber) {
                subscriber.onNext("first");
                subscriber.onNext("two");
                subscriber.onNext("three");
                subscriber.onCompleted();
            }
        });
        Observer<String> observer = new Observer<String>() {
            @Override
            public void onCompleted() {
                Log.i(TAG, "onCompleted: ");
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(String s) {
                Log.i(TAG, "onNext: " + s);
            }
        };
        o.subscribe(observer);
    }

    /**
     * 打印字符串数组
     * 将字符串数组 names 中的所有字符串依次打印出来：
     */
    public void testA() {
        String[] names = {"a", "b", "c"};
        Observable.from(names).subscribe(new Action1<String>() {
            @Override
            public void call(String s) {
                Log.i(TAG, "call: " + s);
            }
        });
    }

    /**
     * 由 id 取得图片并显示
     * 由指定的一个 drawable 文件 id drawableRes 取得图片，并显示在 ImageView 中，并在出现异常的时候打印 Toast 报错：
     */
    private Context context;

    public void testB() {
        final int drawableRes = R.mipmap.ic_launcher;
        final ImageView imageView = new ImageView(context);
        Observable.create(new Observable.OnSubscribe<Drawable>() {
            @Override
            public void call(Subscriber<? super Drawable> subscriber) {
                Drawable drawable = context.getTheme().getDrawable(drawableRes);
                subscriber.onNext(drawable);
                subscriber.onCompleted();
            }
        }).subscribe(new Observer<Drawable>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                Toast.makeText(context, "Error!", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNext(Drawable drawable) {
                imageView.setImageDrawable(drawable);
            }
        });
    }

    //3.线程控制 Scheduler (一)

    /**
     * 由于 subscribeOn(Schedulers.io()) 的指定，被创建的事件的内容 1、2、3、4 将会在 IO 线程发出；
     * 而由于 observeOn(AndroidScheculers.mainThread()) 的指定，因此 subscriber 数字的打印将发生在主线程。
     * 事实上，这种在 subscribe() 之前写上两句 subscribeOn(Scheduler.io()) 和 observeOn(AndroidSchedulers.mainThread()) 的使用方式非常常见，
     * 它适用于多数的 『后台线程取数据，主线程显示』的程序策略。
     */
    public void testScheduler1() {
        Observable.just(1, 2, 3, 4)
                .subscribeOn(Schedulers.io())// 指定 subscribe() 发生在 IO 线程
                .observeOn(AndroidSchedulers.mainThread())// 指定 Subscriber 的回调发生在主线程
                .subscribe(new Action1<Integer>() {
                    @Override
                    public void call(Integer integer) {
                        Log.i(TAG, "number:" + integer);
                    }
                });
    }

    /**
     * 而前面提到的由图片 id 取得图片并显示的例子，如果也加上这两句：
     * 那么，加载图片将会发生在 IO 线程，而设置图片则被设定在了主线程。
     * 这就意味着，即使加载图片耗费了几十甚至几百毫秒的时间，也不会造成丝毫界面的卡顿。
     */
    public void testScheduler2() {
        final int drawableRes = R.mipmap.ic_launcher;
        final ImageView imageView = new ImageView(context);
        Observable.create(new Observable.OnSubscribe<Drawable>() {
            @Override
            public void call(Subscriber<? super Drawable> subscriber) {
                Drawable drawable = null;
                drawable = context.getTheme().getDrawable(drawableRes);
                subscriber.onNext(drawable);
                subscriber.onCompleted();
            }
        })
                .subscribeOn(Schedulers.io()) // 指定 subscribe() 发生在 IO 线程
                .observeOn(AndroidSchedulers.mainThread()) // 指定 Subscriber 的回调发生在主线程
                .subscribe(new Observer<Drawable>() {
                    @Override
                    public void onNext(Drawable drawable) {
                        imageView.setImageDrawable(drawable);
                    }

                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        Toast.makeText(context, "Error!", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    //4.变换
    public void testMap() {
        final ImageView imageView = new ImageView(context);
        Observable.just("images/logo.png") // 输入类型 String
                .map(new Func1<String, Bitmap>() {
                    @Override
                    public Bitmap call(String filePath) { // 参数类型 String
                        Bitmap bitmap = BitmapFactory.decodeFile(filePath);
                        return bitmap; // 返回类型 Bitmap
                    }
                })
                .subscribe(new Action1<Bitmap>() {
                    @Override
                    public void call(Bitmap bitmap) { // 参数类型 Bitmap
                        imageView.setImageBitmap(bitmap);
                    }
                });
    }

    public void testFlatMap() {

    }
}
