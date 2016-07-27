package test.com.cleancodesample;

import test.com.cleancodesample.domain.executor.MainExecutor;

/**
 * Created by hzaied on 7/16/16.
 */
public class TestMainThread implements MainExecutor {
    @Override
    public void post(Runnable runnable) {
        runnable.run();
    }
}
