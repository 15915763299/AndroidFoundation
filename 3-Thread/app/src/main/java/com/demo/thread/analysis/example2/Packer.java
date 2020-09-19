package com.demo.thread.analysis.example2;

import com.demo.thread.analysis.ExampleUtils;

/**
 * @author 尉迟涛
 * create time : 2020/2/15 17:17
 * description : 接收者
 */
public class Packer implements Runnable, Pack {
    private Transmitter2 transmitter;

    public  Packer(Transmitter2 transmitter) {
        this.transmitter = transmitter;
    }

    @Override
    public void run() {
        if (Thread.currentThread().isInterrupted()) {
            return;
        }

        for (String packMessage = transmitter.pack(this);
             !ExampleUtils.END.equals(packMessage);
             packMessage = transmitter.pack(this)) {

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

    @Override
    public String pack(String info) {
        if (ExampleUtils.END.equals(info)) {
            return info;
        }
        return "[" + info + "]";
    }
}
