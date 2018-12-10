package com.zss.myandroid.gson;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;

public class GsonTest3 {
    /**
     * 解析json
     *
     * json字符串：{"name":"怪盗kidou","age":24,"emailAddress":"ikidou_1@example.com","email":"ikidou_2@example.com","email_address":"ikidou_3@example.com"}
     *
     * 当json字符串中同时包含了 emailAddress、email、email_address时，以最后出现的那个为准，即 "email_address":"ikidou_3@example.com"
     */
    public static void test1(){
        Gson gson = new Gson();
        String json = "{\"name\":\"怪盗kidou\",\"age\":24,\"emailAddress\":\"ikidou_1@example.com\",\"email\":\"ikidou_2@example.com\",\"email_address\":\"ikidou_3@example.com\"}";
        User user = gson.fromJson(json, User.class);//解析json字符串
        System.out.println(user.emailAddress);
        // 输出：
        // ikidou_3@example.com
    }

    /**
     * 解析json
     *
     * json字符串：{"name":"怪盗kidou","age":24,"emailAddress":"ikidou_1@example.com","email_address":"ikidou_3@example.com","email":"ikidou_2@example.com"}
     *
     * 当json字符串中同时包含了 emailAddress、email、email_address时，以最后出现的那个为准，即 "email":"ikidou_2@example.com"
     */
    public static void test2(){
        Gson gson = new Gson();
        String json = "{\"name\":\"怪盗kidou\",\"age\":24,\"email_address\":\"ikidou_3@example.com\",\"emailAddress\":\"ikidou_1@example.com\",\"email\":\"ikidou_2@example.com\"}";
        User user = gson.fromJson(json, User.class);//解析json字符串
        System.out.println(user.emailAddress);
        // 输出：
        // ikidou_2@example.com
    }

    /**
     * 解析json
     *
     * json字符串：{"name":"怪盗kidou","age":24,"email":"ikidou_2@example.com","email_address":"ikidou_3@example.com","emailAddress":"ikidou_1@example.com"}
     *
     * 当json字符串中同时包含了 emailAddress、email、email_address时，以最后出现的那个为准即。
     * 这里最后的是 "emailAddress":"ikidou_1@example.com" ，输出的不是 ikidou_1@example.com ，而是 ikidou_3@example.com 。
     * 因为 json字符串中 emailAddress 字段不匹配 User类中的 emailAddr字段
     */
    public static void test3(){
        Gson gson = new Gson();
        String json = "{\"name\":\"怪盗kidou\",\"age\":24,\"email\":\"ikidou_2@example.com\",\"email_address\":\"ikidou_3@example.com\",\"emailAddress\":\"ikidou_1@example.com\"}";
        User user = gson.fromJson(json, User.class);//解析json字符串
        System.out.println(user.emailAddress);
        // 输出：
        // ikidou_3@example.com
    }

    /**
     * 解析json
     *
     * json字符串：{"name":"怪盗kidou","age":24,"email":"ikidou_2@example.com","email_address":"ikidou_3@example.com","emailAddr":"ikidou_1@example.com"}
     *
     * 当json字符串中同时包含了 emailAddress、email、email_address时，以最后出现的那个为准即 即 "email":"ikidou_1@example.com"
     */
    public static void test4(){
        Gson gson = new Gson();
        String json = "{\"name\":\"怪盗kidou\",\"age\":24,\"email\":\"ikidou_2@example.com\",\"email_address\":\"ikidou_3@example.com\",\"emailAddr\":\"ikidou_1@example.com\"}";
        User user = gson.fromJson(json, User.class);//解析json字符串
        System.out.println(user.emailAddress);
        // 输出：
        // ikidou_1@example.com
    }

    /**
     * 生成json
     *
     * 从输出结果可以看出，@SerializedName标记
     */
    public static void test5(){
        Gson gson = new Gson();
        User user = new User("怪盗kidou",24,"ikidou@example.com");
        String jsonString = gson.toJson(user);
        System.out.println(jsonString);
        // 输出：
        // {"Name":"怪盗kidou","Age":24,"emailAddr":"ikidou@example.com"}
    }

    public static class User{
        @SerializedName("Name")
        public String name;

        @SerializedName("Age")
        public int age;

        @SerializedName( value = "emailAddr" , alternate = {"email","email_address"} )
        public String emailAddress;

        public User(String name, int age, String emailAddress) {
            this.name = name;
            this.age = age;
            this.emailAddress = emailAddress;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getAge() {
            return age;
        }

        public void setAge(int age) {
            this.age = age;
        }

        public String getEmailAddress() {
            return emailAddress;
        }

        public void setEmailAddress(String emailAddress) {
            this.emailAddress = emailAddress;
        }
    }
}
