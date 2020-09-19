package com.demo.thread.analysis.example2;

import com.demo.thread.analysis.ExampleUtils;

/**
 * @author 尉迟涛
 * create time : 2020/2/15 17:17
 * description : 接收者
 */
public class Receiver2 implements Runnable {
    private Transmitter2 transmitter;

    public Receiver2(Transmitter2 transmitter) {
        this.transmitter = transmitter;
    }

    @Override
    public void run() {
        if (Thread.currentThread().isInterrupted()) {
            return;
        }

        for (String receivedMessage = transmitter.receive();
             !ExampleUtils.END.equals(receivedMessage);
             receivedMessage = transmitter.receive()) {

            System.out.println(receivedMessage);
            try {
                long time = ExampleUtils.randomTime(1000, 3000);
                Thread.sleep(time);
            } catch (InterruptedException e) {
                e.printStackTrace();
                Thread.currentThread().interrupt();
                break;
            }
        }
    }
}
