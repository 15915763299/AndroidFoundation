package com.demo.threadandcache.analysis.example2;

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

            try {
                long time = Main.randomTime(1000, 3000);
                Thread.sleep(time);
            } catch (InterruptedException e) {
                e.printStackTrace();
                Thread.currentThread().interrupt();
                transmitter.send("End");
                break;
            }
        }
    }
}
