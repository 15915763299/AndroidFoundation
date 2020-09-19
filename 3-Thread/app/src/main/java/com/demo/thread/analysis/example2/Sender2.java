package com.demo.thread.analysis.example2;

import com.demo.thread.analysis.ExampleUtils;

/**
 * @author 尉迟涛
 * create time : 2020/2/15 17:14
 * description : 发送者
 */
public class Sender2 implements Runnable {
    private Transmitter2 transmitter;
    private String[] packets;

    public Sender2(Transmitter2 transmitter, String[] packets) {
        this.transmitter = transmitter;
        this.packets = packets;
    }

    @Override
    public void run() {
        if (Thread.currentThread().isInterrupted()) {
            return;
        }

        for (String packet : packets) {
            transmitter.send(packet);

            try {
                long time = ExampleUtils.randomTime(1000, 3000);
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
