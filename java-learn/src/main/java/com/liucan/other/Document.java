package com.liucan.other;

/**
 * other学习文档
 * @author liucan
 * @version 2018/9/22
 */
public class Document {
    /**
     * 1.接口和抽象类区别
     *   a.抽象类，除了有抽象方法外，还可以提供默认的实现,提供属性和方法
     *   b.接口，根本不是类，只是抽象方法的集合
     *   c.场景：抽象类可将公用的方法实现，不公用的子类实现，当关注事物本身的时候，用抽象类，当关注操作行为的时候，或者想多继承的时候，用接口
     *   e.标记接口：里面没有任何方法
     *
     * 2.instanceof是运算符
     *
     * 3.装箱，拆箱
     *   装箱：将基本类型转换为包装器类型（调用如Integer.valueOf）,拆箱：将保证器类型转换为基本类型（调用如：intValue）
     *   Byte,Short,Integer,Long,Character的valueOf的实现都是，如值（-128,127）返回缓存同样对象，如值（128，xx)返回新对象
     *   Boolean的valueOf都相等，Float,Double的valueOf都是返回新对象
     *   equals会先判断对象的类型是否相等，才判断其xxValue是否相等
     *
     * 4.String类是不可改变的，创建了String对象，值就无法改变了。如需要对字符串做很多修改，使用StringBuffer & StringBuilder 类,并且不产生多个对象
     *   StringBuilder是java5提出来的是线程不安全的，而StringBuffer是线程安全的
     *
     * 5.Arrays专门操作数组,Collections专门操作集合（List，Set，Map等）
     *
     * 6.可变参数必须是最后一个参数且只能有一个
     *
     * 7.finalize函数是在对象析构之前调用
     *   a.一般在里面做一些释放操作，并非是java,new出来的内存，比如说文件句柄关闭，释放调用c，c++的来分配空间等
     *   b.调用该函数会引起死锁和线程挂起，因为并不存在任何一种机制可以把资源的释放与对象的生命周期完全绑定在一起，如果处理不好还会耗尽资源。
     *   c.java9已经抛弃了,不建议使用
     *
     * 8.stream
     *   a.控制台输入System.in,输出System.out
     *   b.参考https://www.w3cschool.cn/java/java-files-io.html
     *   c.一个流被定义为一个数据序列，输入流用于从源读取数据，输出流用于向目标写数据
     *
     * 9.try-with-resource
     *   a.凡是实现了AutoCloseable接口的都可以是使用try-with-resource,能保证走到finally里面关闭
     *   b.在块中可以使用多个资源而且这些资源都能被自动地关闭
     *
     * 10.exception,error
     *   a.Exception和Error都继承Throwable
     *   b.error一般是系统错误、内存溢出、虚拟机错误、栈溢出等，和程序本身没有关系，且无法处理，表示jvm出了问题
     *   c.注意，注意，注意：在try，catch里return语句，执行之后，返回之前，会执行finally语句，若finally语句有返回值，则从finally里面返回，也可以说用
     *     finally返回值代替try，catch返回值
     *
     * 11.方法重写的规则
     *   a.子类的访问权限只能大于等于父类方法的访问权限
     *
     * 12.多态
     *   a.动态绑定，运行时绑定，相对于静态绑定在编译的时候就决定调用哪个函数，而是在运行的时候决定运行哪个方法
     *   b.具体实现就是每个类有个指向方法区里面虚方法表的指针，里面保存了虚方法的的地址，如果没有重写，则父类的方法地址一样，重写了则指向自己的地址
     *   c.成员变量是不支持多态的，因为不需要，方法是类提供给外部调用的，而变量则是内部使用，不对外公开，没有必要支持
     *   e.具体实现：参考：
     *     https://blog.csdn.net/SEU_Calvin/article/details/52191321，
     *     https://www.cnblogs.com/tsiangleo/p/4415628.html
     *     大概是，类加载后，有各自的方法区，里面保存了
     *     指向不同方法的地址，若没有重写，则和父类的方法区方法地址一样，否则就是子类的方法区方法地址
     *
     * 13.外部类，只能用public和default，因为外部类的上一级就是包，权限就只有在包内和包外，没有其他的
     *
     * 14.大端、小端字节序,网络字节序
     *    a.大端：高位字节数据放在内存的低地址
     *    b.小端：低位字节数据放在内存的高地址
     *    c.如：0x12345678,在大端cpu里面是0x12,0x34,0x56,0x78,而在小端里面是0x78,0x56,0x34,0x12
     *      Windows，linux操作系统都是小端字节序,字节序和cpu有关系
     *    d.
     *      网络字节序是大端字节序
     *      C++是主机字节序（高、低都有可能）
     *      JAVA是网络字节序、也就是大端字节序
     *    e:参考
     *      https://blog.csdn.net/aitangyong/article/details/23204817
     *      https://www.cnblogs.com/CharlesGrant/p/7201858.html
     *
     *  15.Hashtable和HashMap和ConcurrentHashMap
     *      参考：https://www.cnblogs.com/heyonggang/p/9112731.html
     *     1.t没有大写
     *     2.Hashtable是Dictionary的实现，而Dictionary已经被抛弃了，被map代替了
     *     3.Hashtable是线程安全的，实现方式在修改数据时，直接锁住整个Hashtable，效率低基本上被弃用了，而HashMap线程不安全
     *     4.Hashtable不支持key和value为空，而HashMap，key和value都可以为空，所以通过get来判断是否存在会有问题的
     *     5.ConcurrentHashMap采用分段锁，一次锁住一个桶，效率高，支持线程安全
     *     6.散列表采用数组+链表，如果链表的长度太大，则会变成树
     *
     *  16.volatile
     *
     *  17.fail-fast迭代器
     */
}