package com.paypal.litengine;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public abstract class BaseEngine<T extends Context<Tuple, Tuple>> implements Engine<T> {

    ThreadPoolExecutor mainExecutor = new ThreadPoolExecutor(1, 5, 0, TimeUnit.MILLISECONDS,
            new LinkedBlockingQueue<Runnable>());

    final Lock lock = new ReentrantLock();
    final Condition finish = lock.newCondition();

    private T context;

    public BaseEngine() {
        super();
    }

    public BaseEngine<T> setContext(T context) {
        this.context = context;
        return this;
    }

    @Override
    public Tuple trigger(T t, Tuple input) {
        execute(t, input);
        return t.getFinalOutput();
    }

	private void execute(T t, Tuple input) {
		t.initTriggerSource(input);
        this.execute(t);
        lock.lock();
        finish.signal();
        lock.unlock();
	}

    @Override
    public Tuple trigger(Tuple input) {
        return this.trigger(this.context, input);
    }

    @Override
    public Tuple trigger(final T t, final Tuple input, int timeout) {
        try {
            lock.lock();
            mainExecutor.execute(new Runnable() {

                @Override
                public void run() {
                	execute(t, input);
                }

            });
            try {
                  finish.await(timeout, TimeUnit.MILLISECONDS);
                  if(Status.DONE!=t.getStatus()){
                      t.setStatus(Status.TIME_OUT);
                  }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            
        } finally {
            lock.unlock();
            return t.getFinalOutput();
        }
    }

    @Override
    public Tuple trigger(Tuple input, int timeout) {
        return this.trigger(this.context, input, timeout);
    }

    public abstract void execute(T context);

}
