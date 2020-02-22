package com.demo.threadandcache.analysis.example;

/**
 * @author 尉迟涛
 * create time : 2020/2/15 17:14
 * description : 发送者
 */
public class Sender implements Runnable {
    private Transmitter transmitter;
    private String[] packets;

    Sender(Transmitter transmitter, String[] packets) {
        this.transmitter = transmitter;
        this.packets = packets;
    }

    public void run() {
        if (Thread.currentThread().isInterrupted()) {
            return;
        }

        for (String packet : packets) {
            transmitter.send(packet);

            // Thread.sleep() to mimic heavy server-side processing
            try {
                long time = Main.randomTime(2000, 5000);
                System.out.println("Sender wait: " + time);
                Thread.sleep(time);
            } catch (InterruptedException e) {
                e.printStackTrace();
                // 捕获到interrupt后，interrupt会被重置，需要再调用interrupt
                Thread.currentThread().interrupt();
                transmitter.send("End");
                break;
            }
        }
    }
}
