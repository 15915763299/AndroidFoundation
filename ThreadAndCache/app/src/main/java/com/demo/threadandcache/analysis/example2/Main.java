package com.demo.threadandcache.analysis.example2;


/**
 * @author 尉迟涛
 * create time : 2020/2/15 17:17
 * description : 升级版
 * <p>
 * 首先需要一个对象作为锁，被三个线程共享，
 * 三个线程每次竞争到锁，都会判断标志位，满足则执行，否则等待，
 * 这样按照标志位的轮转，三个线程按顺序执行
 * <p>
 * 不过这样的缺点是很消耗资源（线程等待时间太长）
 * <p>
 * 再抽象一点，多个线程共享一个对象锁，一个标志位
 * 每个线程有一个标志，竞争到锁后，判断标志位，
 * 符合则执行，并修改标志位至下一标志，唤醒其他线程继续竞争锁，
 * 不符合则等待（wait会释放锁，大家就可以抢啦）
 */
public class Main {

    public static final String END = "End";

    public static void main(String[] args) {
        String[] packets = {
                "First packet",
                "Second packet",
                "Third packet",
                "Fourth packet",
                END
        };
        Transmitter transmitter = new Transmitter();

        Thread sender = new Thread(new Sender(transmitter, packets));
        Thread packer = new Thread(new Packer(transmitter));
        Thread receiver = new Thread(new Receiver(transmitter));

        sender.start();
        packer.start();
        receiver.start();

//        try {
//            Thread.sleep(1000);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        sender.interrupt();
    }

    public static int randomTime(int min, int max) {
        return (int) (Math.random() * (max - min) + min);
    }

}
