package test.com.cleancodesample.domain.executor.impl;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import test.com.cleancodesample.domain.executor.ThreadExecutor;
import test.com.cleancodesample.domain.interactor.base.AbstractInteractor;

/**
 * Created by hzaied on 7/16/16.
 */
public class ThreadExecutorImpl implements ThreadExecutor {
    private static volatile ThreadExecutor sThreadExecutor;

    private final static int CORE_POOL_SIZE = 3;
    private final static int MAX_POOL_SIZE = 5;
    private final static int KEEP_ALIVE_TIME = 120;
    private final static TimeUnit TIME_UNIT = TimeUnit.SECONDS;
    private final static BlockingQueue<Runnable> BLOCKING_QUEUE = new LinkedBlockingQueue<>();

    private ThreadPoolExecutor mThreadPoolExecutor;

    public synchronized static ThreadExecutor getInstance() {
        if (sThreadExecutor == null) {
            sThreadExecutor = new ThreadExecutorImpl();
        }

        return sThreadExecutor;
    }


    public ThreadExecutorImpl() {
        mThreadPoolExecutor = new ThreadPoolExecutor(CORE_POOL_SIZE,
                MAX_POOL_SIZE,
                KEEP_ALIVE_TIME,
                TIME_UNIT,
                BLOCKING_QUEUE);
    }

    @Override
    public void execute(final AbstractInteractor interactor) {
        mThreadPoolExecutor.submit(new Runnable() {
            @Override
            public void run() {
                interactor.run();
                interactor.onFinished();
            }
        });
    }
}
