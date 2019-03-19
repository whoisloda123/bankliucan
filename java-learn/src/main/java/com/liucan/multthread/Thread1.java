package com.liucan.multthread;

import org.springframework.stereotype.Component;

/**
 * 参考：https://blog.csdn.net/zsm2015/article/details/79553593
 * <p>
 * 一.线程：用户线程，守护线程（daemon线程）
 * 1.用户线程全部退出了，daemon线程也会自动退出
 * 2.thread.setDaemon(true)必须在thread.start()之前设置
 * 3.在Daemon线程中产生的新线程也是Daemon的
 * 4.守护线程应该永远不去访问固有资源，如文件、数据库，因为它会在任何时候甚至在一个操作的中间发生中断
 * 5.调用start方法的顺序不代表线程启动顺序
 * <p
 * 二.sleep和yield区别：https://www.cnblogs.com/hypnotizer/p/5579095.html
 * sleep线程处于阻塞状态，让出cpu时间片给其他线程执行，结束后才会转入就绪状态
 * yield是暂停线程，不会阻塞线程，让线程运行状态转入就绪状态，线程调度器可以继续执行
 * <p>
 * 三.suspend()、resume()和stop()已经过期不建议使用，因为调用后不会释放资源，容易造成死锁问题，改为用sleep和notify等待通知机制
 * <p>
 * 四.volatile 参考：https://www.cnblogs.com/chengxiao/p/6528109.html
 * a.多线程同时访问一个对象时（主内存），每个执行的线程可以拥有该对象的一份拷贝（本地内存），而本地内存的操作会在一个时机(线程执行完毕)同步到主内存
 * 所以程序在执行过程中，一个线程看到的变量并不一定是最新的
 * b.轻量级锁
 * c.能保证共享变量对所有线程的可见性，当写一个volatile变量时，JMM会把该线程对应的本地内存中的变量强制刷新到主内存中去
 * 保证所有线程对该变量的可见性
 * d.禁止指令重排序优化
 * e.volatile对于单个的共享变量的读/写具有原子性，但是像num++这种复合操作，volatile无法保证其原子性,可以用原子锁
 *
 * 五.synchronized
 *  参考：https://www.cnblogs.com/paddix/p/5367116.html
 *  1.synchronized锁定是一个对象，其他试图访问该对象synchronized方法或代码块会被锁住,而每一个对象都可以做为一个锁（Monitor锁）
 *  2.在普通方法前面，锁的是当前实例对象（其他的synchronized标志的方法也会被锁住,非synchronized的不会被锁住）
 *  3.在静态方法前面，锁的是整个类
 *  4.在方法块里面synchronized(object),锁的是括号里面的对象
 *  5.实现机制
 *      a.每个对象有个监视器锁（Monitor锁）
 *      b.Monitor被占用的时候其他线程会阻塞，进入执行命令MonitorEnter获取Monitor,退出执行MonitorExit释放Monitor
 *      c.notify/notifyAll和wait方法都依赖Monitor锁
 *      d.synchronized方法，和方法块是基本Monitor锁实现，执行时候进入获取锁，离开释放锁
 *      e.所以notify/notifyAll和wait方法都必须位于synchronized内，否者抛异常
 *
 * 六.线程同步
 *  参考：https://www.cnblogs.com/szlbm/p/5588457.html
 *  1.在线程a里面调用Thread.currentThread()得到的线程未必是a的，是调用该函数的线程，如果是在run函数里面则是a
 *  2.sleep,yield,wait区别
 *      a.sleep后，不会释放当前锁，会释放cpu时间片，暂停线程，时间到线程处于可以调用状态
 *      b.yield后，不会释放当前锁，会释放cpu时间片，线程处于可以调度状态（ps：可能出现yield后，马上又被调用，完全取决于线程调度器）
 *      c.wait后，会释放当前锁，会释放cpu时间片，暂停当前线程，直到被notify/notifyAll通知
 *  3.interrupt
 *      1.不是马上中断线程，而在线程阻塞的时候将线程的中断标记设为true，并产生一个InterruptedException异常，这样让线程中断，
 *          如果线程没有阻塞则不起作用，只是将中断标记设置一下
 *      2.isInterrupted，判断中断标记是否为true
 *  4.ThreadLocal
 *  参考：https://blog.csdn.net/renwei289443/article/details/79540809
 *      1.每个线程独有一份，和TreadLocal在哪个地方和有多少个对象没有关系，和里面的map有关系，ThreadLocal里面是保存的是map（当前线程对应key，对应value）
 *      2.想保存多个本地线程数据，就定义多个TreadLocal，因里面都是和Thread.currentThread操作有关系
 * 七.锁分类
 *      a.公平锁/非公平锁：是否按照申请的顺序来获得锁,通过ReentrantLock构造函数来
 *          1.ReentrantLock构造函数来制定
 *          2.synchronized是非公平锁
 *      b.可重入锁（递归锁）:可多次加锁，ReentrantLock和synchronized都是
 *      c.独享锁/共享锁(互斥锁/读写锁):读写锁，用ReentrantReadWriteLock，读锁共享，写锁互斥
 *      d.乐观锁/悲观锁
 *          1.悲观锁认为对于同一个数据操作其他线程会修改，一定要加锁：常用锁
 *          2.乐观锁认为对于同一个数据操作其他线程不会修改，不需要加锁：用自旋锁
 *      e.偏向锁/轻量级锁/重量级锁:指的是锁的状态，是针对synchronized的---synchronized锁的优化
 *          1.偏向锁:一段代码一直被一个线程所访问，该线程会自动获取锁。降低获取锁的代价
 *          2.轻量级锁:指当锁是偏向锁的时候，被另一个线程所访问，偏向锁就会升级为轻量级锁，其他线程会通过自旋的形式尝试获取锁，不会阻塞，提高性能。
 *          3.重量级锁是指当锁为轻量级锁的时候，另一个线程虽然是自旋，但自旋不会一直持续下去，当自旋一定次数的时候，还没有获取到锁，就会进入阻塞，
 *              该锁膨胀为重量级锁。重量级锁会让其他申请的线程进入阻塞，性能降低
 *      e.自旋锁（无锁）:线程不会阻塞，不会释放cpu时间片，而一直循环等待，采用原子锁cas（compare and swap）方式
 *
 *      f.CAS和AQS
 *          参考：https://www.cnblogs.com/waterystone/p/4920797.html
 *          https://www.cnblogs.com/chengxiao/archive/2017/07/24/7141160.html
 *          https://blog.csdn.net/zhangdong2012/article/details/79983404
 *        1.CAS(compare and swap)，原子操作
 *          a.UnSafe类提供了硬件级别的原子操作，一般AtomicInteger等原子类都做了封装
 *        2.AQS(AbstractQueuedSynchronizer)
 *          a.是ReentrantLock、Semaphore，CountDownLatch等线程同步的基类，是构建锁和同步器的框架
 *          b.通过一个volatile的status状态变量和FIFO队列来实现
 *              1.队列里面保存线程的信息，头结点是获取锁的线程
 *              2.其他线程获取锁先通过同步状态status来判断是否可以获取锁（如ReentrantLock的status如果不是当前线程最多是1
 *                  如果是1，则不能获取锁除非是0），如果能获取则获取，否则构造队列节点放入尾部，然后将当前线程挂起
 *              3.释放锁时，释放同步状态status（如ReentrantLock将状态变为0），同时唤醒后继节点
 *              4.在写自定义同步器的时候只需重写tryAcquire，tryAcquireShared，tryRelease，tryReleaseShared几个方法，来决定同步状态的释放和获取即可
 *              5.有共享模式和独占模式：ReentrantLock独占模式，一次只有一个获取锁的线程接口，CountDownLatch共享模式，一次有多个获取锁的线程节点
 *      d.CountDownLatch，Semaphore等线程同步类
 *          CountDownLatch控制同时等待多少个线程执行结束后再进行，Semaphore可控制有多少个线程同时执行
 *
 *  八.Concurrent同步包各种同步数据结果
 *      参考：https://blog.csdn.net/defonds/article/details/44021605#t8
 *
 *  九.Unsafe类，不建议自己使用，除非很了解他，因为可以像c语言一样，使用指针，操作内存，释放内存容易出现问题
 *      也可以对线程进行挂起和恢复2
 *
 * @author liucan
 * @version 19-1-20
 */
