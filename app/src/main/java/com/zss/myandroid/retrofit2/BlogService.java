package com.zss.myandroid.retrofit2;

import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.HTTP;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.PartMap;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;
import retrofit2.http.Url;


public interface BlogService {
    /**
     * @Path 注解
     */
    @GET("blog/get/{id}")
    Call<ResponseBody> getBlog(@Path("id") int id);

    /**
     * @HTTP 注解
     */
    @HTTP(method = "GET",path = "blog/get/{id}",hasBody = false)
    Call<ResponseBody> getBlog1(@Path("id") int id);

    /**
     * @Url 注解
     * 当GET、POST...HTTP等方法中没有设置Url时，则必须使用 {@Url}提供
     *  Url支持的类型有 okhttp3.HttpUrl, String, java.net.URI, android.net.Uri
     */
    @GET //当有URL注解时，这里的URL就省略了
    Call<ResponseBody> url(@Url String url);

    /**
     * @Query 注解
     */
    @GET("blog/get")
    Call<ResponseBody> query(@Query("username") String name,@Query("age") int age);

    /**
     * @QueryMap 注解
     *  对于Query和QueryMap，如果不是String（或Map的第二个泛型参数不是String）时会被默认会调用toString转换成String类型
     *  @QueryMap 接受的类型是Map<String,String>，非String类型会调用其toString方法。
     */
    @GET("blog/get")
    Call<ResponseBody> queryMap(@QueryMap Map<String,Object> map);

    /**
     * @Headers 注解 和 @Header 注解
     */
    @GET("blog/get")
    @Headers({"CustomHeader1:customHeaderValue1","CustomHeader2:customHeaderValue2"})
    Call<ResponseBody> header(@Header("CustomHeader3") String customHeaderValue3);

    //--------------------------表单--------------------------

    // @FormUrlEncoded 表明是一个表单格式的请求（Content-Type:application/x-www-form-urlencoded）

    // Field("username") 表示将后面的 String name 中 name的取值作为 username 的值
    @POST("blog/post")
    @FormUrlEncoded
    Call<ResponseBody> formUrlEncoded1(@Field("username") String name,@Field("age") int age);

    // Map的key作为表单的键
    // @FieldMap 接受的类型是Map<String,String>，非String类型会调用其toString方法。
    @POST("blog/post")
    @FormUrlEncoded
    Call<ResponseBody> formUrlEncoded2(@FieldMap Map<String,Object> map);

    //--------------------------文件上传--------------------------

    //@Part 后面支持三种类型 RequestBody、okhttp3.MultipartBody.Part、任意类型
    //除 okhttp3.MultipartBody.Part 以外，其它类型都必须带上表单字段。 okhttp3.MultipartBody.Part中已经包含了表单字段的信息。
    @POST("blog/post")
    @Multipart
    Call<ResponseBody> fileUpload1(@Part("username") RequestBody name,@Part("age") RequestBody age,@Part MultipartBody.Part file);

    //@PartMap 注解支持一个Map作为参数，支持 RequestBody 类型，
    //如果有其它的类型，会被 retrofit2.Converter 转换，如后面会介绍的 使用 Gson 的 retrofit2.converter.gson.GsonRequestBodyConverter
    //所以 MultipartBody.Part 就不适用了,所以文件只能用 @Part MultipartBody.Part
    // @PartMap 接受的类型是Map<String,RequestBody>，非RequestBody类型会通过Converter转换（见第三节）。
    @POST("blog/post")
    @Multipart
    Call<ResponseBody> fileUpload2(@PartMap Map<String, RequestBody> map, @Part MultipartBody.Part file);

    @POST("blog/post")
    @Multipart
    Call<ResponseBody> fileUpload3(@PartMap Map<String, RequestBody> map);

    //-----------------------------------------------------------

    /**
     * @Body 注解
     * 被@Body注解的Blog将会被 Gson 转换成 RequestBody
     */
    @POST("blog/post")
    Call<Result<Blog>> body(@Body Blog blog);

    /**
     * Gson、RxJava
     */
    @POST("blog/post")
    Observable<Result<List<Blog>>> getBlogs();

}
