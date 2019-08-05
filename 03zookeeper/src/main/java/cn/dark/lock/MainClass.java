package cn.dark.lock;

import org.apache.zookeeper.KeeperException;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;

/**
 * @author dark
 * @date 2019-07-11
 */
public class MainClass {

    public static void main(String[] args) throws IOException {
        CountDownLatch countDownLatch = new CountDownLatch(10);
        for (int i = 0; i < 10; i++) {
            new Thread(() -> {
                try {
                    // 10个线程同时去竞争锁
                    countDownLatch.await();
                    DistributedLock lock = new DistributedLock();
                    lock.lock();
                    lock.unlock();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (KeeperException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }).start();
            countDownLatch.countDown();
        }
        // System.in.read();
    }

}
