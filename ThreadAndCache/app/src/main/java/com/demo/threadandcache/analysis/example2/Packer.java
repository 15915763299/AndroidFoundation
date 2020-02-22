package com.demo.threadandcache.analysis.example2;

/**
 * @author 尉迟涛
 * create time : 2020/2/15 17:17
 * description : 接收者
 */
public class Packer implements Runnable, Pack {
    private Transmitter transmitter;

    Packer(Transmitter transmitter) {
        this.transmitter = transmitter;
    }

    public void run() {
        if (Thread.currentThread().isInterrupted()) {
            return;
        }

        for (String packMessage = transmitter.pack(this);
             !Main.END.equals(packMessage);
             packMessage = transmitter.pack(this)) {

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

    @Override
    public String pack(String info) {
        if (Main.END.equals(info)) {
            return info;
        }
        return "[" + info + "]";
    }
}
