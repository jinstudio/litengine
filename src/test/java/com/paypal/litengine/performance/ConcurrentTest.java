package com.paypal.litengine.performance;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.paypal.litengine.demo.complex.WordsCountDemo;

public class ConcurrentTest {
 
    private static final int NUMBER = 90;
    static final Logger logger = LoggerFactory.getLogger(ConcurrentTest.class);

    /**
     * @param args
     * @throws InterruptedException 
     */
    public static void main(String[] args) throws InterruptedException {
    	//Thread.currentThread().sleep(15000);
        CountDownLatch startSignal = new CountDownLatch(1);
        CountDownLatch doneSignal = new CountDownLatch(NUMBER);
        long start=System.currentTimeMillis();
        for (int i = 0; i < NUMBER; ++i) // create and start threads
          new Thread(new Worker(startSignal, doneSignal){
        	  
            @Override
            void doWork() {
                { 
                    try {
                        WordsCountDemo.main(new String[] {});
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
        logger.info("{}-{} total time consumed:{}",Thread.currentThread().getName(),Thread.currentThread().getId(),System.currentTimeMillis()-start);
    }
}


