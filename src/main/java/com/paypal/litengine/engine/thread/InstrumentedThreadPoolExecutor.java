package com.paypal.litengine.engine.thread;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

public class InstrumentedThreadPoolExecutor extends ThreadPoolExecutor {
    
 public InstrumentedThreadPoolExecutor(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit,
            BlockingQueue<Runnable> workQueue) {
        super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue);
        // Keep track of all of the request times
        timeOfRequestRunnable = new ConcurrentHashMap<Runnable, Long>();
        timeOfRequestCallable = new ConcurrentHashMap<Callable, Long>();
        startTime = new ThreadLocal<Long>();
        // other variables are AtomicLongs and AtomicIntegers
        totalServiceTime= new  AtomicLong();
        totalPoolTime= new  AtomicLong();
        numberOfRequestsRetired= new  AtomicLong();
        numberOfRequests= new  AtomicLong();
        aggregateInterRequestArrivalTime= new  AtomicLong(); 
 }

    // Keep track of all of the request times
    private final ConcurrentHashMap<Runnable, Long> timeOfRequestRunnable;
    private final ConcurrentHashMap<Callable, Long> timeOfRequestCallable;
    private final ThreadLocal<Long> startTime ;
    private long lastArrivalTime;
    // other variables are AtomicLongs and AtomicIntegers
    private final AtomicLong totalServiceTime;
    private AtomicLong totalPoolTime= new  AtomicLong();
    private AtomicLong numberOfRequestsRetired= new  AtomicLong();
    private AtomicLong numberOfRequests= new  AtomicLong();
    private AtomicLong aggregateInterRequestArrivalTime= new  AtomicLong();    
    
    @Override
    protected void beforeExecute(Thread worker, Runnable task) {
      super.beforeExecute(worker, task);
      startTime.set(System.nanoTime());
    }

    @Override
    protected void afterExecute(Runnable task, Throwable t) {
      try {
        totalServiceTime.addAndGet(System.nanoTime() - startTime.get());
        totalPoolTime.addAndGet(startTime.get() - timeOfRequestRunnable.remove(task));
        numberOfRequestsRetired.incrementAndGet();
      } finally {
        super.afterExecute(task, t);
      }
    }
    
    

    @Override
    public void execute(Runnable task) {
      long now = System.nanoTime();

      numberOfRequests.incrementAndGet();
      synchronized (this) {
        if (lastArrivalTime != 0L) {
          aggregateInterRequestArrivalTime.addAndGet(now - lastArrivalTime);
        }
        lastArrivalTime = now;
        timeOfRequestRunnable.put(task, now);
      }
      super.execute(task);
     }

    @Override
    public <T> Future<T> submit(Callable<T> task) {
        long now = System.nanoTime();

        numberOfRequests.incrementAndGet();
        synchronized (this) {
          if (lastArrivalTime != 0L) {
            aggregateInterRequestArrivalTime.addAndGet(now - lastArrivalTime);
          }
          lastArrivalTime = now;
          timeOfRequestCallable.put(task, now);
        }
        return super.submit(task);
    }

}
