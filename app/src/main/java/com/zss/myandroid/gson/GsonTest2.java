package com.zss.myandroid.gson;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;

public class GsonTest2 {


    /**
     * （1）Java对象生成json字符串
     * （2）@SerializedName的使用
     */
    public static void test2(){
        Gson gson = new Gson();
        User user = new User("怪盗kidou",24,"ikidou@example.com");
        String json = gson.toJson(user);
        System.out.println(json);
        // 输出：
        // {"Name":"怪盗kidou","Age":24,"email_address":"ikidou@example.com"}
    }

    /**
     * 无法正确解析 json（原因：json字符串的字段和Java类使用@SerializedName标记的字段不一致）
     *
     * json字符串：{"name":"怪盗kidou","age":24,"emailAddress":"ikidou_1@example.com"}
     * 其中的字段name、age、emailAddress和 User类中 @SerializedName 标记的字段不一致，所以解析时无法赋值。
     * 因此，String类型的默认值为null，int类型的默认值为0。
     */
    public static void test3(){
        Gson gson = new Gson();
        String json = "{\"name\":\"怪盗kidou\",\"age\":24,\"emailAddress\":\"ikidou_1@example.com\"}";
        User user = gson.fromJson(json, User.class);//解析json字符串
        System.out.println(user.toString());
        // 输出：
        // User{name='null', age=0, emailAddress='null'}
    }

    /**
     * 正确解析 json
     */
    public static void test4(){
        Gson gson = new Gson();
        String json = "{\"Name\":\"怪盗kidou\",\"Age\":24,\"email_address\":\"ikidou_1@example.com\"}";
        User user = gson.fromJson(json, User.class);//解析json字符串
        System.out.println(user.toString());
        // 输出：
        // User{name='怪盗kidou', age=24, emailAddress='ikidou_1@example.com'}
    }

    public static class User{

        @SerializedName("Name")
        public String name;

        @SerializedName("Age")
        public int age;

        @SerializedName("email_address")
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

        @Override
        public String toString() {
            return "User{" +
                    "name='" + name + '\'' +
                    ", age=" + age +
                    ", emailAddress='" + emailAddress + '\'' +
                    '}';
        }
    }
}
