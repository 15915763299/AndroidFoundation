package com.demo.threadandcache.analysis.example2;

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
             !Main.END.equals(receivedMessage);
             receivedMessage = transmitter.receive()) {

            System.out.println(receivedMessage);
            try {
                long time = Main.randomTime(1000, 3000);
                Thread.sleep(time);
            } catch (InterruptedException e) {
                e.printStackTrace();
                Thread.currentThread().interrupt();
                break;
            }
        }
    }
}
