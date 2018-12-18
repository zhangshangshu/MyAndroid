package com.zss.myandroid.retrofit2;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observable;
import rx.Subscriber;
import rx.schedulers.Schedulers;

public class RetrofitTest {

    String baseUrl = "http://www.baidu.com/";
    public static final int RETROFIT_BASE_USE = 0;
    public static final int RETROFIT_GSON_CONVERTER = 1;
    public static final int RETROFIT_RXJAVA_CONVERTER = 2;
    public static final int RETROFIT_GSON_RXJAVA = 3;

    private Retrofit getRetrofit1(){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .build();
        return retrofit;
    }

    /**
     *
     * 在默认情况下Retrofit只支持将HTTP的响应体转换换为ResponseBody,
     * 这也是为什么我在前面的例子接口的返回值都是 Call<ResponseBody>
     *
     * 但如果响应体只是支持转换为ResponseBody的话何必要引入泛型呢，
     * 返回值直接用一个Call就行了嘛，既然支持泛型，那说明泛型参数可以是其它类型的，
     *
     * 而Converter就是Retrofit为我们提供用于将ResponseBody转换为我们想要的类型
     *
     * @return
     */
    private Retrofit getRetrofit2(){
        Gson gson = new GsonBuilder()
                //配置你的Gson
                .setDateFormat("yyyy-MM-dd hh:mm:ss")
                .create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create(gson)) //这样Retrofit就会使用 Gson 将ResponseBody转换我们想要的类型。
                .build();

        return retrofit;
    }

    private Retrofit getRetrofit3(){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();

        return retrofit;
    }

    private Retrofit getRetrofit4(){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();

        return retrofit;
    }

    private BlogService getBlogService(int type){
        Retrofit retrofit = null;
        switch (type){
            case RETROFIT_BASE_USE:
                retrofit = getRetrofit1();
                break;
            case RETROFIT_GSON_CONVERTER:
                retrofit = getRetrofit2();
                break;
            case RETROFIT_RXJAVA_CONVERTER:
                retrofit = getRetrofit3();
                break;
            case RETROFIT_GSON_RXJAVA:
                retrofit = getRetrofit4();
                break;
        }
        if(retrofit==null)
            return null;
        return retrofit.create(BlogService.class);
    }

    public void getBlog(int id){
        Call<ResponseBody> call = getBlogService(RETROFIT_BASE_USE).getBlog(id);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                ResponseBody responseBody = response.body();
                try {
                    String responseBodyString = responseBody.string();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });
    }

    public void formUrlEncoded1(){
        String name = "zss";
        int age = 20;
        Call<ResponseBody> call = getBlogService(RETROFIT_BASE_USE).formUrlEncoded1(name,age);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });
    }

    public void formUrlEncoded2(){
        Map<String,Object> map = new HashMap<>();
        map.put("username","zss");
        map.put("age",20);
        Call<ResponseBody> call = getBlogService(RETROFIT_BASE_USE).formUrlEncoded2(map);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });
    }

    public void fileUpload1(){
        RequestBody name = RequestBody.create(MediaType.parse("text/plain"),"zss");
        RequestBody age = RequestBody.create(MediaType.parse("text/plain"),"20");

        RequestBody file = RequestBody.create(MediaType.parse("application/octet-stream"),"文件的内容");
        MultipartBody.Part filePart = MultipartBody.Part.createFormData("file","test.txt",file);

        Call<ResponseBody> call = getBlogService(RETROFIT_BASE_USE).fileUpload1(name,age,filePart);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });
    }

    public void fileUpload2(){
        Map<String,RequestBody> map = new HashMap<>();
        RequestBody name = RequestBody.create(MediaType.parse("text/plain"),"zss");
        RequestBody age = RequestBody.create(MediaType.parse("text/plain"),"20");
        RequestBody file = RequestBody.create(MediaType.parse("application/octet-stream"),"文件的内容");
        map.put("username",name);
        map.put("age",age);
//        map.put("file",file);//这里并不会被当成文件，因为没有文件名(包含在Content-Disposition请求头中)，但 filePart 有
        MultipartBody.Part filePart = MultipartBody.Part.createFormData("file","text.txt",file);
        Call<ResponseBody> call = getBlogService(RETROFIT_BASE_USE).fileUpload2(map,filePart);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });
    }

    public void fileUpload3(){
        Map<String,RequestBody> map = new HashMap<>();
        RequestBody name = RequestBody.create(MediaType.parse("text/plain"),"zss");
        RequestBody age = RequestBody.create(MediaType.parse("text/plain"),"20");
        RequestBody file = RequestBody.create(MediaType.parse("application/octet-stream"),"文件的内容");
        map.put("username",name);
        map.put("age",age);
        map.put("file\";filename=\"test.txt",file);
        Call<ResponseBody> call = getBlogService(RETROFIT_BASE_USE).fileUpload3(map);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });
    }

    public void body(){
        Blog blog = new Blog();
        blog.content = "新建的Blog";
        blog.title = "测试";
        blog.author = "怪盗kidou";
        Call<Result<Blog>> call = getBlogService(RETROFIT_GSON_CONVERTER).body(blog);
        call.enqueue(new Callback<Result<Blog>>() {
            @Override
            public void onResponse(Call<Result<Blog>> call, Response<Result<Blog>> response) {
                Result<Blog> result = response.body();
                Blog blog = result.data;
            }

            @Override
            public void onFailure(Call<Result<Blog>> call, Throwable t) {

            }
        });
    }

    public void getBlogs(){
        Observable<Result<List<Blog>>> observable = getBlogService(RETROFIT_GSON_RXJAVA).getBlogs();
        observable.subscribeOn(Schedulers.io())
                .subscribe(new Subscriber<Result<List<Blog>>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(Result<List<Blog>> listResult) {

            }
        });
    }

}
