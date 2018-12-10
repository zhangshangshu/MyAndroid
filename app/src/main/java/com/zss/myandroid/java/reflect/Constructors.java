package com.zss.myandroid.java.reflect;

import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Properties;

public class Constructors {

    //------------------------------------------------

    /**
     * 获取Class对象的三种方式
     * 1 Object ——> getClass();
     * 2 任何数据类型（包括基本数据类型）都有一个“静态”的class属性
     * 3 通过Class类的静态方法：forName（String  className）(常用)
     * <p>
     * 在运行期间，一个类，只有一个Class对象产生。
     * 三种方式常用第三种，第一种对象都有了还要反射干什么。
     * 第二种需要导入类的包，依赖太强，不导包就抛编译错误。
     * 一般都第三种，一个字符串可以传入也可写在配置文件中等多种方法。
     */
    public static void getClassObject() {
        //第一种方式获取Class对象
        Student stu1 = new Student();//这一new 产生一个Student对象，一个Class对象。
        Class stuClass = stu1.getClass();//获取Class对象
        System.out.println(stuClass.getName());

        //第二种方式获取Class对象
        Class stuClass2 = Student.class;
        System.out.println(stuClass == stuClass2);//判断第一种方式获取的Class对象和第二种方式获取的是否是同一个

        //第三种方式获取Class对象
        try {
            Class stuClass3 = Class.forName("com.zss.myandroid.java.reflect.Student");//注意此字符串必须是真实路径，就是带包名的类路径，包名.类名
            System.out.println(stuClass3 == stuClass2);//判断三种方式是否获取的是同一个Class对象
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    //------------------------------------------------

    /**
     * 通过反射获取构造方法并使用（可创建对象）
     * * 1.获取构造方法：
     * 1).批量的方法：
     * public Constructor[] getConstructors()：所有"公有的"构造方法
     * public Constructor[] getDeclaredConstructors()：获取所有的构造方法(包括私有、受保护、默认、公有)
     * <p>
     * 2).获取单个的方法，并调用：
     * public Constructor getConstructor(Class... parameterTypes):获取单个的"公有的"构造方法：
     * public Constructor getDeclaredConstructor(Class... parameterTypes):获取"某个构造方法"可以是私有的，或受保护、默认、公有；
     * <p>
     * 调用构造方法：
     * Constructor-->newInstance(Object... initargs)
     * 使用此 Constructor 对象表示的构造方法来创建该构造方法的声明类的新实例，并用指定的初始化参数初始化该实例。
     * 它的返回值是T类型，所以newInstance是创建了一个构造方法的声明类的新实例对象。并为之调用
     *
     * @throws Exception
     */
    public static void getConstructor() throws Exception {

        //1.加载Class对象
        Class clazz = null;

        clazz = Class.forName("com.zss.myandroid.java.reflect.Student");


        //2.获取所有公有构造方法
        System.out.println("**********************所有公有构造方法*********************************");
        Constructor[] conArray = clazz.getConstructors();
        for (Constructor c : conArray) {
            System.out.println(c);
        }


        System.out.println("************所有的构造方法(包括：私有、受保护、默认、公有)***************");
        conArray = clazz.getDeclaredConstructors();
        for (Constructor c : conArray) {
            System.out.println(c);
        }

        System.out.println("*****************获取公有、无参的构造方法*******************************");
        Constructor con = clazz.getConstructor();
        //1>、因为是无参的构造方法所以类型是一个null,不写也可以：这里需要的是一个参数的类型，切记是类型
        //2>、返回的是描述这个无参构造函数的类对象。

        System.out.println("con = " + con);
        //调用构造方法
        Object obj = con.newInstance();
        //	System.out.println("obj = " + obj);
        //	Student stu = (Student)obj;

        System.out.println("******************获取私有构造方法，并调用*******************************");
        con = clazz.getDeclaredConstructor(char.class);
        System.out.println(con);
        //调用构造方法
        con.setAccessible(true);//暴力访问(忽略掉访问修饰符)
        obj = con.newInstance('男');

        /**
         * 输出：

         ***********************所有公有构造方法*********************************
         public fanshe.Student(java.lang.String,int)
         public fanshe.Student(char)
         public fanshe.Student()
         ************所有的构造方法(包括：私有、受保护、默认、公有)***************
         private fanshe.Student(int)
         protected fanshe.Student(boolean)
         public fanshe.Student(java.lang.String,int)
         public fanshe.Student(char)
         public fanshe.Student()
         fanshe.Student(java.lang.String)
         *****************获取公有、无参的构造方法*******************************
         con = public fanshe.Student()
         调用了公有、无参构造方法执行了。。。
         ******************获取私有构造方法，并调用*******************************
         public fanshe.Student(char)
         姓名：男

         */
    }

    //------------------------------------------------

    /*
     * 获取成员变量并调用：
     *
     * 1.批量的
     * 		1).Field[] getFields():获取所有的"公有字段"
     * 		2).Field[] getDeclaredFields():获取所有字段，包括：私有、受保护、默认、公有；
     * 2.获取单个的：
     * 		1).public Field getField(String fieldName):获取某个"公有的"字段；
     * 		2).public Field getDeclaredField(String fieldName):获取某个字段(可以是私有的)
     *
     * 	 设置字段的值：
     * 		Field --> public void set(Object obj,Object value):
     * 					参数说明：
     * 					1.obj:要设置的字段所在的对象；
     * 					2.value:要为字段设置的值；
     *
     */
    public static void getField() throws Exception {
        //1.获取Class对象
        Class stuClass = Class.forName("com.zss.myandroid.java.reflect.Student");
        //2.获取字段
        System.out.println("************获取所有公有的字段********************");
        Field[] fieldArray = stuClass.getFields();
        for (Field f : fieldArray) {
            System.out.println(f);
        }
        System.out.println("************获取所有的字段(包括私有、受保护、默认的)********************");
        fieldArray = stuClass.getDeclaredFields();
        for (Field f : fieldArray) {
            System.out.println(f);
        }
        System.out.println("*************获取公有字段**并调用***********************************");
        Field f = stuClass.getField("name");
        System.out.println(f);
        //获取一个对象
        Object obj = stuClass.getConstructor().newInstance();//产生Student对象--》Student stu = new Student();
        //为字段设置值
        f.set(obj, "刘德华");//为Student对象中的name属性赋值--》stu.name = "刘德华"
        //验证
        Student stu = (Student) obj;
        System.out.println("验证姓名：" + stu.name);


        System.out.println("**************获取私有字段****并调用********************************");
        f = stuClass.getDeclaredField("phoneNum");
        System.out.println(f);
        f.setAccessible(true);//暴力反射，解除私有限定
        f.set(obj, "18888889999");
        System.out.println("验证电话：" + stu);

        /**
         * 输出：

         ************获取所有公有的字段********************
         public java.lang.String fanshe.field.Student.name
         ************获取所有的字段(包括私有、受保护、默认的)********************
         public java.lang.String fanshe.field.Student.name
         protected int fanshe.field.Student.age
         char fanshe.field.Student.sex
         private java.lang.String fanshe.field.Student.phoneNum
         *************获取公有字段**并调用***********************************
         public java.lang.String fanshe.field.Student.name
         验证姓名：刘德华
         **************获取私有字段****并调用********************************
         private java.lang.String fanshe.field.Student.phoneNum
         验证电话：Student [name=刘德华, age=0, sex=

         */

        /**
         *

         由此可见
         调用字段时：需要传递两个参数：

         Object obj = stuClass.getConstructor().newInstance();//产生Student对象--》Student stu = new Student();

         //为字段设置值
         f.set(obj, "刘德华");//为Student对象中的name属性赋值--》stu.name = "刘德华"
         第一个参数：要传入设置的对象，第二个参数：要传入实参

         */
    }

    //------------------------------------------------

    /*
     * 获取成员方法并调用：
     *
     * 1.批量的：
     * 		public Method[] getMethods():获取所有"公有方法"；（包含了父类的方法也包含Object类）
     * 		public Method[] getDeclaredMethods():获取所有的成员方法，包括私有的(不包括继承的)
     * 2.获取单个的：
     * 		public Method getMethod(String name,Class<?>... parameterTypes):
     * 					参数：
     * 					name : 方法名；
     * 					Class ... : 形参的Class类型对象
     * 		public Method getDeclaredMethod(String name,Class<?>... parameterTypes)
     *
     * 	 调用方法：
     * 		Method --> public Object invoke(Object obj,Object... args):
     * 					参数说明：
     * 					obj : 要调用方法的对象；
     * 					args:调用方式时所传递的实参；
     *
     */
    public static void getMethod() throws Exception {
        //1.获取Class对象
        Class stuClass = Class.forName("com.zss.myandroid.java.reflect.Student");
        //2.获取所有公有方法
        System.out.println("***************获取所有的”公有“方法*******************");
        stuClass.getMethods();
        Method[] methodArray = stuClass.getMethods();
        for(Method m : methodArray){
            System.out.println(m);
        }
        System.out.println("***************获取所有的方法，包括私有的*******************");
        methodArray = stuClass.getDeclaredMethods();
        for(Method m : methodArray){
            System.out.println(m);
        }
        System.out.println("***************获取公有的show1()方法*******************");
        Method m = stuClass.getMethod("show1", String.class);
        System.out.println(m);
        //实例化一个Student对象
        Object obj = stuClass.getConstructor().newInstance();
        m.invoke(obj, "刘德华");

        System.out.println("***************获取私有的show4()方法******************");
        m = stuClass.getDeclaredMethod("show4", int.class);
        System.out.println(m);
        m.setAccessible(true);//解除私有限定
        Object result = m.invoke(obj, 20);//需要两个参数，一个是要调用的对象（获取有反射），一个是实参
        System.out.println("返回值：" + result);

        /**
         * 输出：

         ***************获取所有的”公有“方法*******************
         public void fanshe.method.Student.show1(java.lang.String)
         public final void java.lang.Object.wait(long,int) throws java.lang.InterruptedException
         public final native void java.lang.Object.wait(long) throws java.lang.InterruptedException
         public final void java.lang.Object.wait() throws java.lang.InterruptedException
         public boolean java.lang.Object.equals(java.lang.Object)
         public java.lang.String java.lang.Object.toString()
         public native int java.lang.Object.hashCode()
         public final native java.lang.Class java.lang.Object.getClass()
         public final native void java.lang.Object.notify()
         public final native void java.lang.Object.notifyAll()
         ***************获取所有的方法，包括私有的*******************
         public void fanshe.method.Student.show1(java.lang.String)
         private java.lang.String fanshe.method.Student.show4(int)
         protected void fanshe.method.Student.show2()
         void fanshe.method.Student.show3()
         ***************获取公有的show1()方法*******************
         public void fanshe.method.Student.show1(java.lang.String)
         调用了：公有的，String参数的show1(): s = 刘德华
         ***************获取私有的show4()方法******************
         private java.lang.String fanshe.method.Student.show4(int)
         调用了，私有的，并且有返回值的，int参数的show4(): age = 20
         返回值：abcd

         */

        /**
         * 由此可见：

         m = stuClass.getDeclaredMethod("show4", int.class);//调用制定方法（所有包括私有的），
         需要传入两个参数，第一个是调用的方法名称，第二个是方法的形参类型，切记是类型。
         System.out.println(m);

         m.setAccessible(true);//解除私有限定
         Object result = m.invoke(obj, 20);//需要两个参数，一个是要调用的对象（获取有反射），一个是实参
         System.out.println("返回值：" + result);//

         */
    }

    //------------------------------------------------

    /**
     * 反射main方法
     * @throws Exception
     */
    public static void getMainMethod() throws Exception {

        //1、获取Student对象的字节码
        Class clazz = Class.forName("com.zss.myandroid.java.reflect.Student");

        //2、获取main方法
        Method methodMain = clazz.getMethod("main", String[].class);//第一个参数：方法名称，第二个参数：方法形参的类型，
        //3、调用main方法
        // methodMain.invoke(null, new String[]{"a","b","c"});
        //第一个参数，对象类型，因为方法是static静态的，所以为null可以，第二个参数是String数组，这里要注意在jdk1.4时是数组，jdk1.5之后是可变参数
        //这里拆的时候将  new String[]{"a","b","c"} 拆成3个对象。。。所以需要将它强转。
        methodMain.invoke(null, (Object)new String[]{"a","b","c"});//方式一
        // methodMain.invoke(null, new Object[]{new String[]{"a","b","c"}});//方式二

    }

    //------------------------------------------------

    /*
     * 我们利用反射和配置文件，可以使：应用程序更新时，对源码无需进行任何修改
     * 我们只需要将新类发送给客户端，并修改配置文件即可
     *
     * 需求：
     * 当我们升级这个系统时，不要Student类，而需要新写一个Student2的类时，这时只需要更改pro.txt的文件内容就可以了。代码就一点不用改动。
     *
     * 要替换的student2类：
     *
     *   public class Student2 {
	 *      public void show2(){
	 *      	System.out.println("is show2()");
	 *       }
     *  }
     *
     *  配置文件更改为：
     *  className = cn.fanshe.Student2
     *  methodName = show2
     */
    public static void getProperties() throws Exception {
        //  Student类
        //  public class Student {
        //  	public void show(){
        //  		System.out.println("is show()");
        //  	}
        //  }

        //通过反射获取Class对象
        Class stuClass = Class.forName(getValue("className"));//"cn.fanshe.Student"
        //2获取show()方法
        Method m = stuClass.getMethod(getValue("methodName"));//show
        //3.调用show()方法
        m.invoke(stuClass.getConstructor().newInstance());

        //控制台输出：
        //is show()
    }

    //此方法接收一个key，在配置文件中获取相应的value
    public static String getValue(String key) throws IOException {

        //pro.txt配置文件的内容：
        //className = cn.fanshe.Student
        //methodName = show

        Properties pro = new Properties();//获取配置文件的对象
        FileReader in = new FileReader("pro.txt");//获取输入流
        pro.load(in);//将流加载到配置文件对象中
        in.close();
        return pro.getProperty(key);//返回根据key获取的value值
    }

    //------------------------------------------------

    /**
     * 反射方法的其它使用之---通过反射越过泛型检查
     */
    public static void get() throws Exception {
        ArrayList<String> strList = new ArrayList<>();
        strList.add("aaa");
        strList.add("bbb");

        //	strList.add(100);
        //获取ArrayList的Class对象，反向的调用add()方法，添加数据
        Class listClass = strList.getClass(); //得到 strList 对象的字节码 对象
        //获取add()方法
        Method m = listClass.getMethod("add", Object.class);
        //调用add()方法
        m.invoke(strList, 100);

        //遍历集合
        for(Object obj : strList){
            System.out.println(obj);
        }

        //控制台输出：
        //aaa
        //bbb
        //100
    }

    //参考 https://blog.csdn.net/sinat_38259539/article/details/71799078
}
