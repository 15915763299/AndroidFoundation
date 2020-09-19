package com.demo.thread.example;

import com.demo.thread.analysis.ExampleUtils;
import com.demo.thread.analysis.example.Receiver;
import com.demo.thread.analysis.example.Sender;
import com.demo.thread.analysis.example.Transmitter;
import com.demo.thread.analysis.example2.Packer;
import com.demo.thread.analysis.example2.Receiver2;
import com.demo.thread.analysis.example2.Sender2;
import com.demo.thread.analysis.example2.Transmitter2;

import org.junit.Test;

/**
 * @author 尉迟涛
 * create time : 2020/9/19 11:43
 * description :
 */
public class SenderReceiverTest {

    @Test
    public void example1Test() {
        String[] packets = {
                "First packet",
                "Second packet",
                "Third packet",
                "Fourth packet",
                "End"
        };
        Transmitter transmitter = new Transmitter();

        Thread sender = new Thread(new Sender(transmitter, packets));
        Thread receiver = new Thread(new Receiver(transmitter));

        sender.start();
        receiver.start();

        try {
            Thread.sleep(1000);
        } catch (Exception e) {
            e.printStackTrace();
        }
        sender.interrupt();
    }

    @Test
    public void example12est() {
        String[] packets = {
                "First packet",
                "Second packet",
                "Third packet",
                "Fourth packet",
                ExampleUtils.END
        };
        Transmitter2 transmitter = new Transmitter2();

        Thread sender = new Thread(new Sender2(transmitter, packets));
        Thread packer = new Thread(new Packer(transmitter));
        Thread receiver = new Thread(new Receiver2(transmitter));

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
}
