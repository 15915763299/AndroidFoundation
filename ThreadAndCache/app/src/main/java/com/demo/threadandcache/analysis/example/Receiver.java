package com.demo.threadandcache.analysis.example;

/**
 * @author 尉迟涛
 * create time : 2020/2/15 17:17
 * description : 接收者
 */
public class Receiver implements Runnable {
    private Transmitter transmitter;

    Receiver(Transmitter transmitter) {
        this.transmitter = transmitter;
    }

    public void run() {
        if (Thread.currentThread().isInterrupted()) {
            return;
        }

        for (String receivedMessage = transmitter.receive();
             !"End".equals(receivedMessage);
             receivedMessage = transmitter.receive()) {

            System.err.println(receivedMessage);
            try {
                long time = Main.randomTime(2000, 5000);
                System.out.println("Receiver wait: " + time);
                Thread.sleep(time);
            } catch (InterruptedException e) {
                e.printStackTrace();
                // 捕获到interrupt后，interrupt会被重置，需要再调用interrupt
                Thread.currentThread().interrupt();
                break;
            }
        }
    }
}
