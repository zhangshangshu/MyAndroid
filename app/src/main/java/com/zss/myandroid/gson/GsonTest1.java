package com.zss.myandroid.gson;

import com.google.gson.Gson;

public class GsonTest1 {

    /**
     * 生成json
     */
    public static void test1(){
        Gson gson = new Gson();
        User user = new User("怪盗kidou",24,"ikidou@example.com");
        String jsonString = gson.toJson(user);
        System.out.println(jsonString);
        // 输出：
        // {"name":"怪盗kidou","age":24,"emailAddress":"ikidou@example.com"}
    }

    /**
     * 解析json
     * {"name":"怪盗kidou","age":24}
     */
    public static void test2(){
        Gson gson = new Gson();
        String jsonString = "{\"name\":\"怪盗kidou\",\"age\":24}";
        User user = gson.fromJson(jsonString,User.class);
        System.out.print(user.toString());
        // 输出：
        // User{name='怪盗kidou', age=24, emailAddress='null'}
    }

    public static class User{

        public String name;

        public int age;

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
