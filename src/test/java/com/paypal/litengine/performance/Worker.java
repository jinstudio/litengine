package com.paypal.litengine.performance;

import java.util.concurrent.CountDownLatch;

abstract class Worker implements Runnable {
    private final CountDownLatch startSignal;
    private final CountDownLatch doneSignal;
    Worker(CountDownLatch startSignal, CountDownLatch doneSignal) {
       this.startSignal = startSignal;
       this.doneSignal = doneSignal; 
    }
    public void run() {
       try {
         startSignal.await();
         doWork();
         doneSignal.countDown();
       } catch (InterruptedException ex) {} // return;
    }

    abstract void doWork();
  }