@Component
public class Thread1 {

    private final ThreadLocal<String> tl = new ThreadLocal<>();
    private final Object lock = new Object();
    private final Object lock1 = new Object();
    private boolean waitSignal = true;

    public void example() {
        new Exchanger1().test();
        new Executor1().test();
        new Queue1().test();
        new CyclicBarrier1().test();
        new Semaphore1().test();
        new Future1().test();
        threadTest();
    }

    private void threadTest() {
        Thread thread = new Thread(() -> System.out.println(1));
        thread.setDaemon(true); //必须在启动前调用
        thread.setName("线程");
        thread.start();

        Print print = new Print();
        new Thread(print, "C").start();
        new Thread(print, "A").start();
        new Thread(print, "B").start();

//        new Thread(() -> print.method1()).start();
//        new Thread(() -> print.method5()).start();
//        new Thread(() -> print.method2()).start();

//        new Thread(() -> print.method3()).start();
//        new Thread(() -> print.method5()).start();
//        new Thread(() -> print.method4()).start();

        tl.set("1");
        tl.get();
    }

    private void doWait() {
        synchronized (lock1) {
            //此处用while不用if是因为线程会出现假唤醒（spurious wakeups：由于莫名其妙的原因，线程会在没有notify的情况下唤醒）
            while (waitSignal) {
                try {
                    //wait会释放当前对象上的监控器锁
                    lock1.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void doNotify() {
        synchronized (lock1) {
            try {
                waitSignal = false;
                lock1.notifyAll();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 设计一个程序，启动三个线程A,B,C,各个线程只打印特定的字母，各打印5次，例如A线程只打印‘A’。要求在控制台依次显示“ABCABC…”
     */
    private class Print implements Runnable {

        private String currentPrint = "A";
        private int curCount = 0;

        //给线程执行的
        @Override
        public void run() {
            synchronized (lock) {
                while (curCount < 5) {
                    String threadName = Thread.currentThread().getName();
                    if (threadName.equals(currentPrint)) {
                        System.out.println(currentPrint);
                        if (currentPrint.equals("A")) {
                            currentPrint = "B";
                        } else if (currentPrint.equals("B")) {
                            currentPrint = "C";
                            curCount++;
                        } else {
                            currentPrint = "A";
                        }
                        //notify()方法不释放锁
                        lock.notifyAll();
                    } else {
                        try {
                            //调用wait会释放对象上的监视器锁Monitor锁
                            lock.wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }

        private synchronized void method1() {
            try {
                Thread.sleep(3000);
                System.out.println("method1" + Thread.currentThread().getName());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        private synchronized void method2() {
            try {
                Thread.sleep(1000);
                System.out.println("method2" + Thread.currentThread().getName());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        private void method3() {
            try {
                synchronized (this) {
                    Thread.sleep(3000);
                    System.out.println("method3" + Thread.currentThread().getName());
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        private void method4() {
            try {
                synchronized (this) {
                    Thread.sleep(1000);
                    System.out.println("method4" + Thread.currentThread().getName());
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        private void method5() {
            System.out.println("method5" + Thread.currentThread().getName());
        }
    }
}
