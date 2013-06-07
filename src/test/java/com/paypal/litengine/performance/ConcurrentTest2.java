package com.paypal.litengine.performance;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.paypal.litengine.demo.complex.WordsCountDemoAnno2;

public class ConcurrentTest2 {

    private static final int NUMBER = 50;
    static final Logger logger = LoggerFactory.getLogger(ConcurrentTest2.class);

    /**
     * @param args
     * @throws InterruptedException 
     */
    public static void main(String[] args) throws InterruptedException {
        CountDownLatch startSignal = new CountDownLatch(1);
        CountDownLatch doneSignal = new CountDownLatch(NUMBER);
        long start=System.currentTimeMillis();
        for (int i = 0; i < 50; ++i) // create and start threads
          new Thread(new Worker(startSignal, doneSignal){

            @Override
            void doWork() {
                { 
                    try {
                        WordsCountDemoAnno2.main(new String[] {});
                    } catch (InterruptedException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    } catch (ExecutionException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
            }
              
          }).start();

        startSignal.countDown();      // let all threads proceed
        doneSignal.await();           // wait for all to finish
        logger.debug("{}-{} total time consumed:{}",Thread.currentThread().getName(),Thread.currentThread().getId(),System.currentTimeMillis()-start);
    }
}
